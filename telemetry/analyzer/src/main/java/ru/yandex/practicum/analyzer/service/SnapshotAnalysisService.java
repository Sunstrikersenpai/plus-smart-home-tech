package ru.yandex.practicum.analyzer.service;

import com.google.protobuf.util.Timestamps;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;

import java.util.List;
import java.util.Objects;

@Service
public class SnapshotAnalysisService {
    private final ScenarioRepository scenarioRepo;

    @GrpcClient("hub-router")
    private HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public SnapshotAnalysisService(ScenarioRepository scenarioRepo) {
        this.scenarioRepo = scenarioRepo;
    }

    public void processSensor(String hubId, String sensorId, Integer value) {
        List<Scenario> scenarios = scenarioRepo.findByHubId(hubId);

        for (Scenario scenario : scenarios) {
            boolean allTrue = scenario.getConditions().entrySet().stream()
                    .filter(e -> e.getKey().equals(sensorId))
                    .allMatch(e -> checkCondition(e.getValue(), value));

            if (allTrue) {
                scenario.getActions().forEach((key, action) -> executeAction(key, action, hubId, scenario.getName()));
            }
        }
    }

    private boolean checkCondition(Condition condition, Integer value) {

        return switch (condition.getOperation()) {
            case GREATER -> value > condition.getValue();
            case LOWER -> value < condition.getValue();
            case EQUAL -> Objects.equals(value, condition.getValue());
        };
    }

    private void executeAction(String sensorId, Action action, String hubId, String scenario) {
        DeviceActionProto actionProto = DeviceActionProto.newBuilder()
                .setSensorId(sensorId)
                .setType(ActionTypeProto.valueOf(action.getType().name()))
                .setValue(action.getValue())
                .build();

        DeviceActionRequest actionRequest = DeviceActionRequest.newBuilder()
                .setHubId(hubId)
                .setScenarioName(scenario)
                .setAction(actionProto)
                .setTimestamp(Timestamps.fromMillis(System.currentTimeMillis()))
                .build();

        hubRouterClient.handleDeviceAction(actionRequest);
    }
}


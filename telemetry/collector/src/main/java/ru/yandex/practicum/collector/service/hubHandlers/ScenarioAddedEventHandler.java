package ru.yandex.practicum.collector.service.hubHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.collector.kafka.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {

    public ScenarioAddedEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(props, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }

    @Override
    protected ScenarioAddedEventAvro toAvro(HubEventProto event) {
        ScenarioAddedEventProto payload = event.getScenarioAdded();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(payload.getName())
                .setActions(payload.getActionList().stream()
                        .map(this::mapAction)
                        .toList())
                .setConditions(payload.getConditionList().stream()
                        .map(this::mapCondition)
                        .toList())
                .build();
    }

    private DeviceActionAvro mapAction(DeviceActionProto a) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(a.getSensorId())
                .setType(ActionTypeAvro.valueOf(a.getType().name()))
                .setValue(a.getValue())
                .build();
    }

    private ScenarioConditionAvro mapCondition(ScenarioConditionProto c) {
        Object value;

        switch (c.getValueCase()) {
            case INT_VALUE -> value = c.getIntValue();
            case BOOL_VALUE -> value = c.getBoolValue();
            default -> throw new IllegalArgumentException("Неизвестный тип value: " + c.getValueCase());
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(c.getSensorId())
                .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                .setValue(value)
                .build();
    }
}
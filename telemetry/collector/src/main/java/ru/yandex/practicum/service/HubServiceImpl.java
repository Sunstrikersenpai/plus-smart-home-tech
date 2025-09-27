package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.hub.*;

@Service
@AllArgsConstructor
public class HubServiceImpl implements HubService {
    private final KafkaProperties prop;
    private final KafkaEventProducer producer;

    @Override
    public void process(BaseHubEvent event) {
        producer.send(prop.getTopic().getHubs(), null, mapToAvro(event));
    }

    private HubEventAvro mapToAvro(BaseHubEvent e) {
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(e.getHubId())
                .setTimestamp(e.getTimestamp());

        switch (e.getType()) {
            case DEVICE_ADDED -> builder.setPayload(
                    DeviceAddedEventAvro.newBuilder()
                            .setId(((DeviceAddedEvent) e).getId())
                            .setType(DeviceTypeAvro.valueOf(((DeviceAddedEvent) e).getDeviceType().name()))
                            .build()
            );
            case DEVICE_REMOVED -> builder.setPayload(
                    DeviceRemovedEventAvro.newBuilder()
                            .setId(((DeviceRemovedEvent) e).getId())
                            .build()
            );
            case SCENARIO_ADDED -> builder.setPayload(
                    ScenarioAddedEventAvro.newBuilder()
                            .setName(((ScenarioAddedEvent) e).getName())
                            .setActions(
                                    ((ScenarioAddedEvent) e).getActions()
                                            .stream().map(this::mapAction).toList()
                            )
                            .setConditions(
                                    ((ScenarioAddedEvent) e).getConditions()
                                            .stream().map(this::mapCondition).toList()
                            )

                            .build()
            );
            case SCENARIO_REMOVED -> builder.setPayload(
                    ScenarioRemovedEventAvro.newBuilder()
                            .setName(((ScenarioRemovedEvent) e).getName())
                            .build()
            );
        }
        return builder.build();
    }

    private ScenarioConditionAvro mapCondition(ScenarioCondition c) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(c.getSensorId())
                .setType(ConditionTypeAvro.valueOf(c.getType().name()))
                .setOperation(ConditionOperationAvro.valueOf(c.getOperation().name()))
                .setValue(c.getValue())
                .build();
    }

    private DeviceActionAvro mapAction(DeviceAction a) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(a.getSensorId())
                .setType(ActionTypeAvro.valueOf(a.getType().name()))
                .setValue(a.getValue())
                .build();
    }
}
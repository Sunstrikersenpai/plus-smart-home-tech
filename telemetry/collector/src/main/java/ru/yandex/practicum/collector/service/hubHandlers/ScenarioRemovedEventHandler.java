package ru.yandex.practicum.collector.service.hubHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.collector.kafka.KafkaProperties;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {

    public ScenarioRemovedEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(props, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    protected ScenarioRemovedEventAvro toAvro(HubEventProto event) {
        ScenarioRemovedEventProto payload = event.getScenarioRemoved();
        return ScenarioRemovedEventAvro.newBuilder()
                .setName(payload.getName())
                .build();
    }
}
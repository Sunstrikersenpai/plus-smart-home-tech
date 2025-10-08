package ru.yandex.practicum.collector.service.hubHandlers;

import lombok.AllArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.collector.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@AllArgsConstructor
public abstract class BaseHubEventHandler<T extends SpecificRecordBase> implements HubEventHandler {

    private final KafkaProperties props;
    private final KafkaEventProducer producer;

    protected abstract T toAvro(HubEventProto event);

    @Override
    public void handle(HubEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неверный тип события: " + event.getPayloadCase());
        }

        T payload = toAvro(event);

        Instant timestamp = event.hasTimestamp()
                ? Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos())
                : Instant.now();

        HubEventAvro eventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(payload)
                .build();

        producer.send(props.getTopic().getHubs(), null, eventAvro);
    }
}

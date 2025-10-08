package ru.yandex.practicum.collector.service.sensorHandlers;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.collector.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Instant;

public abstract class BaseSensorEventHandler<T extends SpecificRecordBase> implements SensorEventHandler {

    protected final KafkaEventProducer producer;
    protected final KafkaProperties props;

    protected BaseSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        this.producer = producer;
        this.props = props;
    }

    protected abstract T toAvro(SensorEventProto event);

    @Override
    public void handle(SensorEventProto event) {
        if (!event.getPayloadCase().equals(getMessageType())) {
            throw new IllegalArgumentException("Неверный тип события: " + event.getPayloadCase());
        }

        T payload = toAvro(event);

        Instant ts = event.hasTimestamp()
                ? Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos())
                : Instant.now();

        SensorEventAvro avro = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(ts)
                .setPayload(payload)
                .build();

        producer.send(props.getTopic().getSensors(), null, avro);
    }
}
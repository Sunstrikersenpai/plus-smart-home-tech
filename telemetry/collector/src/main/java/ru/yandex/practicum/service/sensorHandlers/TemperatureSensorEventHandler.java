package ru.yandex.practicum.service.sensorHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorEventHandler extends BaseSensorEventHandler<TemperatureSensorAvro> {

    public TemperatureSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(producer, props);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR_EVENT;
    }

    @Override
    protected TemperatureSensorAvro toAvro(SensorEventProto event) {
        var payload = event.getTemperatureSensorEvent();
        return TemperatureSensorAvro.newBuilder()
                .setInttemperatureC(payload.getTemperatureC())
                .setInttemperatureF(payload.getTemperatureF())
                .build();
    }
}
package ru.yandex.practicum.service.sensorHandler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;

@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {

    public ClimateSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(producer, props);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    protected ClimateSensorAvro toAvro(SensorEventProto event) {
        var payload = event.getClimateSensorEvent();
        return ClimateSensorAvro.newBuilder()
                .setCo2Level(payload.getCo2Level())
                .setHumidity(payload.getHumidity())
                .setTemperatureC(payload.getTemperatureC())
                .build();
    }
}
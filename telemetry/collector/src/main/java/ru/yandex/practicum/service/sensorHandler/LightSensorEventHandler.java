package ru.yandex.practicum.service.sensorHandler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {

    public LightSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(producer, props);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }

    @Override
    protected LightSensorAvro toAvro(SensorEventProto event) {
        LightSensorProto payload = event.getLightSensorEvent();
        return LightSensorAvro.newBuilder()
                .setLinkQuality(payload.getLinkQuality())
                .setLuminosity(payload.getLuminosity())
                .build();
    }
}
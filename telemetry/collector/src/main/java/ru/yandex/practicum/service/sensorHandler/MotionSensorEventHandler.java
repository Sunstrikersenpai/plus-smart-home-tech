package ru.yandex.practicum.service.sensorHandler;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;

@Component
public class MotionSensorEventHandler extends BaseSensorEventHandler<MotionSensorAvro> {

    public MotionSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(producer, props);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR_EVENT;
    }

    @Override
    protected MotionSensorAvro toAvro(SensorEventProto event) {
        var payload = event.getMotionSensorEvent();
        return MotionSensorAvro.newBuilder()
                .setLinkQuality(payload.getLinkQuality())
                .setMotion(payload.getMotion())
                .setVoltage(payload.getVoltage())
                .build();
    }
}
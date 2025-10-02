package ru.yandex.practicum.service.sensorHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorEventHandler extends BaseSensorEventHandler<SwitchSensorAvro> {

    public SwitchSensorEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(producer, props);
    }

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    protected SwitchSensorAvro toAvro(SensorEventProto event) {
        var payload = event.getSwitchSensorEvent();
        return SwitchSensorAvro.newBuilder()
                .setState(payload.getState())
                .build();
    }
}
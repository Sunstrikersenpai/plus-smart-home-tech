package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.sensor.*;

@Service
@AllArgsConstructor
public class SensorServiceImpl implements SensorService {
    private final KafkaProperties prop;
    private final KafkaEventProducer producer;

    public void process(BaseSensorEvent event) {
        producer.send(prop.getTopic().getSensors(), null, mapToAvro(event));
    }

    private SensorEventAvro mapToAvro(BaseSensorEvent e) {
        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(e.getId())
                .setHubId(e.getHubId())
                .setTimestamp(e.getTimestamp());

        switch (e.getType()) {
            case LIGHT_SENSOR_EVENT -> builder.setPayload(
                    LightSensorAvro.newBuilder()
                            .setLinkQuality(((LightSensorEvent) e).getLinkQuality())
                            .setLuminosity(((LightSensorEvent) e).getLuminosity())
                            .build()
            );
            case MOTION_SENSOR_EVENT -> builder.setPayload(
                    MotionSensorAvro.newBuilder()
                            .setLinkQuality(((MotionSensorEvent) e).getLinkQuality())
                            .setMotion(((MotionSensorEvent) e).isMotion())
                            .setVoltage(((MotionSensorEvent) e).getVoltage())
                            .build()
            );
            case SWITCH_SENSOR_EVENT -> builder.setPayload(
                    SwitchSensorAvro.newBuilder()
                            .setState(((SwitchSensorEvent) e).isState())
                            .build()
            );
            case CLIMATE_SENSOR_EVENT -> builder.setPayload(
                    ClimateSensorAvro.newBuilder()
                            .setCo2Level(((ClimateSensorEvent) e).getCo2Level())
                            .setHumidity(((ClimateSensorEvent) e).getHumidity())
                            .setTemperatureC(((ClimateSensorEvent) e).getTemperatureC())
                            .build()
            );
            case TEMPERATURE_SENSOR_EVENT -> builder.setPayload(
                    TemperatureSensorAvro.newBuilder()
                            .setInttemperatureC(((TemperatureSensorEvent) e).getTemperatureC())
                            .setInttemperatureF(((TemperatureSensorEvent) e).getTemperatureF())
                            .build()
            );
        }
        return builder.build();
    }
}

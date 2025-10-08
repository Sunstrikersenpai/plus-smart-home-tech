package ru.yandex.practicum.analyzer.handler.snapshot;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.model.enums.SensorType;
import ru.yandex.practicum.analyzer.service.SnapshotAnalysisService;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
public class LightSensorHandler implements SnapshotEventHandler{
    private final SnapshotAnalysisService analysisService;

    @Override
    public SensorType getType() {
        return SensorType.LIGHT_SENSOR;
    }

    @Override
    public Integer extractValue(SensorStateAvro state, ConditionType conditionType) {
        if (!(state.getData() instanceof LightSensorAvro data)) return null;
        return conditionType == ConditionType.LUMINOSITY ? data.getLuminosity() : null;
    }

    @Override
    public void handle(String hubId, String sensorId, Instant timestamp, SensorStateAvro state) {
        Integer value = extractValue(state, ConditionType.LUMINOSITY);
        if (value != null) {
            analysisService.processSensor(hubId, sensorId, value);
        }
    }
}

package ru.yandex.practicum.analyzer.handler.snapshot;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.model.enums.SensorType;
import ru.yandex.practicum.analyzer.service.SnapshotAnalysisService;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

import java.time.Instant;

@Component
@AllArgsConstructor
public class ClimateSensorHandler implements SnapshotEventHandler{
    private final SnapshotAnalysisService analysisService;

    @Override
    public SensorType getType() {
        return SensorType.CLIMATE_SENSOR;
    }

    @Override
    public Integer extractValue(SensorStateAvro state, ConditionType conditionType) {
        ClimateSensorAvro data = (ClimateSensorAvro) state.getData();

        return switch (conditionType) {
            case TEMPERATURE -> data.getTemperatureC();
            case HUMIDITY -> data.getHumidity();
            case CO2LEVEL -> data.getCo2Level();
            default -> null;
        };
    }

    @Override
    public void handle(String hubId, String sensorId, Instant timestamp, SensorStateAvro  state) {

        for (ConditionType type : getType().getConditions()) {
            Integer value = extractValue(state, type);
            if (value != null) {
                analysisService.processSensor(hubId, sensorId, value);
            }
        }
    }
}

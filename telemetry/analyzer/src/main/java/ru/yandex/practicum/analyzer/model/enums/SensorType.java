package ru.yandex.practicum.analyzer.model.enums;

import lombok.Getter;

import java.util.Set;

import static ru.yandex.practicum.analyzer.model.enums.ConditionType.*;

@Getter
public enum SensorType {
    MOTION_SENSOR(Set.of(TEMPERATURE, HUMIDITY, CO2LEVEL)),
    LIGHT_SENSOR(Set.of(LUMINOSITY)),
    TEMPERATURE_SENSOR(Set.of(MOTION)),
    CLIMATE_SENSOR(Set.of(SWITCH)),
    SWITCH_SENSOR(Set.of(TEMPERATURE));

    private final Set<ConditionType> conditions;

    SensorType(Set<ConditionType> conditions) {
        this.conditions = conditions;
    }
}
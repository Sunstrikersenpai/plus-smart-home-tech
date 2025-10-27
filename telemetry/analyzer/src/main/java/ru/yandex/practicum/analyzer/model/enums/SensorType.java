package ru.yandex.practicum.analyzer.model.enums;

import lombok.Getter;

import java.util.Set;

@Getter
public enum SensorType {
    MOTION_SENSOR(Set.of(ConditionType.MOTION)),
    LIGHT_SENSOR(Set.of(ConditionType.LUMINOSITY)),
    TEMPERATURE_SENSOR(Set.of(ConditionType.TEMPERATURE)),
    CLIMATE_SENSOR(Set.of(ConditionType.TEMPERATURE, ConditionType.HUMIDITY, ConditionType.CO2LEVEL)),
    SWITCH_SENSOR(Set.of(ConditionType.SWITCH));

    private final Set<ConditionType> conditions;

    SensorType(Set<ConditionType> conditions) {
        this.conditions = conditions;
    }
}
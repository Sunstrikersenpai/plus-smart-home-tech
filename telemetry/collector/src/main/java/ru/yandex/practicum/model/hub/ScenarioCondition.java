package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.enums.ScenarioConditionOperation;
import ru.yandex.practicum.model.enums.ScenarioConditionType;

@Getter
@Setter
public class ScenarioCondition {
    @NotBlank
    private String sensorId;

    @NotNull
    private ScenarioConditionType type;

    @NotNull
    private ScenarioConditionOperation operation;

    private int value;
}

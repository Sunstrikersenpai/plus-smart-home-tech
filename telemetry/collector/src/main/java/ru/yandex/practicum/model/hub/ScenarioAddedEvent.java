package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.enums.HubEventType;

import java.util.List;

@Getter
@Setter
public class ScenarioAddedEvent extends BaseHubEvent {
    @NotBlank
    private String name;
    @NotNull
    List<ScenarioCondition> conditions;
    @NotNull
    List<DeviceAction> actions;

    @Override

    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}

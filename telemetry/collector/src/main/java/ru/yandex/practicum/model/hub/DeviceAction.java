package ru.yandex.practicum.model.hub;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.model.enums.DeviceActionType;

@Getter
@Setter
public class DeviceAction {
    @NotBlank
    private String sensorId;
    @NotNull
    private DeviceActionType actionType;

    private int value;
}

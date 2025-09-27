package ru.yandex.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.enums.SensorEventType;

@ToString(callSuper = true)
@Getter
@Setter
public class TemperatureSensorEvent extends BaseSensorEvent {
    @NotNull
    private Integer temperatureC;
    @NotNull
    private Integer temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}

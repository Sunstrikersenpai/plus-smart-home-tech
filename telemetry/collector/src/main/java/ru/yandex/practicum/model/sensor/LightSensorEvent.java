package ru.yandex.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.enums.SensorEventType;

@ToString(callSuper = true)
@Getter
@Setter
public class LightSensorEvent extends BaseSensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private Integer luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}

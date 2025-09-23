package ru.yandex.practicum.model.sensor;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.model.enums.SensorEventType;

@ToString(callSuper = true)
@Getter
@Setter
public class MotionSensorEvent extends BaseSensorEvent {
    @NotNull
    private Integer linkQuality;
    @NotNull
    private boolean motion;
    @NotNull
    private Integer voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}

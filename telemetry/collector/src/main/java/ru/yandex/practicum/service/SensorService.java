package ru.yandex.practicum.service;

import ru.yandex.practicum.model.sensor.BaseSensorEvent;

public interface SensorService {
    void process(BaseSensorEvent event);
}

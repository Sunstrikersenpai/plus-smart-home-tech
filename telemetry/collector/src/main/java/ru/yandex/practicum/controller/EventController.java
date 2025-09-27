package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.hub.BaseHubEvent;
import ru.yandex.practicum.model.sensor.BaseSensorEvent;
import ru.yandex.practicum.service.HubService;
import ru.yandex.practicum.service.SensorService;

@RestController
@RequestMapping("/events")
@Slf4j
@AllArgsConstructor
public class EventController {

    private final HubService hubService;
    private final SensorService sensorService;

    @PostMapping("/sensors")
    public void handleSensorEvent(@Valid @RequestBody BaseSensorEvent sensorEvent) {
        sensorService.process(sensorEvent);
    }

    @PostMapping("/hubs")
    public void handleHubEvent(@Valid @RequestBody BaseHubEvent hubEvent) {
        hubService.process(hubEvent);
    }
}

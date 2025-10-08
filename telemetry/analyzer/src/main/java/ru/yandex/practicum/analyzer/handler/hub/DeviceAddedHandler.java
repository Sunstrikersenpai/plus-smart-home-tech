package ru.yandex.practicum.analyzer.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Sensor;
import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Optional;

@Component
@Slf4j
@AllArgsConstructor
public class DeviceAddedHandler implements HubEventHandler{
    private SensorRepository repo;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceAddedEventAvro payload = (DeviceAddedEventAvro)event.getPayload();
        Optional<Sensor> sensor = repo.findByIdAndHubId(event.getHubId(), payload.getId());

        if(sensor.isPresent()) {
            log.info("DeviceAddedHandler: device with id {} exists",payload.getId());
        } else {
            repo.save(Sensor.builder().hubId(event.getHubId()).id(payload.getId()).build());
            log.info("DeviceAddedHandler: device with id {} added",payload.getId());
        }
    }
}

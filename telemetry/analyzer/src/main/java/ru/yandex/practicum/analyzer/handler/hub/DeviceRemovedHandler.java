package ru.yandex.practicum.analyzer.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.analyzer.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Slf4j
@Component
@AllArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {
    private SensorRepository repo;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }

    @Override
    public void handle(HubEventAvro event) {
        DeviceRemovedEventAvro payload = (DeviceRemovedEventAvro) event.getPayload();

        if (payload != null) {
            repo.deleteByIdAndHubId(payload.getId(), event.getHubId());
            log.info("DeviceRemovedHandler: device with id {} removed", payload.getId());
        }
    }
}

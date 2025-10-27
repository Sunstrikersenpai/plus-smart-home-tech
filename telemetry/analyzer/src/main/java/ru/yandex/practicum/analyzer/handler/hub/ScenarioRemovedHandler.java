package ru.yandex.practicum.analyzer.handler.hub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
@AllArgsConstructor
@Slf4j
public class ScenarioRemovedHandler implements HubEventHandler {
    private ScenarioRepository repo;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventAvro event) {
        ScenarioRemovedEventAvro payload = (ScenarioRemovedEventAvro) event.getPayload();

        if (repo.existsByHubIdAndName(event.getHubId(), payload.getName())) {
            repo.deleteByHubIdAndName(event.getHubId(), payload.getName());
            log.info("ScenarioRemovedHandler: scenario {} removed", payload.getName());
        } else {
            log.info("ScenarioRemovedHandler: scenario {} doesnt exist", payload.getName());
        }
    }
}

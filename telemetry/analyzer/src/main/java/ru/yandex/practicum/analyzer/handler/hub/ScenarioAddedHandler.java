package ru.yandex.practicum.analyzer.handler.hub;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Condition;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.analyzer.model.enums.ActionType;
import ru.yandex.practicum.analyzer.model.enums.ConditionOperation;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;


@Component
@AllArgsConstructor
@Slf4j
public class ScenarioAddedHandler implements HubEventHandler {

    private final ScenarioRepository repo;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    @Transactional
    public void handle(HubEventAvro event) {
        ScenarioAddedEventAvro payload = (ScenarioAddedEventAvro) event.getPayload();
        if (payload == null){
            log.info("ScenarioAddedHandler: payload is null");
            return;
        }
        
        Scenario scenario = repo
                .findByHubIdAndName(event.getHubId(), payload.getName())
                .orElseGet(() -> Scenario.builder()
                        .hubId(event.getHubId()).name(payload.getName()).build());
        
        scenario.getConditions().clear();
        scenario.getActions().clear();
        
        payload.getConditions().forEach(c -> {
            Condition condition = new Condition();
            condition.setType(ConditionType.valueOf(c.getType().name()));
            condition.setOperation(ConditionOperation.valueOf(c.getOperation().name()));
            condition.setValue(normalizeValue(c.getValue()));
            scenario.getConditions().put(c.getSensorId(), condition);
        });
        
        payload.getActions().forEach(a -> {
            Action action = new Action();
            action.setType(ActionType.valueOf(a.getType().name()));
            if (a.getValue() != null) action.setValue(a.getValue());
            scenario.getActions().put(a.getSensorId(), action);
        });

        repo.save(scenario);
        log.info("ScenarioAddedHandler: scenario {} added",scenario.getName());
    }

    private Integer normalizeValue(Object value) {
        Integer normalizedValue = null;

        if (value instanceof Integer i) {
            normalizedValue = i;
        } else if (value instanceof Boolean b) {
            normalizedValue = b ? 1 : 0;
        }

        return normalizedValue;
    }
}

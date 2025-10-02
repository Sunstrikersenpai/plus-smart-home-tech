package ru.yandex.practicum.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class SnapshotServiceImpl implements SnapshotService {

    private final Map<String, SensorsSnapshotAvro> sensors = new HashMap<>();

    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        log.debug("Обновление состояния: hubId={}, sensorId={}, timestamp={}",
                event.getHubId(), event.getId(), event.getTimestamp());

        SensorsSnapshotAvro snapshot = sensors.computeIfAbsent(
                event.getHubId(),
                hubId -> SensorsSnapshotAvro.newBuilder()
                        .setHubId(hubId)
                        .setTimestamp(event.getTimestamp())
                        .setSensorsState(new HashMap<>())
                        .build()
        );

        Map<String, SensorStateAvro> sensorsState = snapshot.getSensorsState();

        if (sensorsState.containsKey(event.getId())) {
            SensorStateAvro oldState = sensorsState.get(event.getId());
            if (oldState.getTimestamp().isAfter(event.getTimestamp())
                    || oldState.getData().equals(event.getPayload())) {
                log.debug("Данные не изменились: hubId={}, sensorId={}",
                        event.getHubId(), event.getId());
                return Optional.empty();
            }
        }

        SensorStateAvro newState = SensorStateAvro.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setData(event.getPayload())
                .build();

        sensorsState.put(event.getId(), newState);
        snapshot.setTimestamp(event.getTimestamp());
        log.info("Состояние обновлено: hubId={}, sensorId={}, timestamp={}",
                event.getHubId(), event.getId(), event.getTimestamp());
        return Optional.of(snapshot);
    }
}

package ru.yandex.practicum.analyzer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.snapshot.SnapshotEventHandler;
import ru.yandex.practicum.analyzer.model.enums.SensorType;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotProcessor implements Runnable {
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final KafkaProperties prop;
    private final Map<SensorType, SnapshotEventHandler> snapshotHandlers;

    public SnapshotProcessor(
            KafkaConsumer<String, SensorsSnapshotAvro> consumer,
            KafkaProperties prop,
            Set<SnapshotEventHandler> handlers
    ) {
        this.consumer = consumer;
        this.prop = prop;
        this.snapshotHandlers = handlers.stream()
                .collect(Collectors.toMap(SnapshotEventHandler::getType, Function.identity()));
    }

    @Override
    public void run() {
        consumer.subscribe(List.of(prop.getTopics().getSnapshotsEvents()));


        try {
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        consumer.poll(Duration.ofMillis(prop.getConsumer().getSnapshot().getTimeOut()));

                if (records.isEmpty()) continue;

                for (ConsumerRecord<String, SensorsSnapshotAvro> rec : records) {
                    try {
                        processSnapshot(rec.value());
                    } catch (Exception e) {
                        log.error("Error processing snapshot record: {}", rec, e);
                    }
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {
            log.info("SnapshotProcessor interrupted by WakeupException");
        } finally {
            consumer.close();
        }
    }

    private void processSnapshot(SensorsSnapshotAvro snapshot) {
        String hubId = snapshot.getHubId();
        log.debug("Получен снапшот от {}", hubId);

        snapshot.getSensorsState().forEach((sensorId, state) -> {
            Object data = state.getData();
            if (data == null) {
                return;
            }

            SensorType sensorType = resolveType(data);
            if (sensorType == null) {
                log.warn("Неизвестный тип сенсора: {}", data.getClass().getSimpleName());
                return;
            }

            SnapshotEventHandler handler = snapshotHandlers.get(sensorType);
            if (handler != null) {
                handler.handle(hubId, sensorId, snapshot.getTimestamp(), state);
            } else {
                log.warn("Нет хендлера для сенсора {}", sensorType);
            }
        });
    }

    private SensorType resolveType(Object payload) {
        if (payload == null) {
            return null;
        }

        return switch (payload.getClass().getSimpleName()) {
            case "ClimateSensorAvro" -> SensorType.CLIMATE_SENSOR;
            case "LightSensorAvro" -> SensorType.LIGHT_SENSOR;
            case "MotionSensorAvro" -> SensorType.MOTION_SENSOR;
            case "SwitchSensorAvro" -> SensorType.SWITCH_SENSOR;
            case "TemperatureSensorAvro" -> SensorType.TEMPERATURE_SENSOR;
            default -> null;
        };
    }
}

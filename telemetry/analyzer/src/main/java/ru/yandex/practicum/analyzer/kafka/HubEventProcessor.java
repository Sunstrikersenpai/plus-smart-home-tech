package ru.yandex.practicum.analyzer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.hub.HubEventHandler;
import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final KafkaProperties prop;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public HubEventProcessor(
            @Qualifier("hubConsumer") KafkaConsumer<String, HubEventAvro> consumer,
            KafkaProperties prop,
            Set<HubEventHandler> hubEventHandlers
    ) {
        this.consumer = consumer;
        this.prop = prop;
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getType, Function.identity()));
    }

    @Override
    public void run() {
        consumer.subscribe(List.of(prop.getTopics().getHubEvents()));

        try {
            while (true) {
                ConsumerRecords<String, HubEventAvro> records =
                        consumer.poll(Duration.ofMillis(prop.getConsumer().getHub().getTimeOut()));

                if (records.isEmpty()) continue;

                for (ConsumerRecord<String, HubEventAvro> rec : records) {
                    try {
                        HubEventAvro event = rec.value();
                        HubEventType type = resolveType(event);
                        HubEventHandler handler = hubEventHandlers.get(type);

                        if (handler != null) {
                            handler.handle(event);
                        } else {
                            log.warn("No handler found for type: {}", type);
                        }
                    } catch (Exception e) {
                        log.error("Error processing HubEvent record: {}", rec, e);
                    }
                }
                consumer.commitSync();
            }
        } catch (WakeupException e) {

        } finally {
            consumer.close();
        }
    }

    private HubEventType resolveType(HubEventAvro event) {
        Object payload = event.getPayload();
        if (payload == null) return null;

        return switch (payload.getClass().getSimpleName()) {
            case "DeviceAddedEventAvro" -> HubEventType.DEVICE_ADDED;
            case "DeviceRemovedEventAvro" -> HubEventType.DEVICE_REMOVED;
            case "ScenarioAddedEventAvro" -> HubEventType.SCENARIO_ADDED;
            case "ScenarioRemovedEventAvro" -> HubEventType.SCENARIO_REMOVED;
            default -> null;
        };
    }
}
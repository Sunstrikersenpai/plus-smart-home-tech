package ru.yandex.practicum.aggregator.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.aggregator.config.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class AggregationStarter {

    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private final KafkaProducer<String, SensorsSnapshotAvro> producer;
    private final KafkaProperties prop;
    private final SnapshotService snapshotService;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        try {
            consumer.subscribe(List.of(prop.getTopics()));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(
                        Duration.ofMillis(prop.getConsumer().getTimeoutMs()));

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro event = record.value();
                    Optional<SensorsSnapshotAvro> snapshot = snapshotService.updateState(event);
                    snapshot.ifPresent(s -> producer.send(
                            new ProducerRecord<>(prop.getTopics(), s)));
                }
                consumer.commitAsync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }

        }
    }
}


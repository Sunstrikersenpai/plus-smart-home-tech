package ru.yandex.practicum.collector.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class KafkaEventProducer implements AutoCloseable {
    private final KafkaProducer<String, SpecificRecordBase> producer;

    public void send(String topic, String key, SpecificRecordBase record) {
        producer.send(new ProducerRecord<>(topic, key, record), (recordMetadata, e) -> {
            if (e != null) {
                log.error("Ошибка при отправке в Kafka", e);
            }
        });
    }

    public void close() {
        producer.close();
    }
}

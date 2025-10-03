package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
public class HubEventProcessor implements Runnable {
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final KafkaProperties prop;

    public HubEventProcessor(
            @Qualifier("hubConsumer") KafkaConsumer<String, HubEventAvro> consumer,
            KafkaProperties prop
    ) {
        this.consumer = consumer;
        this.prop = prop;
    }

    @Override
    public void run() {

        try {
            consumer.subscribe(List.of(prop.getTopics().getHubEvents()));

            while (true) {
                ConsumerRecords<String,HubEventAvro> records = consumer.poll(
                        Duration.ofMillis(prop.getConsumer().getHub().getTimeOut()));

                for (ConsumerRecord<String,HubEventAvro> rec:records) {
                    HubEventAvro event = rec.value();
                    //?? сервис обрабатывает,наверное отдает опшинал,если пустой то скип,если нет то обработка
                }
            }
        } finally {
            consumer.close();
        }
    }
}
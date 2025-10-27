package ru.yandex.practicum.analyzer.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean("hubConsumer")
    public KafkaConsumer<String, HubEventAvro> createHubConsumer(KafkaProperties prop) {
        Properties config = new Properties();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getConsumer().getHub().getBootstrapServer());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getConsumer().getHub().getGroupId());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getHub().getKeyDeserializer());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getHub().getValueDeserializer());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, prop.getConsumer().getHub().isEnableAutoCommit());

        return new KafkaConsumer<>(config);
    }

    @Bean("snapshotConsumer")
    public KafkaConsumer<String, SensorsSnapshotAvro> createSnapshotConsumer(KafkaProperties prop) {
        Properties config = new Properties();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getConsumer().getSnapshot().getBootstrapServer());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getConsumer().getSnapshot().getGroupId());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getSnapshot().getKeyDeserializer());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getSnapshot().getValueDeserializer());
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, prop.getConsumer().getSnapshot().isEnableAutoCommit());

        return new KafkaConsumer<>(config);
    }
}
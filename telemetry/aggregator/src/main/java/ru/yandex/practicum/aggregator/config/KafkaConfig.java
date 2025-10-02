package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> createProducer(KafkaProperties prop) {
        Properties config = new Properties();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProducer().getBootstrapServer());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, prop.getProducer().getKeySerializer());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, prop.getProducer().getValueSerializer());

        return new KafkaProducer<>(config);
    }

    @Bean
    public KafkaConsumer<String, SensorEventAvro> createConsumer(KafkaProperties prop) {
        Properties config = new Properties();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,prop.getConsumer().getBootstrapServer());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getKeyDeserializer());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, prop.getConsumer().getValueDeserializer());
        config.put(ConsumerConfig.GROUP_ID_CONFIG,prop.getConsumer().getGroupId());

        return new KafkaConsumer<>(config);
    }
}

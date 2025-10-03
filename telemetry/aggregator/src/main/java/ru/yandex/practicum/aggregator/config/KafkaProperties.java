package ru.yandex.practicum.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ConfigurationProperties(prefix = "app.kafka")
@Component
public class KafkaProperties {
    private Producer producer;
    private Consumer consumer;
    private Topic topic;

    @Getter
    @Setter
    public static class Producer{
        private String bootstrapServer;
        private String keySerializer;
        private String valueSerializer;
    }

    @Getter
    @Setter
    public static class Consumer {
        private String groupId;
        private String bootstrapServer;
        private String keyDeserializer;
        private String valueDeserializer;
        private long timeoutMs;
    }

    @Getter
    @Setter
    public static class Topic {
        private String sensors;
        private String snapshots;
    }
}
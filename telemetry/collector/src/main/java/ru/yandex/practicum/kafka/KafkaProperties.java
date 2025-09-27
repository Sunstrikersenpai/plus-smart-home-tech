package ru.yandex.practicum.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.kafka")
@Component
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapServer;
    private Topic topic = new Topic();

    @Getter
    @Setter
    public static class Topic {
        private String sensors;
        private String hubs;
    }
}

package ru.yandex.practicum.analyzer.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.kafka")
@Getter
@Setter
@Component
public class KafkaProperties {

    private Consumer consumer;
    private Topics topics;

    @Getter
    @Setter
    public static class Consumer {
        private Hub hub;
        private Snapshot snapshot;

        @Getter
        @Setter
        public static class Hub {
            private String bootstrapServer;
            private String keyDeserializer;
            private String valueDeserializer;
            private String groupId;
            private boolean enableAutoCommit;
            private long timeOut;
        }

        @Getter
        @Setter
        public static class Snapshot {
            private String bootstrapServer;
            private String keyDeserializer;
            private String valueDeserializer;
            private String groupId;
            private boolean enableAutoCommit;
            private long timeOut;
        }
    }

    @Getter
    @Setter
    public static class Topics {
        private String hubEvents;
        private String snapshotsEvents;
    }
}

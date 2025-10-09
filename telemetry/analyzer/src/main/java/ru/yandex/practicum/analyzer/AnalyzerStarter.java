package ru.yandex.practicum.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.kafka.HubEventProcessor;
import ru.yandex.practicum.analyzer.kafka.SnapshotProcessor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class AnalyzerStarter implements SmartLifecycle {

    private final HubEventProcessor hubEventProcessor;
    private final SnapshotProcessor snapshotProcessor;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private volatile boolean running = false;

    public AnalyzerStarter(HubEventProcessor hubEventProcessor, SnapshotProcessor snapshotProcessor) {
        this.hubEventProcessor = hubEventProcessor;
        this.snapshotProcessor = snapshotProcessor;
    }

    @Override
    public void start() {
        executor.submit(hubEventProcessor);
        executor.submit(snapshotProcessor);
        running = true;
    }

    @Override
    public void stop() {
        executor.shutdownNow();
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}

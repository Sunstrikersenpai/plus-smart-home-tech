package ru.yandex.practicum.service;

import ru.yandex.practicum.model.hub.BaseHubEvent;

public interface HubService {
    void process(BaseHubEvent event);
}

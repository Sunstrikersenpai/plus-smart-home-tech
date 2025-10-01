package ru.yandex.practicum.service.hubHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.KafkaEventProducer;
import ru.yandex.practicum.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {

    public DeviceRemovedEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(props, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }

    @Override
    protected DeviceRemovedEventAvro toAvro(HubEventProto event) {
        DeviceRemovedEventProto payload = event.getDeviceRemoved();
        return DeviceRemovedEventAvro.newBuilder()
                .setId(payload.getId())
                .build();
    }
}
package ru.yandex.practicum.collector.service.hubHandlers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.collector.kafka.KafkaEventProducer;
import ru.yandex.practicum.collector.kafka.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {

    public DeviceAddedEventHandler(KafkaEventProducer producer, KafkaProperties props) {
        super(props, producer);
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }


    @Override
    protected DeviceAddedEventAvro toAvro(HubEventProto event) {
        DeviceAddedEventProto payload = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setId(payload.getId())
                .setType(DeviceTypeAvro.valueOf(payload.getType().name()))
                .build();
    }
}
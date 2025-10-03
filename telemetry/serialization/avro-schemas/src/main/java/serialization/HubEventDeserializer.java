package serialization;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends GenericAvroDeserializer<HubEventAvro>{
    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}

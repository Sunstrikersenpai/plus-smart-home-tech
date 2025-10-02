package serialization;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;

public class GenericAvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {

    @Override
    public byte[] serialize(String topic, T data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            writer.write(data, encoder);
            encoder.flush();
            return out.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Ошибка сериализации Avro", e);
        }
    }
}
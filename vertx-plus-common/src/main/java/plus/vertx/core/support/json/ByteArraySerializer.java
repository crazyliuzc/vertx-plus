package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Base64;
/**
 * ByteArray序列化器
 * @author crazyliu
 */
public class ByteArraySerializer extends JsonSerializer<byte[]> {
    public ByteArraySerializer() {
    }

    @Override
    public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(Base64.getEncoder().encodeToString(value));
    }
}

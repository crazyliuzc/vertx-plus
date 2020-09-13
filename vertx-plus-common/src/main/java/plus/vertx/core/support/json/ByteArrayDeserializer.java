package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;

/**
 * @author crazyliu
 */
public class ByteArrayDeserializer extends JsonDeserializer<byte[]> {
    public ByteArrayDeserializer() {
    }

    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String text = p.getText();

        try {
            return Base64.getDecoder().decode(text);
        } catch (IllegalArgumentException var5) {
            throw new InvalidFormatException(p, "Expected a base64 encoded byte array", text, Instant.class);
        }
    }
}

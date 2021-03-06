package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Instant反序列化器
 * @author crazyliu
 */
public class InstantDeserializer  extends JsonDeserializer<Instant> {
    public InstantDeserializer() {
    }

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();

        try {
            return Instant.from(DateTimeFormatter.ISO_INSTANT.parse(text));
        } catch (DateTimeException var5) {
            throw new InvalidFormatException(p, "Expected an ISO 8601 formatted date time", text, Instant.class);
        }
    }
}

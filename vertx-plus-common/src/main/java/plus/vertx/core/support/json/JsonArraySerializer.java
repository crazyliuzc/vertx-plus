package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.vertx.core.json.JsonArray;

import java.io.IOException;

/**
 * @author crazyliu
 */
public class JsonArraySerializer extends JsonSerializer<JsonArray> {
    public JsonArraySerializer() {
    }

    @Override
    public void serialize(JsonArray value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getList());
    }
}

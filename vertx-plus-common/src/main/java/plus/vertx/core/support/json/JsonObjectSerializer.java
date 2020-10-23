package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * JsonObject序列化器
 * @author crazyliu
 */
public class JsonObjectSerializer  extends JsonSerializer<JsonObject> {
    public JsonObjectSerializer() {
    }

    @Override
    public void serialize(JsonObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getMap());
    }
}

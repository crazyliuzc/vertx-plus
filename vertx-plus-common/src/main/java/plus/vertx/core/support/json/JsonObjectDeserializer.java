package plus.vertx.core.support.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * JsonObject解析
 *
 * @author crazyliu
 */
public class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {
    @Override
    public JsonObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        return new JsonObject(node.toString());
    }
}

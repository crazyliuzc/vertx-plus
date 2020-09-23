package plus.vertx.core.support.yaml;

import io.vertx.core.json.JsonObject;
import java.io.Serializable;

/**
 * Yaml配置文件转化的实体
 * @author crazyliu
 */
public class YamlBean implements Serializable {
    public YamlBean() {
    }
    
    public YamlBean(JsonObject config) {
    }
}

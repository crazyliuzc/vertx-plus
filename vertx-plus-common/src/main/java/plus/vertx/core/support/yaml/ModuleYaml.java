package plus.vertx.core.support.yaml;

import io.vertx.core.json.JsonObject;
import java.io.Serializable;

/**
 * 项目配置
 *
 * @author crazyliu
 */
public class ModuleYaml implements Serializable {

    /**
     * 模块项目名
     */
    private String name;
    /**
     * 待扫描的启动类包路径
     */
    private String startScan;

    public ModuleYaml() {
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartScan() {
        return startScan;
    }

    public void setStartScan(String startScan) {
        this.startScan = startScan;
    }


}

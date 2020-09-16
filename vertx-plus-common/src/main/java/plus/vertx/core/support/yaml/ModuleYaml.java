package plus.vertx.core.support.yaml;

import io.vertx.core.json.JsonObject;
import java.io.Serializable;

/**
 * 项目配置
 * @author crazyliu
 */
public class ModuleYaml implements Serializable {
    /**
     * 模块项目名
     */
    private String name;
    /**
     * 模块地址
     */
    private String host;
    /**
     * 模块端口号
     */
    private int port;
    /**
     * 模块网址
     */
    private String site;
    /**
     * 待扫描的启动类包路径
     */
    private String verticleScan;
    /**
     * 待扫描的服务包路径
     */
    private String serviceScan;
    
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getVerticleScan() {
        return verticleScan;
    }

    public void setVerticleScan(String verticleScan) {
        this.verticleScan = verticleScan;
    }

    public String getServiceScan() {
        return serviceScan;
    }

    public void setServiceScan(String serviceScan) {
        this.serviceScan = serviceScan;
    }
    
    
}

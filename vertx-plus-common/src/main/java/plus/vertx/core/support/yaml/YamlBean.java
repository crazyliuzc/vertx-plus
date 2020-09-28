package plus.vertx.core.support.yaml;

import io.vertx.core.json.JsonObject;
import java.io.Serializable;
import plus.vertx.core.support.BeanCopyUtil;

/**
 * Yaml配置文件转化的实体
 * @author crazyliu
 */
public class YamlBean implements Serializable {
    /**
     * 分布式配置
     */
    private ClusterYaml cluster;
    
    /**
     * 模块配置
     */
    private ModuleYaml module;
    
    public YamlBean() {
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public YamlBean(JsonObject config) {
        BeanCopyUtil.mapToBean(config.getMap(), this);
    }

    public ClusterYaml getCluster() {
        return cluster;
    }

    public void setCluster(ClusterYaml cluster) {
        this.cluster = cluster;
    }

    public ModuleYaml getModule() {
        return module;
    }

    public void setModule(ModuleYaml module) {
        this.module = module;
    }
    
    
}

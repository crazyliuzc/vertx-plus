package plus.vertx.core.support.yaml;

import java.io.Serializable;

/**
 * Yaml配置文件转化的实体
 * @author crazyliu
 */
public class YamlBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2136132874479545050L;

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

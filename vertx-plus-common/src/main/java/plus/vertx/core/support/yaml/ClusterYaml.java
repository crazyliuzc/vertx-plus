package plus.vertx.core.support.yaml;

import java.io.Serializable;

/**
 * 分布式配置
 *
 * @author crazyliu
 */
public class ClusterYaml implements Serializable {

    public ClusterYaml() {
    }

    /**
     * 是否启用分布式
     */
    private boolean enable = false;

    /**
     * 启用哪一种分布式
     */
    private String type = "Hazelcast";

    public enum ClusterType {
        Hazelcast("Hazelcast"), Zookeeper("Zookeeper");
        private final String clusterType;

        private ClusterType(String clusterType) {
            this.clusterType = clusterType;
        }

        @Override
        public String toString() {
            return this.clusterType;
        }
    }

    /**
     * 分布式名称
     */
    private String name;

    /**
     * 分布式密码
     */
    private String password;

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

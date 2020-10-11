package plus.vertx.core.support.yaml;

import java.io.Serializable;

/**
 * 分布式配置
 *
 * @author crazyliu
 */
public class ClusterYaml implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5071897041463099816L;

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
    private String name = "dev";

    /**
     * 分布式外部节点IP地址
     */
    private String address;

    /**
     * 分布式节点连接超时时间,单位秒,默认5秒
     */
    private int timeout = 5;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}

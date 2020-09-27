package plus.vertx.core.support.yaml;

import com.hazelcast.core.Hazelcast;
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
    private Boolean enable;

    /**
     * 启用哪一个分布式
     */
    private ClusterType clusterType;
    
    /**
     * 分布式名称
     */
    private String clusterName;
    
    /**
     * 分布式密码
     */
    private String password;
    
    /**
     * Hazelcast配置
     */
    private HazelcastYaml hazelcast;

    private enum ClusterType {
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
    
    private class HazelcastYaml {
        private int port;
        private long timeout;

        public HazelcastYaml() {
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public long getTimeout() {
            return timeout;
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }
        
    }
}

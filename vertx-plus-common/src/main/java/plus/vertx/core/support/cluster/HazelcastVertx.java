package plus.vertx.core.support.cluster;

import com.hazelcast.config.*;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.vertx.core.startup.BaseStart;
import plus.vertx.core.support.IpUtil;
import plus.vertx.core.support.ValidateUtil;

/**
 * @author crazyliu
 * @date 2020年10月7日, 0007
 */
public class HazelcastVertx {
    public static final Logger log = LoggerFactory.getLogger(HazelcastVertx.class);

    /**
     * 创建Hazelcast分布式
     * @param clusterName 分布式名称
     * @param requiredMemberIp 外部成员IP
     * @param timeout 分布式节点连接超时时间,单位秒,默认5秒
     * @return vertx
     */
    public static Future<Vertx> getHazelcastVertx(String clusterName,String requiredMemberIp,int timeout) {
        Promise<Vertx> result = Promise.promise();
        //Hazelcast的日志格式，可以使用slf4j，也可以选择不记录日志none
        System.setProperty("hazelcast.logging.type", "slf4j");
        //强制Hazelcast使用IPv4，因为Hazelcast默认选择IPv6
        System.setProperty("java.net.preferIPv4Stack", "true");
        Config hazelcastConfig = new Config();
        hazelcastConfig.setLiteMember(true);
        GroupConfig groupConfig = new GroupConfig();
        if (ValidateUtil.isEmpty(clusterName)) {
            clusterName = "dev";
        }
        groupConfig.setName(clusterName);
        hazelcastConfig.setGroupConfig(groupConfig);
        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
        managementCenterConfig.setEnabled(false);
        hazelcastConfig.setManagementCenterConfig(managementCenterConfig);
        // 获取network元素<network></network>
        NetworkConfig netConfig = new NetworkConfig();
        // 设置组网起始监听端口
        netConfig.setPort(10701);
        netConfig.setPortAutoIncrement(true);
        netConfig.setPortCount(1000);

        // 获取join元素<join></join>
        JoinConfig joinConfig = new JoinConfig();

        // 获取multicast元素<multicast></multicast>
        MulticastConfig multicastConfig = new MulticastConfig();
        // 禁用multicast协议
        multicastConfig.setEnabled(false);
        joinConfig.setMulticastConfig(multicastConfig);

        // 获取tcp元素
        TcpIpConfig tcpIpConfig = new TcpIpConfig();
        tcpIpConfig.setEnabled(true);
        String localIp = IpUtil.getLocalIp();
        if (ValidateUtil.isEmpty(requiredMemberIp)) {
            requiredMemberIp = localIp;
        }
        tcpIpConfig.setRequiredMember(requiredMemberIp);
        tcpIpConfig.addMember(localIp);
        tcpIpConfig.setConnectionTimeoutSeconds(timeout);
        joinConfig.setTcpIpConfig(tcpIpConfig);

        netConfig.setJoin(joinConfig);
        hazelcastConfig.setNetworkConfig(netConfig);

        ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
        VertxOptions options = new VertxOptions();
        options
                .setClusterManager(mgr)
                .setEventBusOptions(new EventBusOptions().setHost(localIp).setClustered(true).setConnectTimeout(400000))
                .setBlockedThreadCheckInterval(999999999L)
                .setEventLoopPoolSize(20)
                .setMaxEventLoopExecuteTime(Long.MAX_VALUE)
                .setWorkerPoolSize(256)
                .setMaxWorkerExecuteTime(Long.MAX_VALUE)
                .setWarningExceptionTime(Long.MAX_VALUE)
                .setPreferNativeTransport(true);
        log.info("尝试加入Hazelcast分布式服务节点...");
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                log.info("成功加入Hazelcast分布式服务节点");
                result.complete(res.result());
            } else {
                log.error("加入Hazelcast分布式服务节点失败",res.cause());
                result.fail(res.cause());
            }
        });
        return result.future();
    }
}

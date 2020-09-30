package plus.vertx.core.support;

import com.hazelcast.config.*;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Vertx启动工具类
 * @author crazyliu
 */
public class VertxUtil {
    private static final Logger log = LoggerFactory.getLogger(VertxUtil.class);

    public static Future<Void> close(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        vertx.close(ar->{
            if (ar.succeeded()) {
                result.complete();
            } else {
                log.error("",ar.cause());
                result.fail(ar.cause());
            }
        });
        return result.future();
    }
    
    /**
     * 获取vertx
     * @return
     */
    public static Vertx getVertx() {
        EventBusOptions eventBusOptions = new EventBusOptions();
        eventBusOptions.setConnectTimeout(400000);
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions
                .setEventBusOptions(eventBusOptions)
                .setBlockedThreadCheckInterval(999999999L)
                .setEventLoopPoolSize(20)
                .setMaxEventLoopExecuteTime(Long.MAX_VALUE)
                .setWorkerPoolSize(256)
                .setMaxWorkerExecuteTime(Long.MAX_VALUE)
                .setWarningExceptionTime(Long.MAX_VALUE)
                .setPreferNativeTransport(true);
        Vertx vertx = Vertx.vertx(vertxOptions);
        return vertx;
    }

    /**
     * 创建Hazelcast分布式
     * @param clusterName 分布式名称
     * @param requiredMemberIp
     * @param timeout 分布式节点连接超时时间,单位秒,默认5秒
     * @return
     */
    public static Future<Vertx> getHazelcastVertx(String clusterName,String requiredMemberIp,int timeout) {
        Promise<Vertx> result = Promise.promise();
        System.setProperty("hazelcast.logging.type", "slf4j");
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
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                result.complete(res.result());
            } else {
                log.error("",res.cause());
                result.fail(res.cause());
            }
        });
        return result.future();
    }
    
    /**
     * 执行verticle类
     * @param verticle
     * @param vertx
     * @param deploymentOptions
     * @return 
     */
    public static Future<String> run(Verticle verticle, Vertx vertx, DeploymentOptions deploymentOptions) {
        Promise<String> result = Promise.promise();
        Consumer<Vertx> runner = (Vertx vert) -> {
            try {
                if (deploymentOptions != null) {
                    vert.deployVerticle(verticle, deploymentOptions, result);
                } else {
                    vert.deployVerticle(verticle, result);
                }
            } catch (Throwable t) {
                log.error("",t);
            }
        };
        runner.accept(vertx);
        return result.future();
    }
    
    /**
     * 执行verticle类
     * @param clazz
     * @param vertx
     * @param deploymentOptions
     * @return 
     */
    public static Future<String> run(Class clazz, Vertx vertx, DeploymentOptions deploymentOptions) {
        return run((Verticle)BeanUtil.newInstance(clazz), vertx, deploymentOptions);
    }

}

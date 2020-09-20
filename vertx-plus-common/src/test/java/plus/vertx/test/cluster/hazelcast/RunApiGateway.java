package plus.vertx.test.cluster.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
/**
 *
 * @author crazyliu
 */
public class RunApiGateway {
    public static void main(String[] args) {  
        // Hazelcast配置类  
        Config cfg = new Config();  
  
        // 关闭UDP组播，采用TCP进行集群通信。  
        JoinConfig joinConfig = cfg.getNetworkConfig().getJoin();  
        joinConfig.getMulticastConfig().setEnabled(false);  
        joinConfig.getTcpIpConfig().setEnabled(true);  
        joinConfig.getTcpIpConfig().addMember("192.168.6.100,192.168.6.132,192.168.6.130");// 有多个目标节点，就需要写多少地址。  
  
        // 这里指定所用通信的网卡（在本机多个网卡时如不指定会有问题，无论有无多个网卡最好设置一下。）  
        cfg.getNetworkConfig().getInterfaces().setEnabled(true);  
        cfg.getNetworkConfig().getInterfaces().addInterface("192.168.6.*");  
  
        // 申明集群管理器  
        ClusterManager mgr = new HazelcastClusterManager(cfg);  
        VertxOptions options = new VertxOptions().setClusterManager(mgr);  
        
        EventBusOptions eventBusOptions = new EventBusOptions();
        eventBusOptions.setClustered(true);
        eventBusOptions.setHost("192.168.1.100");//这个一定要设置（在本机有多个网卡的时候，如果不设置，会收不到消息。）  
        options.setEventBusOptions(eventBusOptions);
        
        // 集群化vertx   
        Vertx.clusteredVertx(options, res -> {  
            if (res.succeeded()) {  
                Vertx vertx = res.result();  
//                vertx.deployVerticle(ApiGatewayVerticle.class.getName());  
                System.out.println("Api Gateway : cluster succeeded");  
            } else {  
                res.cause().printStackTrace();  
            }  
        }); 
        
        
//            suspend fun getVertx(): Vertx {
//                val hazelcastConfig = com.hazelcast.config.Config()
//
//                hazelcastConfig.setProperty( "hazelcast.logging.type", "slf4j" );
//                hazelcastConfig.setProperty("hazelcast.discovery.enabled", "true")
//
//                hazelcastConfig.networkConfig.join.multicastConfig.isEnabled = false
//                hazelcastConfig.networkConfig.join.tcpIpConfig.isEnabled = false
//
//                val strategyConfig = DiscoveryStrategyConfig(HazelcastKubernetesDiscoveryStrategyFactory());
//                strategyConfig.addProperty("namespace", "default")
//                strategyConfig.addProperty("service-name", "vertx-cluster-service")
//                hazelcastConfig.getNetworkConfig().getJoin().getDiscoveryConfig().addDiscoveryStrategyConfig(strategyConfig);
//
//                val mgr = HazelcastClusterManager(hazelcastConfig);
//                val options = VertxOptions().setClusterManager(mgr)
//
//                return awaitResult<Vertx> {
//                    Vertx.clusteredVertx(options, it)
//                }
//        }

//        默认情况下，如果Hazelcast在300秒内未收到心跳，则将从群集中删除节点。要更改此值hazelcast.max.no.heartbeat.seconds系统属性，例如：
//        -Dhazelcast.max.no.heartbeat.seconds = 5

//        使用Hazelcast异步方法
//        Hazelcast IMap和IAtomicLong接口可与returning异步方法一起使用ICompletableFuture<V>，这自然适合Vert.x线程模型。
//        提供-Dvertx.hazelcast.async-api=true有关JVM启动的选项，将指示将使用异步Hazelcast API方法与hazelcast集群进行通信。实际上，这意味着，当启用该选项时，所有的执行Counter操作和AsyncMap.get，AsyncMap.put以及AsyncMap.remove将发生在调用线程（事件循环）操作，而不是用一个工作线程vertx.executeBlocking。
    }  
}

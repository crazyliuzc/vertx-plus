package plus.vertx.core.startup;

import com.hazelcast.config.Config;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import plus.vertx.core.Constants;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.support.CopyUtil;
import plus.vertx.core.support.LoadUtil;
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.yaml.ClusterYaml;
import plus.vertx.core.support.yaml.YamlBean;

/**
 * 公共启动服务
 *
 * @author crazyliu
 */
public class MainVerticle extends BaseStart {

    /**
     * 按照顺序启动start注释服务类
     *
     * @param vertx
     * @return
     */
    @Override
    public Future<Void> action(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        JsonObject config = config();
        getYaml(config).onComplete(rs -> {
            if (rs.succeeded()) {
                //转换DeploymentOptions配置
                YamlBean yamlBean = rs.result();
                if (null==yamlBean) {
                    log.error("找不到项目配置文件...");
                    result.fail("找不到项目配置文件...");
                } else {
                    if (null==yamlBean.getCluster() || !yamlBean.getCluster().getEnable()) {
                        //没有开启分布式
                        Vertx commonVertx = Vertx.vertx();
                        //扫描启动服务，并按照顺序启动
                        result.complete();
                    } else {
                        //开启分布式
                        ClusterYaml clusterYaml = yamlBean.getCluster();
                        if (clusterYaml.getType().equals(ClusterYaml.ClusterType.Hazelcast)) {
                            Config hazelcastConfig = new Config();
                            ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
                            VertxOptions options = new VertxOptions().setClusterManager(mgr);
                            Vertx.clusteredVertx(options, res -> {
                                if (res.succeeded()) {
                                    Vertx commonVertx = res.result();
                                    //扫描启动服务，并按照顺序启动
                                    result.complete();
                                } else {
                                    log.error("",res.cause());
                                    result.fail(res.cause());
                                }
                            });
                        } else {
                            log.error("暂不支持其它分布式..");
                            result.fail("暂不支持其它分布式..");
                        }
                    }
                }
            } else {
                result.fail(rs.cause());
            }
        });
        return result.future();
    }

    /**
     * 获取项目配置信息，并转化成类
     *
     * @param config
     * @return
     */
    public static Future<YamlBean> getYaml(JsonObject config) {
        Promise<YamlBean> result = Promise.promise();
        Vertx vertx = Vertx.currentContext().owner();
        if (null != Constants.CONFIG) {
            //关闭公共启动用的临时vertx
            vertx.close();
            result.complete(Constants.CONFIG);
        } else if (null != config && !config.isEmpty()) {
            //从外部json文件读取
            log.info("Read configuration files from outside...");
            Constants.CONFIG = CopyUtil.toBean(config,YamlBean.class);
            log.info("The configuration file was read successfully...");
            //关闭公共启动用的临时vertx
            vertx.close();
            result.complete(Constants.CONFIG);
        } else {
            //从内部yaml文件读取
            log.info("Read configuration files from inside...");
            ConfigStoreOptions store = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject()
                            .put("path", Constants.PROFILE_YAML)
                    );
            ConfigRetriever retriever = ConfigRetriever.create(vertx,
                    new ConfigRetrieverOptions().addStore(store));
            retriever.getConfig(json -> {
                //关闭配置文件
                retriever.close();
                //关闭公共启动用的临时vertx
                vertx.close();
                if (json.succeeded() && !json.result().isEmpty()) {
                    Constants.CONFIG = CopyUtil.toBean(json.result(),YamlBean.class);
                    log.info("The configuration file was read successfully...");
                    result.complete(Constants.CONFIG);
                } else {
                    log.error("Failed to read the configuration file....");
                    result.fail("Failed to read the configuration file....");
                }
            });
        }
        return result.future();
    }

    /**
     * start服务启动类
     *
     * @param vertx
     * @param deploymentOptions
     * @param verticleSpan
     * @return
     */
    public static Future<Void> runStart(Vertx vertx, DeploymentOptions deploymentOptions, String verticleSpan) {
        Promise<Void> result = Promise.promise();
        //默认扫描地址
        String startSpan = "plus.vertx.core.startup";
        if (ValidateUtil.isNotEmpty(verticleSpan)) {
            //如果外包需要扫描别的,则合并路径字符串
            startSpan += "," + verticleSpan;
        }
        Set<Class<?>> verticles = LoadUtil.getClasses(startSpan, Start.class);
        if (ValidateUtil.isNotEmpty(verticles)) {
            //启动类排序,从大到小
            Comparator<Class<?>> comparator = (c1, c2) -> {
                Start verticle1 = c1.getAnnotation(Start.class);
                Start verticle2 = c2.getAnnotation(Start.class);
                return Long.compare(verticle2.order(), verticle1.order());
            };
            List<Class<?>> verticleList = verticles.stream().sorted(comparator).collect(Collectors.toList());
            //递归从大到小顺序执行启动类
            runStart(vertx, deploymentOptions, verticleList, 0, verticleList.size() - 1).onComplete(ar -> {
                if (ar.succeeded()) {
                    result.complete();
                } else {
                    result.fail(ar.cause());
                }
            });
        } else {
            log.info("No boot classes need to be started...");
            result.complete();
        }
        return result.future();
    }

    /**
     * 递归start服务启动类
     *
     * @param vertx
     * @param deploymentOptions
     * @param verticleList
     * @param index
     * @param max
     * @return
     */
    public static Future<Void> runStart(Vertx vertx, DeploymentOptions deploymentOptions, List<Class<?>> verticleList, int index, int max) {
        Promise<Void> result = Promise.promise();
        Class<?> verticle = verticleList.get(index);
        if (verticle.isAnnotationPresent(Start.class)) {
            log.info("start startVerticle: {}", verticle.getName());
            VertxUtil.run(verticle, vertx, deploymentOptions).onComplete(ar -> {
                if (ar.succeeded()) {
                    if (index < max) {
                        int next = index + 1;
                        runStart(vertx, deploymentOptions, verticleList, next, max).onComplete(nAr -> {
                            if (nAr.succeeded()) {
                                result.complete();
                            } else {
                                result.fail(nAr.cause());
                            }
                        });
                    } else {
                        result.complete();
                    }
                } else {
                    //停止服务
                    vertx.close();
                    log.error("", ar.cause());
                    result.fail(ar.cause());
                }
            });
        } else {
            //停止服务
            vertx.close();
            log.error("Illegal boot class: {}", verticle.getName());
            result.fail("Illegal boot class: " + verticle.getName());
        }
        return result.future();
    }

}

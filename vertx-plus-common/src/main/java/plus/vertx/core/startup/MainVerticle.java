package plus.vertx.core.startup;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.vertx.core.Constants;
import plus.vertx.core.support.CopyUtil;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.yaml.ClusterYaml;
import plus.vertx.core.support.yaml.YamlBean;

/**
 * 公共启动服务
 *
 * @author crazyliu
 */
public class MainVerticle extends BaseStart {
    public static final Logger log = LoggerFactory.getLogger(MainVerticle.class);

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
            //关闭公共启动用的临时vertx,关闭后在当前后续方法再取反射取不到
            VertxUtil.close(vertx).onComplete(cAr->{
                if (cAr.succeeded()) {
                    if (rs.succeeded()) {
                        //转换DeploymentOptions配置
                        YamlBean yamlBean = rs.result();
                        if (null==yamlBean || null==yamlBean.getModule()) {
                            log.error("找不到项目配置文件...");
                            result.fail("找不到项目配置文件...");
                        } else {
                            if (null==yamlBean.getCluster() || !yamlBean.getCluster().getEnable()) {
                                //没有开启分布式
                                Vertx commonVertx = Vertx.vertx();
                                //扫描启动服务，并按照顺序启动
                                DeploymentOptions deploymentOptions = new DeploymentOptions();
                                deploymentOptions.setWorker(true);
                                VertxUtil.run(new StartVerticle(),commonVertx,deploymentOptions).onComplete(Sar -> {
                                    if (Sar.succeeded()) {
                                        result.complete();
                                    } else {
                                        log.error("", Sar.cause());
                                        result.fail(Sar.cause());
                                    }
                                });
                            } else {
                                //开启分布式
                                ClusterYaml clusterYaml = yamlBean.getCluster();
                                if (clusterYaml.getType().equals(ClusterYaml.ClusterType.Hazelcast.toString())) {
                                    VertxUtil.getHazelcastVertx(clusterYaml.getName(),clusterYaml.getAddress(),clusterYaml.getTimeout()).onComplete(hAr->{
                                        if (hAr.succeeded()) {
                                            Vertx commonVertx = hAr.result();
                                            //扫描启动服务，并按照顺序启动
                                            DeploymentOptions deploymentOptions = new DeploymentOptions();
                                            deploymentOptions.setWorker(true);
                                            VertxUtil.run(new StartVerticle(),commonVertx,deploymentOptions).onComplete(Sar->{
                                                if (Sar.succeeded()) {
                                                    result.complete();
                                                } else {
                                                    log.error("",Sar.cause());
                                                    result.fail(Sar.cause());
                                                }
                                            });
                                        } else {
                                            log.error("",hAr.cause());
                                            result.fail(hAr.cause());
                                        }
                                    });
                                } else {
                                    log.error("暂不支持其它分布式..");
                                    result.fail("暂不支持其它分布式..");
                                }
                            }
                        }
                    } else {
                        log.error("",rs.cause());
                        result.fail(rs.cause());
                    }
                } else {
                    result.fail(cAr.cause());
                }
            });
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
        if (null != Constants.CONFIG) {
            result.complete(Constants.CONFIG);
        } else if (null != config && !config.isEmpty()) {
            //从外部json文件读取
            log.info("Read configuration files from outside...");
            Constants.CONFIG = CopyUtil.toBean(config,YamlBean.class);
            log.info("The configuration file was read successfully...");
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
            ConfigRetriever retriever = ConfigRetriever.create(
                    Vertx.currentContext().owner(),
                    new ConfigRetrieverOptions().addStore(store)
            );
            retriever.getConfig(json -> {
                //关闭配置文件
                retriever.close();
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
}

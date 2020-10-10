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
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.cluster.HazelcastVertx;
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
     * @param vertx 启动参数
     * @return 返回启动结果
     */
    @Override
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
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
                                VertxUtil.run(new StartVerticle(),commonVertx,deploymentOptions).onComplete(sAr -> {
                                    if (sAr.succeeded()) {
                                        result.complete();
                                    } else {
                                        log.error("", sAr.cause());
                                        result.fail(sAr.cause());
                                    }
                                });
                            } else {
                                //开启分布式
                                ClusterYaml clusterYaml = yamlBean.getCluster();
                                if (clusterYaml.getType().equals(ClusterYaml.ClusterType.Hazelcast.toString())) {
                                    HazelcastVertx.getHazelcastVertx(clusterYaml.getName(), clusterYaml.getAddress(), clusterYaml.getTimeout()).onComplete(hAr->{
                                        if (hAr.succeeded()) {
                                            Vertx commonVertx = hAr.result();
                                            //扫描启动服务，并按照顺序启动
                                            DeploymentOptions deploymentOptions = new DeploymentOptions();
                                            deploymentOptions.setWorker(true);
                                            VertxUtil.run(new StartVerticle(),commonVertx,deploymentOptions).onComplete(sAr->{
                                                if (sAr.succeeded()) {
                                                    result.complete();
                                                } else {
                                                    log.error("",sAr.cause());
                                                    result.fail(sAr.cause());
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
     * @param config 获取到的json参数
     * @return 返回公共配置文件参数
     */
    public static Future<YamlBean> getYaml(JsonObject config) {
        return getYaml(config,"");
    }

    /**
     * 获取项目配置信息，并转化成类
     *
     * @param config 获取到的json参数
     * @param profileName 配置文件名
     * @return 返回公共配置文件参数
     */
    public static Future<YamlBean> getYaml(JsonObject config,String profileName) {
        Promise<YamlBean> result = Promise.promise();
        if (null != Constants.CONFIG) {
            result.complete(Constants.CONFIG);
        } else if (null != config && !config.isEmpty()) {
            if (config.containsKey(Constants.PROFILE_NAME)) {
                //自定义配置文件名
                getYaml(null,config.getString(Constants.PROFILE_NAME)).onComplete(yAr->{
                    if (yAr.succeeded()) {
                        result.complete(yAr.result());
                    } else {
                        log.error("",yAr.cause());
                        result.fail(yAr.cause());
                    }
                });
            } else {
                //从外部json文件读取
                log.info("Read configuration files from outside...");
                Constants.CONFIG = CopyUtil.toBean(config,YamlBean.class);
                log.info("The configuration file was read successfully...");
                result.complete(Constants.CONFIG);
            }
        } else {
            //从内部yaml文件读取
            log.info("Read configuration files from inside...");
            if (ValidateUtil.isEmpty(profileName)) {
                //如果没有自定义配置文件名,则取默认的
                profileName = Constants.PROFILE_YAML;
            }
            ConfigStoreOptions store = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject()
                            .put("path", profileName)
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

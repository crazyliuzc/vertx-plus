package plus.vertx.core.startup;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import plus.vertx.core.Constants;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.support.LoadUtil;
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;
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
        getYaml(vertx).onComplete(rs -> {
            if (rs.succeeded()) {
            } else {
            }
        });
        result.complete();
        return result.future();
    }

    /**
     * 获取项目配置信息，并转化成类
     *
     * @param vertx
     * @return
     */
    public static Future<YamlBean> getYaml(Vertx vertx) {
        Promise<YamlBean> result = Promise.promise();
        if (null != Constants.CONFIG) {
            result.complete(Constants.CONFIG);
        } else {
            log.info("Get Profile...");
            ConfigStoreOptions store = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject()
                            .put("path", Constants.PROFILE_YAML)
                    );
            ConfigRetriever retriever = ConfigRetriever.create(vertx,
                    new ConfigRetrieverOptions().addStore(store));
            retriever.getConfig(json -> {
                retriever.close();
                if (json.succeeded() && !json.result().isEmpty()) {
                    Constants.CONFIG = new YamlBean(json.result().copy());
                    log.info("Get Profile Success...");
                    result.complete(Constants.CONFIG);
                } else {
                    log.error("Get Profile Error....");
                    result.fail("Get Profile Error....");
                }
            });
        }
        return result.future();
    }

    /**
     * start服务启动类
     *
     * @param vertx
     * @param verticleSpan
     * @return
     */
    public static Future<Void> runStart(Vertx vertx, String verticleSpan) {
        Promise<Void> result = Promise.promise();
        if (ValidateUtil.isEmpty(verticleSpan)) {
            log.error("Please fill in the start app path");
            result.fail("Please fill in the start app path");
            return result.future();
        }
        Set<Class<?>> verticles = LoadUtil.getServiceClasses(verticleSpan, Start.class);
        if (ValidateUtil.isNotEmpty(verticles)) {
            //启动类排序,从大到小
            Comparator<Class<?>> comparator = (c1, c2) -> {
                Start verticle1 = c1.getAnnotation(Start.class);
                Start verticle2 = c2.getAnnotation(Start.class);
                return Long.compare(verticle2.order(), verticle1.order());
            };
            List<Class<?>> verticleList = verticles.stream().sorted(comparator).collect(Collectors.toList());
            //递归从大到小顺序执行启动类
            runStart(vertx, verticleList, 0, verticleList.size() - 1).onComplete(ar -> {
                if (ar.succeeded()) {
                    result.complete();
                } else {
                    result.fail(ar.cause());
                    vertx.close();
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
     * @param verticleList
     * @param index
     * @param max
     * @return
     */
    public static Future<Void> runStart(Vertx vertx, List<Class<?>> verticleList, int index, int max) {
        Promise<Void> result = Promise.promise();
        Class<?> verticle = verticleList.get(index);
        if (verticle.isAnnotationPresent(Start.class)) {
            try {
                log.info("start startVerticle: {}", verticle.getName());
                VertxUtil.run((Verticle) verticle.newInstance(), vertx).onComplete(ar -> {
                    if (ar.succeeded()) {
                        if (index < max) {
                            int next = index + 1;
                            runStart(vertx, verticleList, next, max).onComplete(nAr -> {
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
                        log.error("", ar.cause());
                        result.fail(ar.cause());
                    }
                });
            } catch (IllegalAccessException | InstantiationException e) {
                log.error("", e);
                result.fail(e);
            }
        } else {
            log.error("Illegal boot class: {}", verticle.getName());
            result.fail("Illegal boot class: " + verticle.getName());
        }
        return result.future();
    }

}

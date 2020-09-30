package plus.vertx.core.startup;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import plus.vertx.core.Constants;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.support.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务启动类
 * Created by crazyliu on 2020/9/30.
 */
public class StartVerticle extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        //默认扫描地址
        String startSpan = "plus.vertx.core.startup";
        String verticleSpan = Constants.CONFIG.getModule().getStartScan();
        if (ValidateUtil.isNotEmpty(verticleSpan)) {
            //如果外包需要扫描别的,则合并路径字符串
            startSpan += "," + verticleSpan;
        }
        Set<Class<?>> verticles = ScanUtil.getClasses(startSpan, Start.class);
        if (ValidateUtil.isNotEmpty(verticles)) {
            //启动类排序,从大到小
            Comparator<Class<?>> comparator = (c1, c2) -> {
                Start verticle1 = c1.getAnnotation(Start.class);
                Start verticle2 = c2.getAnnotation(Start.class);
                return Long.compare(verticle2.order(), verticle1.order());
            };
            List<Class<?>> verticleList = verticles.stream().sorted(comparator).collect(Collectors.toList());
            //递归从大到小顺序执行启动类
            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setWorker(true);
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

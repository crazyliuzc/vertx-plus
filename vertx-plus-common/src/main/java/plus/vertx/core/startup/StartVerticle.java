package plus.vertx.core.startup;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import plus.vertx.core.Constants;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.support.CastUtil;
import plus.vertx.core.support.ScanUtil;
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;

/**
 * 服务启动类
 *
 * @author crazyliu
 */
public class StartVerticle extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
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
            List<Class<?>> verticleList = verticles.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
            //递归从大到小顺序执行启动类
            runStart(vertx, verticleList, 0, verticleList.size() - 1).onComplete(ar -> {
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
     * @param vertx 启动参数
     * @param verticleList 待启动的服务列表
     * @param index 当前启动的服务标识
     * @param max 带启动的服务列表个数
     * @return 返回结果
     */
    public static Future<Void> runStart(Vertx vertx, List<Class<?>> verticleList, int index, int max) {
        Promise<Void> result = Promise.promise();
        Class<?> verticle = verticleList.get(index);
        if (!verticle.isAnnotationPresent(Start.class)) {
            //启动类没有添加启动注解,停止服务
            vertx.close();
            log.error("启动类没有添加启动注解: {}", verticle.getName());
            result.fail("启动类没有添加启动注解: " + verticle.getName());
        } else if (!Verticle.class.isAssignableFrom(verticle)) {
            //启动类没有实现Verticle,停止服务
            vertx.close();
            log.error("启动类没有实现Verticle: {}", verticle.getName());
            result.fail("启动类没有实现Verticle: " + verticle.getName());
        } else {
            log.info("start startVerticle: {}", verticle.getName());
            Start verticleAnnotation = verticle.getAnnotation(Start.class);
            //设置启动参数
            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setWorker(verticleAnnotation.isWorker());
            VertxUtil.run(CastUtil.<Class<? extends Verticle>>cast(verticle), vertx, deploymentOptions).onComplete(ar -> {
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
                    //停止服务
                    vertx.close();
                    log.error("启动"+verticle.getName()+"失败:", ar.cause());
                    result.fail(ar.cause());
                }
            });
        }
        return result.future();
    }
}

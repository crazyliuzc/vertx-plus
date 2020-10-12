package plus.vertx.core.support;

import io.vertx.core.*;
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
     * 执行verticle类
     * @param verticle 待执行的服务
     * @param vertx 系统参数
     * @param deploymentOptions 服务参数
     * @return vertx
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
     * @param clazz 待执行的服务类
     * @param vertx 系统参数
     * @param deploymentOptions 服务参数
     * @return vertx
     */
    public static Future<String> run(Class<? extends Verticle> clazz, Vertx vertx, DeploymentOptions deploymentOptions) {
        return run(BeanUtil.newInstance(clazz), vertx, deploymentOptions);
    }

}

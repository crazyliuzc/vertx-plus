package plus.vertx.core.support;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vertx启动工具类
 * @author crazyliu
 */
public class VertxUtil {
    private static final Logger log = LoggerFactory.getLogger(VertxUtil.class);
    
    /**
     * 临时获取vertx,仅用于测试
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
     * 执行verticle类
     * @param verticle
     * @param vertx
     * @param deploymentOptions
     * @return 
     */
    public static Future<String> runFull(Verticle verticle, Vertx vertx, DeploymentOptions deploymentOptions) {
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
    public static Future<String> runFull(Class clazz, Vertx vertx, DeploymentOptions deploymentOptions) {
        String exampleDir = "/src/main/java/" + clazz.getPackage().getName().replace(".", "/");
        String verticleID = clazz.getName();
        try {
            // We need to use the canonical file. Without the file name is .
            File current = new File(".").getCanonicalFile();
            if (exampleDir.startsWith(current.getName()) && !exampleDir.equals(current.getName())) {
                exampleDir = exampleDir.substring(current.getName().length() + 1);
            }
        } catch (IOException e) {
            log.error("",e);
        }

        Promise<String> result = Promise.promise();
        System.setProperty("vertx.cwd", exampleDir);
        Consumer<Vertx> runner = vert -> {
            try {
                if (deploymentOptions != null) {
                    vert.deployVerticle(verticleID, deploymentOptions, result);
                } else {
                    vert.deployVerticle(verticleID, result);
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
     * @param verticle
     * @param vertx
     * @return 
     */
    public static Future<String> run(Verticle verticle, Vertx vertx) {
        return run(verticle, vertx, new JsonObject());
    }

    /**
     * 执行verticle类
     * @param verticle
     * @param vertx
     * @param config
     * @return 
     */
    public static Future<String> run(Verticle verticle, Vertx vertx, JsonObject config) {
        if (config.isEmpty()) {
            return runFull(verticle, vertx,null);
        } else {
            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setConfig(config);
            return runFull(verticle, vertx, deploymentOptions);
        }
    }
    
    /**
     * 执行verticle类
     * @param clazz
     * @param vertx
     * @return 
     */
    public static Future<String> run(Class clazz, Vertx vertx) {
        return run(clazz, vertx, new JsonObject());
    }

    /**
     * 执行verticle类
     * @param clazz
     * @param vertx
     * @param config
     * @return 
     */
    public static Future<String> run(Class clazz, Vertx vertx, JsonObject config) {
        if (config.isEmpty()) {
            return runFull(clazz, vertx,null);
        } else {
            DeploymentOptions deploymentOptions = new DeploymentOptions();
            deploymentOptions.setConfig(config);
            return runFull(clazz, vertx, deploymentOptions);
        }
    }
}

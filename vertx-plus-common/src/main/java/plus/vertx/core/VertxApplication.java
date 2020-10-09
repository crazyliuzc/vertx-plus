package plus.vertx.core;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import plus.vertx.core.startup.MainVerticle;
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.cluster.SingleVertx;

import javax.annotation.Resource;

/**
 * 启动类
 * @author crazyliu
 */
public class VertxApplication {

    public VertxApplication() {
    }

    /**
     * 启动服务,传入配置文件名
     * @param profileName
     */
    public static void run(String profileName) {
        Vertx vertx = SingleVertx.getVertx();
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        JsonObject jsonObject = new JsonObject();
        if (ValidateUtil.isEmpty(profileName)) {
            profileName = Constants.PROFILE_YAML;
        }
        jsonObject.put(Constants.PROFILE_NAME,profileName);
        deploymentOptions.setConfig(jsonObject);
        VertxUtil.run(MainVerticle.class, vertx, deploymentOptions);
    }

    /**
     * 启动服务,查询传入类是否有注解配置文件,如有则按照注解读取,没有则取默认
     * @param primarySource 项目启动类
     */
    public static void run(Class<?> primarySource) {
        if (primarySource.isAnnotationPresent(Resource.class)) {
            Resource primarySourceAnnotation = primarySource.getAnnotation(Resource.class);
            if (ValidateUtil.isNotEmpty(primarySourceAnnotation.name())) {
                run(primarySourceAnnotation.name());
            }
        } else {
            run("");
        }
    }

    public static void run() {
        run("");
    }
}

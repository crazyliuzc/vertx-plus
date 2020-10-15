package plus.vertx.core;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Log4j2LogDelegateFactory;
import plus.vertx.core.startup.MainVerticle;
import plus.vertx.core.support.ValidateUtil;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.cluster.SingleVertx;

import javax.annotation.Resource;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static io.vertx.core.spi.resolver.ResolverProvider.DISABLE_DNS_RESOLVER_PROP_NAME;

/**
 * 启动类
 * @author crazyliu
 */
public class VertxApplication {

    public VertxApplication() {
    }

    static {
        //默认日志框架为log4j2
        System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, Log4j2LogDelegateFactory.class.getName());
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
        //Vert.x自带有一个DNS Resolver，缺省情况下，启动Vert.x之后，Vert.x会尝试读取hosts文件，若该文件不存在或内容为空，
        // 则Vert.x会使用Google的公开DNS服务器，
        // IP地址是8.8.8.8和8.8.4.4，由于众所周知的原因，国内的用户系统极有可能无法连上该IP地址而导致访问超时等失败，
        // 尤其是当用户使用WebClient, HttpClient等制作一些爬虫，或者是访问其他系统的时候，该问题就会出现。
        System.setProperty(DISABLE_DNS_RESOLVER_PROP_NAME, "true");
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

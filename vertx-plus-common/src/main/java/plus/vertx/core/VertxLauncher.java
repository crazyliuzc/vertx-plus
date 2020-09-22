package plus.vertx.core;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Log4j2LogDelegateFactory;
import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static io.vertx.core.spi.resolver.ResolverProvider.DISABLE_DNS_RESOLVER_PROP_NAME;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * vertx启动工具类
 * @author crazyliu
 */
public class VertxLauncher extends io.vertx.core.Launcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxLauncher.class);
     static {
        //默认日志框架为log4j2
        System.setProperty(LOGGER_DELEGATE_FACTORY_CLASS_NAME, Log4j2LogDelegateFactory.class.getName());
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
        //Vert.x自带有一个DNS Resolver，缺省情况下，启动Vert.x之后，Vert.x会尝试读取hosts文件，若该文件不存在或内容为空，
        // 则Vert.x会使用Google的公开DNS服务器，
        // IP地址是8.8.8.8和8.8.4.4，由于众所周知的原因，国内的用户系统极有可能无法连上该IP地址而导致访问超时等失败，
        // 尤其是当用户使用WebClient, HttpClient等制作一些爬虫，或者是访问其他系统的时候，该问题就会出现。
        System.setProperty(DISABLE_DNS_RESOLVER_PROP_NAME,"true");
    }

    public static void main(String[] args) {
        new VertxLauncher().dispatch(args);
    }
}

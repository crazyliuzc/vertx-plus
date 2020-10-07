package plus.vertx.core.support.cluster;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;

/**
 * 单机Vertx
 * @author crazyliu
 * @date 2020年10月7日, 0007
 */
public class SingleVertx {
    /**
     * 获取vertx
     * @return vertx
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
        return Vertx.vertx(vertxOptions);
    }
}

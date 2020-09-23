package plus.vertx.core.startup;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动服务基类
 *
 * @author crazyliu
 */
public abstract class BaseStart extends AbstractVerticle {

    public static final Logger log = LoggerFactory.getLogger(BaseStart.class);

    public BaseStart() {
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        action(vertx).onComplete(ar -> {
            if (ar.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(ar.cause());
            }
        });
    }

    /**
     * 具体执行类
     *
     * @param vertx
     * @return
     */
    public Future<Void> action(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        result.complete();
        return result.future();
    }
}

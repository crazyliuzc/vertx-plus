package plus.vertx.core.startup;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import plus.vertx.core.annotation.Start;

/**
 * 路由服务
 * @author crazyliu
 */
@Start(order = 10010,isWorker = true)
public class RouterVerticle extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
        return super.action(vertx, result);
    }
}

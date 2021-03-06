package plus.vertx.core.startup;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

/**
 * 基于EventBus的Rpc服务启动类
 * Created by crazyliu on 2020/10/17.
 */
public class EventBusRpcVerticle extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
        return super.action(vertx, result);
    }
}

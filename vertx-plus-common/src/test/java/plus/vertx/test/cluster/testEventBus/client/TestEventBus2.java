package plus.vertx.test.cluster.testEventBus.client;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.startup.BaseStart;
import plus.vertx.core.support.SeqUtil;

/**
 * Created by crazyliu on 2020/9/30.
 */
@Start(order = 10)
public class TestEventBus2 extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
        String message = SeqUtil.getId();
        log.info("啦啦啦，这是消息发送方，序列号{}", message);
        vertx.eventBus().request("test-event-bus-server",message,ar->{
            if (ar.succeeded()) {
                log.info(ar.result().body().toString());
            } else {
                log.error("",ar.cause());
            }
        });
        result.complete();
        return result.future();
    }
}

package plus.vertx.test.cluster.hazelcast;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.startup.BaseStart;

/**
 * Created by crazyliu on 2020/9/30.
 */
@Start
public class TestEventBus1 extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        log.info("啦啦啦");
        result.complete();
        return result.future();
    }
}

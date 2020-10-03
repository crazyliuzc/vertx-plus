package plus.vertx.test.cluster.testEventBus.server;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import plus.vertx.core.annotation.Start;
import plus.vertx.core.startup.BaseStart;
import plus.vertx.core.support.SeqUtil;

/**
 * Created by crazyliu on 2020/9/30.
 */
@Start(order = 11)
public class TestEventBus1 extends BaseStart {
    @Override
    public Future<Void> action(Vertx vertx) {
        Promise<Void> result = Promise.promise();
        String message = SeqUtil.getId();
        log.info("啦啦啦，这是消息接收方，序列号{}", message);
        vertx.eventBus().consumer("test-event-bus-server",ar->{
            log.info(ar.body().toString());
            ar.reply("测试返回"+message);
        });
        result.complete();
        return result.future();
    }
}

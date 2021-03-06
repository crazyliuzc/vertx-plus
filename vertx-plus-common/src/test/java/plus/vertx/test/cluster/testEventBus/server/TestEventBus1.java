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
    public Future<Void> action(Vertx vertx, Promise<Void> result) {
        String message = SeqUtil.getSnowflakeId();
        log.info("啦啦啦，这是消息接收方，序列号{}", message);
        log.info("server...Vertx.currentContext().isWorkerContext(): {}", Vertx.currentContext().isWorkerContext());
        vertx.eventBus().consumer("test-event-bus-server",ar->{
            log.info("trans: {}", Vertx.currentContext().<String>get("trans"));
            Vertx.currentContext().put("trans", "测试一下...咯"+SeqUtil.getSnowflakeId());
            log.info("consumer... Vertx.currentContext().isWorkerContext(): {}", Vertx.currentContext().isWorkerContext());
            log.info(ar.body().toString());
            ar.reply("测试返回"+message);
        });
        result.complete();
        return result.future();
    }
}

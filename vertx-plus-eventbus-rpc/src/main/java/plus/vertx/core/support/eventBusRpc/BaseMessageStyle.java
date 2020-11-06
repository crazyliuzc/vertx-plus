package plus.vertx.core.support.eventBusRpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * 接收消息处理类(公共)
 *
 * @author crazyliu
 */
public abstract class BaseMessageStyle implements Handler<Message<JsonObject>> {
    public static final Logger log = LoggerFactory.getLogger(BaseMessageStyle.class);

    @Override
    public void handle(Message<JsonObject> msg) {
        if (msg.body() != null) {
            Vertx.currentContext().executeBlocking(b->{}, r->{});
//            msg.reply(new JsonObject().put("code",HttpCode.OK.value()));
//            msg.fail(HttpCode.BAD_REQUEST.value(), "consul set fail");
        } else {
//            msg.fail(HttpCode.BAD_REQUEST.value(), "parameter could not be found");
        }
    }

    public <T> Future<T> action(Vertx vertx,Promise<T> result) {
        result.complete();
        return result.future();
    }
}

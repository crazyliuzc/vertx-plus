package plus.vertx.core.support;

import com.google.common.base.Strings;
import com.google.protobuf.Message;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息工具
 * @author crazyliu
 */
public class EventBusUtil {
     final private static Logger LOGGER = LoggerFactory.getLogger(EventBusUtil.class);
    private static EventBus singletonEventBus;
    private static DeliveryOptions deliveryOptions;

    public EventBusUtil() {
    }

    public static void init(EventBus eventBus) {
        //默认40秒
        init(eventBus,8000);
    }

    public static void init(EventBus eventBus,long sendTimeoutMs) {
        Objects.requireNonNull(eventBus, "未初始化EventBus");
        singletonEventBus = eventBus;
        deliveryOptions = new DeliveryOptions();
        deliveryOptions.setSendTimeout(sendTimeoutMs);
    }

    public static EventBus getEventBusInstance() {
        Objects.requireNonNull(singletonEventBus, "未初始化EventBus");
        return singletonEventBus;
    }

    /**
     * 发送消息，并收取回调
     * @param address
     * @param params
     * @return replyHandler
     */
    public static Future<Message> send(String address, JsonObject params){
        Promise<Message> result = Promise.promise();
        if(Strings.isNullOrEmpty(address)||null==params||params.isEmpty()){
            LOGGER.error("缺少请求参数，调用消息失败");
            result.fail("缺少请求参数，调用消息失败");
        }else{
            getEventBusInstance().request(address, params, deliveryOptions, replyHandler->{
                if (replyHandler.succeeded()) {
                    result.complete((Message) replyHandler.result());
                } else {
                    result.fail(replyHandler.cause());
                }
            });
        }
        return result.future();
    }
}

package plus.vertx.core.support;

import com.google.common.base.Strings;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
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

    public EventBusUtil(Vertx vertx) {
        singletonEventBus = vertx.eventBus();
    }

    public static EventBus getInstance() {
        Objects.requireNonNull(singletonEventBus, "未初始化EventBus");
        return singletonEventBus;
    }

    /**
     * 发送消息，并收取回调
     * @param <T>
     * @param address 地址
     * @param params 参数
     * @param sendTimeoutMs 超时时间，单位毫秒
     * @return replyHandler
     */
    public static <T> Future<Message<T>> send(String address, JsonObject params,long sendTimeoutMs){
        Promise<Message<T>> result = Promise.promise();
        String errorMsg = "";
        if(ValidateUtil.isEmpty(address)){
            errorMsg = "缺少请求地址，调用消息失败";
        } else if (null==params||params.isEmpty()) {
            errorMsg = "缺少请求参数，调用消息失败";
        } else {
            DeliveryOptions deliveryOptions = new DeliveryOptions();
            deliveryOptions.setSendTimeout(sendTimeoutMs);
            getInstance().<T>request(address, params, deliveryOptions, replyHandler->{
                if (replyHandler.succeeded()) {
                    result.complete((Message<T>) replyHandler.result());
                } else {
                    LOGGER.error("\n 消息地址:{} \n 参数:{} \n ",address,params.encodePrettily());
                    LOGGER.error("",replyHandler.cause());
                    result.fail(replyHandler.cause());
                }
            });
        }
        if (ValidateUtil.isNotEmpty(errorMsg)) {
            LOGGER.error(errorMsg);
            result.fail(errorMsg);
        }
        return result.future();
    }
}

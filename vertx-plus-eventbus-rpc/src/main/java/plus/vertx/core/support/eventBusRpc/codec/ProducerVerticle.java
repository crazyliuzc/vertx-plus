package plus.vertx.core.support.eventBusRpc.codec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;

public class ProducerVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        //发布消息(群发)
        eventBus.publish("com.hou", "群发祝福!");
        //发送消息(单发),只会发送注册此地址的一个,采用不严格的轮询算法选择
        DeliveryOptions options = new DeliveryOptions();//设置消息头等
        options.addHeader("some-header", "some-value");
        eventBus.request("com.hou", "单发消息",options,ar->{
            if(ar.succeeded()) System.out.println("收到消费者确认信息:"+ar.result().body());
        });
        //发送自定义对象,需要编解码器
        eventBus.registerCodec(new CustomizeMessageCodec());//注册编码器
        DeliveryOptions options1 = new DeliveryOptions().setCodecName("myCodec");//必须指定名字
        // OrderMessage orderMessage = new OrderMessage();
        // orderMessage.setName("侯征");
        // eventBus.send("com.hou", orderMessage, options1);
    }
}

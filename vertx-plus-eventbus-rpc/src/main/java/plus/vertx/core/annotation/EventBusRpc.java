package plus.vertx.core.annotation;

import java.lang.annotation.*;

/**
 * 用于项目服务启动后需要部署执行的Verticle
 * @author crazyliu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventBusRpc {

    /**
     * 启动顺序，数字越大越先注册
     * @return 执行顺序
     */
    long order() default 2;

    /**
     * 是否多线程池Verticle
     * @return 判断是否是多线程池Verticle
     */
    boolean isWorker() default true;

    /**
     * 部署实例数量
     * @return 实例数量
     */
    int instances() default 1;
}

package plus.vertx.core.annotation;

import java.lang.annotation.*;

/**
 * 用于项目服务启动后需要部署执行的Verticle handler
 * @author crazyliu
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Start {

    /**
     * 启动顺序，数字越大越先注册
     */
    long order() default 2;
}

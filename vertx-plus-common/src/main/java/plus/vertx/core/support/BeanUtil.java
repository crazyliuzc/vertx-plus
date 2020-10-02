package plus.vertx.core.support;

import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实体工具类
 * Created by crazyliu on 2020/9/29.
 */
public class BeanUtil {

    public static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * Objenesis是一个小的Java库，它有一个用途：实例化一个特定类的新对象
     * Java已经支持使用class.newinstance()的类动态实例化，但是必须要有一个合适的构造函数。而很多场景下类不能够用这种方式去实例化，例如：
     *  构造函数需要参数（Constructors that require arguments）
     *  有副作用的构造函数（Constructors that have side effects）
     *  会抛出异常的构造函数（Constructors that throw exceptions）
     *  因此，常见的是在类库中看到类必须要有一个默认的构造函数的限制，Objenesis旨在通过绕过对象实例化的构造函数来克服这些限制。
     */
    private static final ThreadLocal<ObjenesisStd> OBJENESIS_STD_THREADLOCAL = ThreadLocal.withInitial(ObjenesisStd::new);

    public BeanUtil() {
    }

    /**
     * 实例化实体
     * @param target 类
     * @param <T> 类型
     * @return 返回实例化
     */
    public static <T> T newInstance(Class<T> target) {
        return OBJENESIS_STD_THREADLOCAL.get().newInstance(target);
    }
}

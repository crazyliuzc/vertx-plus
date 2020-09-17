package plus.vertx.core.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanMap;
import org.objenesis.ObjenesisStd;

/**
 * 基于cglib的Bean复制
 * @author crazyliu
 */
public class BeanCopyUtil {
    private BeanCopyUtil() {
    }

    /**
     * Objenesis是一个小的Java库，它有一个用途：实例化一个特定类的新对象
     * Java已经支持使用class.newinstance()的类动态实例化，但是必须要有一个合适的构造函数。而很多场景下类不能够用这种方式去实例化，例如：
     *  构造函数需要参数（Constructors that require arguments）
     *  有副作用的构造函数（Constructors that have side effects）
     *  会抛出异常的构造函数（Constructors that throw exceptions）
     *  因此，常见的是在类库中看到类必须要有一个默认的构造函数的限制，Objenesis旨在通过绕过对象实例化的构造函数来克服这些限制。
     */
    private static final ThreadLocal<ObjenesisStd> objenesisStdThreadLocal = ThreadLocal.withInitial(ObjenesisStd::new);
    /**
     * 缓存BeanCopier
     */
    private static final ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>, BeanCopier>> cache = new ConcurrentHashMap<>();

    /**
     * 实体复制
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    public static <T> T copy(Object source, Class<T> target) {
        return copy(source, objenesisStdThreadLocal.get().newInstance(target));
    }
    
    /**
     * 实体复制
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    public static <T> T copy(Object source, T target) {
        BeanCopier beanCopier = getCacheBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
        return target;
    }
    
    /**
     * 集合复制
     * @param <T>
     * @param sources
     * @param target
     * @return 
     */
    public static <T> List<T> copyList(List<?> sources, Class<T> target) {
        if (sources.isEmpty()) {
            return Collections.emptyList();
        }
        
        return sources.stream()
                .filter(Objects::nonNull)
                .map(source -> copy(source, target))
                .collect(Collectors.toList());
    }

    /**
     * Map转实体
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    public static <T> T mapToBean(Map<?, ?> source, Class<T> target) {
        T bean = objenesisStdThreadLocal.get().newInstance(target);
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(source);
        return bean;
    }

    /**
     * 实体转Map
     * @param <T>
     * @param source
     * @return 
     */
    public static <T> Map<?, ?> beanToMap(T source) {
        return BeanMap.create(source);
    }

    /**
     * 获取BeanCopier
     * @param <S>
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    private static <S, T> BeanCopier getCacheBeanCopier(Class<S> source, Class<T> target) {
        ConcurrentHashMap<Class<?>, BeanCopier> copierConcurrentHashMap = cache.computeIfAbsent(source, () -> new ConcurrentHashMap<>(16));
        return copierConcurrentHashMap.computeIfAbsent(target, () -> BeanCopier.create(source, target, false));
    }
}

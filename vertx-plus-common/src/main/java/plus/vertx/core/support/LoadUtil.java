package plus.vertx.core.support;

import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import org.reflections.Reflections;

/**
 * SPI机制中的服务加载工具类，流程如下
 * <pre>
 *     1、创建接口，并创建实现类
 *     2、ClassPath/META-INF/services下创建与接口全限定类名相同的文件
 *     3、文件内容填写实现类的全限定类名
 * </pre> 相关介绍见：https://www.jianshu.com/p/3a3edbcd8f24
 *
 * @author crazyliu
 */
public class LoadUtil {

    /**
     * 加载第一个可用服务，如果用户定义了多个接口实现类，只获取第一个不报错的服务。
     *
     * @param <T> 接口类型
     * @param clazz 服务接口
     * @return 第一个服务接口实现对象，无实现返回{@code null}
     */
    public static <T> T loadFirstAvailable(Class<T> clazz) {
        final Iterator<T> iterator = load(clazz).iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            try {
                return iterator.next();
            } catch (ServiceConfigurationError e) {
                // ignore
            }
        }
        return null;
    }

    /**
     * 加载第一个服务，如果用户定义了多个接口实现类，只获取第一个。
     *
     * @param <T> 接口类型
     * @param clazz 服务接口
     * @return 第一个服务接口实现对象，无实现返回{@code null}
     */
    public static <T> T loadFirst(Class<T> clazz) {
        final Iterator<T> iterator = load(clazz).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * 加载服务
     *
     * @param <T> 接口类型
     * @param clazz 服务接口
     * @return 服务接口实现列表
     */
    public static <T> ServiceLoader<T> load(Class<T> clazz) {
        return ServiceLoader.load(clazz);
    }

    /**
     * 加载服务
     *
     * @param <T> 接口类型
     * @param clazz 服务接口
     * @param loader {@link ClassLoader}
     * @return 服务接口实现列表
     */
    public static <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
        return ServiceLoader.load(clazz, loader);
    }

    /**
     * 获取扫码到的服务类
     *
     * @param serviceScan 待扫描的路径集合，以英文逗号分隔
     * @param annotation 带扫描的注解类
     * @return
     */
    public static Set<Class<?>> getClasses(String serviceScan, Class<? extends Annotation> annotation) {
        if (ValidateUtil.isEmpty(serviceScan)) {
            return new Reflections().getTypesAnnotatedWith(annotation);
        } else if (serviceScan.contains(",")) {
            return new Reflections(Sets.newHashSet(serviceScan.split(","))).getTypesAnnotatedWith(annotation);
        } else {
            return new Reflections(Sets.newHashSet(serviceScan)).getTypesAnnotatedWith(annotation);
        }
    }
}

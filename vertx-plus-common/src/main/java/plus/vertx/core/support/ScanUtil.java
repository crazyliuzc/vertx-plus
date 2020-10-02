package plus.vertx.core.support;

import com.google.common.collect.Sets;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 类扫描工具
 * @author crazyliu
 * @date 2020/9/30
 */
public class ScanUtil {
    public static final Logger log = LoggerFactory.getLogger(ScanUtil.class);

    /**
     * 获取扫码到的服务类
     *
     * @param serviceScan 待扫描的路径集合，以英文逗号分隔
     * @param annotation 带扫描的注解类
     * @return 返回扫描到的去重复类集合
     */
    public static Set<Class<?>> getClasses(String serviceScan, Class<? extends Annotation> annotation) {
        String split = ",";
        if (ValidateUtil.isEmpty(serviceScan)) {
            return new Reflections().getTypesAnnotatedWith(annotation);
        } else if (serviceScan.contains(split)) {
            return new Reflections(Sets.newHashSet(serviceScan.split(split))).getTypesAnnotatedWith(annotation);
        } else {
            return new Reflections(serviceScan).getTypesAnnotatedWith(annotation);
        }
    }
}

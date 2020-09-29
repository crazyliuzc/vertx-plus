package plus.vertx.core.support;

import com.google.protobuf.MessageOrBuilder;
import io.vertx.core.json.JsonObject;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于cglib的Bean深复制,
 * 部分不能复制的使用Jackson转换,
 * 可以复制Google protocol MessageOrBuilder实体
 * 注意这里的复制必须属性同名同类型,重度复制用MapStrut
 * @author crazyliu
 */
public class CopyUtil {
    public static final Logger log = LoggerFactory.getLogger(CopyUtil.class);
    private CopyUtil() {
    }

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
        return copy(source, BeanUtil.newInstance(target));
    }
    
    /**
     * 实体复制(深复制)
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    public static <T> T copy(Object source, T target) {
        //如果待复制的实体是google protocol,则另外处理
        if (target instanceof MessageOrBuilder) {
            return JsonUtil.toEntity(JsonUtil.toJson(source,false), (Class<T>) target.getClass());
        } else {
            BeanCopier beanCopier = getCacheBeanCopier(source.getClass(), target.getClass());
            beanCopier.copy(source, target, new Converter() {
                @Override
                public Object convert(Object value, Class target, Object context) {
                    if(target.isSynthetic()){
                        BeanCopier.create(target, target, true).copy(value, value, this);
                    }
                    return value;
                }
            });
            return target;
        }
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
     * Map转实体(Map转JsonObject,再通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param <T>
     * @param source
     * @param target
     * @return 
     */
    public static <T> T toBean(Map<String, Object> source, Class<T> target) {
        //必须通过BeanUtil来实例化,防止出现问题
        return toBean(source, BeanUtil.newInstance(target));
    }
    
    /**
     * Map转实体(Map转JsonObject,再通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param <T>
     * @param source
     * @param bean
     * @return 
     */
    public static <T> T toBean(Map<String, Object> source, T bean) {
        return toBean(new JsonObject(source),bean);
    }

    /**
     * JsonObject转实体(通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T toBean(JsonObject source, Class<T> target) {
        //必须通过BeanUtil来实例化,防止出现问题
        return toBean(source, BeanUtil.newInstance(target));
    }

    /**
     * JsonObject转实体(通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param source
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T toBean(JsonObject source, T bean) {
        return toBean(source.encode(),bean);
    }

    /**
     * Json字符串转实体(通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T toBean(String source, Class<T> target) {
        //必须通过BeanUtil来实例化,防止出现问题
        return toBean(source, BeanUtil.newInstance(target));
    }

    /**
     * Json字符串转实体(通过Jackson转Bean),cglib的BeanMap无法深拷贝
     * @param source
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> T toBean(String source, T bean) {
        return JsonUtil.toEntity(source, (Class<T>) bean.getClass());
    }

    /**
     * 实体转Map,cglib的BeanMap无法深拷贝
     * @param <T>
     * @param source
     * @return 
     */
    public static <T> Map<String, Object> toMap(T source) {
        return toJsonObject(source).getMap();
    }

    /**
     * 实体转JsonObject,cglib的BeanMap无法深拷贝
     * @param source
     * @param <T>
     * @return
     */
    public static <T> JsonObject toJsonObject(T source) {
        return new JsonObject(JsonUtil.toJson(source,false));
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
        ConcurrentHashMap<Class<?>, BeanCopier> copierConcurrentHashMap = cache.computeIfAbsent(source, aClass -> new ConcurrentHashMap<>(16));
        return copierConcurrentHashMap.computeIfAbsent(target, aClass -> BeanCopier.create(source, target, true));
    }
}
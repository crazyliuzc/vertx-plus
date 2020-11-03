package plus.vertx.core.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;

/**
 * 基于Jackson的Bean深复制以及转换,
 * 可以复制Google protocol MessageOrBuilder实体
 * 注意这里的复制必须属性同名同类型,重度复制用MapStrut
 * @author crazyliu
 */
public class CopyUtil {
    public static final Logger log = LoggerFactory.getLogger(CopyUtil.class);
    private CopyUtil() {
    }

    /**
     * 实体复制
     * @param <T> 待复制的类型
     * @param source 待复制的实体
     * @param target 目标实体
     * @return 复制后的实体
     */
    public static <T> T copy(Object source, Class<T> target) {
        return copy(source, BeanUtil.newInstance(target));
    }
    
    /**
     * 实体复制(深复制)
     * @param <T> 待复制的类型
     * @param source 待复制的实体
     * @param target 目标实体
     * @return 复制后的实体
     */
    public static <T> T copy(Object source, T target) {
        return JsonUtil.toEntity(JsonUtil.toJson(source,false), CastUtil.<Class<T>>cast(target.getClass()));
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

        return JsonUtil.toList(JsonUtil.toJson(sources,false), CastUtil.<Class<T>>cast(target.getClass()));
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
        return JsonUtil.toEntity(source, CastUtil.<Class<T>>cast(bean.getClass()));
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

}
package plus.vertx.core.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.protobuf.MessageOrBuilder;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plus.vertx.core.support.json.ByteArrayDeserializer;
import plus.vertx.core.support.json.ByteArraySerializer;
import plus.vertx.core.support.json.InstantDeserializer;
import plus.vertx.core.support.json.InstantSerializer;
import plus.vertx.core.support.json.JsonArraySerializer;
import plus.vertx.core.support.json.JsonObjectDeserializer;
import plus.vertx.core.support.json.JsonObjectSerializer;

/**
 * Json工具类，基于Jackson实现基础转换
 *
 * @author crazyliu
 */
public class JsonUtil {

    final private static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 获取Jackson实例
     *
     * @return
     */
    public static ObjectMapper get() {
        return get(false);
    }

    /**
     * 获取Jackson实例
     *
     * @param isNumberToStr 是否数字转字符串
     * @return
     */
    public static ObjectMapper get(boolean isNumberToStr) {
        ObjectMapper objectMapper = OBJECT_MAPPER.get();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonObject.class, new JsonObjectSerializer());
        module.addDeserializer(JsonObject.class, new JsonObjectDeserializer());
        module.addSerializer(JsonArray.class, new JsonArraySerializer());
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addDeserializer(Instant.class, new InstantDeserializer());
        module.addSerializer(byte[].class, new ByteArraySerializer());
        module.addDeserializer(byte[].class, new ByteArrayDeserializer());

        if (isNumberToStr) {
            //数字转字符串
            module.addSerializer(Long.TYPE, ToStringSerializer.instance);
            module.addSerializer(Long.class, ToStringSerializer.instance);
            module.addSerializer(Integer.class, ToStringSerializer.instance);
            module.addSerializer(Integer.TYPE, ToStringSerializer.instance);
            module.addSerializer(Double.class, ToStringSerializer.instance);
            module.addSerializer(Double.TYPE, ToStringSerializer.instance);
        }

        objectMapper.registerModule(module);
        return objectMapper;
    }

    public static <T> String toJson(T t, boolean isNumberToStr) {
        try {
            ObjectMapper objectMapper = get(isNumberToStr);
            String tempStr = objectMapper.writeValueAsString(t);
            if (isNumberToStr) {
                if (t instanceof MessageOrBuilder) {
                    //Proto转换
                    return toJson(new JsonObject(tempStr), isNumberToStr);
                } else if (t instanceof List
                        && ValidateUtil.isNotEmpty((List) t)
                        && ((List) t).get(0) instanceof MessageOrBuilder) {
                    //Proto转换
                    return toJson(new JsonArray(tempStr), isNumberToStr);
                } else {
                    return tempStr;
                }
            } else {
                return tempStr;
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("", e);
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * 禁止调用无参构造
     *
     */
    private JsonUtil() {
        throw new RuntimeException("Can't create an instance!");
    }

    /**
     * 使用ThreadLocal创建对象，防止出现线程安全问题
     */
    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER = new ThreadLocal<ObjectMapper>() {
        @Override
        protected ObjectMapper initialValue() {
            ObjectMapper objectMapper = new ObjectMapper();
            //允许使用未带引号的字段名
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            //允许使用单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            //反序列化的时候如果多了其他属性,不抛出异常
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
            //如果是空对象的时候,不抛异常
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            //支持protobuf
            objectMapper.registerModule(new ProtobufModule());
            //如果字段都是驼峰命名规则，需要这一句
            /*
            KebabCase: 肉串策略 - 单词小写，使用连字符'-'连接
            SnakeCase: 蛇形策略 - 单词小写，使用下划线'_'连接；即老版本中的LowerCaseWithUnderscoresStrategy
            LowerCase: 小写策略 - 简单的把所有字母全部转为小写，不添加连接符
            UpperCamelCase: 驼峰策略 - 单词首字母大写其它小写，不添加连接符；即老版本中的
            * */
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
//            jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
//            jacksonObjectMapperBuilder.modulesToInstall(ProtobufModule.class);

            return objectMapper;
        }
    };
}

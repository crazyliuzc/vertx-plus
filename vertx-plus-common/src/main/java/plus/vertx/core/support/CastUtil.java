package plus.vertx.core.support;
/**
 * 解决 Java 泛型类型转换时的警告工具
 * @author crazyliu
 */
public class CastUtil {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
 
    private CastUtil() {
        throw new UnsupportedOperationException();
    }

}

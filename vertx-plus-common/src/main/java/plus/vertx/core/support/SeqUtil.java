package plus.vertx.core.support;

import plus.vertx.core.support.sequence.Snowflake;
import plus.vertx.core.support.sequence.UUID;

/**
 * ID生成工具类，基于雪花算法实现
 */
public class SeqUtil {

    // ******************************** UUID

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    // ********************************雪花算法ID

    /**
     * 获取下一个ID
     */
    public static String getId() {
        return Long.toString(next());
    }

    /**
     * 获取下一个ID
     */
    public static long next() {
        return SNOWFLAKE.get().nextId();
    }

    /**
     * 根据序列号ID获取时间戳
     * @param id 传入生成id
     * @return 返回时间戳
     */
    public static long getTimeFromId(long id){
        return SNOWFLAKE.get().getTimestamp(id);
    }

    // ################################################################################################################

    /**
     * 禁止调用无参构造
     *
     * @throws IllegalAccessException
     */
    private SeqUtil() throws IllegalAccessException {
        throw new IllegalAccessException("Can't create an instance!");
    }

    /**
     * 使用ThreadLocal创建对象，防止出现线程安全问题
     */
    private static final ThreadLocal<Snowflake> SNOWFLAKE = new ThreadLocal<Snowflake>() {
        @Override
        protected Snowflake initialValue() {
            Snowflake instance = Snowflake.getInstance();
            instance.initWorker(1,0);
            return instance;
        }
    };
}

package plus.vertx.core.support;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.shareddata.Counter;
import io.vertx.core.shareddata.SharedData;
import plus.vertx.core.support.sequence.Snowflake;
import plus.vertx.core.support.sequence.UUID;

/**
 * ID生成工具类，基于雪花算法实现
 */
public class SeqUtil {
    public static final Logger log = LoggerFactory.getLogger(SeqUtil.class);

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
    public static String getSnowflakeId() {
        return Long.toString(nextSnowflakeId());
    }

    /**
     * 获取下一个ID
     */
    public static long nextSnowflakeId() {
        Snowflake instance = Snowflake.getInstance();
        instance.initWorker(1,0);
        return instance.nextId();
    }

    /**
     * 根据序列号ID获取时间戳
     * @param snowflakeId 传入生成snowflakeId
     * @return 返回时间戳
     */
    public static long getTimeFromSnowflakeId(long snowflakeId){
        Snowflake instance = Snowflake.getInstance();
        instance.initWorker(1,0);
        return instance.getTimestamp(snowflakeId);
    }

    // ********************************分布式明显时间自增序列号，(年-2000)+月+日+时+分+秒，前缀12位，后补5位相同前缀下自增序列由vertx生成

    /**
     * 获取年月日时分秒格式化值
     * @return
     */
    private static String getFullTimePrefix() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int year = c.get(Calendar.YEAR);
        //JDK日历类的月份范围是[0,11]
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String monthFmt = String.format("%02d", month);
        String dayFmt = String.format("%02d", day);
        String hourFmt = String.format("%02d", hour);
        String minuteFmt = String.format("%02d", minute);
        String secondFmt = String.format("%02d", second);
        return (year-2000)+monthFmt+dayFmt+hourFmt+minuteFmt+secondFmt;
    }

    /**
     * 根据时间前缀生成分布式ID，前12位为时间，后5位是在相同时间前缀下的自增序列
     * @return
     */
    public static Future<String> getFullTimeId() {
        Promise<String> result = Promise.promise();
        String prefix = getFullTimePrefix();
        Context context = Vertx.currentContext();
        if (null==context) {
            log.error("获取序列号Counter{}出错:Vertx没有启动",prefix);
            result.fail("获取序列号Counter"+prefix+"出错:Vertx没有启动");
            return result.future();
        }
        Vertx vertx = context.owner();
        SharedData sharedData = vertx.sharedData();
        sharedData.getCounter(prefix, rs->{
            if (rs.succeeded()) {
                Counter counter = rs.result();
                counter.incrementAndGet(gRs->{
                    if (gRs.succeeded()) {
                        String suffixFmt = String.format("%05d", gRs.result());
                        result.complete(prefix+suffixFmt);
                    } else {
                        log.error("获取序列号Counter{}自增出错:{}",prefix, gRs.cause());
                        result.fail("获取序列号Counter"+prefix+"自增出错");
                    }
                });
            } else {
                log.error("获取序列号Counter{}出错:{}",prefix, rs.cause());
                result.fail("获取序列号Counter"+prefix+"出错");
            }
        });
        return result.future();
    }

    /**
     * 根据时间前缀生成分布式ID，前12位为时间，后5位是在相同时间前缀下的自增序列
     * @return
     */
    public static Future<Long> nextFullTimeId() {
        Promise<Long> result = Promise.promise();
        getFullTimeId().onComplete(gRs->{
            if (gRs.succeeded()) {
                result.complete(Long.parseLong(gRs.result()));
            } else {
                result.fail(gRs.cause());
            }
        });
        return result.future();
    }

}

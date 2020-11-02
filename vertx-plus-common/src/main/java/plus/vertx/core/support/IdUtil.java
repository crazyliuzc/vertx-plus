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

/**
 * 全局序列号，(年-2000)+月+日+时+分+秒，前缀12位，后补5位相同前缀下自增序列由vertx生成
 */
public class IdUtil {
    public static final Logger log = LoggerFactory.getLogger(IdUtil.class);
    
    /**
     * 获取年月日时分秒格式化值
     * @return
     */
    private static String getOrderIdPrefix() {
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
     * 根据时间前缀生成分布式ID
     * @param prefix
     * @return
     */
    private static Future<Long> getFullSeq(String prefix) {
        Promise<Long> result = Promise.promise();
        Context context = Vertx.currentContext();
        Vertx vertx;
        if (null==context) {
            log.error("获取序列号Counter{}出错:Vertx没有启动",prefix);
            result.fail("获取序列号Counter"+prefix+"出错:Vertx没有启动");
            return result.future();
        } else {
            vertx = context.owner();
        }
        SharedData sharedData = vertx.sharedData();
        sharedData.getCounter(prefix, rs->{
            if (rs.succeeded()) {
                Counter counter = rs.result();
                counter.incrementAndGet(gRs->{
                    if (gRs.succeeded()) {
                        String suffixFmt = String.format("%05d", gRs.result());
                        Long seqId = Long.parseLong(prefix+suffixFmt);
                        result.complete(seqId);
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
     * 生成分布式ID，前12位为时间，后5位是在相同时间前缀下的自增序列
     * @return
     */
    public static Future<Long> getId() {
        return getFullSeq(getOrderIdPrefix());
    }
}

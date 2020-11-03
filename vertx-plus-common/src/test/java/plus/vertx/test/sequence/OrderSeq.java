package plus.vertx.test.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;
import plus.vertx.core.support.SeqUtil;
import plus.vertx.core.support.cluster.SingleVertx;

public class OrderSeq {
    public static final Logger log = LoggerFactory.getLogger(OrderSeq.class);
    /**
     * 订单号的生成通常要满足的需求：
     * 1)唯一性：订单号不可重复，以免发生业务冲突
     * 2)简短：便于记录、存储
     * 3)业务相关性：可根据订单号定位相关业务
     * 4)时间相关性：可根据订单号定位发生时间范围
     * 5)有序性：订单号应当有序，便于建立索引，提高查询速度
     * 6)安全性：订单号不应透露用户量、交易量等信息，也不要让别人随便就能拼出来。
     */

    public static void main(String[] args) {
        Vertx vertx = SingleVertx.getVertx();
        for (int i = 0; i < 10000; i++) {
            log.info("雪花算法ID:         {}",SeqUtil.nextSnowflakeId());
            vertx.executeBlocking(b->{
                SeqUtil.nextFullTimeId().onComplete(gRs->{
                    if (gRs.succeeded()) {
                        log.info("getOrderId:        {}",gRs.result());
                    } else {
                        log.error("msg:", gRs.cause());
                    }
                });
            }, rh->{
            });
            //1379123704365088
            //87340649610870784
            //1604302228134
            //123736088576847054
            //  201102162825
            //20200917172602bL
        }
    }
}

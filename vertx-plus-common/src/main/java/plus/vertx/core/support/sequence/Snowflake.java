package plus.vertx.core.support.sequence;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 雪花算法ID
 *
 * @author crazyliu
 */
public class Snowflake {
    
    final private static Logger LOG = LoggerFactory.getLogger(Snowflake.class);

    private final long unusedBits = 1L;

    /**
     * 起始时间戳
     *
     */
    private final long epochMilli = 1583478594543L;

    /**
     * Datetime length
     */
    private final long timestampBits = 41L;
    /**
     * Data identifier id length
     */
    private final long datacenterIdBits = 5L;
    /**
     * Worker id length
     */
    private final long workerIdBits = 5L;
    /**
     * Sequence length
     */
    private final long sequenceBits = 12L;

    /**
     * max values of timeStamp, workerId, datacenterId and sequence
     * 2^5-1
     */
    private final long maxDatacenterId = ~(-1L << datacenterIdBits);
    /**
     * 2^5-1
     */
    private final long maxWorkerId = ~(-1L << workerIdBits);
    /**
     * 2^12-1
     */
    private final long maxSequence = ~(-1L << sequenceBits);

    /**
     * left shift bits of timeStamp, workerId and datacenterId
     */
    private final long timestampShift = sequenceBits + datacenterIdBits + workerIdBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long workerIdShift = sequenceBits;

    /**
     * data center number the process running on, its value can't be modified
     * after initialization.
     */
    private long datacenterId = -1;

    /**
     * machine or process number, its value can't be modified after
     * initialization.
     */
    private long workerId = -1;

    /**
     * the unique and incrementing sequence number scoped in only one
     * period/unit (here is ONE millisecond). its value will be increased by 1
     * in the same specified period and then reset to 0 for next period.
     */
    private long sequence = 0L;

    /**
     * the time stamp last snowflake ID generated
     */
    private long lastTimestamp = -1L;

    private static Snowflake instance;

    public static synchronized Snowflake getInstance() {
        if (instance == null) {
            instance = new Snowflake();
        }
        return instance;
    }

    private Snowflake() {
    }

    /**
     * Initialization worker
     *
     * @param datacenterId see setDatacenterId
     * @param workerId see setWorkerId
     */
    public void initWorker(long datacenterId, long workerId) {
        setDatacenterId(datacenterId);
        setWorkerId(workerId);
    }

    /**
     * set data center id
     *
     * @param datacenterId data center number the process running on, value
     * range: [0,31]
     */
    public void setDatacenterId(long datacenterId) {
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            LOG.error("datacenter id can't be greater than {} or less than 0", maxDatacenterId);
        }
        this.datacenterId = datacenterId;
    }

    /**
     * set worker id
     *
     * @param workerId machine or process number, value range: [0,31]
     */
    public void setWorkerId(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            LOG.error("worker Id can't be greater than {} or less than 0", maxWorkerId);
        }
        this.workerId = workerId;
    }

    /**
     * generate an unique and incrementing id
     *
     * @return id
     */
    public synchronized long nextId() {
        if (datacenterId < 0 || workerId < 0) {
            LOG.error("datacenter Id and worker Id MUST BE initialization.");
        }

        long currTimestamp = timestampNow();

        if (currTimestamp < lastTimestamp) {
            LOG.error("Clock moved backwards. Refusing to generate id for {} milliseconds",
                            lastTimestamp - currTimestamp);
        }
        if (currTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                // overflow: greater than max sequence
                currTimestamp = waitNextMillis(currTimestamp);
            }
        } else { // reset to 0 for next period/millisecond
            sequence = 0L;
        }
        lastTimestamp = currTimestamp;

        long snowflake = ((currTimestamp - epochMilli) << timestampShift)
                | // timestamp
                (datacenterId << datacenterIdShift)
                | // datacenter
                (workerId << workerIdShift)
                | // worker
                sequence; // sequence
        return snowflake;
    }

    /**
     * track the amount of calling {@link #waitNextMillis(long)} method
     */
    private final AtomicLong waitCount = new AtomicLong(0);

    /**
     * @return the amount of calling {@link #waitNextMillis(long)} method
     */
    public long getWaitCount() {
        return this.waitCount.get();
    }

    public long getEpochMilli() {
        return this.epochMilli;
    }

    /**
     * running loop blocking until next millisecond
     *
     * @param	currTimestamp	current time stamp
     * @return current time stamp in millisecond
     */
    private long waitNextMillis(long currTimestamp) {
        waitCount.incrementAndGet();
        while (currTimestamp <= lastTimestamp) {
            currTimestamp = timestampNow();
        }
        return currTimestamp;
    }

    private long timestampNow() {
        return Instant.now(Clock.systemUTC()).toEpochMilli();
    }

    public String formatId(long id) {
        long timestamp = ((id & diode(unusedBits, timestampBits)) >> timestampShift) + epochMilli;
        long datacenterId_temp = (id & diode(unusedBits + timestampBits, datacenterIdBits)) >> datacenterIdShift;
        long workerId_temp = (id & diode(unusedBits + timestampBits + datacenterIdBits, workerIdBits)) >> workerIdShift;
        long sequence_temp = (id & diode(unusedBits + timestampBits + datacenterIdBits + workerIdBits, sequenceBits));
        String tmf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(
                Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("UTC")));
        return String.format("%s, #%d, @(%d,%d)", tmf, sequence_temp, datacenterId_temp, workerId_temp);
    }

    public long getTimestamp(long id) {
        return ((id & diode(unusedBits, timestampBits)) >> timestampShift) + epochMilli;
    }

    /**
     * a diode is a long value whose left and right margin are ZERO, while
     * middle bits are ONE in binary string layout. it looks like a diode in
     * shape.
     *
     * @param offset left margin position
     * @param length offset+length is right margin position
     * @return a long value
     */
    private long diode(long offset, long length) {
        int lb = (int) (64 - offset);
        int rb = (int) (64 - (offset + length));
        return (-1L << lb) ^ (-1L << rb);
    }

    @Override
    public String toString() {
        return "Snowflake Settings [timestampBits=" + timestampBits + ", datacenterIdBits=" + datacenterIdBits
                + ", workerIdBits=" + workerIdBits + ", sequenceBits=" + sequenceBits + ", epochMilli=" + epochMilli
                + ", datacenterId=" + datacenterId + ", workerId=" + workerId + "]";
    }
}

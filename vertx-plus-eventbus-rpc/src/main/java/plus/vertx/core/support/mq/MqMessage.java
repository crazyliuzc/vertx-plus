package plus.vertx.core.support.mq;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息体
 */
public class MqMessage implements Serializable {
	private static final long serialVersionUID = 42L;

	private long id;				// 消息ID,
	private String topic;			// 消息主题
	private String group;			// 消息分组, 分组一致时消息仅消费一次；存在多个分组时，多个分组时【广播消费】；
	private String data;			// 消息数据
	private String status;			// 消息状态, @sae MqMessageStatus
	private int retryCount;			// 重试次数, 执行失败且大于0时生效，每重试一次减一；
	private long shardingId;		// 分片ID, 大于0时启用，否则使用消息ID；消费者通过该参数进行消息分片消费；分片ID不一致时分片【并发消费】、一致时【串行消费】；
	private int timeout;			// 超时时间，单位秒；大于0时生效，处于锁定运行状态且运行超时时，将主动标记运行失败；
	private Date effectTime;		// 生效时间, new Date()立即执行, 否则在生效时间点之后开始执行;
	private Date addTime;			// 创建时间
	private String log;				// 流转日志


	public MqMessage() {
	}

	public MqMessage(String topic, String data) {
		this.topic = topic;
		this.data = data;
	}

	public MqMessage(String topic, String data, Date effectTime) {
		this.topic = topic;
		this.data = data;
		this.effectTime = effectTime;
	}

	public MqMessage(String topic, String data, long shardingId) {
		this.topic = topic;
		this.data = data;
		this.shardingId = shardingId;
	}

	// for clone
	public MqMessage(MqMessage MqMessage) {
		this.id = MqMessage.id;
		this.topic = MqMessage.topic;
		this.group = MqMessage.group;
		this.data = MqMessage.data;
		this.status = MqMessage.status;
		this.retryCount = MqMessage.retryCount;
		this.shardingId = MqMessage.shardingId;
		this.timeout = MqMessage.timeout;
		this.effectTime = MqMessage.effectTime;
		this.addTime = MqMessage.addTime;
		this.log = MqMessage.log;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public long getShardingId() {
		return shardingId;
	}

	public void setShardingId(long shardingId) {
		this.shardingId = shardingId;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Override
	public String toString() {
		return "MqMessage{" +
				"id=" + id +
				", topic='" + topic + '\'' +
				", group='" + group + '\'' +
				", data='" + data + '\'' +
				", status='" + status + '\'' +
				", retryCount=" + retryCount +
				", shardingId=" + shardingId +
				", timeout=" + timeout +
				", effectTime=" + effectTime +
				", addTime=" + addTime +
				", log='" + log + '\'' +
				'}';
	}

}
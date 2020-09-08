package plus.vertx.core;

import io.vertx.core.AbstractVerticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 常量表
 * 
 */
public class Constants extends AbstractVerticle {
	final private static Logger LOGGER = LoggerFactory.getLogger(Constants.class);

	/**
	 * 配置文件名
	 */
	private static String PROFILE_YAML = "my-config.yaml";
	/**
	 * 换行符
	 */
	public static String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
	/**
	 * 系统文件路径分隔符
	 */
	public static String FILE_SEPARATOR = File.separator;
	/**
	 * 编译后项目根目录
	 */
	public static String CLASS_PATH = Thread.currentThread().getContextClassLoader ().getResource("").getPath();

	//===========================数值定义============================
	/**
	 * 每天小时数
	 */
	public static final int HOURS_PER_DAY = 24;

	/**
	 * 每小时分钟数
	 */
	public static final int MINUTES_PER_HOUR = 60;

	/**
	 * 每分钟秒数
	 */
	public static final int SECONDS_PER_MINUTE = 60;

	/**
	 * 每秒毫秒数
	 */
	public static final int MILLISECOND_PER_SECONDS = 1000;

	/**
	 * 每天秒数
	 */
	public static final int SECONDS_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;

	/**
	 * 每天毫秒数
	 */
	public static final int MILLISECOND_PER_DAY = SECONDS_PER_DAY * MILLISECOND_PER_SECONDS;

	//===========================异常定义============================
	/**
	 * 解析日期时异常
	 */
	public static final String PARSE_LOCAL_DATE_EXCEPTION = "Unable to obtain";

	//===========================正则定义============================

	/**
	 * 纯数字
	 */
	public static final Pattern NUMERIC_REGEX = Pattern.compile("[0-9]+");

	/**
	 * 字母开头
	 */
	public static final Pattern START_WITH_WORD_REGEX = Pattern.compile("^[A-Za-z].*");

	/**
	 * 中文
	 */
	public static final Pattern CHINESE_REGEX = Pattern.compile("[\u4E00-\u9FFF]");
	//===========================其他定义============================

	/**
	 * MonthDay 默认解析前缀
	 * 解析字符串需要加前缀，如"--12-03"
	 *  java.time.MonthDay.parse(CharSequence)
	 */
	public static final String MONTHDAY_FORMAT_PRE = "--";

	/**
	 * 中文
	 */
	public static final String ZH = "zh";

	/**
	 * 除夕，节日处理使用
	 */
	public static final String CHUXI = "CHUXI";

	/**
	 * 春节，节日处理使用
	 */
	public static final String CHUNJIE = "0101";

	private static volatile Constants constants;

	private Constants(){
	}

	public static Constants getInstance(){
		if(constants == null){
			synchronized(Constants.class){
				if(constants == null){
					constants = new Constants();
				}
			}
		}
		return constants;
	}

	//===========================常用方法============================

	/**
	 * 是否为中文语言环境
	 * @return boolean
	 */
	public static boolean isChineseLanguage(){
		return Locale.getDefault().getLanguage().equals(ZH);
	}

}

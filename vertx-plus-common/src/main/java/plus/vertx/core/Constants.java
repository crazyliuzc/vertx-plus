package plus.vertx.core;

import io.vertx.core.AbstractVerticle;
import plus.vertx.core.support.yaml.YamlBean;

import java.io.File;

/**
 * 常量表
 *
 * @author crazyliu
 */
public class Constants extends AbstractVerticle {

    /**
     * 配置文件key
     */
    public static final String PROFILE_NAME = "profileName";

    /**
     * 配置文件名
     */
    public static final String PROFILE_YAML = "my-config.yaml";
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
    public static String CLASS_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    /**
     * 配置文件类
     */
    public static YamlBean CONFIG;

    //===========================常用方法============================
}

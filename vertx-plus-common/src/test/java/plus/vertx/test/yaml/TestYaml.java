package plus.vertx.test.yaml;

import plus.vertx.core.VertxApplication;

import javax.annotation.Resource;

/**
 * 测试获取配置文件
 *
 * @author crazyliu
 */
@Resource(name = "t1.yaml")
public class TestYaml {
    public static void main(String[] args) {
        VertxApplication.run(TestYaml.class);
    }
}

package plus.vertx.test.yaml;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import plus.vertx.core.startup.MainVerticle;
import plus.vertx.core.support.VertxUtil;

/**
 * 测试获取配置文件
 * @author crazyliu
 */
public class TestYaml {
    public static void main(String[] args) {
        Vertx vertx = VertxUtil.getVertx();
        VertxUtil.run(MainVerticle.class, vertx,new DeploymentOptions());
    }
}

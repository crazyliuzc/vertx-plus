package plus.vertx.core;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import plus.vertx.core.support.VertxUtil;
import plus.vertx.core.support.cluster.SingleVertx;

/**
 * 需要制作控制台，配合云函数
 * Created by crazyliu on 2020/9/23.
 */
public class RouterUtil {
    public static void main(String[] args) {
        Vertx vertx = SingleVertx.getVertx();
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.clear();
    }
}

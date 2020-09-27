package plus.vertx.test.yaml;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import plus.vertx.core.startup.MainVerticle;
import plus.vertx.core.support.VertxUtil;

/**
 * 测试获取配置文件
 *
 * @author crazyliu
 */
public class TestYaml {

    public static void main(String[] args) {
        System.out.println("plus.vertx.test.yaml.TestYaml.main()"+getLocalIp());
        Vertx vertx = VertxUtil.getVertx();
        VertxUtil.run(MainVerticle.class, vertx, new DeploymentOptions());
    }

    public static String getLocalIp() {
        String localIp = null;
        InetAddress inetAddress = getLocalHostLANAddress();
        if (null != inetAddress) {
            localIp = inetAddress.getHostAddress();
        }
        return localIp;
    }

    private static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress;
        } catch (SocketException | UnknownHostException e) {
            return null;
        }
    }
}

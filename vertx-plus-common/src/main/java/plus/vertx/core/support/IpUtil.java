package plus.vertx.core.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Ip工具类
 * @author crazyliu
 */
public class IpUtil {
    private static final Logger log = LoggerFactory.getLogger(IpUtil.class);

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
            for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        // 排除loopback类型地址
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
            return InetAddress.getLocalHost();
        } catch (SocketException | UnknownHostException e) {
            log.error("",e);
            return null;
        }
    }
}

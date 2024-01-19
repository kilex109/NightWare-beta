package nightware.main.utility.misc;

import java.net.InetAddress;

public class PingUtility {

    public static long ping;
    public static long getPing(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            long startTime = System.currentTimeMillis();
            boolean isReachable = inetAddress.isReachable(5000);
            if(isReachable) {
                long endTime = System.currentTimeMillis();
                ping = endTime - startTime;

            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return ping;
    }
}

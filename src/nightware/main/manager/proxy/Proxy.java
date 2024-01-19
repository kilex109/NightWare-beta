package nightware.main.manager.proxy;

public class Proxy {
   public String ipPort = "";
   public Proxy.ProxyType type;
   public String username;
   public String password;

   public Proxy() {
      this.type = Proxy.ProxyType.SOCKS5;
      this.username = "";
      this.password = "";
   }

   public Proxy(boolean isSocks4, String ipPort, String username, String password) {
      this.type = Proxy.ProxyType.SOCKS5;
      this.username = "";
      this.password = "";
      this.type = isSocks4 ? Proxy.ProxyType.SOCKS4 : Proxy.ProxyType.SOCKS5;
      this.ipPort = ipPort;
      this.username = username;
      this.password = password;
   }

   public int getPort() {
      return Integer.parseInt(this.ipPort.split(":")[1]);
   }

   public String getIp() {
      return this.ipPort.split(":")[0];
   }

   public static enum ProxyType {
      SOCKS4,
      SOCKS5;
   }
}

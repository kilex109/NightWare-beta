package nightware.main.manager.proxy;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;

public class ProxyManager {
   public static boolean proxyEnabled = false;
   public static Proxy proxy = new Proxy();
   private static final File proxyFile;

   public static String getProxyIp() {
      return proxy.ipPort.isEmpty() ? "Нет" : (proxyEnabled ? proxy.getIp() : "Отключено");
   }

   public void init() throws IOException {
      if (!proxyFile.exists()) {
         proxyFile.createNewFile();
      } else {
         this.load();
      }

   }

   private void load() {
      try {
         FileInputStream fileInputStream = new FileInputStream(proxyFile.getAbsolutePath());
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));
         String line = reader.readLine().trim();
         String type = line.split(";")[0];
         String ipPort = line.split(";")[1];
         String username = line.split(";")[2];
         String password = line.split(";")[3];
         String enabled = line.split(";")[4];
         proxy = new Proxy(type.equals("SOCKS4"), ipPort, username, password);
         proxyEnabled = Boolean.parseBoolean(enabled);
         reader.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void save() {
      try {
         Files.write(proxyFile.toPath(), (proxy.type.toString() + ";" + proxy.ipPort + ";" + proxy.username + ";" + proxy.password + ";" + proxyEnabled).getBytes(), new OpenOption[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   static {
      proxyFile = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\proxy.ss");
   }
}

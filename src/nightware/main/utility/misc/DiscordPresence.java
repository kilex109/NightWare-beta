package nightware.main.utility.misc;

import java.awt.image.BufferedImage;

public class DiscordPresence {
   private static Thread rpcThread;
   private static final long lastTimeMillis = System.currentTimeMillis();
   public static String avatarUrl;
   public static BufferedImage avatar;

   public static void startDiscord() {
      System.out.println("����� �� ���������� DISCORD RPC ���� ��� ��� ����!?!?!?");
   }

   public static void shutdownDiscord() {

   }

   private static void updatePresence() {

   }
}

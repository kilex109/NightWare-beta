package nightware.main.utility.misc;

import nightware.main.NightWare;
import nightware.main.ProcessChecker;
import nightware.main.utility.Utility;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtility implements Utility {
   public static String chatPrefix;

   public static void addChatMessage(String message) {
      mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
      ProcessChecker.checkProcess();
   }

   public static void sendChatMessage(String message) {
      mc.player.sendChatMessage(message);
      ProcessChecker.checkProcess();
   }

   static {
      chatPrefix = TextFormatting.GOLD + NightWare.NAME + TextFormatting.GRAY + " // " + TextFormatting.RESET;
      ProcessChecker.checkProcess();
   }
}

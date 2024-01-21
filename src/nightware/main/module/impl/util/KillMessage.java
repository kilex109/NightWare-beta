package nightware.main.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketTitle;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.utility.misc.BullingUtility;
import nightware.main.utility.misc.ChatUtility;

@ModuleAnnotation(
   name = "KillMessage",
   category = Category.UTIL
)
public class KillMessage extends Module {

   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketTitle) {
         SPacketTitle title = (SPacketTitle) eventPacket.getPacket();
         String m = title.getMessage().getUnformattedText();
         StringBuilder builder = new StringBuilder();
         char[] buffer = m.toCharArray();
         for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '§') {
               i++;
            } else {
               builder.append(buffer[i]);
            }
         }
         if (m.contains("(2 HP)")) {
            BullingUtility.bull().thenAccept(result -> {
               if (result != null) {
                  ChatUtility.sendChatMessage("!" + AimBot.lastTarget.getTarget().getName() + " " + result);
               }
            });
         }
      }
   }
}

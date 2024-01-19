package nightware.main.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.math.BlockPos;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.utility.misc.BullingUtility;
import nightware.main.utility.misc.ChatUtility;

@ModuleAnnotation(
   name = "BullingBot",
   category = Category.COMBAT
)
public class BullingBot extends Module {

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
            ChatUtility.sendChatMessage("!" + AimBot.lastTarget.getTarget().getName() + " " + BullingUtility.bull(" "));
         }
      }
   }
}

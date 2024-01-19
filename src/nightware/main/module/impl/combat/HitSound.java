package nightware.main.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.utility.misc.SoundUtility;
import net.minecraft.network.play.server.SPacketTitle;

@ModuleAnnotation(
   name = "HitSound",
   category = Category.COMBAT
)
public class HitSound extends Module {
   private final ModeSetting mode = new ModeSetting("«‚ÛÍ", "Bonk", "Bonk", "Bell", "Bubble", "Crime", "Rust", "Hit");

   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketTitle) {
         SPacketTitle title = (SPacketTitle) eventPacket.getPacket();
         String m = title.getMessage().getUnformattedText();
         StringBuilder builder = new StringBuilder();
         char[] buffer = m.toCharArray();
         for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == 'ß') {
               i++;
            } else {
               builder.append(buffer[i]);
            }
         }
         if (m.contains("HP)")) {
            SoundUtility.playSound(mode.get().toLowerCase() + ".wav", 1);
         }
      }
   }
}

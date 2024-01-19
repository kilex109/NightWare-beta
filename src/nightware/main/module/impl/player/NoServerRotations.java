package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleAnnotation(
   name = "NoServerRotations",
   category = Category.PLAYER
)
public class NoServerRotations extends Module {
   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketPlayerPosLook) {
         SPacketPlayerPosLook packet = (SPacketPlayerPosLook)eventPacket.getPacket();
         packet.yaw = mc.player.rotationYaw;
         packet.pitch = mc.player.rotationPitch;
      }

   }
}

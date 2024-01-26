package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketChatMessage;
import nightware.main.event.misc.EventPush;
import nightware.main.event.packet.EventSendPacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventOverlay;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.MultiBooleanSetting;
import nightware.main.utility.misc.ChatUtility;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

@ModuleAnnotation(
   name = "NoAction",
   category = Category.PLAYER
)
public class NoAction extends Module {
   public final MultiBooleanSetting actions = new MultiBooleanSetting("Действия", Arrays.asList("NoPush", "NoHurtCam", "CameraClip", "NoJumpDelay", "NoClickDelay"));

   @EventTarget
   public void onPush(EventPush e) {
      if (actions.get(0)) {
         e.setCancelled(true);
      }
   }

   @EventTarget
   public void onOverlayRender(EventOverlay e) {
      if (e.getOverlayType().equals(EventOverlay.OverlayType.HURT_CAM) && actions.get(1)) {
         e.setCancelled(true);
      }

      if (e.getOverlayType().equals(EventOverlay.OverlayType.CAMERA_CLIP) && actions.get(2)) {
         e.setCancelled(true);
      }
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if (actions.get(3)) {
         mc.player.jumpTicks = 0;
      }

      if (actions.get(4)) {
         mc.rightClickDelayTimer = 0;
      }
   }

   @Override
   public void onDisable() {
      if (mc.player != null) {
         mc.player.jumpTicks = 10;
         mc.rightClickDelayTimer = 6;
      }
   }
}

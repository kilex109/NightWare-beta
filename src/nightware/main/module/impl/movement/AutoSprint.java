package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventSendPacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.utility.move.MovementUtility;
import net.minecraft.network.play.client.CPacketEntityAction;

@ModuleAnnotation(
   name = "AutoSprint",
   category = Category.MOVEMENT
)
public class AutoSprint extends Module {
   public static BooleanSetting omnidirectional = new BooleanSetting("Во все стороны", false);

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player != null) {
         if (mc.player.getFoodStats().getFoodLevel() > 6 && !mc.player.isSneaking() && !mc.player.isCollidedHorizontally) {
            mc.player.setSprinting(omnidirectional.get() ? MovementUtility.isMoving() : mc.player.movementInput.forwardKeyDown);
         }

      }
   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      if (omnidirectional.get() && event.getPacket() instanceof CPacketEntityAction) {
         CPacketEntityAction packet = (CPacketEntityAction)event.getPacket();
         if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING) {
            event.setCancelled(true);
         }

         if (packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
            event.setCancelled(true);
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      if (mc.player != null) {
         mc.player.setSprinting(false);
      }
   }
}

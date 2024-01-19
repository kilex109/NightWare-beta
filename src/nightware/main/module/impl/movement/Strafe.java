package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.Utils;

@ModuleAnnotation(
   name = "Strafe",
   category = Category.MOVEMENT
)
public class Strafe extends Module {

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player.onGround) {
         Utils.setSpeed(0.22f);
      } else {
         Utils.setSpeed(0.25f);
      }
   }
}

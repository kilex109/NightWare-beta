package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;

@ModuleAnnotation(
   name = "Timer",
   category = Category.MOVEMENT
)
public class Timer extends Module {
   public final NumberSetting timerSpeed = new NumberSetting("Скорость", 0.6F, 0.1F, 2.0F, 0.1F);

   public void onDisable() {
      mc.timer.field_194147_b = 1.0F;
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      mc.timer.field_194147_b = this.timerSpeed.get();
   }
}

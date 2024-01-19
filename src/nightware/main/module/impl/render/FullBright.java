package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

@ModuleAnnotation(
   name = "FullBright",
   category = Category.RENDER
)
public class FullBright extends Module {
   @EventTarget
   public void onUpdate(EventUpdate event) {
      mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 6000, 1));
   }

   public void onDisable() {
      if (mc.player != null) {
         mc.player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
      }

      super.onDisable();
   }
}

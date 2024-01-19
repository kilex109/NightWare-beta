package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.MultiBooleanSetting;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Arrays;

@ModuleAnnotation(
   name = "AntiAFK",
   category = Category.PLAYER
)
public class AntiAFK extends Module {
   public final MultiBooleanSetting actions = new MultiBooleanSetting("Действия", Arrays.asList("Нажание", "Прыжок", "Сообщение"));

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (!mc.player.isDead && mc.player.getHealth() > 0.0F) {
         if (this.actions.get(0) && mc.player.ticksExisted % 60 == 0) {
            mc.clickMouse();
         }

         if (this.actions.get(1) && mc.player.ticksExisted % 40 == 0 && !mc.gameSettings.keyBindJump.pressed && mc.player.onGround) {
            mc.player.jump();
         }

         if (this.actions.get(2) && mc.player.ticksExisted % 400 == 0) {
            mc.player.sendChatMessage("/hlep");
         }
      }
   }
}

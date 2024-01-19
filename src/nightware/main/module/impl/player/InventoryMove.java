package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.ContainerRepair;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;

@ModuleAnnotation(
   name = "InventoryMove",
   category = Category.PLAYER
)
public class InventoryMove extends Module {

   @EventTarget
   public void onUpdate(EventUpdate event) {
      KeyBinding[] keys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
      if (mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiEditSign || mc.currentScreen instanceof GuiRepair)
         return;

      KeyBinding[] var1 = keys;
      int var2 = keys.length;

      for (int var3 = 0; var3 < var2; ++var3) {
         KeyBinding keyBinding = var1[var3];
         keyBinding.pressed = Keyboard.isKeyDown(keyBinding.getKeyCode());
      }
   }
}

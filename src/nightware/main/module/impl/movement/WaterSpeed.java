package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.move.MoveUtil;

import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(
   name = "WaterSpeed",
   category = Category.MOVEMENT
)
public class WaterSpeed extends Module {
   public NumberSetting speed = new NumberSetting("Скорость", 0.4f, 0.1f, 10f, 0.1f);
   public BooleanSetting miniJump = new BooleanSetting("Мини-прыжки", true);

   @EventTarget
   public void onUpdate(EventUpdate event) {
         List<ItemStack> stacks = new ArrayList<>();
         mc.player.getArmorInventoryList().forEach(stacks::add);
         stacks.removeIf(w -> w.getItem() instanceof ItemAir);
         if (mc.player.isCollidedHorizontally) {
            return;
         }
         if (!mc.player.isInWater()) return;
         if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking() && !(mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
            mc.player.motionY = 0.12f;
         }
         if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.player.motionY = -0.12f;
         }


         if (miniJump.get() && mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir && mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.player.setSneaking(true);
            mc.player.motionY = .12f;
         }
         MoveUtil.setMotion(speed.get());
         mc.player.setSneaking(true);
   }

   @Override
   public void onDisable() {
      super.onDisable();
   }
}

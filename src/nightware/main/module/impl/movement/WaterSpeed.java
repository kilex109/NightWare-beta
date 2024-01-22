package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.Utils;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.misc.ChatUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ModuleAnnotation(
   name = "WaterSpeed",
   category = Category.MOVEMENT
)
public class WaterSpeed extends Module {
    public NumberSetting speed = new NumberSetting("Скорость", 0.4f, 0.1f, 25f, 0.1f);
    public NumberSetting speedLimit = new NumberSetting("Лимит скорости", 2.0f, 0.1f, 25f, 0.1f);
    public NumberSetting addSpeed = new NumberSetting("Скорость накопления", 0.5f, 0.1f, 5f, 0.01f);
    public static ModeSetting mode = new ModeSetting("Режим", "Плавный", "Плавный", "Резкий");
    public static float newSpeed;

    public WaterSpeed() {
        speed.setVisible(() -> mode.is("Резкий"));
        speedLimit.setVisible(() -> mode.is("Плавный"));
        addSpeed.setVisible(() -> mode.is("Плавный"));
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.is("Резкий")) {
            List<ItemStack> stacks = new ArrayList<>();
            mc.player.getArmorInventoryList().forEach(stacks::add);
            stacks.removeIf(w -> w.getItem() instanceof ItemAir);
            mc.player.setSprinting(false);
            if (!mc.player.isInWater()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking() && !(mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
                mc.player.motionY = 0.12f;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.12f;
            }


            if (mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir && mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.setSneaking(true);
                mc.player.motionY = .12f;
            }
            Utils.setSpeed(speed.get());
            mc.player.setSneaking(true);
        } else if (mode.is("Плавный")) {
            List<ItemStack> stacks = new ArrayList<>();
            mc.player.getArmorInventoryList().forEach(stacks::add);
            stacks.removeIf(w -> w.getItem() instanceof ItemAir);
            mc.player.setSprinting(false);
            if (!mc.player.isInWater()) return;
            if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isSneaking() && !(mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir)) {
                mc.player.motionY = 0.12f;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -0.12f;
            }


            if (mc.world.getBlockState(mc.player.getPosition().add(0, 1, 0)).getBlock() instanceof BlockAir && mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.setSneaking(true);
                mc.player.motionY = .12f;
            }
            if (newSpeed >= speedLimit.get()) {
                newSpeed = speedLimit.get();
            }
            Utils.setSpeed(newSpeed);

            mc.player.setSneaking(true);
        }
    }

    @Override
    public void onDisable() {
        newSpeed = 0.6f;
        super.onDisable();
    }

    @Override
    public void onEnable() {
        newSpeed = 0.6f;
        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    newSpeed += addSpeed.get();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        super.onEnable();
    }
}

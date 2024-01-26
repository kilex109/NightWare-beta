package nightware.main.module.impl.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

import java.util.UUID;

@ModuleAnnotation(
   name = "FakePlayer",
   category = Category.UTIL
)
public class FakePlayer extends Module {
   public EntityPlayer player;

   @Override
   public void onEnable() {
      player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("6714531a-1c69-438e-b7d6-d6d41ca6838b"), "FakePlayer"));
      player.copyLocationAndAnglesFrom(mc.player);
      player.inventory.copyInventory(mc.player.inventory);
      mc.world.addEntityToWorld(-9151, player);
   }

   @Override
   public void onDisable() {
      mc.world.removeEntityFromWorld(-9151);
   }
}

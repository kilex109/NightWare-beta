package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import nightware.main.event.misc.EventFullCube;
import nightware.main.event.misc.EventPush;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.packet.EventSendPacket;
import nightware.main.event.player.EventLivingUpdate;
import nightware.main.event.player.EventMotion;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.move.MovementUtility;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;

@ModuleAnnotation(
   name = "FreeCam",
   category = Category.PLAYER
)
public class FreeCam extends Module {
   private float old;
   private double oldX;
   private double oldY;
   private double oldZ;
   private final NumberSetting speed = new NumberSetting("Скорость", 1.0F, 0.1F, 5.0F, 0.05F);

   @Override
   public void onDisable() {
      if (mc.player != null) {
         mc.player.capabilities.isFlying = false;
         mc.player.capabilities.setFlySpeed(old);
         mc.player.noClip = false;
         mc.renderGlobal.loadRenderers();
         mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
         mc.world.removeEntityFromWorld(-7776);
         mc.player.motionZ = 0;
         mc.player.motionX = 0;
      }

      super.onDisable();
   }

   @Override
   public void onEnable() {
      if (mc.player != null) {
         oldX = mc.player.posX;
         oldY = mc.player.posY;
         oldZ = mc.player.posZ;
         mc.player.noClip = true;
         EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
         fakePlayer.copyLocationAndAnglesFrom(mc.player);
         fakePlayer.posY -= 0;
         fakePlayer.rotationYawHead = mc.player.rotationYawHead;
         mc.world.addEntityToWorld(-7776, fakePlayer);
      }

      super.onEnable();
   }

   @EventTarget
   public void onFullCube(EventFullCube event) {
      event.setCancelled(true);
   }

   @EventTarget
   public void onPush(EventPush event) {
      event.setCancelled(true);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      mc.player.noClip = true;
      mc.player.onGround = false;
      mc.player.capabilities.setFlySpeed(speed.get() / 5);
      mc.player.capabilities.isFlying = true;
      event.setPosX(oldX);
      event.setPosY(oldY);
      event.setPosZ(oldZ);
   }


   @EventTarget
   public void onReceivePacket(EventReceivePacket event) {
      if (true) {
         if (event.getPacket() instanceof SPacketPlayerPosLook) {
            event.setCancelled(true);
         }
      }
   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      mc.player.setSprinting(false);
   }
}
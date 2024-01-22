package nightware.main.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.packet.EventSendPacket;
import nightware.main.event.player.EventMove;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.Utils;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.utility.misc.TimeV3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ModuleAnnotation(
   name = "PigPackFly",
   category = Category.MOVEMENT
)
public class PigPackFly extends Module {
   public static BooleanSetting luckyDayz = new BooleanSetting("LuckyDayz", true);
   public static ModeSetting phase = new ModeSetting("Фаза", "NCP", "NCP", "VANILLA", "NONE");
   public static ModeSetting packetMode = new ModeSetting("Режим", "DOWN", "DOWN", "UP", "ZERO", "BYPASS");
   private int teleportId;
   private CPacketPlayer.Position startingOutOfBoundsPos;
   private ArrayList<CPacketPlayer> packets = new ArrayList();
   private Map<Integer, TimeV3D> posLooks = new ConcurrentHashMap();
   private int antiKickTicks;
   private int vDelay;
   private int hDelay;
   private boolean limitStrict;
   private int limitTicks;
   private int jitterTicks;
   private boolean oddJitter;
   double speedX;
   double speedY;
   double speedZ;
   private float postYaw;
   private float postPitch;
   private int factorCounter;
   private Timer intervalTimer = new Timer();
   private static final Random random = new Random();
   public static float TIMER_VALUE;

   @EventTarget
   public void onTick(EventUpdate eventUpdate) {
      if (mc.currentScreen instanceof GuiDisconnected || mc.currentScreen instanceof GuiMainMenu || mc.currentScreen instanceof GuiMultiplayer || mc.currentScreen instanceof GuiDownloadTerrain) {
         toggle();
      }


      TIMER_VALUE = 1.0f;
   }

   public static boolean isPlayerMoving() {
      return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown();
   }

   public static double[] directionSpeed(double speed) {
      float forward = mc.player.moveForward;
      float side = mc.player.movementInput.moveStrafe;
      float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

      if (forward != 0.0f) {
         if (side > 0.0f) {
            yaw += ((forward > 0.0f) ? -45 : 45);
         } else if (side < 0.0f) {
            yaw += ((forward > 0.0f) ? 45 : -45);
         }
         side = 0.0f;
         if (forward > 0.0f) {
            forward = 1.0f;
         } else if (forward < 0.0f) {
            forward = -1.0f;
         }
      }

      final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
      final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
      final double posX = forward * speed * cos + side * speed * sin;
      final double posZ = forward * speed * sin - side * speed * cos;
      return new double[]{posX, posZ};
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player == null || mc.world == null) {
         toggle();
         return;
      }
      if (mc.player.ticksExisted % 20 == 0) {
         cleanPosLooks();
      }
      mc.player.setVelocity(0.0, 0.0, 0.0);
      if (teleportId <= 0) {
         startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1.0, randomHorizontal(), mc.player.onGround);
         packets.add((CPacketPlayer) startingOutOfBoundsPos);
         mc.player.connection.sendPacket(startingOutOfBoundsPos);
         return;
      }
      final boolean phasing = checkCollisionBox();
      speedX = 0.0;
      speedY = 0.0;
      speedZ = 0.0;
      if (mc.gameSettings.keyBindJump.isKeyDown() && (hDelay < 1)) {
         if (mc.player.ticksExisted % 20 == 0) {
            speedY = -0.032;
         } else speedY = 0.062;

         antiKickTicks = 0;
         vDelay = 5;
      } else if (mc.gameSettings.keyBindSneak.isKeyDown() && (hDelay < 1)) {
         speedY = -0.062;
         antiKickTicks = 0;
         vDelay = 5;
      }
      if (!mc.gameSettings.keyBindSneak.isKeyDown() || !mc.gameSettings.keyBindJump.isKeyDown()) {
         if (isPlayerMoving()) {
            final double[] dir = directionSpeed(((phasing && phase.isIgnoreCase("NCP")) ? 0.062 : 0.26) * 1f);
            if ((dir[0] != 0.0 || dir[1] != 0.0) && (vDelay < 1)) {
               speedX = dir[0];
               speedZ = dir[1];
               hDelay = 5;
            }
         }
      }
      if (phasing && ((phase.isIgnoreCase("NCP") && mc.player.moveForward != 0.0) || (mc.player.moveStrafing != 0.0 && speedY != 0.0))) {
         speedY /= 2.5;
      }

      float rawFactor = luckyDayz.get() ? 1.7f : 1;

      int factorInt = (int) Math.floor(rawFactor);
      ++factorCounter;
      if (factorCounter > (int) (20.0 / ((rawFactor - (double) factorInt) * 20.0))) {
         ++factorInt;
         factorCounter = 0;
      }
      for (int i = 1; i <= factorInt; ++i) {
         mc.player.setVelocity(speedX * i, speedY * i, speedZ * i);
         sendPackets(speedX * i, speedY * i, speedZ * i, packetMode.get(), true, false);
      }
      speedX = mc.player.motionX;
      speedY = mc.player.motionY;
      speedZ = mc.player.motionZ;

      --vDelay;
      --hDelay;
      ++limitTicks;
      ++jitterTicks;
      if (limitTicks > 3) {
         limitTicks = 0;
         limitStrict = !limitStrict;
      }
      if (jitterTicks > 7) {
         jitterTicks = 0;
      }
   }

   private void sendPackets(final double x, final double y, final double z, final String Client, final boolean sendConfirmTeleport, final boolean sendExtraCT) {
      final Vec3d nextPos = new Vec3d(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
      final Vec3d bounds = getBoundsVec(x, y, z, Client);
      final CPacketPlayer nextPosPacket = (CPacketPlayer)new CPacketPlayer.Position(nextPos.x, nextPos.y, nextPos.z, mc.player.onGround);
      packets.add(nextPosPacket);
      mc.player.connection.sendPacket(nextPosPacket);
      final CPacketPlayer boundsPacket = (CPacketPlayer)new CPacketPlayer.Position(bounds.x, bounds.y, bounds.z, mc.player.onGround);
      packets.add(boundsPacket);
      mc.player.connection.sendPacket(boundsPacket);
      if (sendConfirmTeleport) {
         ++teleportId;
         if (sendExtraCT) {
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId - 1));
         }
         mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId));
         posLooks.put(teleportId, new TimeV3D(nextPos.x, nextPos.y, nextPos.z, System.currentTimeMillis()));
         if (sendExtraCT) {
            mc.player.connection.sendPacket(new CPacketConfirmTeleport(teleportId + 1));
         }
      }
   }

   private Vec3d getBoundsVec(final double x, final double y, final double z, final String Mode) {
      switch (Mode) {
         case "UP":{

         }
         case "ZERO":{

         }
         case "BYPASS":{
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, mc.player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 1,mc.player.posZ, mc.player.onGround));
         }
         default: {
            return new Vec3d(mc.player.posX + x, mc.player.posY - 1337.0, mc.player.posZ + z);
         }

      }
   }

   public double randomHorizontal() {
      int randomValue = random.nextInt(29000000) + 500;
      return random.nextBoolean() ? (double)randomValue : (double)(-randomValue);
   }

   public static double randomLimitedVertical() {
      int randomValue = random.nextInt(22);
      randomValue += 70;
      return random.nextBoolean() ? (double)randomValue : (double)(-randomValue);
   }

   public static double randomLimitedHorizontal() {
      int randomValue = random.nextInt(10);
      return random.nextBoolean() ? (double)randomValue : (double)(-randomValue);
   }

   private void cleanPosLooks() {
      this.posLooks.forEach((tp, timeVec3d) -> {
         if (System.currentTimeMillis() - timeVec3d.getTime() > TimeUnit.SECONDS.toMillis(30L)) {
            this.posLooks.remove(tp);
         }

      });
   }

   public void onEnable() {
      if (mc.player != null && mc.world != null) {
         this.packets.clear();
         this.posLooks.clear();
         this.teleportId = 0;
         this.vDelay = 0;
         this.hDelay = 0;
         this.postYaw = -400.0F;
         this.postPitch = -400.0F;
         this.antiKickTicks = 0;
         this.limitTicks = 0;
         this.jitterTicks = 0;
         this.speedX = 0.0;
         this.speedY = 0.0;
         this.speedZ = 0.0;
         this.oddJitter = false;
         this.startingOutOfBoundsPos = null;
         this.startingOutOfBoundsPos = new CPacketPlayer.Position(this.randomHorizontal(), 1.0, this.randomHorizontal(), mc.player.onGround);
         this.packets.add(this.startingOutOfBoundsPos);
         mc.player.connection.sendPacket(this.startingOutOfBoundsPos);
      } else {
         this.toggle();
      }
   }

   @Override
   public void onDisable() {
      if (mc.player != null) {
         mc.player.setVelocity(0.0, 0.0, 0.0);
      }
      TIMER_VALUE = 1.0f;
   }

   @EventTarget
   public void onPacketReceive(EventReceivePacket event ) {
      if (event.getPacket() instanceof SPacketPlayerPosLook) {
         if (!(mc.currentScreen instanceof GuiDownloadTerrain)) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (mc.player.isEntityAlive()) {
               if (teleportId <= 0) {
                  teleportId = ((SPacketPlayerPosLook) event.getPacket()).getTeleportId();
               }
               else if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), false)) {
                  if (posLooks.containsKey(packet.getTeleportId())) {
                     final TimeV3D vec = posLooks.get(packet.getTeleportId());
                     if (vec.x == packet.getX() && vec.y == packet.getY() && vec.z == packet.getZ()) {
                        posLooks.remove(packet.getTeleportId());
                        event.setCancelled(true);

                        return;
                     }
                  }
               }
            }
            packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
            packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
            teleportId = packet.getTeleportId();
         }
         else {
            teleportId = 0;
         }
      }
   }

   @EventTarget
   public void onMove(EventMove event) {
      if (teleportId <= 0) return;


      event.motionX = speedX;
      event.motionY = speedY;
      event.motionZ = speedZ;

      if ((!phase.isIgnoreCase("NONE") && phase.isIgnoreCase("VANILLA")) || checkCollisionBox()) {
         mc.player.noClip = true;
      }
   }

   private boolean checkCollisionBox() {
      return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(0.0, 0.0, 0.0)).isEmpty() || !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, 2.0, 0.0).contract(0.0, 1.99, 0.0)).isEmpty();
   }

   @EventTarget
   public void onPacketSend(EventSendPacket event) {
      if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
         event.setCancelled(true);
      }

      if (event.getPacket() instanceof CPacketPlayer) {
         CPacketPlayer packet = (CPacketPlayer)event.getPacket();
         if (this.packets.contains(packet)) {
            this.packets.remove(packet);
            return;
         }

         event.setCancelled(true);
      }

   }
}

package nightware.main.utility.move;

import net.minecraft.potion.Potion;
import nightware.main.utility.Utility;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class MovementUtility implements Utility {
   public static boolean reason(boolean water) {
      boolean critWater = water && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ)).getBlock() instanceof BlockAir;
      return mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isOnLadder() || mc.player.isInWater() && !critWater || mc.player.isInWeb || mc.player.capabilities.isFlying;
   }

   public static boolean getUnderBoxes(EntityPlayer player) {
      return !mc.world.getCollisionBoxes(player, player.getEntityBoundingBox().shrink(0.0625D)).isEmpty();
   }

   public static double getBaseSpeed() {
      double baseSpeed = 0.2873;
      if (mc.player.isPotionActive(Potion.getPotionById(1))) {
         int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
         baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
      }
      return baseSpeed;
   }

   public static boolean isBlockAboveHead() {
      AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3D, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ + 0.3D, mc.player.posX + 0.3D, mc.player.posY + (!mc.player.onGround ? 1.5D : 2.5D), mc.player.posZ - 0.3D);
      return !mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
   }

   public static boolean getOffsetBoxes() {
      return mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 0.1D, 0.0D)).isEmpty();
   }

   public static boolean isMoving() {
      return mc.player.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F;
   }

   public static void strafe(double motion) {
      if (isMoving()) {
         double radians = (double)getDirection();
         mc.player.motionX = -Math.sin(radians) * motion;
         mc.player.motionZ = Math.cos(radians) * motion;
      }
   }

   public static float getDirection() {
      float rotationYaw = mc.player.rotationYaw;
      float strafeFactor = 0.0F;
      if (mc.player.moveForward > 0.0F) {
         strafeFactor = 1.0F;
      }

      if (mc.player.moveForward < 0.0F) {
         strafeFactor = -1.0F;
      }

      if (strafeFactor == 0.0F) {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 90.0F;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 90.0F;
         }
      } else {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 45.0F * strafeFactor;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 45.0F * strafeFactor;
         }
      }

      if (strafeFactor < 0.0F) {
         rotationYaw -= 180.0F;
      }

      return (float)Math.toRadians((double)rotationYaw);
   }

   public static void setMotion(double motion) {
      double forward = (double)mc.player.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0D && strafe == 0.0D) {
         mc.player.motionX = 0.0D;
         mc.player.motionZ = 0.0D;
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         double sin = (double)MathHelper.sin((float)Math.toRadians((double)(yaw + 90.0F)));
         double cos = (double)MathHelper.cos((float)Math.toRadians((double)(yaw + 90.0F)));
         mc.player.motionX = forward * motion * cos + strafe * motion * sin;
         mc.player.motionZ = forward * motion * sin - strafe * motion * cos;
      }

   }

   public static float getMotion() {
      return (float)Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
   }

   public static boolean isInLiquid() {
      return mc.player.isInWater() || mc.player.isInLava();
   }
}

package nightware.main.utility.move;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.*;
import nightware.main.utility.Utility;

import java.util.Objects;

public class MoveUtil implements Utility {
    public static void setSpeed(float speed) {

        if (mc.player.moveForward > 0) {
            mc.player.motionX = -Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveForward < 0) {
            mc.player.motionX = Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = -Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing > 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing < 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw)) * speed;
        }
        if (mc.player.moveStrafing > 0 && mc.player.moveForward > 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw + 45)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw + 45)) * speed;
        }
        if (mc.player.moveStrafing < 0 && mc.player.moveForward > 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw - 45)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw - 45)) * speed;
        }
        if (mc.player.moveStrafing > 0 && mc.player.moveForward < 0) {
            mc.player.motionX = -Math.cos(Math.toRadians(mc.player.rotationYaw + 135)) * speed;
            mc.player.motionZ = -Math.sin(Math.toRadians(mc.player.rotationYaw + 135)) * speed;
        }
        if (mc.player.moveStrafing < 0 && mc.player.moveForward < 0) {
            mc.player.motionX = Math.cos(Math.toRadians(mc.player.rotationYaw - 135)) * speed;
            mc.player.motionZ = Math.sin(Math.toRadians(mc.player.rotationYaw - 135)) * speed;
        }
    }

    public static void setMotion(double motion) {
        double forward = mc.player.moveForward;
        double strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0 && strafe == 0) {
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (float) (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (float) (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else if (forward < 0) {
                    forward = -1;
                }
            }
            double sin = MathHelper.sin((float) Math.toRadians(yaw + 90));
            double cos = MathHelper.cos((float) Math.toRadians(yaw + 90));
            mc.player.motionX = forward * motion * cos + strafe * motion * sin;
            mc.player.motionZ = forward * motion * sin - strafe * motion * cos;
        }
    }

    public static boolean isUnderBedrock() {
        if (mc.player.posY <= 3.0) {
            RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0, mc.player.posZ), false, false, false);
            return trace == null || trace.typeOfHit != RayTraceResult.Type.BLOCK;
        }
        return false;
    }
    public static void setSpeed2(float speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90)));
        mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90)));
    }

    public static boolean reason(boolean water) {
        boolean critWater = water && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock()
                instanceof BlockLiquid && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1,
                mc.player.posZ)).getBlock() instanceof BlockAir;
        return mc.player.isPotionActive(MobEffects.BLINDNESS) || mc.player.isOnLadder()
                || mc.player.isInWater() && !critWater || mc.player.isInWeb || mc.player.capabilities.isFlying;
    }

    public static double getPlayerMotion() {
        return Math.hypot(mc.player.motionX, mc.player.motionZ);
    }

    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (mc.player.isPotionActive(Potion.getPotionById(1))) {
            int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double) (amplifier + 1);
        }
        return baseSpeed;
    }
    public static double[] getSpeed(double speed) {
        float yaw = mc.player.rotationYaw;
        float forward = mc.player.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        if (forward != 0) {
            if (strafe > 0) {
                yaw += (forward > 0 ? -45 : 45);
            } else if (strafe < 0) {
                yaw += (forward > 0 ? 45 : -45);
            }
            strafe = 0;
            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }
        return new double[]{
                (forward * speed * Math.cos(Math.toRadians(yaw + 90))
                        + strafe * speed * Math.sin(Math.toRadians(yaw + 90))),
                (forward * speed * Math.sin(Math.toRadians(yaw + 90))
                        - strafe * speed * Math.cos(Math.toRadians(yaw + 90))),
                yaw};
    }



    public static int getSpeedEffect() {
        if (mc.player.isPotionActive(MobEffects.SPEED)) {
            return Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
        }
        return 0;
    }

    public static void setStrafe(double motion) {
        if (!isMoving()) return;
        double radians = getDirection();
        mc.player.motionX = -Math.sin(radians) * motion;
        mc.player.motionZ = Math.cos(radians) * motion;
    }

    public static boolean isMoving() {
        return mc.player.movementInput.moveStrafe != 0.0 || mc.player.moveForward != 0.0;
    }

    public static float getDirection() {
        float rotationYaw = mc.player.rotationYaw;

        float strafeFactor = 0f;

        if (mc.player.moveForward > 0)
            strafeFactor = 1;
        if (mc.player.moveForward < 0)
            strafeFactor = -1;

        if (strafeFactor == 0) {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 90;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 90;
        } else {
            if (mc.player.movementInput.moveStrafe > 0)
                rotationYaw -= 45 * strafeFactor;

            if (mc.player.movementInput.moveStrafe < 0)
                rotationYaw += 45 * strafeFactor;
        }

        if (strafeFactor < 0)
            rotationYaw -= 180;

        return (float) Math.toRadians(rotationYaw);
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(mc.player.posX - 0.3, mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ + 0.3, mc.player.posX + 0.3, mc.player.posY + (!mc.player.onGround ? 1.5 : 2.5),
                mc.player.posZ - 0.3);
        return !mc.world.getCollisionBoxes(mc.player, axisAlignedBB).isEmpty();
    }

    public static float getMotion() {
        return (float) Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
    }

    public static boolean isInGround() {
        return mc.player.onGround && !mc.gameSettings.keyBindJump.pressed;
    }

    public static boolean isInLiquid() {
        return mc.player.isInWater() || mc.player.isInLava();
    }
}


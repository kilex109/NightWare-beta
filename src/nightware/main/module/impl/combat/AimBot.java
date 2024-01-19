package nightware.main.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import nightware.main.NightWare;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nightware.main.utility.misc.ChatUtility;
import nightware.main.utility.render.RenderUtility;

import java.util.ArrayList;
import java.util.Comparator;

import static nightware.main.module.Utils.mc;

@ModuleAnnotation(
        name = "AimBot",
        category = Category.COMBAT
)
public class AimBot extends Module {

    public static boolean work = false;
    public static TargetResult lastTarget;
    public static TargetResult target;
    int ticks = 0;
    public TimerUtils timer = new TimerUtils();
    public TimerUtils timerLastSee = new TimerUtils();

    public static BooleanSetting bots = new BooleanSetting("Боты", false);
    public static BooleanSetting silent = new BooleanSetting("Сайлент", true);
    public static BooleanSetting multipoint = new BooleanSetting("Все конечности", false);
    public static NumberSetting accuracy = new NumberSetting("Аккуратность", 0.1f, 0.01f, 0.3f, 0.01f);
    public static NumberSetting fov = new NumberSetting("Радиус", 100, 10, 360, 1);
    public static NumberSetting predict = new NumberSetting("Предикт", 5.5f, 0, 10, 0.1f);
    public static BooleanSetting autoPredict = new BooleanSetting("Авто-предикт", true);
    public static BooleanSetting autoShoot = new BooleanSetting("Авто-Стрельба", false);
    public static BooleanSetting checkCoolDown = new BooleanSetting("Проверять задержку", false);
    public static NumberSetting cps = new NumberSetting("КПС", 15, 1, 20, 1);
    public static boolean ishandcontainbow;

    public AimBot() {
        super();
        predict.setVisible(() -> !autoPredict.get());
        checkCoolDown.setVisible(() -> autoShoot.get());
        cps.setVisible(() -> autoShoot.get());
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        RenderUtility.drawCircle((new ScaledResolution(mc)).getScaledWidth() / 2, (new ScaledResolution(mc)).getScaledHeight() / 2, fov.get(), -1);
        if (mc.currentScreen instanceof GuiSleepMP) {
            setToggled(false);
        }
        ishandcontainbow = mc.player.getHeldItemMainhand().itemDamage == 440 || mc.player.getHeldItemMainhand().itemDamage == 441 || mc.player.getHeldItemMainhand().itemDamage == 442 || mc.player.getHeldItemMainhand().itemDamage == 443 || mc.player.getHeldItemMainhand().itemDamage == 444 || mc.player.getHeldItemMainhand().itemDamage == 445 || mc.player.getHeldItemMainhand().itemDamage == 446 || mc.player.getHeldItemMainhand().itemDamage == 447 || mc.player.getHeldItemMainhand().itemDamage == 448 || mc.player.getHeldItemMainhand().itemDamage == 449 || mc.player.getHeldItemMainhand().itemDamage == 450 || mc.player.getHeldItemMainhand().itemDamage == 451 || mc.player.getHeldItemMainhand().itemDamage == 452 || mc.player.getHeldItemMainhand().itemDamage == 453 || mc.player.getHeldItemMainhand().itemDamage == 454 || mc.player.getHeldItemMainhand().itemDamage == 455 || mc.player.getHeldItemMainhand().itemDamage == 456 || mc.player.getHeldItemMainhand().itemDamage == 457 || mc.player.getHeldItemMainhand().itemDamage == 458 || mc.player.getHeldItemMainhand().itemDamage == 459 || mc.player.getHeldItemMainhand().itemDamage == 470 || mc.player.getHeldItemMainhand().itemDamage == 471 || mc.player.getHeldItemMainhand().itemDamage == 472 || mc.player.getHeldItemMainhand().itemDamage == 473 || mc.player.getHeldItemMainhand().itemDamage == 474 || mc.player.getHeldItemMainhand().itemDamage == 475 || mc.player.getHeldItemMainhand().itemDamage == 476 || mc.player.getHeldItemMainhand().itemDamage == 477 || mc.player.getHeldItemMainhand().itemDamage == 478 || mc.player.getHeldItemMainhand().itemDamage == 479;
        lastTarget = getTarget(predict.get());
        target = getTarget(predict.get());
        if (target == null) {
            work = false;
            ticks = 0;
            return;
        }
        timerLastSee.reset();
        work = true;
        ticks++;
        aim(target, event);
        if (autoShoot.get() && ticks > 1) {
            if (timer.hasReached((long) (1000l / cps.get()))) {
                shoot(target.getTarget());
            }
        }
    }

    public void shoot(EntityLivingBase target) {
        if ((checkCoolDown.get() && mc.player.getCooldownTracker().getCooldown(mc.player.inventory.getCurrentItem().getItem(), mc.getRenderPartialTicks()) == 0) || !checkCoolDown.get()) {
            mc.playerController.clickBlock(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ), EnumFacing.UP);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            timer.reset();
        }
    }

    public float getBowOffset(Entity t) {
        double distY = Math.abs(mc.player.posY - t.posY) * Math.abs(mc.player.posY - t.posY) / 36f;
        return (float) (((mc.player.getDistanceToEntity(t) * mc.player.getDistanceToEntity(t)) / (360 * 5.5)) + ((ishandcontainbow) ? distY : 0));
    }

    public void aim(TargetResult target, EventUpdate event) {
        float pred = autoPredict.get() ? (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) / 20f : predict.get();
        float[] rotations = getAim(target, pred);
        if (silent.get()) {
            event.setRotationYaw(rotations[0]);
            event.setRotationPitch(rotations[1]);
            mc.player.rotationYawHead = rotations[0];
            mc.player.rotationPitchHead = rotations[1];
            mc.player.renderYawOffset = rotations[0];
        } else {
            mc.player.rotationYaw = rotations[0];
            mc.player.rotationPitch = rotations[1];
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    public float[] getAim(TargetResult target, float predict) {
        predict += (ishandcontainbow) ? mc.player.getDistanceToEntity(target.getTarget()) / 5 : 0;
        float bowOffset = (ishandcontainbow) ? getBowOffset(target.getTarget()) : 0;
        double posX = (target.getTarget().lastTickPosX - target.getTarget().posX) * predict;
        double posZ = (target.getTarget().lastTickPosZ - target.getTarget().posZ) * predict;
        double posY = bowOffset;
        return getNeededRotations(target.getTarget().posX - posX + target.getPos().xCoord, target.getTarget().posY + target.getPos().yCoord + posY, target.getTarget().posZ - posZ + target.getPos().zCoord, mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public Vec3d getMultipointPos(EntityLivingBase target, float predict) {
        if (mc.player.canEntityBeSeen(target)) {
            return new Vec3d(0, target.getEyeHeight(), 0);
        }
        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        double posX = (target.lastTickPosX - target.posX) * predict;
        double posZ = (target.lastTickPosZ - target.posZ) * predict;
        if (mc.player.canEntityBeSeen((target.posX - posX), (target.posY) + target.getEyeHeight(), (target.posZ - posZ))) {
            return new Vec3d(0, target.getEyeHeight(), 0);
        } else if (multipoint.get()) {
            for (float x = -(target.width / 2); x < target.width / 2; x += accuracy.get()) {
                for (float y = 0; y < target.height; y += accuracy.get()) {
                    for (float z = -(target.width / 2); z < target.width / 2; z += accuracy.get()) {
                        if (mc.player.canEntityBeSeen((target.posX - posX) + x, (target.posY) + y, (target.posZ - posZ) + z)) {
                            vec3ds.add(new Vec3d(x, y, z));
                        }
                    }
                }
            }

            if (vec3ds.size() > 0) {
                vec3ds.sort(new Comparator<Vec3d>() {
                    @Override
                    public int compare(Vec3d o1, Vec3d o2) {
                        return (int) ((getDistance(o1, new Vec3d(0, target.getEyeHeight(), 0)) - getDistance(o2, new Vec3d(0, target.getEyeHeight(), 0))) * 1000);
                    }
                });
                return vec3ds.get(0);
            } else {
                return null;
            }
        }
        return null;
    }

    public TargetResult getTarget(float predict) {
        ArrayList<TargetResult> targetResults = new ArrayList<>();
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase && mc.player != entity && !entity.isInvisible()) {
                if (fov(entity, fov.get()) && !NightWare.getInstance().getFriendManager().isFriend(entity.getName()) && entity.getEntityId() != -7777 && entity.getEntityId() != -7776 && ((EntityLivingBase) entity).getHealth() > 0) {
                    if ((bots.get() && (entity instanceof EntityPlayer || entity instanceof EntityZombie)) || (entity instanceof EntityPlayer && !bots.get())) {
                        Vec3d vec3d = getMultipointPos((EntityLivingBase) entity, predict);
                        if (vec3d != null) {
                            targetResults.add(new TargetResult((EntityLivingBase) entity, vec3d));
                        }
                    }
                }
            }
        }
        targetResults.sort(new Comparator<TargetResult>() {
            @Override
            public int compare(TargetResult o1, TargetResult o2) {
                return (int) (mc.player.getDistanceToEntity(o1.getTarget()) - mc.player.getDistanceToEntity(o2.getTarget()));
            }
        });
        if (targetResults.size() > 0) {
            return targetResults.get(0);
        }
        return null;
    }

    public static float[] getNeededRotations(final double x, final double y, final double z, double playerX, double playerY, double playerZ) {
        double diffX = x - (playerX);
        double diffZ = z - (playerZ);
        double diffY = y - (playerY);
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f));
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        return new float[]{yaw, pitch};
    }

    public static double getDistance(Vec3d vec3d1, Vec3d vec3d2) {

        double d0 = vec3d1.xCoord - vec3d2.xCoord;
        double d1 = vec3d1.yCoord - vec3d2.yCoord;
        double d2 = vec3d1.zCoord - vec3d2.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;

    }

    public static boolean fov(Entity entity, float fov) {
        fov = (float) ((double) fov * 0.5D);
        double v = ((double) (Minecraft.getMinecraft().player.rotationYaw - fovToEntity(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return v >= 0.0D && v <= (double) fov || (double) (-fov) <= v && v <= 0.0D;
    }

    public static float fovToEntity(Entity ent) {
        double x = ent.posX - Minecraft.getMinecraft().player.posX;
        double z = ent.posZ - Minecraft.getMinecraft().player.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float) (yaw * -1.0D);
    }
}


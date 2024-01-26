package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nightware.main.NightWare;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.math.MathUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.AnimationMath;

import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ModuleAnnotation(name = "Particles", category = Category.RENDER)
public class PARTICOLES extends Module {
    public ModeSetting type = new ModeSetting("Режим", "Крестики", "Крестики", "Круги");
    public NumberSetting count = new NumberSetting("Количество", 3, 1, 25, 1);
    public List<Point> pointList = new ArrayList<>();
    public ArrayList<Trails.Point> p = new ArrayList<>();


    @EventTarget
    public void onPacket(EventReceivePacket e) {
        SPacketTitle title = (SPacketTitle) e.getPacket();
        String m = title.getMessage().getUnformattedText();
        EntityLivingBase entity = AimBot.lastTarget.getTarget();
        if (m.contains("HP)")) {
            for (int z = 0; z < count.get(); z++) {
                pointList.add(new Point(entity.posX, entity.posY, entity.posZ));
            }
        }
    }

    @EventTarget
    public void onDisplay(EventRender2D e) {
        pointList.removeIf(point -> (System.currentTimeMillis() - point.time) > 4410);

        for (Point entity : pointList) {
            entity.updatePhysics();

            double x = entity.x;
            double y = entity.y + 1;
            double z = entity.z;
            double width = 0;
            double height = 0;
            AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width,
                    y + height, z + width);
            List<Vec3d> vectors = Arrays.asList(
                    new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ),
                    new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ),
                    new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ),
                    new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ),
                    new Vec3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ),
                    new Vec3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ),
                    new Vec3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ),
                    new Vec3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

            mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);

            ScaledResolution sr = e.getResolution();
            Vector4d position = null;
            for (Vec3d vector : vectors) {
                vector = RenderUtility.vectorTo2D(2,
                        vector.x - mc.getRenderManager().renderPosX,
                        vector.y - mc.getRenderManager().renderPosY,
                        vector.z - mc.getRenderManager().renderPosZ);
                if (vector != null && vector.z > 0 && vector.z < 1) {

                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0);
                    }

                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
            }

            if (position != null) {

                mc.entityRenderer.setupOverlayRendering(2);
                double posX = position.x;
                double posY = position.y;

                if (type.is("Крестики")) {
                    float alpha = ((float) (5000 - (System.currentTimeMillis() - entity.time)) / 5000);

                    Color color = new Color(NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getRed(), NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getGreen(), NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getBlue(), (int) (alpha * 255));

                    RenderUtility.drawFixedGlow((float) (posX - 3), (float) (posY - 3), 7, 7, 15, color.getRGB());
                    RenderUtility.drawLine(posX - 2, posY - 2, posX + 4, posY + 4, 3, color.getRGB());
                    RenderUtility.drawLine(posX + 4, posY - 2, posX - 2, posY + 4, 3, color.getRGB());
                }
                if (type.is("Круги")) {
                    float alpha = ((float) (5000 - (System.currentTimeMillis() - entity.time)) / 5000);
                    Color color = new Color(NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getRed(), NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getGreen(), NightWare.getInstance().getC(pointList.indexOf(entity) * 10).getBlue(), (int) (alpha * 128));
                    RenderUtility.drawFixedGlow((float) (posX - 6.5f), (float) (posY - 6.5f), 6, 6, 15, color.getRGB());
                    RenderUtility.drawRoundCircle((float) (posX - 3), (float) (posY - 3), 5, 5, color.getRGB());
                }
            }
            GlStateManager.enableBlend();
            mc.entityRenderer.setupOverlayRendering();
        }
    }

    public class Point {
        double x, y, z;
        double motionX, motionY, motionZ;
        long time;

        public Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = System.currentTimeMillis();
            motionX = MathUtility.randomizeFloat(-0.01f, 0.01f);
            motionY = MathUtility.randomizeFloat(-0.01f, 0.01f);
            motionZ = MathUtility.randomizeFloat(-0.01f, 0.01f);
        }

        public void updatePhysics() {
            if (mc.world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() != Blocks.AIR) {
                motionY += 0.001f;
                motionX *= 1.1f;
                motionZ *= 1.1f;
            } else {
                motionY -= 0.00005;
            }
            motionX = AnimationMath.fast((float) motionX, 0, 1);
            motionY = AnimationMath.fast((float) motionY, 0, 1);
            motionZ = AnimationMath.fast((float) motionZ, 0, 1);
            this.x += this.motionX;
            this.y += this.motionY;
            this.z += this.motionZ;
        }
    }
}
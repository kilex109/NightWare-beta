package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.jhlabs.vecmath.Vector4f;
import nightware.main.event.render.EventRender2D;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.AnimationMath;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
   name = "Target ESP",
   category = Category.RENDER
)
public class TargetESP extends Module {
   public ModeSetting mode = new ModeSetting("Mode", "Default", new String[]{"Default"});
   public NumberSetting circleSpeed = new NumberSetting("Speed", 2.0F, 1.0F, 5.0F, 0.1F, () -> {
      return this.mode.is("Default");
   });
   public static double prevCircleStep;
   public static double circleStep;

   @EventTarget
   public void onRender(EventRender3D event) {
      if (this.mode.get().equals("Default")) {
         EntityLivingBase targetEntity = AimBot.target.getTarget();
         if (targetEntity != null) {
            prevCircleStep = circleStep;
            circleStep += (double)this.circleSpeed.get() * AnimationMath.deltaTime();
            float eyeHeight = targetEntity.getEyeHeight();
            if (targetEntity.isSneaking()) {
               eyeHeight -= 0.2F;
            }

            double cs = prevCircleStep + (circleStep - prevCircleStep) * (double)mc.getRenderPartialTicks();
            double prevSinAnim = Math.abs(1.0D + Math.sin(cs - 0.5D)) / 2.0D;
            double sinAnim = Math.abs(1.0D + Math.sin(cs)) / 2.0D;
            double x = targetEntity.lastTickPosX + (targetEntity.posX - targetEntity.lastTickPosX) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosX;
            double y = targetEntity.lastTickPosY + (targetEntity.posY - targetEntity.lastTickPosY) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosY + prevSinAnim * (double)eyeHeight;
            double z = targetEntity.lastTickPosZ + (targetEntity.posZ - targetEntity.lastTickPosZ) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosZ;
            double nextY = targetEntity.lastTickPosY + (targetEntity.posY - targetEntity.lastTickPosY) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosY + sinAnim * (double)eyeHeight;
            GL11.glPushMatrix();
            GL11.glDisable(2884);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glDisable(2929);
            GL11.glDisable(3008);
            GL11.glShadeModel(7425);
            GL11.glBegin(8);

            int i;
            Color color;
            for(i = 0; i <= 360; ++i) {
               color = Arraylist.getColor(i);
               GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.6F);
               GL11.glVertex3d(x + Math.cos(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D, nextY, z + Math.sin(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D);
               GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.01F);
               GL11.glVertex3d(x + Math.cos(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D, y, z + Math.sin(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D);
            }

            GL11.glEnd();
            GL11.glEnable(2848);
            GL11.glBegin(2);

            for(i = 0; i <= 360; ++i) {
               color = Arraylist.getColor(i);
               GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, 0.8F);
               GL11.glVertex3d(x + Math.cos(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D, nextY, z + Math.sin(Math.toRadians((double)i)) * (double)targetEntity.width * 0.8D);
            }

            GL11.glEnd();
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glEnable(2929);
            GL11.glShadeModel(7424);
            GL11.glDisable(3042);
            GL11.glEnable(2884);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
         }

      }
   }
}

package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.ColorSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.AntiAliasingUtility;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
   name = "Tracers",
   category = Category.RENDER
)
public class Tracers extends Module {
   public NumberSetting lineWidth = new NumberSetting("Line Width", 1.0F, 0.1F, 2.0F, 0.1F);
   public ColorSetting colorSetting;
   public ColorSetting friendColorSetting;

   public Tracers() {
      this.colorSetting = new ColorSetting("Color", Color.WHITE.getRGB());
      this.friendColorSetting = new ColorSetting("Friend Color", Color.GREEN.getRGB());
   }

   @EventTarget
   public void onRender(EventRender3D eventRender3D) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if (entity != mc.player && entity instanceof EntityPlayer) {
            Color color = NightWare.getInstance().getFriendManager().isFriend(entity.getName()) ? this.friendColorSetting.getColor() : this.colorSetting.getColor();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getRenderPartialTicks() - mc.getRenderManager().renderPosZ;
            float red = (float)color.getRed() / 255.0F;
            float green = (float)color.getGreen() / 255.0F;
            float blue = (float)color.getBlue() / 255.0F;
            float alpha = (float)color.getAlpha() / 255.0F;
            GL11.glPushMatrix();
            boolean old = mc.gameSettings.viewBobbing;
            mc.gameSettings.viewBobbing = false;
            mc.entityRenderer.setupCameraTransform(eventRender3D.getPartialTicks(), 2);
            mc.gameSettings.viewBobbing = old;
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3042);
            GL11.glLineWidth(this.lineWidth.get());
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            AntiAliasingUtility.hook(true, false, false);
            GL11.glDepthMask(false);
            GlStateManager.color(red, green, blue, alpha);
            GL11.glBegin(3);
            Vec3d vec = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch((float)(-Math.toRadians((double)mc.player.rotationPitch))).rotateYaw((float)(-Math.toRadians((double)mc.player.rotationYaw)));
            GL11.glVertex3d(vec.x, vec.y + (double)mc.player.getEyeHeight(), vec.z);
            GL11.glVertex3d(x, y, z);
            GL11.glEnd();
            AntiAliasingUtility.unhook(true, false, false);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
         }
      }

   }
}

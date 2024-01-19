package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.AntiAliasingUtility;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
   name = "ChinaHat",
   category = Category.RENDER
)
public class ChinaHat extends Module {
   public BooleanSetting outline = new BooleanSetting("Outline", true);
   public NumberSetting lineWidth = new NumberSetting("Line Width", 1.0F, 0.1F, 5.0F, 0.1F, () -> {
      return this.outline.get();
   });

   @EventTarget
   public void onRender(EventRender3D event) {
      if (mc.gameSettings.thirdPersonView != 0 && mc.player.isEntityAlive()) {
         double ix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)event.getPartialTicks());
         double iy = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)event.getPartialTicks());
         double iz = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)event.getPartialTicks());
         float x = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)event.getPartialTicks());
         float y = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)event.getPartialTicks()) + mc.player.height + 0.01F - (mc.player.isSneaking() ? 0.2F : 0.0F);
         float z = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)event.getPartialTicks());
         GlStateManager.pushMatrix();
         GL11.glDepthMask(false);
         GlStateManager.enableDepth();
         GL11.glRotatef(-mc.player.rotationYaw, 0.0F, 1.0F, 0.0F);
         mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
         GlStateManager.translate(ix, iy, iz);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GlStateManager.disableTexture2D();
         GL11.glDisable(2884);
         GL11.glShadeModel(7425);
         GL11.glDisable(3008);
         AntiAliasingUtility.hook(true, false, false);
         GlStateManager.alphaFunc(516, 0.0F);
         GL11.glBegin(6);
         Color c1 = Arraylist.getColor(1);
         GL11.glColor4f((float)c1.getRed() / 255.0F, (float)c1.getGreen() / 255.0F, (float)c1.getBlue() / 255.0F, 0.39215687F);
         GL11.glVertex3f(x, y + 0.35F, z);

         for(int i = 0; i <= 360; ++i) {
            double x1 = Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.66D;
            double z1 = Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.66D;
            Color c = Arraylist.getColor(i);
            GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 0.39215687F);
            GL11.glVertex3d((double)x + x1, (double)y, (double)z + z1);
         }

         GL11.glEnd();
         if (this.outline.get()) {
            GL11.glLineWidth(this.lineWidth.get());
            GL11.glBegin(2);

            for(int i = 0; i <= 360; ++i) {
               double x1 = Math.cos((double)i * 3.141592653589793D / 180.0D) * 0.66D;
               double z1 = Math.sin((double)i * 3.141592653589793D / 180.0D) * 0.66D;
               Color c = Arraylist.getColor(i);
               GL11.glColor4f((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, 1.0F);
               GL11.glVertex3d((double)x + x1, (double)y, (double)z + z1);
            }

            GL11.glEnd();
         }

         AntiAliasingUtility.unhook(true, false, false);
         GL11.glEnable(3008);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GL11.glEnable(2884);
         mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
         GlStateManager.resetColor();
         GL11.glDepthMask(true);
         GlStateManager.popMatrix();
      }
   }
}

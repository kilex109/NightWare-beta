package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.AntiAliasingUtility;
import nightware.main.utility.render.ColorUtility;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
   name = "Trails",
   category = Category.RENDER
)
public class Trails extends Module {
   private final NumberSetting removeTicks = new NumberSetting("Delete through", 100.0F, 100.0F, 500.0F, 1.0F);
   public ArrayList<Trails.Point> points = new ArrayList();

   @EventTarget
   public void onRender(EventRender3D eventRender3D) {
      if (mc.gameSettings.thirdPersonView != 0 && mc.player.isEntityAlive()) {
         boolean old = mc.gameSettings.viewBobbing;
         mc.gameSettings.viewBobbing = false;
         mc.entityRenderer.setupCameraTransform(eventRender3D.getPartialTicks(), 2);
         mc.gameSettings.viewBobbing = old;
         if (!mc.gameSettings.showDebugInfo) {
            double ix = -(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)eventRender3D.getPartialTicks());
            double iy = -(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)eventRender3D.getPartialTicks());
            double iz = -(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)eventRender3D.getPartialTicks());
            float x = (float)(mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double)eventRender3D.getPartialTicks());
            float y = (float)(mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double)eventRender3D.getPartialTicks());
            float z = (float)(mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double)eventRender3D.getPartialTicks());
            if (mc.player.motionX != 0.0D && mc.player.motionZ != 0.0D) {
               this.points.add(new Trails.Point(new Vec3d((double)x, (double)y, (double)z)));
            }

            this.points.removeIf((pointx) -> {
               return (float)pointx.time >= this.removeTicks.get();
            });
            GlStateManager.pushMatrix();
            GL11.glDepthMask(false);
            GlStateManager.translate(ix, iy, iz);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GlStateManager.disableTexture2D();
            GL11.glDisable(2884);
            GL11.glShadeModel(7425);
            GL11.glDisable(3008);
            GlStateManager.alphaFunc(516, 0.0F);
            AntiAliasingUtility.hook(true, false, false);
            GL11.glBegin(8);
            Iterator var12 = this.points.iterator();

            Trails.Point point;
            Color color;
            float alpha;
            Trails.Point temp;
            int color2;
            while(var12.hasNext()) {
               point = (Trails.Point)var12.next();
               color = Arraylist.getColor(this.points.indexOf(point));
               if (this.points.indexOf(point) < this.points.size() - 1) {
                  alpha = 100.0F * ((float)this.points.indexOf(point) / (float)this.points.size());
                  temp = (Trails.Point)this.points.get(this.points.indexOf(point) + 1);
                  color2 = ColorUtility.setAlpha((new Color(color.getRGB())).getRGB(), (int)alpha);
                  ColorUtility.setColor(color2);
                  GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
                  GL11.glVertex3d(temp.pos.x, temp.pos.y + (double)mc.player.height - 0.1D, temp.pos.z);
                  ++point.time;
               }
            }

            GL11.glEnd();
            GL11.glLineWidth(2.0F);
            GL11.glBegin(3);
            var12 = this.points.iterator();

            while(var12.hasNext()) {
               point = (Trails.Point)var12.next();
               color = Arraylist.getColor(this.points.indexOf(point));
               if (this.points.indexOf(point) < this.points.size() - 1) {
                  alpha = (float)(new Color(color.getRGB())).getAlpha() * ((float)this.points.indexOf(point) / (float)this.points.size());
                  temp = (Trails.Point)this.points.get(this.points.indexOf(point) + 1);
                  color2 = ColorUtility.setAlpha(ColorUtility.fade(5, this.points.indexOf(point) * 10, new Color(color.getRGB()), 1.0F).brighter().getRGB(), (int)alpha);
                  ColorUtility.setColor(color2);
                  GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
                  ++point.time;
               }
            }

            GL11.glEnd();
            GL11.glBegin(3);
            var12 = this.points.iterator();

            while(var12.hasNext()) {
               point = (Trails.Point)var12.next();
               color = Arraylist.getColor(this.points.indexOf(point));
               if (this.points.indexOf(point) < this.points.size() - 1) {
                  alpha = (float)(new Color(color.getRGB())).getAlpha() * ((float)this.points.indexOf(point) / (float)this.points.size());
                  temp = (Trails.Point)this.points.get(this.points.indexOf(point) + 1);
                  color2 = ColorUtility.setAlpha(ColorUtility.fade(5, this.points.indexOf(point) * 10, new Color(color.getRGB()), 1.0F).brighter().getRGB(), (int)alpha);
                  ColorUtility.setColor(color2);
                  GL11.glVertex3d(temp.pos.x, temp.pos.y + (double)mc.player.height - 0.1D, temp.pos.z);
                  ++point.time;
               }
            }

            GL11.glEnd();
            AntiAliasingUtility.unhook(true, false, false);
            GL11.glEnable(3008);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GL11.glEnable(2884);
            GlStateManager.resetColor();
            GL11.glDepthMask(true);
            GlStateManager.popMatrix();
         }
      }
   }

   public static class Point {
      public Vec3d pos;
      public long time;

      public Point(Vec3d pos) {
         this.pos = pos;
      }
   }
}

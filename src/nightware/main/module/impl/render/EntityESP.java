package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.jhlabs.vecmath.Vector4f;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.MultiBooleanSetting;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@ModuleAnnotation(
   name = "EntityESP",
   category = Category.RENDER
)
public class EntityESP extends Module {
   public final List<Entity> collectedEntities = new ArrayList();
   private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
   private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
   public MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Box", "Name", "Item", "Health"));

   @EventTarget
   public void onRender2D(EventRender2D event) {
      int color = NightWare.getInstance().getC(0).getRGB();
      int color2 = NightWare.getInstance().getC(500).getRGB();
      RenderManager renderMng = mc.getRenderManager();
      EntityRenderer entityRenderer = mc.entityRenderer;
      Iterator var6 = mc.world.playerEntities.iterator();

      while(true) {
         EntityPlayer entity;
         do {
            do {
               do {
                  if (!var6.hasNext()) {
                     return;
                  }

                  entity = (EntityPlayer)var6.next();
               } while(mc.player == entity && mc.gameSettings.thirdPersonView == 0);
            } while(entity.isDead);
         } while(!RenderUtility.isInViewFrustum((Entity)entity));

         GlStateManager.pushMatrix();
         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)event.getPartialTicks();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)event.getPartialTicks();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)event.getPartialTicks();
         double width = (double)entity.width / 1.5D;
         double height = (double)(entity.height + 0.2F - (entity.isSneaking() ? 0.2F : 0.0F));
         AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
         Vec3d[] vectors = new Vec3d[]{new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
         entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
         Vector4f position = null;
         Vec3d[] var21 = vectors;
         int var22 = vectors.length;

         for(int var23 = 0; var23 < var22; ++var23) {
            Vec3d vector = var21[var23];
            vector = RenderUtility.project2D(2, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
            if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
               if (position == null) {
                  position = new Vector4f((float)vector.x, (float)vector.y, (float)vector.z, 1.0F);
               }

               position.x = (float)Math.min(vector.x, (double)position.x);
               position.y = (float)Math.min(vector.y, (double)position.y);
               position.z = (float)Math.max(vector.x, (double)position.z);
               position.w = (float)Math.max(vector.y, (double)position.w);
            }
         }

         if (position != null) {
            mc.entityRenderer.setupOverlayRendering(2);
            double posX = (double)position.x;
            double posY = (double)position.y;
            double endPosX = (double)position.z;
            double endPosY = (double)position.w;
            if (this.elements.get(0)) {
               RenderUtility.drawRectNoWH(posX - 1.0D, posY, posX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtility.drawRectNoWH(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 0.5D + 0.5D, Color.black.getRGB());
               RenderUtility.drawRectNoWH(endPosX - 0.5D - 0.5D, posY, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtility.drawRectNoWH(posX - 1.0D, endPosY - 0.5D - 0.5D, endPosX + 0.5D, endPosY + 0.5D, Color.black.getRGB());
               RenderUtility.drawRectNoWH(posX - 0.5D, posY, posX + 0.5D - 0.5D, endPosY, -1);
               RenderUtility.drawRectNoWH(posX, endPosY - 0.5D, endPosX, endPosY, -1);
               RenderUtility.drawRectNoWH(posX - 0.5D, posY, endPosX, posY + 0.5D, -1);
               RenderUtility.drawRectNoWH(endPosX - 0.5D, posY, endPosX, endPosY, -1);
            }

            double hpPercentage = (double)(entity.getHealth() / entity.getMaxHealth());
            double hpHeight2 = (endPosY - posY) * hpPercentage;
            double hpHeight3 = endPosY - posY;
            double dif = (endPosX - posX) / 2.0D;
            double textWidth = (double) Fonts.nunito12.getStringWidth(entity.getName());
            if (this.elements.get(1)) {
               Fonts.nunito12.drawStringWithOutline(ChatFormatting.stripFormatting(entity.getName()), (double)((float)(posX + dif - textWidth / 2.0D) - 1.0F), (double)((float)posY - 20.0F + 15.0F), -1);
            }

            if (this.elements.get(3)) {
               RenderUtility.drawRectNoWH((double)((float)(posX - 3.5D)), (double)((float)(endPosY + 0.5D)), (double)((float)(posX - 1.5D)), (double)((float)(endPosY - hpHeight3 - 0.5D)), (new Color(0, 0, 0, 255)).getRGB());
               RenderUtility.drawVGradientRect((float)(posX - 3.0D), (float)endPosY, (float)(posX - 2.0D), (float)(endPosY - hpHeight3), color, color2);
               RenderUtility.drawRectNoWH(posX - 3.5D, posY, posX - 1.5D, endPosY - hpHeight2, (new Color(0, 0, 0, 255)).getRGB());
            }

            if (!entity.getHeldItemMainhand().isEmpty() && this.elements.get(2)) {
               Fonts.nunito12.drawCenteredStringWithOutline(ChatFormatting.stripFormatting(entity.getHeldItemMainhand().getDisplayName()), (double)((float)(posX + (endPosX - posX) / 2.0D)), (double)((float)(endPosY + 0.5D) + 4.0F), -1);
            }

            mc.entityRenderer.setupOverlayRendering();
         }

         GlStateManager.popMatrix();
      }
   }

   private boolean isValid(Entity entity) {
      if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) {
         return false;
      } else {
         return !entity.isDead;
      }
   }

   private void collectEntities() {
      this.collectedEntities.clear();
      Iterator var1 = mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer entity = (EntityPlayer)var1.next();
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }

   private Vec3d project2D(int scaleFactor, double x, double y, double z) {
      GL11.glGetFloat(2982, this.modelview);
      GL11.glGetFloat(2983, this.projection);
      GL11.glGetInteger(2978, this.viewport);
      return GLU.gluProject((float)x, (float)y, (float)z, this.modelview, this.projection, this.viewport, this.vector) ? new Vec3d((double)(this.vector.get(0) / (float)scaleFactor), (double)(((float)Display.getHeight() - this.vector.get(1)) / (float)scaleFactor), (double)this.vector.get(2)) : null;
   }

   public static int getHealthColor(EntityLivingBase entity, int c1, int c2) {
      float health = entity.getHealth();
      float maxHealth = entity.getMaxHealth();
      float hpPercentage = health / maxHealth;
      int red = (int)((float)(c2 >> 16 & 255) * hpPercentage + (float)(c1 >> 16 & 255) * (1.0F - hpPercentage));
      int green = (int)((float)(c2 >> 8 & 255) * hpPercentage + (float)(c1 >> 8 & 255) * (1.0F - hpPercentage));
      int blue = (int)((float)(c2 & 255) * hpPercentage + (float)(c1 & 255) * (1.0F - hpPercentage));
      return (new Color(red, green, blue)).getRGB();
   }
}

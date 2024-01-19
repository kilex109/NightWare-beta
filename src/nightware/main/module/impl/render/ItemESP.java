package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.jhlabs.vecmath.Vector4f;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
   name = "ItemESP",
   category = Category.RENDER
)
public class ItemESP extends Module {
   @EventTarget
   public void onDisplay(EventRender2D eventRender2D) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(true) {
         Entity entity;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               entity = (Entity)var2.next();
            } while(!(entity instanceof EntityItem));
         } while(!RenderUtility.isInViewFrustum(entity));

         double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)mc.getRenderPartialTicks();
         double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)mc.getRenderPartialTicks();
         double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)mc.getRenderPartialTicks();
         double width = (double)entity.width / 1.5D;
         double height = (double)(entity.height + 0.2F);
         AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
         Vec3d[] vectors = new Vec3d[]{new Vec3d(aabb.minX, aabb.minY, aabb.minZ), new Vec3d(aabb.minX, aabb.maxY, aabb.minZ), new Vec3d(aabb.maxX, aabb.minY, aabb.minZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vec3d(aabb.minX, aabb.minY, aabb.maxZ), new Vec3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
         mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);
         ScaledResolution sr = eventRender2D.getResolution();
         Vector4f position = null;
         Vec3d[] var18 = vectors;
         int var19 = vectors.length;

         for(int var20 = 0; var20 < var19; ++var20) {
            Vec3d vector = var18[var20];
            vector = RenderUtility.project2D(2, vector.x - mc.getRenderManager().renderPosX, vector.y - mc.getRenderManager().renderPosY, vector.z - mc.getRenderManager().renderPosZ);
            if (vector != null && vector.z > 0.0D && vector.z < 1.0D) {
               if (position == null) {
                  position = new Vector4f((float)vector.x, (float)vector.y, (float)vector.z, 0.0F);
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
            int build = -1;
            String tag = ((EntityItem)entity).getEntityItem().getDisplayName() + (((EntityItem)entity).getEntityItem().stackSize < 1 ? "" : " x" + ((EntityItem)entity).getEntityItem().stackSize);
            Fonts.nunito12.drawStringWithOutline(tag, (double)((float)(posX + (endPosX - posX) / 2.0D - (double)(Fonts.nunito12.getStringWidth(tag) / 2))), (double)((float)(posY - 10.0D)), build);
         }

         GL11.glEnable(2929);
         GlStateManager.enableBlend();
         mc.entityRenderer.setupOverlayRendering();
      }
   }
}

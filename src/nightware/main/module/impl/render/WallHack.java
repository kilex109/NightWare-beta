package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;

import java.util.ArrayList;
import java.util.List;

@ModuleAnnotation(
   name = "WallHack",
   category = Category.RENDER
)
public class WallHack extends Module {
   public static BooleanSetting glow = new BooleanSetting("Свечение", false);
   public static BooleanSetting wallHack = new BooleanSetting("Просвет", true);
   public static List<Entity> entities = new ArrayList<>();

   void render(Entity entity, float ticks) {
      try {
         if (entity == null || entity == mc.player) {
            return;
         }
         if (entity == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
            return;
         }
         mc.entityRenderer.disableLightmap();
         mc.getRenderManager().renderEntityStatic(entity, ticks, false);
         mc.entityRenderer.enableLightmap();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }


   @EventTarget
   public void onUpdate(EventUpdate event) {
      for (Entity entity : mc.world.loadedEntityList) {
         if (entity instanceof EntityArmorStand) {
            entities.add(entity);
         }
      }

      for (Entity entity : mc.world.loadedEntityList) {
         if (glow.get() && !entity.isGlowing()) {
            entity.setGlowing(true);
         } else if (!glow.get() && entity.isGlowing()) {
            entity.setGlowing(false);
         }
      }
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      GlStateManager.clear(256);
      RenderHelper.enableStandardItemLighting();
      if (wallHack.get()) {
         for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityArmorStand && entity != mc.getRenderViewEntity()) {
               this.render(entity, mc.getRenderPartialTicks());
            }
         }
      }
   }
}

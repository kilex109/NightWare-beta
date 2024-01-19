package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.RenderUtility;
import java.awt.Color;
import net.minecraft.util.math.RayTraceResult;

@ModuleAnnotation(
   name = "Crosshair",
   category = Category.RENDER
)
public class Crosshair extends Module {
   public BooleanSetting dot = new BooleanSetting("Dot", false);
   public BooleanSetting cooldown = new BooleanSetting("Cooldown Indicator", false);
   public BooleanSetting highlightEntities = new BooleanSetting("Highlight Entities", true);
   public BooleanSetting tShaped = new BooleanSetting("T-shaped", false);
   public NumberSetting radius = new NumberSetting("Radius", 3.0F, 3.0F, 6.0F, 0.5F);

   @EventTarget
   public void onRender(EventRender2D event) {
      if (mc.player != null) {
         if (mc.gameSettings.thirdPersonView == 0) {
            int color = this.highlightEntities.get() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY ? Color.RED.getRGB() : Color.WHITE.getRGB();
            float scaledWidth = (float)event.getResolution().getScaledWidth() / 2.0F;
            float scaledHeight = (float)event.getResolution().getScaledHeight() / 2.0F;
            float offset = this.radius.get() + (this.cooldown.get() ? (1.0F - mc.player.getCooledAttackStrength(0.5F)) * 10.0F : 0.0F);
            if (this.dot.get()) {
               RenderUtility.drawRectWithOutline(scaledWidth, scaledHeight, 1.0F, 1.0F, color, -16777216);
            }

            RenderUtility.drawRectWithOutline(scaledWidth - (offset + 2.0F), scaledHeight, 3.0F, 1.0F, color, -16777216);
            if (!this.tShaped.get()) {
               RenderUtility.drawRectWithOutline(scaledWidth, scaledHeight - (offset + 2.0F), 1.0F, 3.0F, color, -16777216);
            }

            RenderUtility.drawRectWithOutline(scaledWidth + offset, scaledHeight, 3.0F, 1.0F, color, -16777216);
            RenderUtility.drawRectWithOutline(scaledWidth, scaledHeight + offset, 1.0F, 3.0F, color, -16777216);
         }
      }
   }
}

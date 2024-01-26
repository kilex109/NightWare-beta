package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import java.awt.Color;
import net.minecraft.util.math.RayTraceResult;

@ModuleAnnotation(
   name = "Crosshair",
   category = Category.RENDER
)
public class Crosshair extends Module {
   public BooleanSetting highlightEntities = new BooleanSetting("Подсвечивать игроков", true);
   public NumberSetting radius = new NumberSetting("Радиус", 3.0F, 3.0F, 6.0F, 0.5F);

   @EventTarget
   public void onRender(EventRender2D event) {
      if (mc.player != null) {
         if (mc.gameSettings.thirdPersonView == 0) {
            Color col = NightWare.getInstance().getC(0);
            Color entcol = ColorUtility.TwoColorEffect(new Color(255, 0, 0), new Color(175, 55, 55), 5, 0);
            float scaledWidth = event.getResolution().getScaledWidth();
            float scaledHeight = event.getResolution().getScaledHeight();
            int color = this.highlightEntities.get() && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY ? entcol.getRGB() : col.getRGB();
            RenderUtility.drawRoundCircle(scaledWidth / 2, scaledHeight / 2, radius.get(), radius.get(), color);
            RenderUtility.drawFixedGlow(scaledWidth / 2 - (radius.get() / 2), scaledHeight / 2 - (radius.get() / 2), radius.get(), radius.get(), 10, color);
         }
      }
   }
}

package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventOverlay;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "NoHurtCam",
   category = Category.RENDER
)
public class NoHurtCam extends Module {

   @EventTarget
   public void onOverlayRender(EventOverlay event) {
      if (event.getOverlayType().equals(EventOverlay.OverlayType.HURT_CAM)) {
         event.setCancelled(true);
      }
   }
}

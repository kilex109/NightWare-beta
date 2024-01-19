package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventOverlay;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "CameraClip",
   category = Category.RENDER
)
public class CameraClip extends Module {
   @EventTarget
   public void onOverlay(EventOverlay event) {
      if (event.getOverlayType().equals(EventOverlay.OverlayType.CAMERA_CLIP)) {
         event.setCancelled(true);
      }

   }
}

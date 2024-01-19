package nightware.main.event.render;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventOverlay extends EventCancellable {
   private final EventOverlay.OverlayType overlayType;

   public EventOverlay.OverlayType getOverlayType() {
      return this.overlayType;
   }

   public EventOverlay(EventOverlay.OverlayType overlayType) {
      this.overlayType = overlayType;
   }

   public static enum OverlayType {
      TOTEM_ANIMATION,
      FIRE,
      BOSS_BAR,
      FOG,
      SCOREBOARD,
      HURT_CAM,
      WEATHER,
      ARMOR,
      PARTICLES,
      SKYLIGHT,
      CAMERA_CLIP;
   }
}

package nightware.main.event.render;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRender2D implements Event {
   private ScaledResolution resolution;
   private float partialTicks;

   public ScaledResolution getResolution() {
      return this.resolution;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public EventRender2D(ScaledResolution resolution, float partialTicks) {
      this.resolution = resolution;
      this.partialTicks = partialTicks;
   }
}

package nightware.main.event.render;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class EventRenderChunk implements Event {
   private RenderChunk renderChunk;

   public RenderChunk getRenderChunk() {
      return this.renderChunk;
   }

   public EventRenderChunk(RenderChunk renderChunk) {
      this.renderChunk = renderChunk;
   }
}

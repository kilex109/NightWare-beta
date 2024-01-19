package nightware.main.event.render;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.renderer.chunk.RenderChunk;

public class EventRenderChunkContainer implements Event {
   private RenderChunk renderChunk;

   public RenderChunk getRenderChunk() {
      return this.renderChunk;
   }

   public EventRenderChunkContainer(RenderChunk renderChunk) {
      this.renderChunk = renderChunk;
   }
}

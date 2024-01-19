package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.render.EventRenderChunk;
import nightware.main.event.render.EventRenderChunkContainer;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;

@ModuleAnnotation(
   name = "ChunkAnimator",
   category = Category.RENDER
)
public class ChunkAnimator extends Module {
   private final WeakHashMap<RenderChunk, AtomicLong> renderChunkMap = new WeakHashMap();

   private double easeOutCubic(double t) {
      return --t * t * t + 1.0D;
   }

   @EventTarget
   private void onRenderChunk(EventRenderChunk event) {
      if (mc.player != null && mc.world != null && !this.renderChunkMap.containsKey(event.getRenderChunk())) {
         this.renderChunkMap.put(event.getRenderChunk(), new AtomicLong(-1L));
      }

   }

   @EventTarget
   private void onRenderChunkContainer(EventRenderChunkContainer event) {
      if (this.renderChunkMap.containsKey(event.getRenderChunk())) {
         AtomicLong timeAlive = (AtomicLong)this.renderChunkMap.get(event.getRenderChunk());
         long timeClone = timeAlive.get();
         if (timeClone == -1L) {
            timeClone = System.currentTimeMillis();
            timeAlive.set(timeClone);
         }

         long timeDifference = System.currentTimeMillis() - timeClone;
         if ((float)timeDifference <= 600.0F) {
            double chunkY = (double)event.getRenderChunk().getPosition().getY();
            double offsetY = chunkY * this.easeOutCubic((double)((float)timeDifference / 600.0F));
            GlStateManager.translate(0.0D, -chunkY + offsetY, 0.0D);
         }
      }

   }
}

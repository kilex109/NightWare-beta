package nightware.main.event.misc;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.math.BlockPos;

public class EventObsidianPlaced implements Event {
   private final BlockObsidian block;
   private final BlockPos pos;

   public BlockObsidian getBlock() {
      return this.block;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public EventObsidianPlaced(BlockObsidian block, BlockPos pos) {
      this.block = block;
      this.pos = pos;
   }
}

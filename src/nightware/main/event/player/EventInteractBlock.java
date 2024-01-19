package nightware.main.event.player;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class EventInteractBlock extends EventCancellable {
   private BlockPos pos;
   private EnumFacing face;

   public BlockPos getPos() {
      return this.pos;
   }

   public EnumFacing getFace() {
      return this.face;
   }

   public void setPos(BlockPos pos) {
      this.pos = pos;
   }

   public void setFace(EnumFacing face) {
      this.face = face;
   }

   public EventInteractBlock(BlockPos pos, EnumFacing face) {
      this.pos = pos;
      this.face = face;
   }
}

package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.render.RenderUtility;

import java.util.ArrayList;

@ModuleAnnotation(name = "OreESP", category = Category.RENDER)
public class OreESP extends Module {
    public static ArrayList<BlockPos> xRayBlocks = new ArrayList<>();

    @EventTarget
    public void onRender3D(EventRender3D event) {
        for (BlockPos blockPos : xRayBlocks) {
            int b = this.mc.world.getBlockState((BlockPos)blockPos).getMapColor((IBlockAccess)this.mc.world, (BlockPos)blockPos).colorIndex;
            if (b == 38 || b == 46 || b == 42) {
                RenderUtility.blockEspFrame(blockPos, 0.8901960849761963, 0.8666666746139526, 0.22745098173618317);
                continue;
            }
            if (b == 36 || b == 44 || b == 40) {
                RenderUtility.blockEspFrame(blockPos, 0.0, 0.0, 0.0);
                continue;
            }
            if (b != 45 && b != 41 && b != 37) {
                RenderUtility.blockEspFrame(blockPos, 0.615686297416687, 0.6117647290229797, 0.5647059082984924);
                continue;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
            // empty if block
        }
        for (int i = 0; i < xRayBlocks.size(); ++i) {
            int b = this.mc.world.getBlockState((BlockPos)OreESP.xRayBlocks.get((int)i)).getMapColor((IBlockAccess)this.mc.world, (BlockPos)OreESP.xRayBlocks.get((int)i)).colorIndex;
            if (this.mc.world.getBlockState(xRayBlocks.get(i)).getBlock() == Blocks.STAINED_HARDENED_CLAY && b != 0) continue;
        }
    }
}

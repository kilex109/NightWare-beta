package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import nightware.main.NightWare;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventRender3D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.Utils;
import nightware.main.utility.misc.TimerHelper;
import nightware.main.utility.render.RenderUtility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

@ModuleAnnotation(
   name = "AutoFarm",
   category = Category.PLAYER
)
public class AutoFarm extends Module {
   TimerHelper timer;
   BlockPos blockPos;

   public AutoFarm() {
      timer = new TimerHelper();
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      RenderUtility.blockEspFrame(blockPos, NightWare.getInstance().getC(0).getRed() / 255f, NightWare.getInstance().getC(0).getGreen() / 255f, NightWare.getInstance().getC(0).getBlue() / 255f);
   }


   @EventTarget
   public void onUpdate(EventUpdate event) {
      for (Entity entity : mc.world.loadedEntityList) {
         if (entity.getDistanceToEntity(mc.player) <= 4 && entity instanceof EntityArmorStand) {
//            System.out.println(entity.getCustomNameTag());
            ArrayList<BlockPos> blockPoss = new ArrayList<>();
            int i = (int) entity.posX;
            int j = (int) entity.posZ;
            for (int x = -2; x < 2; x++) {
               for (int z = -2; z < 2; z++) {
                  Block block = mc.world.getBlockState(new BlockPos(i + x, entity.posY, j + z)).getBlock();
                  if (block != Blocks.AIR && (block == Blocks.STAINED_HARDENED_CLAY || block == Blocks.LOG || block == Blocks.LOG2)) {
                     blockPoss.add(new BlockPos(i + x, entity.posY, j + z));
                  }
               }
            }
            blockPoss.sort(new Comparator<BlockPos>() {
               @Override
               public int compare(BlockPos o1, BlockPos o2) {
                  return (int) ((entity.getDistanceSq(o1) - entity.getDistanceSq(o2)));
               }
            });
            blockPos = blockPoss.get(0);


            if (blockPos != null) {
               if (mc.player.getCooldownTracker().getCooldown(mc.player.getHeldItemMainhand().getItem(), mc.getRenderPartialTicks()) == 0) {
                  mc.player.swingArm(EnumHand.MAIN_HAND);
                  EntityArmorStand entityArmorStand = (EntityArmorStand) entity;
                  mc.getConnection().sendPacket(new CPacketUseEntity(entity, EnumHand.MAIN_HAND));
                  mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                  timer.reset();
               }

               float[] rotation = Utils.getNeededRotations(entity.posX, entity.posY + 0.37f, entity.posZ, (float) mc.player.posX, (float) mc.player.posY + mc.player.getEyeHeight(), (float) mc.player.posZ);
               event.setRotationYaw(rotation[0]);
               event.setRotationPitch(rotation[1]);
            }
         }
      }
   }
}

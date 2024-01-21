package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.concurrent.CompletableFuture;

@ModuleAnnotation(
   name = "AutoRespawn",
   category = Category.PLAYER
)
public class AutoRespawn extends Module {
   @EventTarget
   public void onUpdateEvent(EventReceivePacket event) {
      if (event.getPacket() instanceof SPacketOpenWindow) {
         SPacketOpenWindow openWindow = (SPacketOpenWindow) event.getPacket();
         String windowName = openWindow.getWindowTitle().getUnformattedText();

         CompletableFuture.runAsync(() -> {
            try {
               if (windowName.contains("Вы мертвы") || windowName.contains("Dead")) {
                  Thread.sleep(500L);
                  mc.playerController.windowClick(mc.player.openContainer.windowId, 29, 0, ClickType.PICKUP, mc.player);
                  Thread.sleep(250L);
                  mc.playerController.clickBlock(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ), EnumFacing.UP);
               }
            } catch (InterruptedException e) {
               throw new RuntimeException(e);
            }
         });
      }
   }
}

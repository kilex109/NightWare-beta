package nightware.main.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.manager.notification.NotificationManager;
import nightware.main.manager.notification.NotificationType;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.misc.ChatUtility;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.TextFormatting;

@ModuleAnnotation(
   name = "DeathCoordinates",
   category = Category.UTIL
)
public class DeathCoordinates extends Module {

   @EventTarget
   public void onPiski(EventReceivePacket event) {
      if (event.getPacket() instanceof SPacketOpenWindow) {
         SPacketOpenWindow openWindow = (SPacketOpenWindow) event.getPacket();
         String windowName = openWindow.getWindowTitle().getUnformattedText();

         if (windowName.contains("Вы мертвы") || windowName.contains("Dead")) {
            String sdohSoobshenie = mc.player.getPosition().getX() + ", " + mc.player.getPosition().getY() + ", " + mc.player.getPosition().getZ();
            ChatUtility.addChatMessage(sdohSoobshenie);
         }
      }
   }
}

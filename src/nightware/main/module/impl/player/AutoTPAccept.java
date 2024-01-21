package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.NightWare;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.manager.friend.Friend;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.TimerUtils;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.MultiBooleanSetting;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.server.SPacketChat;
import nightware.main.utility.misc.ChatUtility;

@ModuleAnnotation(
   name = "AutoAccept",
   category = Category.PLAYER
)
public class AutoTPAccept extends Module {
   private final MultiBooleanSetting che = new MultiBooleanSetting("Принимать", Arrays.asList("Запрос на телепортацию", "Запрос в друзья", "Запрос на трейд"));
   public static BooleanSetting autoAccept = new BooleanSetting("Подтверждать трейд", true);
   public AutoTPAccept() {
      autoAccept.setVisible(() -> che.get(2));
   }

   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketChat) {
         SPacketChat packet = (SPacketChat) eventPacket.getPacket();
         String m = packet.getChatComponent().getUnformattedText();
         if (m.contains("/tpa") && che.get(0)) {
            ChatUtility.sendChatMessage("/tpa");
         } else if (m.contains("/friends") && che.get(0)) {
            ChatUtility.sendChatMessage("/friends accept");
         } else if (m.contains("/trade") && che.get(0)) {
            ChatUtility.sendChatMessage("/trade accept");
            if (autoAccept.get()) {
               CompletableFuture.runAsync(() -> {
                  try {
                     Thread.sleep(500L);
                  } catch (InterruptedException e) {
                     throw new RuntimeException(e);
                  }
                  mc.playerController.windowClick(mc.player.openContainer.windowId, 49, 0, ClickType.PICKUP, mc.player);
               });
            }
         }
      }
   }
}

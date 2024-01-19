package nightware.main.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;

@ModuleAnnotation(
   name = "BetterChat",
   category = Category.UTIL
)
public class BetterChat extends Module {
   public static BooleanSetting antiSpam = new BooleanSetting("Anti-Spam", false);
   public static BooleanSetting chatHistory = new BooleanSetting("Save History", false);
   private String lastMessage = "";
   private int amount;
   private int line;

   @EventTarget
   public void onReceivePacket(EventReceivePacket event) {
      if (antiSpam.get()) {
         if (event.getPacket() instanceof SPacketChat && ((SPacketChat)event.getPacket()).func_192590_c().equals(ChatType.CHAT)) {
            SPacketChat chatPacket = (SPacketChat)event.getPacket();
            ITextComponent message = chatPacket.getChatComponent();
            String rawMessage = message.getFormattedText();
            GuiNewChat chatGui = mc.ingameGUI.getChatGUI();
            if (this.lastMessage.equals(rawMessage)) {
               chatGui.deleteChatLine(this.line);
               ++this.amount;
               chatPacket.getChatComponent().appendText(ChatFormatting.GRAY + " [x" + this.amount + "]");
            } else {
               this.amount = 1;
            }

            ++this.line;
            this.lastMessage = rawMessage;
            chatGui.printChatMessageWithOptionalDeletion(message, this.line);
            if (this.line > 256) {
               this.line = 0;
            }

            event.setCancelled(true);
         }

      }
   }
}

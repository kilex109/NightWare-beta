package nightware.main.command;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.event.misc.EventMessage;
import nightware.main.utility.misc.ChatUtility;

public class CommandHandler {
   public CommandManager commandManager;

   public CommandHandler(CommandManager commandManager) {
      this.commandManager = commandManager;
   }

   @EventTarget
   public void onMessage(EventMessage event) {
      String msg = event.getMessage();
      if (msg.startsWith(CommandManager.getPrefix())) {
         event.setCancelled(true);
         if (!this.commandManager.execute(msg)) {
            ChatUtility.addChatMessage(ChatFormatting.GRAY + "����� ������� �� ����������.");
         }
      }

   }
}

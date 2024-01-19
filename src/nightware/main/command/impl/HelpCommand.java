package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;

import java.util.Iterator;

@Command(
   name = "help",
   description = "help"
)
public class HelpCommand extends CommandAbstract {
   public void execute(String[] args) throws Exception {
      this.sendMessage(ChatFormatting.GRAY + "Список команд: ");
      Iterator var2 = NightWare.getInstance().getCommandManager().getCommands().iterator();

      while(var2.hasNext()) {
         CommandAbstract command = (CommandAbstract)var2.next();
         if (!(command instanceof HelpCommand)) {
            this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + command.name + ChatFormatting.GOLD + " - " + ChatFormatting.RESET + command.description);
         }
      }

   }

   public void error() {
      this.sendMessage(TextFormatting.GOLD + "Error");
   }
}

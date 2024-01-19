package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;

@Command(
   name = "prefix",
   description = "Изменяет префикс для команд"
)
public class PrefixCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "Ошибка в использовании" + ChatFormatting.WHITE + ":");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "prefix " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "symbol" + ChatFormatting.GRAY + ">");
   }

   public void execute(String[] args) throws Exception {
      CommandManager.setPrefix(args[1]);
      this.sendMessage(ChatFormatting.GRAY + "Префикс успешно изменен на " + TextFormatting.GOLD + args[1]);
   }
}

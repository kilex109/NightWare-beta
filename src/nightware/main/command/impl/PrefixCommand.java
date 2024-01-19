package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;

@Command(
   name = "prefix",
   description = "�������� ������� ��� ������"
)
public class PrefixCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "prefix " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "symbol" + ChatFormatting.GRAY + ">");
   }

   public void execute(String[] args) throws Exception {
      CommandManager.setPrefix(args[1]);
      this.sendMessage(ChatFormatting.GRAY + "������� ������� ������� �� " + TextFormatting.GOLD + args[1]);
   }
}

package nightware.main.command.impl;

import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;

@Command(
   name = "reload",
   description = "��������� ������������� ������, �������� ����."
)
public class ReloadCommand extends CommandAbstract {
   public void execute(String[] args) throws Exception {
      NightWare.getInstance().reload();
   }

   public void error() {
      this.sendMessage(TextFormatting.GOLD + "Error");
   }
}

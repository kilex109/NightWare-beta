package nightware.main.command.impl.report;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;

import java.util.Iterator;

@Command(
   name = "report",
   description = "Отправляет репорт прямо к администрации клиента"
)
public class ReportCommand extends CommandAbstract {
   public void execute(String[] args) throws Exception {

   }

   public void error() {
      this.sendMessage(TextFormatting.GOLD + "Error");
   }
}

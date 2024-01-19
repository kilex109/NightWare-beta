package nightware.main.command.impl;

import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.impl.util.NameProtect;
import nightware.main.utility.misc.ChatUtility;

import javax.naming.Name;

import static nightware.main.module.Utils.mc;

@Command(
   name = "np",
   description = "Изменяет ник в NameProtect"
)
public class NameProtectCommand extends CommandAbstract {

   public void execute(String[] args) throws Exception {
      if (args[1] != null) {
         NameProtect.protectedNick = args[1];
      }
   }

   public void error() {
      this.sendMessage(TextFormatting.GOLD + "Error");
   }
}

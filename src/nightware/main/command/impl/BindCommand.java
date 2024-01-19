package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.module.Module;
import nightware.main.module.impl.render.ClickGuiModule;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;

@Command(
   name = "bind",
   description = "��������� ������� ������"
)
public class BindCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
      this.sendMessage(ChatFormatting.WHITE + ".bind " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> <" + TextFormatting.GOLD + "key" + ChatFormatting.GRAY + "> - ��������� ������");
      this.sendMessage(ChatFormatting.WHITE + ".bind " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> " + ChatFormatting.WHITE + "none" + ChatFormatting.GRAY + " - ���������� ������");
      this.sendMessage(ChatFormatting.WHITE + ".bind list" + ChatFormatting.GRAY + " - ������ ���� ������");
      this.sendMessage(ChatFormatting.WHITE + ".bind clear" + ChatFormatting.GRAY + " - �������� ��� �����");
   }

   public void execute(String[] args) throws Exception {
      if (args.length >= 2) {
         Iterator var4;
         Module module;
         if (args[1].equals("clear")) {
            var4 = NightWare.getInstance().getModuleManager().getModules().iterator();

            while(var4.hasNext()) {
               module = (Module)var4.next();
               if (!(module instanceof ClickGuiModule) && module.getBind() != 0) {
                  module.bind = 0;
               }
            }

            this.sendMessage(ChatFormatting.GRAY + "��� ����� ���� �������!");
            return;
         }

         if (args[1].equals("list")) {
            this.sendMessage(ChatFormatting.GREEN + "������ ������: ");
            var4 = NightWare.getInstance().getModuleManager().getModules().iterator();

            while(var4.hasNext()) {
               module = (Module)var4.next();
               if (module.getBind() != 0) {
                  if (module.getBind() < 0) {
                     this.sendMessage(ChatFormatting.WHITE + "������: " + TextFormatting.GOLD + module.getName() + ChatFormatting.WHITE + ", ������: " + TextFormatting.GOLD + (module.getBind() + 100));
                  } else {
                     this.sendMessage(ChatFormatting.WHITE + "������: " + TextFormatting.GOLD + module.getName() + ChatFormatting.WHITE + ", �������: " + TextFormatting.GOLD + Keyboard.getKeyName(module.getBind()));
                  }
               }
            }

            return;
         }

         module = NightWare.getInstance().getModuleManager().getModule(args[1]);
         if (module == null) {
            this.sendMessage(ChatFormatting.GRAY + "������ " + TextFormatting.GOLD + args[1] + ChatFormatting.GRAY + " �� ����������!");
         } else if (args[2].equalsIgnoreCase("none")) {
            module.bind = 0;
            this.sendMessage(ChatFormatting.GRAY + "������ " + TextFormatting.GOLD + args[1] + ChatFormatting.GRAY + " ��� ���������!");
         } else {
            int keyBind = Keyboard.getKeyIndex(args[2].toUpperCase());
            if (keyBind == 0) {
               this.sendMessage(ChatFormatting.GRAY + "����� ������� �� ����������!");
            } else {
               module.bind = keyBind;
               this.sendMessage(ChatFormatting.GRAY + "������ " + TextFormatting.GOLD + args[1] + ChatFormatting.GRAY + " ��� �������� �� ������� " + TextFormatting.GOLD + args[2].toUpperCase());
            }
         }
      } else {
         this.error();
      }

   }
}

package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;
import nightware.main.manager.config.ConfigManager;
import java.io.File;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

@Command(
   name = "cfg",
   description = "��������� ��������� ��������� ����"
)
public class ConfigCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "������ � �������������" + ChatFormatting.WHITE + ":");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg load " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + ">");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg save " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + ">");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg delete " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + ">");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg list" + ChatFormatting.GRAY + " - ������ ��������");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "cfg dir" + ChatFormatting.GRAY + " - ������� ����� � ���������");
   }

   public void execute(String[] args) throws Exception {
      if (args.length >= 2) {
         String var2 = args[1];
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -1335458389:
            if (var2.equals("delete")) {
               var3 = 4;
            }
            break;
         case 99469:
            if (var2.equals("dir")) {
               var3 = 0;
            }
            break;
         case 3322014:
            if (var2.equals("list")) {
               var3 = 1;
            }
            break;
         case 3327206:
            if (var2.equals("load")) {
               var3 = 2;
            }
            break;
         case 3522941:
            if (var2.equals("save")) {
               var3 = 3;
            }
         }

         switch(var3) {
         case 0:
            OpenGlHelper.openFile(new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\configs"));
            break;
         case 1:
            File file = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\configs");
            if (ConfigManager.getLoadedConfigs().isEmpty()) {
               this.sendMessage("������������ �� �������.");
               break;
            } else {
               this.sendMessage(ChatFormatting.GREEN + "������ ������������: ");
               File[] var5 = (File[])Objects.requireNonNull(file.listFiles());
               int var6 = var5.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  File s = var5[var7];
                  this.sendMessage(s.getName().replaceAll(".nw", ""));
               }

               return;
            }
         case 2:
            if (NightWare.getInstance().getConfigManager().loadConfig(args[2])) {
               this.sendMessage(ChatFormatting.GRAY + "������������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ���� ���������.");
            } else {
               this.sendMessage(ChatFormatting.GRAY + "������������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " �� ���� ���������. ������ �����, ��� �� ����������.");
            }
            break;
         case 3:
            NightWare.getInstance().getConfigManager().saveConfig(args[2]);
            this.sendMessage(ChatFormatting.GRAY + "������������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ���� ���������.");
            break;
         case 4:
            if (NightWare.getInstance().getConfigManager().deleteConfig(args[2])) {
               this.sendMessage(ChatFormatting.GRAY + "������������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ���� �������.");
            } else {
               this.sendMessage(ChatFormatting.GRAY + "������������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " �� ���� �������. ������ �����, ��� �� ����������.");
            }
         }
      } else {
         this.error();
      }

   }
}

package nightware.main.command.impl;

import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;
import nightware.main.module.impl.render.StaffList;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

@Command(
   name = "staff",
   description = "��������� �������� ������� � StaffList"
)
public class StaffCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(TextFormatting.GRAY + "������ � �������������:");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff add " + TextFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + TextFormatting.GRAY + "> - �������� ������ � StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff remove " + TextFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + TextFormatting.GRAY + "> - ������� ������ �� StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff list" + TextFormatting.GRAY + " - ���������� StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff clear" + TextFormatting.GRAY + " - �������� StaffList");
   }

   public void execute(String[] args) throws Exception {
      if (args.length > 1) {
         String var2 = args[1];
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -934610812:
            if (var2.equals("remove")) {
               var3 = 1;
            }
            break;
         case 96417:
            if (var2.equals("add")) {
               var3 = 0;
            }
            break;
         case 3322014:
            if (var2.equals("list")) {
               var3 = 3;
            }
            break;
         case 94746189:
            if (var2.equals("clear")) {
               var3 = 2;
            }
         }

         switch(var3) {
         case 0:
            if (args[2].equalsIgnoreCase(Minecraft.getMinecraft().getSession().getUsername())) {
               this.sendMessage(TextFormatting.GRAY + "������ �������� ������ ���� � StaffList");
            } else if (NightWare.getInstance().getStaffManager().getStaff().contains(args[2])) {
               this.sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + TextFormatting.GRAY + " ��� ���� � StaffList.");
            } else {
               NightWare.getInstance().getStaffManager().addStaff(args[2]);
               this.sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + TextFormatting.GRAY + " ������� �������� � StaffList!");
               StaffList.updateList();
            }
            break;
         case 1:
            if (NightWare.getInstance().getStaffManager().isStaff(args[2])) {
               NightWare.getInstance().getStaffManager().removeStaff(args[2]);
               this.sendMessage(TextFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + TextFormatting.GRAY + " ������� ������ �� StaffList.");
               StaffList.updateList();
            } else {
               this.sendMessage(TextFormatting.GRAY + "������ " + TextFormatting.GOLD + args[2] + TextFormatting.GRAY + " ��� � StaffList.");
            }
            break;
         case 2:
            if (NightWare.getInstance().getStaffManager().getStaff().isEmpty()) {
               this.sendMessage(TextFormatting.GRAY + "StaffList ����!");
            } else {
               NightWare.getInstance().getStaffManager().clearStaff();
               this.sendMessage(TextFormatting.GRAY + "StaffList ������!");
               StaffList.updateList();
            }
            break;
         case 3:
            if (NightWare.getInstance().getStaffManager().getStaff().isEmpty()) {
               this.sendMessage(TextFormatting.GRAY + "StaffList ����!");
               return;
            }

            this.sendMessage(TextFormatting.GRAY + "Staff: ");
            NightWare.getInstance().getStaffManager().getStaff().forEach(this::sendMessage);
            break;
         default:
            this.error();
         }
      } else {
         this.error();
      }

   }
}

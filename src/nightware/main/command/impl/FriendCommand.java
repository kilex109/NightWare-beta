package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;
import net.minecraft.client.Minecraft;

@Command(
   name = "friend",
   description = "��������� ��������� ������� ������"
)
public class FriendCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "������ � �������������:");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend add " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> - �������� ������ � ������");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend remove " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> - ������� ������ �� ������");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend list" + ChatFormatting.GRAY + " - ���������� ������ ������");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend clear" + ChatFormatting.GRAY + " - �������� ������ ������");
   }

   public void execute(String[] args) throws Exception {
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
            this.sendMessage(ChatFormatting.GRAY + "������ �������� ������ ���� � ������ :)");
         } else if (NightWare.getInstance().getFriendManager().getFriends().contains(args[2])) {
            this.sendMessage(ChatFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ��� ���� � ������ ������.");
         } else {
            NightWare.getInstance().getFriendManager().addFriend(args[2]);
            this.sendMessage(ChatFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ������� �������� � ������!");
         }
         break;
      case 1:
         if (NightWare.getInstance().getFriendManager().isFriend(args[2])) {
            NightWare.getInstance().getFriendManager().removeFriend(args[2]);
            this.sendMessage(ChatFormatting.GRAY + "����� " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ������� ������ �� ������ ������.");
         } else {
            this.sendMessage(ChatFormatting.GRAY + "������ " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " ��� � ������ ������.");
         }
         break;
      case 2:
         if (NightWare.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(ChatFormatting.GRAY + "������ ������ ���� :(");
         } else {
            NightWare.getInstance().getFriendManager().clearFriend();
            this.sendMessage(ChatFormatting.GRAY + "������ ������ ������� ������!");
         }
         break;
      case 3:
         if (NightWare.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(ChatFormatting.GRAY + "������ ������ ���� :(");
            return;
         }

         this.sendMessage(ChatFormatting.GRAY + "������ ������: ");
         NightWare.getInstance().getFriendManager().getFriends().forEach((friend) -> {
            this.sendMessage(friend.getName());
         });
      }

   }
}

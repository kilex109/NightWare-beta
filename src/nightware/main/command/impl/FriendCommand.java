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
   description = "Позволяет управлять списком друзей"
)
public class FriendCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(ChatFormatting.GRAY + "Ошибка в использовании:");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend add " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> - добавить игрока в друзья");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend remove " + ChatFormatting.GRAY + "<" + TextFormatting.GOLD + "name" + ChatFormatting.GRAY + "> - удалить игрока из друзей");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend list" + ChatFormatting.GRAY + " - посмотреть список друзей");
      this.sendMessage(ChatFormatting.WHITE + CommandManager.getPrefix() + "friend clear" + ChatFormatting.GRAY + " - очистить список друзей");
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
            this.sendMessage(ChatFormatting.GRAY + "Нельзя добавить самого себя в друзья :)");
         } else if (NightWare.getInstance().getFriendManager().getFriends().contains(args[2])) {
            this.sendMessage(ChatFormatting.GRAY + "Игрок " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " уже есть в списке друзей.");
         } else {
            NightWare.getInstance().getFriendManager().addFriend(args[2]);
            this.sendMessage(ChatFormatting.GRAY + "Игрок " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " успешно добавлен в друзья!");
         }
         break;
      case 1:
         if (NightWare.getInstance().getFriendManager().isFriend(args[2])) {
            NightWare.getInstance().getFriendManager().removeFriend(args[2]);
            this.sendMessage(ChatFormatting.GRAY + "Игрок " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " успешно удален из списка друзей.");
         } else {
            this.sendMessage(ChatFormatting.GRAY + "Игрока " + TextFormatting.GOLD + args[2] + ChatFormatting.GRAY + " нет в списке друзей.");
         }
         break;
      case 2:
         if (NightWare.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(ChatFormatting.GRAY + "Список друзей пуст :(");
         } else {
            NightWare.getInstance().getFriendManager().clearFriend();
            this.sendMessage(ChatFormatting.GRAY + "Список друзей успешно очищен!");
         }
         break;
      case 3:
         if (NightWare.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(ChatFormatting.GRAY + "Список друзей пуст :(");
            return;
         }

         this.sendMessage(ChatFormatting.GRAY + "Список друзей: ");
         NightWare.getInstance().getFriendManager().getFriends().forEach((friend) -> {
            this.sendMessage(friend.getName());
         });
      }

   }
}

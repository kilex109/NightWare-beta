package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import nightware.main.NightWare;
import nightware.main.event.input.EventMouse;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.utility.misc.ChatUtility;
import nightware.main.utility.player.InventoryUtility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

@ModuleAnnotation(
   name = "MCF",
   category = Category.PLAYER
)
public class MiddleClick extends Module {

   @EventTarget
   public void onMouse(EventMouse event) {
      if (event.getButton() == 2 && mc.pointedEntity instanceof EntityLivingBase) {
         String name = mc.pointedEntity.getName();
         if (name.equals(mc.player.getName())) {
            ChatUtility.addChatMessage(ChatFormatting.GRAY + "Нельзя добавить самого себя в друзья :)");
            return;
         }
         if (NightWare.getInstance().getFriendManager().isFriend(name)) {
            NightWare.getInstance().getFriendManager().removeFriend(name);
            ChatUtility.addChatMessage(ChatFormatting.GRAY + "Игрок " + TextFormatting.GOLD +name + ChatFormatting.GRAY + " успешно удален из списка друзей.");
         } else {
            NightWare.getInstance().getFriendManager().addFriend(name);
            ChatUtility.addChatMessage(ChatFormatting.GRAY + "Игрок " + TextFormatting.GOLD + name + ChatFormatting.GRAY + " успешно добавлен в друзья!");
         }
      }
   }
}

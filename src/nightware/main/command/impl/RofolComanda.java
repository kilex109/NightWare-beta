package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.command.CommandManager;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.impl.combat.TimerUtils;
import nightware.main.utility.misc.ChatUtility;
import nightware.main.utility.misc.SoundUtility;

import java.util.Iterator;

import static nightware.main.module.Utils.mc;

@Command(
   name = "debug",
   description = "show debug info"
)
public class RofolComanda extends CommandAbstract {

   public void execute(String[] args) throws Exception {
      ChatUtility.addChatMessage("Айди предмета в руке - " + mc.player.getHeldItemMainhand().itemDamage);
      ChatUtility.addChatMessage("Находится ли лук в руке - " + AimBot.ishandcontainbow);
      if (AimBot.lastTarget != null) {
         ChatUtility.addChatMessage("Последний таргет - " + AimBot.lastTarget.getTarget().getName());
      }
      ChatUtility.addChatMessage("Юзернейм - " + NightWare.getInstance().getUserInfo().getName());
      ChatUtility.addChatMessage("Уникальный айди - " + NightWare.getInstance().getUserInfo().getUid());
      ChatUtility.addChatMessage("Роль - " + NightWare.getInstance().getUserInfo().getRole());
      ChatUtility.addChatMessage("Рекомендуемый предикт - " + (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) / 20f);
   }

   public void error() {
      this.sendMessage(TextFormatting.GOLD + "Error");
   }
}

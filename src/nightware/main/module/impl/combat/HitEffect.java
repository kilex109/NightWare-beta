package nightware.main.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.player.EventDamage;
import nightware.main.event.player.EventDamageEntityItem;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.ModeSetting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import nightware.main.utility.misc.SoundUtility;

@ModuleAnnotation(
   name = "HitEffect",
   category = Category.COMBAT
)
public class HitEffect extends Module {
   public ModeSetting effect = new ModeSetting("Эффект", "Молния", "Молния", "Взрыв крови", "Дым", "Динамит");

   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketTitle) {
         SPacketTitle title = (SPacketTitle) eventPacket.getPacket();
         String m = title.getMessage().getUnformattedText();
         StringBuilder builder = new StringBuilder();
         char[] buffer = m.toCharArray();
         for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == '§') {
               i++;
            } else {
               builder.append(buffer[i]);
            }
         }
         if (m.contains("HP)")) {
            EntityLivingBase entity = AimBot.lastTarget.getTarget();
            if (this.effect.get().equals("Молния")) {
               mc.world.addWeatherEffect(new EntityLightningBolt(mc.world, entity.posX, entity.posY, entity.posZ, true));
            } else if (this.effect.get().equals("Дым")) {
               mc.renderGlobal.playEvent(mc.player, 2000, new BlockPos(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), 152);
            } else if (this.effect.get().equals("Взрыв крови")) {
               mc.renderGlobal.playEvent(mc.player, 2001, new BlockPos(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), 152);
            } else if (this.effect.get().equals("Динамит")) {
               mc.renderGlobal.playEvent(mc.player, 3000, new BlockPos(entity.posX, entity.posY + (double) entity.getEyeHeight(), entity.posZ), 152);
            }
         }
      }
   }
}

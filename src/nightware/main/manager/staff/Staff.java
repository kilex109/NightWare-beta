package nightware.main.manager.staff;

import nightware.main.utility.Utility;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class Staff implements Utility {
   private String name;
   private String prefix;
   private boolean isVanished;

   public String getStaffPrefix() {
      EntityPlayer player = mc.world.getPlayerEntityByName(this.name);
      if (player != null) {
         return TextFormatting.YELLOW + "[N] " + TextFormatting.RESET;
      } else {
         NetworkPlayerInfo networkPlayerInfo = mc.player.connection.getPlayerInfo(this.name);
         if (this.isVanished) {
            return TextFormatting.RED + "[S] " + TextFormatting.RESET;
         } else {
            return networkPlayerInfo != null && networkPlayerInfo.getGameType().equals(GameType.SPECTATOR) ? TextFormatting.GOLD + "[GM3] " + TextFormatting.RESET : "";
         }
      }
   }

   public String getText() {
      return this.getStaffPrefix() + this.prefix + this.name;
   }

   public String getName() {
      return this.name;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public boolean isVanished() {
      return this.isVanished;
   }

   public Staff(String name, String prefix, boolean isVanished) {
      this.name = name;
      this.prefix = prefix;
      this.isVanished = isVanished;
   }
}

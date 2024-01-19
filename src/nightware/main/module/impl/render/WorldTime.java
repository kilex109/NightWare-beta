package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;
import net.minecraft.network.play.server.SPacketTimeUpdate;

@ModuleAnnotation(
   name = "WorldTime",
   category = Category.RENDER
)
public class WorldTime extends Module {
   private final ModeSetting mode = new ModeSetting("Mode", "Day", new String[]{"Day", "Night", "Sunrise", "Sunset", "Custom"});
   private final NumberSetting time = new NumberSetting("Time", 1000.0F, 0.0F, 24000.0F, 100.0F, () -> {
      return this.mode.get().equals("Custom");
   });

   @EventTarget
   public void onUpdate(EventUpdate event) {
      String var2 = this.mode.get();
      byte var3 = -1;
      switch(var2.hashCode()) {
      case -1807305034:
         if (var2.equals("Sunset")) {
            var3 = 3;
         }
         break;
      case -191907083:
         if (var2.equals("Sunrise")) {
            var3 = 2;
         }
         break;
      case 68476:
         if (var2.equals("Day")) {
            var3 = 0;
         }
         break;
      case 75265016:
         if (var2.equals("Night")) {
            var3 = 1;
         }
         break;
      case 2029746065:
         if (var2.equals("Custom")) {
            var3 = 4;
         }
      }

      switch(var3) {
      case 0:
         mc.world.setWorldTime(5000L);
         break;
      case 1:
         mc.world.setWorldTime(17000L);
         break;
      case 2:
         mc.world.setWorldTime(0L);
         break;
      case 3:
         mc.world.setWorldTime(13000L);
         break;
      case 4:
         mc.world.setWorldTime((long)this.time.get());
      }

   }

   @EventTarget
   public void onPacket(EventReceivePacket event) {
      if (event.getPacket() instanceof SPacketTimeUpdate) {
         event.setCancelled(true);
      }

   }
}

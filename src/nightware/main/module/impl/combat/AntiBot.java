package nightware.main.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.player.EventUpdate;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import net.minecraft.entity.player.EntityPlayer;

@ModuleAnnotation(
        name = "AntiBot",
        category = Category.COMBAT
)
public class AntiBot extends Module {

    @EventTarget
    public void onUpdate(EventUpdate event){
        for (int i = 0; i < mc.world.loadedEntityList.size(); i++) {
            if (mc.world.loadedEntityList.get(i) instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) mc.world.loadedEntityList.get(i);
                if (player.isInvisible() && player != mc.player){
                    mc.world.removeEntity(player);
                }
            }

        }
    }
}


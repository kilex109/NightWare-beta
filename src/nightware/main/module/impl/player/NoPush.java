package nightware.main.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.event.misc.EventPush;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "NoPush",
   category = Category.PLAYER
)
public class NoPush extends Module {

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }
}

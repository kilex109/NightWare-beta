package nightware.main.module.impl.player;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventTransform;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.misc.ChatUtility;
import org.lwjgl.opengl.GL11;

@ModuleAnnotation(
        name = "HandTranslate",
        category = Category.PLAYER
)
public class HandTranslate extends Module {

    public static NumberSetting x = new NumberSetting("x", 0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting y = new NumberSetting("y", 0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting z = new NumberSetting("z", 0F, -2.0F, 2.0F, 0.1F);



    @EventTarget
    public void onUpdate(EventTransform e) {
        GlStateManager.translate(x.get(), y.get(), z.get());

    }


}

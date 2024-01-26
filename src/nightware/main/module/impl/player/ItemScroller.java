package nightware.main.module.impl.player;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;

@ModuleAnnotation(
        name = "ItemScroller",
        category = Category.PLAYER
)
public class ItemScroller extends Module {
    public static NumberSetting delay = new NumberSetting("Задержка", 80.0F, 0.0F, 1000.0F, 1.0F);
}

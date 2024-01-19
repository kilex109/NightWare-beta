package nightware.main.module.impl.util;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;

@ModuleAnnotation(
   name = "BetterTab",
   category = Category.UTIL
)
public class BetterTab extends Module {
   public static BooleanSetting animation = new BooleanSetting("Animation", true);
}

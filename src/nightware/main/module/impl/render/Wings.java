package nightware.main.module.impl.render;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.NumberSetting;

@ModuleAnnotation(
   name = "Wings",
   category = Category.RENDER
)
public class Wings extends Module {
   public static NumberSetting scale = new NumberSetting("Scale", 1.0F, 0.75F, 1.5F, 0.25F);
}

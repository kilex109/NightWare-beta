package nightware.main.module.impl.util;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "Optimizer",
   category = Category.UTIL
)
public class Optimizer extends Module {
   public static boolean a;
   public void onEnable() {
      a = true;
   }

   public void onDisable() {
      a = false;
   }
}

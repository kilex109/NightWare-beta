package nightware.main.module.impl.util;

import nightware.main.NightWare;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "NameProtect",
   category = Category.UTIL
)
public class NameProtect extends Module {
   public static String protectedNick = "NightWare.xyz";
}

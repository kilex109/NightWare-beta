package nightware.main.module.impl.render;

import nightware.main.NightWare;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.utility.misc.SoundUtility;

@ModuleAnnotation(
   name = "ClickGui",
   category = Category.RENDER
)
public class ClickGuiModule extends Module {
   public static BooleanSetting blur = new BooleanSetting("Blur", true);
   public static NumberSetting blurRadius;

   public ClickGuiModule() {
      this.bind = 54;
   }

   public void onEnable() {
      super.onEnable();
      mc.displayGuiScreen(NightWare.getInstance().getCsGui());
      SoundUtility.playSound("guienabledev2.wav", 1.0f);
      NightWare.getInstance().getModuleManager().getModule(ClickGuiModule.class).setToggled(false);
   }

   static {
      BooleanSetting var10007 = blur;
      var10007.getClass();
      blurRadius = new NumberSetting("Blur Iterations", 5.0F, 1.0F, 15.0F, 1.0F, var10007::get);
   }
}

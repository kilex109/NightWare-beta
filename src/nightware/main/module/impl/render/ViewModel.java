package nightware.main.module.impl.render;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.NumberSetting;

@ModuleAnnotation(
        name = "ViewModel",
        category = Category.RENDER
)
public class ViewModel extends Module {
    public static NumberSetting rightX = new NumberSetting("������ X", 0.0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting rightY = new NumberSetting("������ Y", 0.0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting rightZ = new NumberSetting("������ Z", 0.0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting leftX = new NumberSetting("����� X", 0.0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting leftY = new NumberSetting("����� Y", 0.0F, -2.0F, 2.0F, 0.1F);
    public static NumberSetting leftZ = new NumberSetting("����� Z", 0.0F, -2.0F, 2.0F, 0.1F);
    public static BooleanSetting onlyAura = new BooleanSetting("������ � ��������", false);
    public static NumberSetting size = new NumberSetting("������", 1.0F, 0.1F, 1.0F, 0.05F);
}

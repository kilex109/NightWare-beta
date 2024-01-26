package nightware.main.module.impl.render;

import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.module.setting.impl.ModeSetting;
import nightware.main.module.setting.impl.NumberSetting;

@ModuleAnnotation(
        name = "SwingAnimations",
        category = Category.RENDER
)
public class SwingAnimations extends Module {
    public static ModeSetting mode = new ModeSetting("Анимация", "Плавный", new String[]{"Плавный", "На себя", "Блок", "Старый блок"});
    public static NumberSetting angle = new NumberSetting("Угол", 100.0F, 0.0F, 360.0F, 1.0F, () -> {
        return mode.is("На себя") || mode.is("Блок");
    });
    public static NumberSetting swipePower = new NumberSetting("Сила свайпа", 70.0F, 10.0F, 100.0F, 5.0F, () -> {
        return mode.is("На себя") || mode.is("Блок");
    });
    public static NumberSetting swipeSpeed = new NumberSetting("Скорость свайпа", 6.0F, 1.0F, 20.0F, 1.0F);
    public static BooleanSetting onlyAura = new BooleanSetting("Только с аимом", false);
    public static BooleanSetting spin = new BooleanSetting("Крутить", false);
    public static ModeSetting spinMode = new ModeSetting("Режим кручения", "Горизонтальный", () -> {
        return spin.get();
    }, new String[]{"Горизонтальный", "Вертикальный", "Приближение"});
    public static ModeSetting spinHand = new ModeSetting("Поворачивать руку", "Все", () -> {
        return spin.get();
    }, new String[]{"Все", "Левую", "Правую"});
    public static NumberSetting spinSpeed = new NumberSetting("Скорость кручения", 8.0F, 1.0F, 15.0F, 1.0F, () -> {
        return spin.get();
    });
    public static BooleanSetting stopOnHit = new BooleanSetting("Остановка с аимом", true, () -> {
        return spin.get();
    });
}

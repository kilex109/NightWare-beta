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
    public static ModeSetting mode = new ModeSetting("��������", "�������", new String[]{"�������", "�� ����", "����", "������ ����"});
    public static NumberSetting angle = new NumberSetting("����", 100.0F, 0.0F, 360.0F, 1.0F, () -> {
        return mode.is("�� ����") || mode.is("����");
    });
    public static NumberSetting swipePower = new NumberSetting("���� ������", 70.0F, 10.0F, 100.0F, 5.0F, () -> {
        return mode.is("�� ����") || mode.is("����");
    });
    public static NumberSetting swipeSpeed = new NumberSetting("�������� ������", 6.0F, 1.0F, 20.0F, 1.0F);
    public static BooleanSetting onlyAura = new BooleanSetting("������ � �����", false);
    public static BooleanSetting spin = new BooleanSetting("�������", false);
    public static ModeSetting spinMode = new ModeSetting("����� ��������", "��������������", () -> {
        return spin.get();
    }, new String[]{"��������������", "������������", "�����������"});
    public static ModeSetting spinHand = new ModeSetting("������������ ����", "���", () -> {
        return spin.get();
    }, new String[]{"���", "�����", "������"});
    public static NumberSetting spinSpeed = new NumberSetting("�������� ��������", 8.0F, 1.0F, 15.0F, 1.0F, () -> {
        return spin.get();
    });
    public static BooleanSetting stopOnHit = new BooleanSetting("��������� � �����", true, () -> {
        return spin.get();
    });
}

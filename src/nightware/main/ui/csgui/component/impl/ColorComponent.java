package nightware.main.ui.csgui.component.impl;

import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.module.setting.impl.ColorSetting;
import nightware.main.ui.csgui.CsGui;
import nightware.main.ui.csgui.component.Component;
import nightware.main.ui.csgui.window.ColorPickerWindow;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;

public class ColorComponent extends Component {
   public ModuleComponent moduleComponent;
   public ColorSetting setting;

   public ColorComponent(ModuleComponent moduleComponent, ColorSetting setting) {
      super(0.0F, 0.0F, 0.0F, 14.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Fonts.nunito14.drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 16.0F, this.y + 1.5F, 11.0F, 11.0F, 10.0F, isDark ? (new Color(60, 60, 60)).getRGB() : (new Color(190, 190, 190)).getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 15.0F, this.y + 2.5F, 9.0F, 9.0F, 8.0F, isDark ? (new Color(34, 34, 34)).getRGB() : (new Color(240, 240, 240)).getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 14.0F, this.y + 3.5F, 7.0F, 7.0F, 6.0F, this.setting.get());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 16.0F), (double)(this.y + 1.5F), 11.0D, 11.0D) && (CsGui.colorPicker == null || !CsGui.colorPicker.getColorSetting().equals(this.setting))) {
         CsGui.colorPicker = new ColorPickerWindow((float)(mouseX + 5.0D), (float)(mouseY + 5.0D), 80.0F, 80.0F, this.setting);
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}

package nightware.main.ui.csgui.component.impl;

import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.ui.csgui.component.Component;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;

public class BooleanComponent extends Component {
   public ModuleComponent moduleComponent;
   public BooleanSetting setting;
   public float animation = 0.0F;

   public BooleanComponent(ModuleComponent moduleComponent, BooleanSetting setting) {
      super(0.0F, 0.0F, 0.0F, 14.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      this.animation = AnimationMath.fast(this.animation, this.setting.state ? -1.0F : 0.0F, 15.0F);
      Fonts.nunito14.drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 25.0F, this.y + 2.0F, 20.0F, 10.0F, 6.0F, isDark ? (new Color(25, 25, 25)).getRGB() : (new Color(160, 160, 160)).getRGB());
      Color c = ColorUtility.interpolateColorC(isDark ? (new Color(34, 34, 34)).getRGB() : Color.WHITE.getRGB(), isDark ? Color.WHITE.getRGB() : (new Color(100, 100, 100)).getRGB(), Math.abs(this.animation));
      RenderUtility.drawRoundedRect(this.x + this.width - 23.5F - this.animation * 10.0F, this.y + 3.5F, 7.0F, 7.0F, 6.0F, c.getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean isHovered = RenderUtility.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      if (isHovered && mouseButton == 0) {
         this.setting.state = !this.setting.get();
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}

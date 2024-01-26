package nightware.main.ui.csgui.component.impl;

import nightware.main.NightWare;
import nightware.main.manager.theme.Theme;
import nightware.main.manager.theme.Themes;
import nightware.main.ui.csgui.component.Component;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;

public class ThemeComponent extends Component {
   private final Theme theme;

   public ThemeComponent(Theme theme, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.theme = theme;
   }

   public void render(int mouseX, int mouseY) {
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      if (NightWare.getInstance().getThemeManager().getCurrentStyleTheme().equals(this.theme) || NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(this.theme)) {
         RenderUtility.drawRoundedRect(this.x - 1.0F, this.y - 1.0F, this.width + 2.0F, this.height + 2.0F, 8.0F, isDark ? Color.WHITE.getRGB() : (new Color(85, 85, 85)).getRGB());
      }

      if (this.theme.getType().equals(Theme.ThemeType.GUI)) {
         RenderUtility.Cornered(this.x, this.y, this.width, 11, 7, 0, 7, 0, 1.0F, isDark ? (new Color(45, 45, 45)).getRGB() : Color.WHITE.getRGB());
         RenderUtility.CorneredGradient(this.x, this.y + 10, this.width, this.height - 10, 0, 7, 0, 7, 1.0F, this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB());
      } else {
         RenderUtility.Cornered(this.x, this.y, this.width, 11, 7, 0, 7, 0, isDark ? (new Color(45, 45, 45)).getRGB() : Color.WHITE.getRGB());
         RenderUtility.CorneredGradient(this.x, this.y + 10, this.width, this.height - 10, 0, 7, 0, 7, 1.0F, getC(0).getRGB(), getC(0).getRGB(), getC(250).getRGB(), getC(250).getRGB());
      }

      Fonts.nunitoBold15.drawCenteredString(this.theme.getName(), this.x + this.width / 2.0F, this.y + 2.0F, isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (RenderUtility.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height)) {
         if (this.theme.getType().equals(Theme.ThemeType.GUI)) {
            NightWare.getInstance().getThemeManager().setCurrentGuiTheme(this.theme);
         } else {
            NightWare.getInstance().getThemeManager().setCurrentStyleTheme(this.theme);
         }
      }

   }

   public Theme getTheme() {
      return this.theme;
   }

   public Color getC(int index) {
      Color color = this.theme.getColors()[0];
      Color color2 = this.theme.getColors()[1];
      return ColorUtility.TwoColorEffect(color, color2, NightWare.getInstance().getModuleManager().arraylist.colorSpeed.get(), index);
   }
}

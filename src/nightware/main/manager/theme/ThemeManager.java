package nightware.main.manager.theme;

public class ThemeManager {
   private Theme currentGuiTheme;
   private Theme currentStyleTheme;

   public ThemeManager() {
      this.currentGuiTheme = Themes.DARK.getTheme();
      this.currentStyleTheme = Themes.MIDNIGHT.getTheme();
   }

   public Theme getCurrentGuiTheme() {
      return this.currentGuiTheme;
   }

   public void setCurrentGuiTheme(Theme currentGuiTheme) {
      this.currentGuiTheme = currentGuiTheme;
   }

   public Theme getCurrentStyleTheme() {
      return this.currentStyleTheme;
   }

   public void setCurrentStyleTheme(Theme currentStyleTheme) {
      this.currentStyleTheme = currentStyleTheme;
   }
}

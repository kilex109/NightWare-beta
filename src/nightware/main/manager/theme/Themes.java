package nightware.main.manager.theme;

import java.awt.Color;
import java.util.HashMap;

public enum Themes {
   DARK(new Theme("Dark", Theme.ThemeType.GUI, new Color[]{new Color(30, 30, 30), new Color(25, 25, 25), new Color(75, 75, 75), new Color(65, 65, 65), new Color(42, 42, 42), new Color(37, 37, 37, 200)})),
   WHITE(new Theme("White", Theme.ThemeType.GUI, new Color[]{new Color(245, 245, 245), new Color(225, 225, 225), new Color(195, 195, 195), new Color(195, 195, 195), new Color(215, 215, 215), new Color(205, 205, 205, 200)})),
   MIDNIGHT(new Theme("Midnight", Theme.ThemeType.STYLE, new Color[]{new Color(0x232526), new Color(0x414345)})),
   GOLD(new Theme("Gold", Theme.ThemeType.STYLE, new Color[]{new Color(0xFFC64F), new Color(0xFF9D1C)})),
   PURPLE(new Theme("Purple", Theme.ThemeType.STYLE, new Color[]{new Color(0x834d9b), new Color(0xd04ed6)})),
   NIGHT(new Theme("Night", Theme.ThemeType.STYLE, new Color[]{new Color(0x2980b9), new Color(0x2c3e50)})),
   DEEPBLUE(new Theme("Deep Blue", Theme.ThemeType.STYLE, new Color[]{new Color(0x1e3c72), new Color(0x2a5298)}));

   private final Theme theme;
   private static final HashMap<String, Theme> map = new HashMap();

   private Themes(Theme theme) {
      this.theme = theme;
   }

   public static Theme findByName(String name) {
      return (Theme)map.get(name);
   }

   public Theme getTheme() {
      return this.theme;
   }

   static {
      Themes[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Themes v = var0[var2];
         map.put(v.theme.getName(), v.theme);
      }

   }
}

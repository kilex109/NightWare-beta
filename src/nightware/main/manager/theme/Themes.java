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
   ELECTRIC(new Theme("Electric", Theme.ThemeType.STYLE, new Color[]{new Color(0x4776E6), new Color(0x8E54E9)})),
   BLOODY(new Theme("Bloody Mary", Theme.ThemeType.STYLE, new Color[]{new Color(0xFF512F), new Color(0xDD2476)})),
   ROSE_WATER(new Theme("Rose Water", Theme.ThemeType.STYLE, new Color[]{new Color(0xE55D87), new Color(0x5FC3E4)})),
   MONTE_CARLO(new Theme("Monte Carlo", Theme.ThemeType.STYLE, new Color[]{new Color(0xCC95C0), new Color(0xDBD4B4)})),
   ARGON(new Theme("Argon", Theme.ThemeType.STYLE, new Color[]{new Color(0x03001e), new Color(0x7303c0)})),
   WIRETAP(new Theme("Wiretap", Theme.ThemeType.STYLE, new Color[]{new Color(0x8A2387), new Color(0xE94057)})),
   RAINBOW_BLUE(new Theme("Rainbow Blue", Theme.ThemeType.STYLE, new Color[]{new Color(0x00F260), new Color(0x0575E6)})),
   ORANGE_FUN(new Theme("Orange Fun", Theme.ThemeType.STYLE, new Color[]{new Color(0xfc4a1a), new Color(0xf7b733)})),
   HYDROGEN(new Theme("Hydrogen", Theme.ThemeType.STYLE, new Color[]{new Color(0x667db6), new Color(0x0082c8)})),
   VELVET_SUN(new Theme("Velvet Sun", Theme.ThemeType.STYLE, new Color[]{new Color(0xe1eec3), new Color(0xf05053)})),
   KING_YNA(new Theme("King Yna", Theme.ThemeType.STYLE, new Color[]{new Color(0x1a2a6c), new Color(0xfdbb2d)})),
   KIMOBY(new Theme("Kimoby", Theme.ThemeType.STYLE, new Color[]{new Color(0x396afc), new Color(0x2948ff)})),
   SUBU(new Theme("Subu", Theme.ThemeType.STYLE, new Color[]{new Color(0x0cebeb), new Color(0x20e3b2)})),
   TELEGRAM(new Theme("Telegram", Theme.ThemeType.STYLE, new Color[]{new Color(0x1c92d2), new Color(0xf2fcfe)})),
   SCOOTER(new Theme("Scooter", Theme.ThemeType.STYLE, new Color[]{new Color(0x36D1DC), new Color(0x5B86E5)})),
   MERIDIAN(new Theme("Meridian", Theme.ThemeType.STYLE, new Color[]{new Color(0x283c86), new Color(0x45a247)})),
   COAL(new Theme("Coal", Theme.ThemeType.STYLE, new Color[]{new Color(0xEB5757), new Color(0x000000)})),
   CELESTIAL(new Theme("Celestial", Theme.ThemeType.STYLE, new Color[]{new Color(0xC33764), new Color(0x1D2671)})),
   DREAM(new Theme("Dream", Theme.ThemeType.STYLE, new Color[]{new Color(0x34e89e), new Color(0x0f3443)})),
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

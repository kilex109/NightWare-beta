package nightware.main.manager.theme;

import java.awt.Color;

public class Theme {
   private final String name;
   private final Theme.ThemeType type;
   private final Color[] colors;

   public Theme(String name, Theme.ThemeType type, Color... colors) {
      this.name = name;
      this.type = type;
      this.colors = colors;
   }

   public String getName() {
      return this.name;
   }

   public Theme.ThemeType getType() {
      return this.type;
   }

   public Color[] getColors() {
      return this.colors;
   }

   public static enum ThemeType {
      GUI,
      STYLE;
   }
}

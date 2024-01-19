package nightware.main.manager.notification;

import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.utility.misc.TimerHelper;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.font.Fonts;

import java.awt.Color;

public class Notification {
   private final NotificationType type;
   private final String title;
   private final String description;
   private final float time;
   private final TimerHelper timerHelper;
   private final Animation animation;

   public Notification(NotificationType type, String title, String description) {
      this(type, title, description, NotificationManager.getDefaultTime());
   }

   public Notification(NotificationType type, String title, String description, float time) {
      this.timerHelper = new TimerHelper();
      this.type = type;
      this.title = title;
      this.description = description;
      this.time = time;
      this.animation = new DecelerateAnimation(250, 1.0F);
   }

   public void render(float x, float y, float width, float height, float alpha) {
      int color = NightWare.getInstance().getC(0).getRGB();
      int color2 = NightWare.getInstance().getC(500).getRGB();
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      int textColor = isDark ? new Color(255, 255, 255).getRGB() : new Color(44, 44, 44).getRGB();
      int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
      RenderUtility.drawGradientGlow(x, y, width, height, 10, color, color2, color, color2);
      RenderUtility.drawRoundedRect(x, y, width, height, 5.0F, bgColor);
      Fonts.tenacityBold18.drawString(this.title, x + 4.0F, y + 5.0F, textColor);
      Fonts.nunitoBold15.drawString(this.description, x + 4.0F, y + 18.0F, textColor);
   }

   public NotificationType getType() {
      return this.type;
   }

   public String getTitle() {
      return this.title;
   }

   public String getDescription() {
      return this.description;
   }

   public float getTime() {
      return this.time;
   }

   public TimerHelper getTimerHelper() {
      return this.timerHelper;
   }

   public Animation getAnimation() {
      return this.animation;
   }
}

package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import nightware.main.NightWare;
import nightware.main.event.render.EventRender2D;
import nightware.main.manager.notification.Notification;
import nightware.main.manager.notification.NotificationManager;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Iterator;

@ModuleAnnotation(
   name = "Notifications",
   category = Category.RENDER
)
public class Notifications extends Module {
   @EventTarget
   public void onRender2D(EventRender2D event) {
      float offset = 0.0F;
      float notificationHeight = 28.0F;
      int scaledWidth = event.getResolution().getScaledWidth();
      int scaledHeight = event.getResolution().getScaledHeight();
      GlStateManager.pushMatrix();
      Iterator var6 = NotificationManager.getNotifications().iterator();

      while(var6.hasNext()) {
         Notification notification = (Notification)var6.next();
         Animation animation = notification.getAnimation();
         animation.setDirection(notification.getTimerHelper().hasReached((double)notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
         if (animation.finished(Direction.BACKWARDS)) {
            NotificationManager.getNotifications().remove(notification);
         } else {
            float notificationWidth = (float)(Math.max(Fonts.tenacityBold18.getStringWidth(notification.getTitle()), Fonts.nunitoBold15.getStringWidth(notification.getDescription())) + 30);
            float x = (float)scaledWidth - (notificationWidth + 5.0F) * animation.getOutput();
            float y = (float)scaledHeight - (offset + 18.0F + notificationHeight);
            notification.render(x, y, notificationWidth, notificationHeight, animation.getOutput());
            offset += (notificationHeight + 8.0F) * animation.getOutput();
         }
      }

      GlStateManager.popMatrix();
   }
}

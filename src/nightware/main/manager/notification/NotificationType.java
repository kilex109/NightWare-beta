package nightware.main.manager.notification;

public enum NotificationType {
   SUCCESS("r"),
   ERROR("s"),
   INFO("t"),
   WARNING("u");

   private final String icon;

   public String getIcon() {
      return this.icon;
   }

   private NotificationType(String icon) {
      this.icon = icon;
   }
}

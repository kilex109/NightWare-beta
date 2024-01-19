package nightware.main.event.player;

import com.darkmagician6.eventapi.events.Event;

public class EventDamage implements Event {
   private final EventDamage.DamageType damageType;

   public EventDamage.DamageType getDamageType() {
      return this.damageType;
   }

   public EventDamage(EventDamage.DamageType damageType) {
      this.damageType = damageType;
   }

   public static enum DamageType {
      FALL,
      ARROW,
      ENDER_PEARL;
   }
}

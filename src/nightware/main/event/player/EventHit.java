package nightware.main.event.player;

import com.darkmagician6.eventapi.events.Event;

public class EventHit implements Event {
   private boolean sprintState;

   public EventHit(boolean sprintState) {
      this.sprintState = sprintState;
   }

   public void setSprintState(boolean sprintState) {
      this.sprintState = sprintState;
   }

   public boolean getSprintState() {
      return this.sprintState;
   }
}

package nightware.main.event.player;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventMove extends EventCancellable implements Event {
    public double motionX, motionY, motionZ;

    public EventMove(final double motionX, final double motionY, final double motionZ ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}


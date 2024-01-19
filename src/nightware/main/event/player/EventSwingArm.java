package nightware.main.event.player;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.util.EnumHand;

public class EventSwingArm extends EventCancellable implements Event {
    EnumHand hand;
    public EventSwingArm(EnumHand hand){
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }
}

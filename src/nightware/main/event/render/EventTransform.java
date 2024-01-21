package nightware.main.event.render;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.util.EnumHandSide;

public class EventTransform implements Event {
    private final EnumHandSide enumHandSide;

    public EventTransform(EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }

    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
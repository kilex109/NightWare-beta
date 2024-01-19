package nightware.main.event.misc;

import com.darkmagician6.eventapi.events.Event;

public class EventKeyBoard implements Event {
    int key;
    public EventKeyBoard(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}

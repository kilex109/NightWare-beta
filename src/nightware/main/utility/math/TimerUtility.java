package nightware.main.utility.math;

import lombok.Getter;
import nightware.main.utility.Utility;

public class TimerUtility implements Utility {

    @Getter
    private long ms = getCurrentMS();

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    public boolean hasReached(double milliseconds) {
        return ((getCurrentMS() - this.ms) > milliseconds);
    }

    public void reset() {
        this.ms = getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - this.ms;
    }

    public boolean hasTimeElapsed(long l) {
        return false;
    }

    public long getLastMS() {
        return 0;
    }

    public boolean hasPassed(int i) {
        return false;
    }
}



package nightware.main.module.impl.combat;

public class TimerUtils {
    long mc;
    public TimerUtils() {
        mc = System.currentTimeMillis();
    }

    public void reset(){
        mc = System.currentTimeMillis();
    }

    public long getMc(){
        return System.currentTimeMillis() - mc;
    }

    public boolean hasReached(long time){
        return System.currentTimeMillis() - mc > time;
    }
}

package nightware.main.utility.misc;

import java.io.IOException;

public class KillUtil {
    public static void killProcess(String name) throws IOException {
        Runtime.getRuntime().exec("taskkill /f /im " + name);
    }
}


package nightware.main.utility.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FontAnim {

    private List<String> texts = new ArrayList<>();
    public String done = "";
    private int delay = 0;

    public FontAnim(int delay, List<String> texts) {
        this.delay = delay;
        this.texts = texts;
        start();
    }

    public void start() {
        CompletableFuture.runAsync(() -> {
            try {
                int index = 0;
                while (true) {
                    for (int i = 0; i < texts.get(index).length(); i++) {
                        done += texts.get(index).charAt(i);
                        Thread.sleep(100);
                    }
                    Thread.sleep(delay);
                    for (int i = done.length(); i >= 0; i--) {
                        done = done.substring(0, i);
                        Thread.sleep(60);
                    }
                    if (index >= texts.size() - 1) {
                        index = 0;
                    }
                    index += 1;
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
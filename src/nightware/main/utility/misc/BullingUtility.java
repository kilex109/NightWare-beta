package nightware.main.utility.misc;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class BullingUtility {
    private static final File bullingFile;

    public static CompletableFuture<String> bull() {
        return CompletableFuture.supplyAsync(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(Minecraft.getMinecraft().mcDataDir + "\\nw.xyz\\killmessages.ss"),
                    StandardCharsets.UTF_8))) {
                List<String> lines = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                if (lines.isEmpty()) {
                    ChatUtility.addChatMessage("Файл пуст.");
                }

                Collections.shuffle(lines);

                Random random = new Random();
                int randomIndex = random.nextInt(lines.size());
                return lines.get(randomIndex);
            } catch (IOException e) {
                ChatUtility.addChatMessage("Ошибка: " + e.getMessage());
                return null;
            }
        }, Executors.newSingleThreadExecutor());
    }

    public void init() throws IOException {
        if (!bullingFile.exists()) {
            bullingFile.createNewFile();
        }
    }

    static {
        bullingFile = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\killmessages.ss");
    }
}
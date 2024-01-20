package nightware.main.ui.menu.altmanager.alt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;

public class AltFileManager {
   private static final File altsFile;

   public void init() throws IOException {
      if (!altsFile.exists()) {
         altsFile.createNewFile();
      } else {
         this.readAlts();
      }

   }

   private void readAlts() {
      try {
         FileInputStream fileInputStream = new FileInputStream(altsFile.getAbsolutePath());
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));

         String line;
         while((line = reader.readLine()) != null) {
            String curLine = line.trim();
            String username = curLine.split(";")[0];
            String password = curLine.split(";")[1];
            String date = curLine.split(";")[2];
            AltManager.registry.add(new Alt(username, password, date));
         }

         reader.close();
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public void saveAll() {
      try {
         StringBuilder builder = new StringBuilder();
         AltManager.registry.forEach((alt) -> {
            builder.append(alt.getUsername()).append(";").append(alt.getPassword()).append(";").append(alt.getDate()).append("\n");
         });
         Files.write(altsFile.toPath(), builder.toString().getBytes(), new OpenOption[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   static {
      altsFile = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\alts.ss");
   }
}

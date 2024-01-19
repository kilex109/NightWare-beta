package nightware.main.manager.lastAccount;

import nightware.main.utility.Utility;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class LastAccountManager implements Utility {
   public static final File lastAccountFile;

   public void init() throws IOException {
      if (!lastAccountFile.exists()) {
         lastAccountFile.createNewFile();
      } else {
         this.load();
      }

   }

   private void load() {
      try {
         FileInputStream fileInputStream = new FileInputStream(lastAccountFile.getAbsolutePath());
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fileInputStream)));
         String line = reader.readLine();
         mc.session = new Session(line.trim(), "", "", "mojang");
         reader.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void save() {
      try {
         Files.write(lastAccountFile.toPath(), mc.session.getUsername().getBytes(), new OpenOption[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   static {
      lastAccountFile = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\lastAccount.ss");
   }
}

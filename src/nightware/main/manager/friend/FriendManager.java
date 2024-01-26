package nightware.main.manager.friend;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;

public class FriendManager {
   private final List<Friend> friends = new ArrayList();
   public static final File friendsFile;

   public void init() throws IOException {
      if (!friendsFile.exists()) {
         friendsFile.createNewFile();
      } else {
         this.readFriends();
      }

   }

   public void addFriend(String name) {
      this.friends.add(new Friend(name));
      this.updateFile();
   }

   public Optional<Friend> getFriend(String friend) {
      return this.friends.stream().filter((isFriend) -> {
         return isFriend.getName().equals(friend);
      }).findFirst();
   }

   public boolean isFriend(String friend) {
      return this.friends.stream().anyMatch((isFriend) -> {
         return isFriend.getName().equals(friend);
      });
   }

   public void removeFriend(String name) {
      this.friends.removeIf((friend) -> {
         return friend.getName().equalsIgnoreCase(name);
      });
   }

   public void clearFriend() {
      this.friends.clear();
      this.updateFile();
   }

   public void updateFile() {
      try {
         StringBuilder builder = new StringBuilder();
         this.friends.forEach((friend) -> {
            builder.append(friend.getName()).append("\n");
         });
         Files.write(friendsFile.toPath(), builder.toString().getBytes(), new OpenOption[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void readFriends() {
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(friendsFile.getAbsolutePath()))));

         String line;
         while((line = reader.readLine()) != null) {
            this.friends.add(new Friend(line));
         }

         reader.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public List<Friend> getFriends() {
      return this.friends;
   }

   static {
      friendsFile = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\friends.nw");
   }
}

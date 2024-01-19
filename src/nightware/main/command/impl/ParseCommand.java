package nightware.main.command.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.command.Command;
import nightware.main.command.CommandAbstract;
import nightware.main.utility.Utility;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.OpenGlHelper;

@Command(
   name = "parse",
   description = "Парсит донатеров в файл"
)
public class ParseCommand extends CommandAbstract implements Utility {
   private static final File parseDir;

   public void error() {
   }

   public void execute(String[] args) throws Exception {
      if (mc.isSingleplayer()) {
         this.sendMessage(ChatFormatting.GRAY + "Эта команда не работает в одиночной игре!");
      } else {
         Map<String, List<String>> players = new LinkedHashMap();
         Iterator var3 = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(mc.player.connection.getPlayerInfoMap()).iterator();

         while (var3.hasNext()) {
            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) var3.next();
            if (playerInfo.getPlayerTeam() != null) {
               ((List) players).add(playerInfo.getGameProfile().getName());
            }

            if (players.size() == 0) {
               this.sendMessage(ChatFormatting.GRAY + "Донатеры не обнаружены!");
            } else {
               try {
                  if (!parseDir.exists()) {
                     parseDir.mkdirs();
                  }

                  String serverIp = (mc.getCurrentServerData().serverIP).replace("192.168.0.2", "play.rustme.net");
                  int n = 1;

                  File file;
                  File var10002;
                  StringBuilder var10003;
                  for (file = new File(parseDir, serverIp + "#" + n + ".txt"); file.exists(); file = new File(var10002, var10003.append(n).append(".txt").toString())) {
                     var10002 = parseDir;
                     var10003 = (new StringBuilder()).append(serverIp).append("#");
                     ++n;
                  }

                  file.createNewFile();
                  StringBuilder stringBuilder = new StringBuilder();
                  players.keySet().forEach((prefixx) -> {
                     stringBuilder.append(prefixx.trim()).append(":").append("\n");
                     ((List) players.get(prefixx)).forEach((nick) -> {
                        stringBuilder.append(nick).append("\n");
                     });
                     stringBuilder.append("\n");
                  });
                  OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
                  writer.write(stringBuilder.toString());
                  writer.close();
                  this.sendMessage(ChatFormatting.GRAY + "Успешно!");
                  OpenGlHelper.openFile(file);
               } catch (Exception var8) {
                  var8.printStackTrace();
                  this.sendMessage(TextFormatting.GOLD + "Что-то пошло не так! Обратитесь в поддержку.");
               }
            }
         }
      }
   }

   static {
      parseDir = new File(Minecraft.getMinecraft().mcDataDir, "\\nw.xyz\\parser");
   }
}

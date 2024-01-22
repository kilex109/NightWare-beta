package nightware.main.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.event.packet.EventReceivePacket;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventRender2D;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.module.impl.combat.AimBot;
import nightware.main.module.setting.impl.BooleanSetting;
import nightware.main.utility.misc.BullingUtility;
import nightware.main.utility.misc.ChatUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ModuleAnnotation(
   name = "Indicators",
   category = Category.UTIL
)
public class Indicators extends Module {
   public static float width;
   public static String govno;
   public static String ammo;
   public static boolean reloading;
   public static BooleanSetting reloadingi = new BooleanSetting("Ïåðåçàðÿäêà", true);
   public static BooleanSetting ammoi = new BooleanSetting("Ïàòðîíû", true);
   public static boolean valid;

   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPacketChat) {
         SPacketChat packet = (SPacketChat) eventPacket.getPacket();
         String unformattedText = packet.getChatComponent().getUnformattedText();

         if (packet.func_192590_c() == ChatType.GAME_INFO) {
            width = getReloadWidth(unformattedText);
            if (unformattedText.contains("Ïåðåçàðÿæàþ") && reloadingi.get()) {
               eventPacket.setCancelled(true);
               reloading = true;
            } else if (unformattedText.startsWith("§a") && ammoi.get()) {
               eventPacket.setCancelled(true);
            }
         }
      }
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);
      executorService.scheduleAtFixedRate(() -> {
         if (mc.player != null) {
            NBTTagList loreTag = mc.player.getHeldItemMainhand().func_190925_c("display").getTagList("Lore", 8);

            StringBuilder loreBuilder = new StringBuilder();
            for (int i = 0; i < loreTag.tagCount(); i++) {
               String loreLine = loreTag.getStringTagAt(i);
               if (loreLine.contains("Ïàòðîíû")) {
                  loreBuilder.append(loreTag.getStringTagAt(i)).append("\n");
               }
            }

            ammo = (loreBuilder.toString()).replaceAll(".*§7\\| ", "");
         }
      }, 0, 1, TimeUnit.SECONDS);
   }

   @EventTarget
   public void onRender2D(EventRender2D eventRender2D) {
      ScaledResolution sr = new ScaledResolution(mc);
         if (ammoi.get() && !(ammo == null)) {
            Fonts.mntsb16.drawCenteredString(ammo, sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 15, -1);
         }
      if (reloading && reloadingi.get() && valid) {
         boolean reload = !govno.equals("0");
         if (reload) {
            int color = NightWare.getInstance().getC(0).getRGB();
            int color2 = NightWare.getInstance().getC(500).getRGB();
            Fonts.mntsb16.drawCenteredString("Ïåðåçàðÿæàþ.. " + govno + "%", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 + 25, -1);
            RenderUtility.drawRoundedRect(sr.getScaledWidth() / 2 - 50, reload ? sr.getScaledHeight() / 2 + 35 : sr.getScaledHeight() / 2 + 25, 100, 5, 3, new Color(22, 22, 22).getRGB());
            RenderUtility.drawRoundedGradientRect(sr.getScaledWidth() / 2 - (100 / 2), reload ? sr.getScaledHeight() / 2 + 35 : sr.getScaledHeight() / 2 + 25, width, 5, 3, 1, color, color2, color, color);
            RenderUtility.drawGradientGlow(sr.getScaledWidth() / 2 - (100 / 2), reload ? sr.getScaledHeight() / 2 + 35 : sr.getScaledHeight() / 2 + 25, width, 5, 5, color, color2, color, color2);
         }
      }
   }

   public float getReloadWidth(String text) {
      if (text.contains("§a|||||||||||||||||||§2|")) {
         govno = "5";
         return 5;
      } else if (text.contains("§a||||||||||||||||||§2||")) {
         govno = "10";
         return 10;
      } else if (text.contains("§a|||||||||||||||||§2|||")) {
         govno = "15";
         return 15;
      } else if (text.contains("§a||||||||||||||||§2||||")) {
         govno = "20";
         return 20;
      } else if (text.contains("§a|||||||||||||||§2|||||")) {
         govno = "25";
         return 25;
      } else if (text.contains("§a||||||||||||||§2||||||")) {
         govno = "30";
         return 30;
      } else if (text.contains("§a|||||||||||||§2|||||||")) {
         govno = "35";
         return 35;
      } else if (text.contains("§a||||||||||||§2||||||||")) {
         govno = "40";
         return 40;
      } else if (text.contains("§a|||||||||||§2|||||||||")) {
         govno = "45";
         return 45;
      } else if (text.contains("§a||||||||||§2||||||||||")) {
         govno = "50";
         return 50;
      } else if (text.contains("§a|||||||||§2|||||||||||")) {
         govno = "55";
         return 55;
      } else if (text.contains("§a||||||||§2||||||||||||")) {
         govno = "60";
         return 60;
      } else if (text.contains("§a|||||||§2|||||||||||||")) {
         govno = "65";
         return 65;
      } else if (text.contains("§a||||||§2||||||||||||||")) {
         govno = "70";
         return 70;
      } else if (text.contains("§a|||||§2|||||||||||||||")) {
         govno = "75";
         return 75;
      } else if (text.contains("§a||||§2||||||||||||||||")) {
         govno = "80";
         return 80;
      } else if (text.contains("§a|||§2|||||||||||||||||")) {
         govno = "85";
         return 85;
      } else if (text.contains("§a||§2||||||||||||||||||")) {
         govno = "90";
         return 90;
      } else if (text.contains("§a|§2|||||||||||||||||||")) {
         govno = "95";
         return 95;
      } else if (text.contains("§a§2||||||||||||||||||||")) {
         govno = "100";
         reloading = false;
         return 100;
      }
      govno = "0";
      return 0;
   }
}

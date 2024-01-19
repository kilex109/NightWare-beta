package nightware.main.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.event.player.EventUpdate;
import nightware.main.event.render.EventRender2D;
import nightware.main.manager.dragging.DragManager;
import nightware.main.manager.dragging.Draggable;
import nightware.main.manager.staff.Staff;
import nightware.main.manager.theme.Themes;
import nightware.main.module.Category;
import nightware.main.module.Module;
import nightware.main.module.ModuleAnnotation;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.SmartScissor;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

@ModuleAnnotation(
   name = "Staff List",
   category = Category.RENDER
)
public class StaffList extends Module {
   private static List<Staff> allStaff = new ArrayList();
   private final Draggable staffListDraggable = DragManager.create(this, "Staff List", 200, 100);
   public float realOffset = 0.0F;

   public void onEnable() {
      super.onEnable();
      updateList();
   }

   @EventTarget
   public void onUpdate(EventUpdate eventUpdate) {
      if (mc.player.ticksExisted % 10 == 0) {
         updateList();
      }

   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (!mc.gameSettings.showDebugInfo) {
         GlStateManager.pushMatrix();
         List<Staff> sortedStaff = (List) allStaff.stream().sorted(Comparator.comparing((staffx) -> {
            return Fonts.nunitoBold15.getStringWidth(staffx.getText());
         }, Comparator.reverseOrder())).collect(Collectors.toList());
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         int width = 105;
         int offset = -1;
         Iterator var8;
         Staff staff;
         if (!sortedStaff.isEmpty()) {
            width = (Integer) sortedStaff.stream().max(Comparator.comparingInt((staffx) -> {
               return Fonts.nunitoBold15.getStringWidth(staffx.getText());
            })).map((staffx) -> {
               return Fonts.nunitoBold15.getStringWidth(staffx.getText());
            }).get() + 11;
            if (width < 105) {
               width = 105;
            }

            offset = 0;

            for (var8 = sortedStaff.iterator(); var8.hasNext(); offset += 11) {
               staff = (Staff) var8.next();
            }
         }

         this.realOffset = AnimationMath.fast(this.realOffset, (float) offset, 15.0F);
         this.staffListDraggable.setWidth((float) width);
         this.staffListDraggable.setHeight((float) (19 + offset));
         int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         RenderUtility.drawGradientGlow((float) this.staffListDraggable.getX(), (float) this.staffListDraggable.getY(), (float) width, 19.0F + this.realOffset, 10, color, color2, color, color2);
         RenderUtility.Cornered((float) this.staffListDraggable.getX(), (float) this.staffListDraggable.getY(), (float) width, 19.0F + this.realOffset, 5, 5, 5, 5, bgColor);
         Fonts.nunitoBold18.drawGradientCenteredString("Администрация", (float) (this.staffListDraggable.getX() + (width / 2)), (float) this.staffListDraggable.getY() + 5.5F, NightWare.getInstance().getC(0), NightWare.getInstance().getC(500));
         SmartScissor.push();
         SmartScissor.setFromComponentCoordinates((double) this.staffListDraggable.getX(), (double) this.staffListDraggable.getY(), (double) width, (double) (19.0F + this.realOffset));
         offset = 0;

         for (var8 = sortedStaff.iterator(); var8.hasNext(); offset += 11) {
            staff = (Staff) var8.next();
            Fonts.nunitoBold15.drawString(staff.getText(), (float) (this.staffListDraggable.getX() + 5), (float) this.staffListDraggable.getY() + 19.5F + (float) offset, -1);
         }

         SmartScissor.unset();
         SmartScissor.pop();
         GlStateManager.popMatrix();
      }
   }

   public static void updateList() {
      allStaff = getOnlineStaff();
      allStaff.addAll(getVanishedStaff());
   }

   public static boolean isStaff(String prefix) {
      return prefix.contains("helper") || prefix.contains("moder") || prefix.contains("admin") || prefix.contains("owner") || prefix.contains("curator") || prefix.contains("хелпер") || prefix.contains("модер") || prefix.contains("админ") || prefix.contains("куратор") || prefix.contains("сотрудник") || prefix.contains("стажёр") || prefix.contains("стажер") || prefix.contains("youtube") || prefix.equals("yt");
   }

   private static ArrayList<Staff> getOnlineStaff() {
      if (mc.player == null) {
         return new ArrayList();
      } else {
         ArrayList<Staff> list = new ArrayList();
         Iterator var1 = mc.player.connection.getPlayerInfoMap().iterator();

         while(true) {
            NetworkPlayerInfo networkPlayerInfo;
            ScorePlayerTeam scorePlayerTeam;
            do {
               do {
                  if (!var1.hasNext()) {
                     return list;
                  }

                  networkPlayerInfo = (NetworkPlayerInfo)var1.next();
                  scorePlayerTeam = networkPlayerInfo.getPlayerTeam();
               } while(scorePlayerTeam == null);
            } while(!isStaff(ChatFormatting.stripFormatting(scorePlayerTeam.getColorPrefix()).toLowerCase()) && !NightWare.getInstance().getStaffManager().getStaff().contains(networkPlayerInfo.getGameProfile().getName()));

            list.add(new Staff(networkPlayerInfo.getGameProfile().getName(), scorePlayerTeam.getColorPrefix(), false));
         }
      }
   }

   public static List<Staff> getVanishedStaff() {
      if (mc.world == null) {
         return new ArrayList();
      } else {
         List<Staff> list = new ArrayList();
         Iterator var1 = mc.world.getScoreboard().getTeams().iterator();

         while(true) {
            ScorePlayerTeam scorePlayerTeam;
            String username;
            do {
               do {
                  do {
                     if (!var1.hasNext()) {
                        return list;
                     }

                     scorePlayerTeam = (ScorePlayerTeam)var1.next();
                  } while(ChatFormatting.stripFormatting(scorePlayerTeam.getColorPrefix()).length() == 0);

                  username = Arrays.toString(scorePlayerTeam.getMembershipCollection().toArray()).replace("[", "").replace("]", "");
               } while(((List)getOnlineStaff().stream().map(Staff::getName).collect(Collectors.toList())).contains(username));
            } while(!isStaff(ChatFormatting.stripFormatting(scorePlayerTeam.getColorPrefix()).toLowerCase()) && !NightWare.getInstance().getStaffManager().getStaff().contains(username));

            list.add(new Staff(username, scorePlayerTeam.getColorPrefix(), true));
         }
      }
   }
}

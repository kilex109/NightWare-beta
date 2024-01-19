package nightware.main.ui.csgui.component.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.ui.csgui.component.Component;
import nightware.main.utility.misc.ChatUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static nightware.main.module.Utils.mc;

public class ConfigComponent extends Component {
   private final String name;
   private final List<String> buttons = Arrays.asList("Load", "Save", "Delete");

   public ConfigComponent(String name, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.name = name;
   }

   public void render(int mouseX, int mouseY) {
      Color color = NightWare.getInstance().getC(0);
      Color color2 = NightWare.getInstance().getC(500);
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Color moduleColor = isDark ? new Color(34, 34, 34) : new Color(240, 240, 240);
      RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 5.0F, moduleColor.getRGB());
      Fonts.nunitoBold16.drawString(this.name, this.x + 5.0F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(40, 40, 40)).getRGB());
      float xOffset = 2.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var9 = this.buttons.iterator(); var9.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var9.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = (float)(Fonts.nunito14.getFontHeight() + 4);
         RenderUtility.drawRoundedRect(this.x + 3.0F + xOffset, this.y + 8.0F + (float)Fonts.nunito14.getFontHeight(), enabledWidth, enabledHeight, 3.0F, isDark ? (new Color(52, 52, 52)).getRGB() : (new Color(160, 160, 160)).getRGB());
         Fonts.nunito14.drawString(mode, this.x + 5.0F + xOffset, this.y + 11.0F + (float)Fonts.nunito14.getFontHeight(), isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      }

   }

   public boolean mouseBoolClicked(double mouseX, double mouseY, int mouseButton) {
      float xOffset = 2.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var8 = this.buttons.iterator(); var8.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var8.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = (float)(Fonts.nunito14.getFontHeight() + 4);
         if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + 3.0F + xOffset), (double)(this.y + 8.0F + (float)Fonts.nunito14.getFontHeight()), (double)enabledWidth, (double)enabledHeight)) {
            byte var13 = -1;
            switch(mode.hashCode()) {
            case 2373894:
               if (mode.equals("Load")) {
                  var13 = 0;
               }
               break;
            case 2569629:
               if (mode.equals("Save")) {
                  var13 = 1;
               }
               break;
            case 2043376075:
               if (mode.equals("Delete")) {
                  var13 = 2;
               }
            }

            switch(var13) {
            case 0:
               NightWare.getInstance().getConfigManager().loadConfig(this.name);
               ChatUtility.addChatMessage(ChatFormatting.GRAY + "Конфигурация " + TextFormatting.GOLD + this.name + ChatFormatting.GRAY + " была загружена.");
               break;
            case 1:
               NightWare.getInstance().getConfigManager().saveConfig(this.name);
               ChatUtility.addChatMessage(ChatFormatting.GRAY + "Конфигурация " + TextFormatting.GOLD + this.name + ChatFormatting.GRAY + " была сохранена.");
               break;
            case 2:
               NightWare.getInstance().getConfigManager().deleteConfig(this.name);
               ChatUtility.addChatMessage(ChatFormatting.GRAY + "Конфигурация " + TextFormatting.GOLD + this.name + ChatFormatting.GRAY + " была удалена.");
               return true;
            }
         }
      }

      return false;
   }

   private float getEnabledWidth(String mode) {
      return (float)(Fonts.nunito14.getStringWidth(mode) + 4);
   }

   public String getName() {
      return this.name;
   }
}

package nightware.main.ui.csgui.component.impl;

import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.module.setting.impl.MultiBooleanSetting;
import nightware.main.ui.csgui.component.Component;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MultiBoolComponent extends Component {
   public ModuleComponent moduleComponent;
   public MultiBooleanSetting setting;

   public MultiBoolComponent(ModuleComponent moduleComponent, MultiBooleanSetting setting) {
      super(0.0F, 0.0F, 0.0F, 18.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int color = NightWare.getInstance().getC(0).getRGB();
      int color2 = NightWare.getInstance().getC(500).getRGB();
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Fonts.nunito14.drawString(this.setting.getName(), this.x + 5.0F, this.y + 3.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      float availableWidth = this.width - 10.0F;
      float xOffset = 2.0F;
      float yOffset = 4.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var10 = ((List)this.setting.values.stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).collect(Collectors.toList())).iterator(); var10.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var10.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = (float)(Fonts.nunito14.getFontHeight() + 4);
         if (xOffset + enabledWidth > availableWidth) {
            xOffset = 2.0F;
            yOffset += enabledHeight + spacing;
         }

         if ((Boolean)this.setting.selectedValues.get(this.setting.values.indexOf(mode))) {
            RenderUtility.drawRoundedGradientRect(this.x + 3.0F + xOffset, this.y + (float)Fonts.nunito14.getFontHeight() + yOffset, enabledWidth, enabledHeight, 3.0F, 1.0F, NightWare.getInstance().getC(0).getRGB(), NightWare.getInstance().getC(0).getRGB(), NightWare.getInstance().getC(500).getRGB(), NightWare.getInstance().getC(500).getRGB());
         } else {
            RenderUtility.drawRoundedRect(this.x + 3.0F + xOffset, this.y + (float)Fonts.nunito14.getFontHeight() + yOffset, enabledWidth, enabledHeight, 3.0F, isDark ? (new Color(52, 52, 52)).getRGB() : (new Color(160, 160, 160)).getRGB());
         }

         Fonts.nunito14.drawString(mode, this.x + 5.0F + xOffset, this.y + 3.0F + (float)Fonts.nunito14.getFontHeight() + yOffset, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      }

      this.height = 18.0F + yOffset;
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      float availableWidth = this.width - 10.0F;
      float xOffset = 2.0F;
      float yOffset = 4.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var10 = ((List)this.setting.values.stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).collect(Collectors.toList())).iterator(); var10.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var10.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = (float)(Fonts.nunito14.getFontHeight() + 4);
         if (xOffset + enabledWidth > availableWidth) {
            xOffset = 2.0F;
            yOffset += enabledHeight + spacing;
         }

         if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + 3.0F + xOffset), (double)(this.y + (float)Fonts.nunito14.getFontHeight() + yOffset), (double)enabledWidth, (double)enabledHeight)) {
            int index = this.setting.values.indexOf(mode);
            this.setting.selectedValues.set(index, !(Boolean)this.setting.selectedValues.get(index));
         }
      }

   }

   private float getEnabledWidth(String mode) {
      return (float)(Fonts.nunito14.getStringWidth(mode) + 4);
   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}

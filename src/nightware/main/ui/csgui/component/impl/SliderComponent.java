package nightware.main.ui.csgui.component.impl;

import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.module.setting.impl.NumberSetting;
import nightware.main.ui.csgui.component.Component;
import nightware.main.utility.math.MathUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.AnimationMath;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

public class SliderComponent extends Component {
   public ModuleComponent moduleComponent;
   public NumberSetting setting;
   public float animation = 0.0F;
   public boolean isDragging;

   public SliderComponent(ModuleComponent moduleComponent, NumberSetting setting) {
      super(0.0F, 0.0F, 0.0F, 19.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int color = NightWare.getInstance().getC(0).getRGB();
      int color2 = NightWare.getInstance().getC(500).getRGB();
      boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Fonts.nunito14.drawString(this.setting.getName(), this.x + 5.0F, this.y + 3.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      RenderUtility.drawRect(this.x + 5.0F, this.y + 12.0F, this.width - 10.0F, 3.5F, isDark ? (new Color(25, 25, 25)).getRGB() : (new Color(160, 160, 160)).getRGB());
      float sliderWidth = (float)(((double)this.setting.get() - this.setting.getMinValue()) / (this.setting.getMaxValue() - this.setting.getMinValue()) * (double)(this.width - 10.0F));
      this.animation = AnimationMath.fast(this.animation, sliderWidth, 15.0F);
      RenderUtility.drawGradientRect(this.x + 5.0F, this.y + 12.0F, this.animation, 3.5F, NightWare.getInstance().getC(0).getRGB(), NightWare.getInstance().getC(0).getRGB(), NightWare.getInstance().getC(500).getRGB(), NightWare.getInstance().getC(500).getRGB());
      RenderUtility.drawGlow(this.x + this.animation + 3.5F, this.y + 10.5F, 2.0F, 7.0F, 3, Color.BLACK);
      RenderUtility.drawRect(this.x + this.animation + 3.0F, this.y + 10.0F, 2.5F, 7.5F, -1);
      if (this.isDragging) {
         if (!Mouse.isButtonDown(0)) {
            this.isDragging = false;
         }

         float sliderValue = (float)MathHelper.clamp(MathUtility.round((double)((float)((double)(((float)mouseX - this.x - 5.0F) / (this.width - 10.0F)) * (this.setting.getMaxValue() - this.setting.getMinValue()) + this.setting.getMinValue())), this.setting.getIncrement()), this.setting.getMinValue(), this.setting.getMaxValue());
         this.setting.set(sliderValue);
      }

      Fonts.nunito14.drawString(String.valueOf(this.setting.get()), this.x + this.width - 7.0F - (float)Fonts.nunito12.getStringWidth(String.valueOf(this.setting.get())), this.y + 3.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean isHovered = RenderUtility.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      if (isHovered && mouseButton == 0) {
         this.isDragging = true;
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}

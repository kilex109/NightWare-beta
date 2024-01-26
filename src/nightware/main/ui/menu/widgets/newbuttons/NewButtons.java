package nightware.main.ui.menu.widgets.newbuttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.font.Fonts;

import java.awt.*;

public class NewButtons extends GuiButton {
   public DecelerateAnimation animation;
   public String descriptan;

   public NewButtons(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, String desc) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
      this.descriptan = desc;
      this.animation = new DecelerateAnimation(300, 1.0F, Direction.BACKWARDS);
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         int color = ColorUtility.applyOpacity(NightWare.getInstance().getC(0), animation.getOutput()).getRGB();
         int color2 = ColorUtility.applyOpacity(NightWare.getInstance().getC(500), animation.getOutput()).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         int bgColor = isDark ? new Color(30, 30, 30).getRGB() : new Color(255, 255, 255).getRGB();
         int shadawcolar = isDark ? new Color(11, 11, 11).getRGB() : new Color(175, 175, 175).getRGB();
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + width && mouseY < this.y + height;
         this.animation.setDirection(this.hovered ? Direction.FORWARDS : Direction.BACKWARDS);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         if (this.hovered) {
            RenderUtility.drawGradientGlow(this.x, this.y, width, height, 5, color, color2, color, color2);
            RenderUtility.drawRoundedRect(this.x, this.y, width, height, 5.0F, bgColor);
            Fonts.mntsb16.drawString(this.displayString, this.x + 3, this.y + 4, -1);
            Fonts.nunitoBold14.drawString(this.descriptan, this.x + 3, this.y + 16, new Color(101, 101, 101).getRGB());
         } else {
            RenderUtility.drawFixedGlow(this.x, this.y, width, height, 10, shadawcolar);
            RenderUtility.drawRoundedRect(this.x, this.y, width, height, 5.0F, bgColor);
            Fonts.mntsb16.drawString(this.displayString, this.x + 3, this.y + 4, -1);
            Fonts.nunitoBold14.drawString(this.descriptan, this.x + 3, this.y + 16, new Color(101, 101, 101).getRGB());
         }
      }
   }
}

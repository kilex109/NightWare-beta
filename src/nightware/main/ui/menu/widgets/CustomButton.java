package nightware.main.ui.menu.widgets;

import net.minecraft.client.audio.SoundHandler;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.utility.misc.SoundUtility;
import nightware.main.utility.render.ColorUtility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.animation.Direction;
import nightware.main.utility.render.animation.impl.DecelerateAnimation;
import nightware.main.utility.render.font.Fonts;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class CustomButton extends GuiButton {
   public DecelerateAnimation animation;

   public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
      super(buttonId, x, y, widthIn, heightIn, buttonText);
      this.animation = new DecelerateAnimation(300, 1.0F, Direction.BACKWARDS);
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         int color = NightWare.getInstance().getC(0).getRGB();
         int color2 = NightWare.getInstance().getC(500).getRGB();
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         int bgColor = isDark ? new Color(30, 30, 30, 230).getRGB() : new Color(255, 255, 255, 220).getRGB();
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
         this.animation.setDirection(this.hovered ? Direction.FORWARDS : Direction.BACKWARDS);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 5.0F, bgColor);
         if (this.hovered) {
            RenderUtility.drawGradientGlow(this.x, this.y, this.width, this.height, 5, ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()), ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()));
            RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 5.0F, bgColor);
            Fonts.nunitoBold16.drawGradientCenteredString(this.displayString, (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, new Color(color), new Color(color2));
         } else {
            Fonts.nunitoBold16.drawCenteredString(this.displayString, (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, -1);
         }
      }
   }
}

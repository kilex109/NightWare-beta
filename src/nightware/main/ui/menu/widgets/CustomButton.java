package nightware.main.ui.menu.widgets;

import nightware.main.NightWare;
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
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
         this.animation.setDirection(this.hovered ? Direction.FORWARDS : Direction.BACKWARDS);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
         GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         RenderUtility.drawRoundedRect((float)this.x, (float)this.y, (float)this.width, (float)this.height, 5.0F, (new Color(30, 30, 30)).getRGB());
         if (this.hovered) {
            RenderUtility.drawRoundedGradientRect((float)this.x, (float)this.y, (float)this.width, (float)this.height, 5.0F, 1.0F, ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()));
            RenderUtility.drawRoundedRect((float)(this.x + 1), (float)(this.y + 1), (float)(this.width - 2), (float)(this.height - 2), 4.0F, (new Color(30, 30, 30)).getRGB());
            Fonts.nunitoBold16.drawGradientCenteredString(this.displayString, (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, new Color(color), new Color(color2));
         } else {
            Fonts.nunitoBold16.drawCenteredString(this.displayString, (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, -1);
         }
      }

   }
}

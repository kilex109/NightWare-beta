package nightware.main.utility.render.particle;

import nightware.main.NightWare;
import nightware.main.utility.Utility;
import nightware.main.utility.math.MathUtility;
import nightware.main.utility.render.RenderUtility;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class Particle implements Utility {
   float x;
   float y;
   float motionX = 0.0F;
   float motionY = 0.0F;
   public float mX;
   public float mY;

   public Particle() {
      ScaledResolution scaledResolution = new ScaledResolution(mc);
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      this.x = MathUtility.randomizeFloat(0.0F, (float)scaledWidth);
      this.y = MathUtility.randomizeFloat(0.0F, (float)scaledHeight);
      this.motionY = MathUtility.randomizeFloat(-0.1F, 0.1F);
      this.motionX = MathUtility.randomizeFloat(-0.1F, 0.1F);
   }

   public void draw(List<Particle> particles, int mouseX, int mouseY) {
      RenderUtility.drawRoundCircle(this.x, this.y, 5.0F, 4.0F, (new Color(255, 255, 255, 150)).getRGB());
      particles.forEach((particle) -> {
         if (particle != this && MathUtility.getDistance((double)this.x, (double)this.y, (double)particle.x, (double)particle.y) < 50.0D) {
            RenderUtility.drawLine((double)this.x, (double)this.y, (double)particle.x, (double)particle.y, 2.0F, (new Color(255, 255, 255, 15)).getRGB());
         }

      });
   }

   public void update(int mouseX, int mouseY, int scaledWidth, int scaledHeight) {
      this.x += this.motionX;
      this.y += this.motionY;
      this.motionX = (float)((double)this.motionX * 0.99D);
      this.motionY = (float)((double)this.motionY * 0.99D);
      if ((double)Math.abs(this.motionX) < 0.1D) {
         this.motionX += MathUtility.randomizeFloat(-0.01F, 0.01F);
      }

      if ((double)Math.abs(this.motionY) < 0.1D) {
         this.motionY += MathUtility.randomizeFloat(-0.01F, 0.01F);
      }

      if (Mouse.isButtonDown(0) && MathUtility.getDistance((double)this.x, (double)this.y, (double)mouseX, (double)mouseY) < 50.0D) {
         this.motionX += ((float)mouseX - this.x) / 500.0F;
         this.motionY += ((float)mouseY - this.y) / 500.0F;
      }

      if (this.x < 0.0F) {
         this.x = (float)scaledWidth;
      }

      if (this.x > (float)scaledWidth) {
         this.x = 0.0F;
      }

      if (this.y < 0.0F) {
         this.y = (float)scaledHeight;
      }

      if (this.y > (float)scaledHeight) {
         this.y = 0.0F;
      }

   }
}

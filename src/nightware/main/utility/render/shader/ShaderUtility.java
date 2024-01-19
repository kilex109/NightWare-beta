package nightware.main.utility.render.shader;

import nightware.main.NightWare;
import nightware.main.utility.Utility;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;

public class ShaderUtility implements Utility {
   public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
      if (framebuffer != null && framebuffer.framebufferWidth == mc.displayWidth && framebuffer.framebufferHeight == mc.displayHeight) {
         return framebuffer;
      } else {
         if (framebuffer != null) {
            framebuffer.deleteFramebuffer();
         }

         return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
      }
   }

   public static void bindTexture(int texture) {
      GlStateManager.bindTexture(texture);
   }

   public static void drawQuads() {
      ScaledResolution sr = new ScaledResolution(mc);
      float width = sr.getScaledWidth();
      float height = sr.getScaledHeight();
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2f(0.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2f(0.0F, height);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2f(width, height);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2f(width, 0.0F);
      GL11.glEnd();
   }
}

package nightware.main.utility.render;

import nightware.main.utility.Utility;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class StencilUtility implements Utility {
   public static void checkSetupFBO(Framebuffer framebuffer) {
      if (framebuffer != null && framebuffer.depthBuffer > -1) {
         setupFBO(framebuffer);
         framebuffer.depthBuffer = -1;
      }

   }

   public static void setupFBO(Framebuffer framebuffer) {
      EXTFramebufferObject.glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
      int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
      EXTFramebufferObject.glBindRenderbufferEXT(36161, stencilDepthBufferID);
      EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, mc.displayWidth, mc.displayHeight);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencilDepthBufferID);
      EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencilDepthBufferID);
   }

   public static void doDispose() {
      GL11.glDisable(2960);
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
   }

   public static void erase(boolean invert) {
      GL11.glStencilFunc(invert ? 514 : 517, 1, 65535);
      GL11.glStencilOp(7680, 7680, 7681);
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GL11.glAlphaFunc(516, 0.0F);
   }

   public static void write(boolean renderClipLayer) {
      checkSetupFBO(mc.getFramebuffer());
      GL11.glClearStencil(0);
      GL11.glClear(1024);
      GL11.glEnable(2960);
      GL11.glStencilFunc(519, 1, 65535);
      GL11.glStencilOp(7680, 7680, 7681);
      if (!renderClipLayer) {
         GlStateManager.colorMask(false, false, false, false);
      }

   }

   public static void write() {
      mc.getFramebuffer().bindFramebuffer(false);
      checkSetupFBO(mc.getFramebuffer());
      GL11.glClear(1024);
      GL11.glEnable(2960);
      GL11.glStencilFunc(519, 1, 1);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glColorMask(false, false, false, false);
   }

   public static void initStencilToWrite() {
      mc.getFramebuffer().bindFramebuffer(false);
      checkSetupFBO(mc.getFramebuffer());
      GL11.glClear(1024);
      GL11.glEnable(2960);
      GL11.glStencilFunc(519, 1, 1);
      GL11.glStencilOp(7681, 7681, 7681);
      GL11.glColorMask(false, false, false, false);
   }

   public static void readStencilBuffer(int ref) {
      GL11.glColorMask(true, true, true, true);
      GL11.glStencilFunc(514, ref, 1);
      GL11.glStencilOp(7680, 7680, 7680);
   }

   public static void uninitStencilBuffer() {
      GL11.glDisable(2960);
   }
}

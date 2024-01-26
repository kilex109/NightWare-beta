package nightware.main.utility.render.blur;

import lombok.Getter;
import nightware.main.utility.Utility;
import nightware.main.utility.render.RenderUtility;
import nightware.main.utility.render.shader.Shader;
import nightware.main.utility.render.shader.ShaderUtility;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL20.glUniform1;

public class GaussianBlur implements Utility {

   @Getter
   public static Shader blurShader = new Shader("nightware/shaders/gaussian.frag", true);

   @Getter
   public static Framebuffer framebuffer = new Framebuffer(1, 1, false);


   public static void setupUniforms(float dir1, float dir2, float radius) {
      blurShader.setupUniform1i("textureIn", 0);
      blurShader.setupUniform2f("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
      blurShader.setupUniform2f("direction", dir1, dir2);
      blurShader.setupUniform1f("radius", radius);

      final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
      for (int i = 0; i <= radius; i++) {
         weightBuffer.put(calculateGaussianValue(i, radius / 2));
      }

      weightBuffer.rewind();
      glUniform1(blurShader.getUniform("weights"), weightBuffer);
   }




   public static void renderBlur(float radius) {
      GlStateManager.enableBlend();
      GlStateManager.color(1, 1, 1, 1);
      OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);


      framebuffer = RenderUtility.createFrameBuffer(framebuffer);

      framebuffer.framebufferClear();
      framebuffer.bindFramebuffer(true);
      blurShader.useProgram();
      setupUniforms(1, 0, radius);

      RenderUtility.bindTexture(mc.getFramebuffer().framebufferTexture);

      ShaderUtility.drawQuads();
      framebuffer.unbindFramebuffer();
      blurShader.unloadProgram();

      mc.getFramebuffer().bindFramebuffer(true);
      blurShader.useProgram();
      setupUniforms(0, 1, radius);

      RenderUtility.bindTexture(framebuffer.framebufferTexture);
      ShaderUtility.drawQuads();
      blurShader.unloadProgram();

      RenderUtility.resetColor();
      GlStateManager.bindTexture(0);
   }

   public static float calculateGaussianValue(float x, float sigma) {
      double PI = 3.141592653;
      double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
      return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
   }
}

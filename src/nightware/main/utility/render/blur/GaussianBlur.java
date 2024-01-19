package nightware.main.utility.render.blur;

import nightware.main.utility.Utility;
import nightware.main.utility.render.shader.Shader;
import nightware.main.utility.render.shader.ShaderUtility;
import java.nio.FloatBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class GaussianBlur implements Utility {
   public static Shader blurShader = new Shader("nightware/shaders/gaussian.fsh", true);
   public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

   public static void setupUniforms(float dir1, float dir2, float radius) {
      blurShader.setupUniform1i("textureIn", 0);
      blurShader.setupUniform2f("texelSize", 1.0F / (float)mc.displayWidth, 1.0F / (float)mc.displayHeight);
      blurShader.setupUniform2f("direction", dir1, dir2);
      blurShader.setupUniform1f("radius", radius);
      FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);

      for(int i = 0; (float)i <= radius; ++i) {
         weightBuffer.put(calculateGaussianValue((float)i, radius / 2.0F));
      }

      weightBuffer.rewind();
      GL20.glUniform1(blurShader.getUniform("weights"), weightBuffer);
   }

   public static void renderBlur(float radius) {
      GlStateManager.enableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      framebuffer = ShaderUtility.createFrameBuffer(framebuffer);
      framebuffer.framebufferClear();
      framebuffer.bindFramebuffer(true);
      blurShader.useProgram();
      setupUniforms(1.0F, 0.0F, radius);
      ShaderUtility.bindTexture(mc.getFramebuffer().framebufferTexture);
      ShaderUtility.drawQuads();
      framebuffer.unbindFramebuffer();
      blurShader.unloadProgram();
      mc.getFramebuffer().bindFramebuffer(true);
      blurShader.useProgram();
      setupUniforms(0.0F, 1.0F, radius);
      ShaderUtility.bindTexture(framebuffer.framebufferTexture);
      ShaderUtility.drawQuads();
      blurShader.unloadProgram();
      GlStateManager.resetColor();
      GlStateManager.bindTexture(0);
   }

   public static float calculateGaussianValue(float x, float sigma) {
      double PI = 3.141592653D;
      double output = 1.0D / Math.sqrt(2.0D * PI * (double)(sigma * sigma));
      return (float)(output * Math.exp((double)(-(x * x)) / (2.0D * (double)(sigma * sigma))));
   }
}

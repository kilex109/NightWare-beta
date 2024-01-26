package nightware.main.utility.render;

import com.jhlabs.image.GaussianFilter;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.shader.Framebuffer;
import nightware.main.NightWare;
import nightware.main.manager.theme.Themes;
import nightware.main.module.impl.util.Optimizer;
import nightware.main.utility.Utility;
import nightware.main.utility.misc.DiscordPresence;
import nightware.main.utility.render.blur.GaussianBlur;
import nightware.main.utility.render.shader.Shader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtility implements Utility {
   private static final Tessellator TESSELLATOR = Tessellator.getInstance();
   private static final BufferBuilder BUILDER;
   private static final Shader ROUNDED_GRADIENT;
   private static final Shader ROUNDED;
   private static final Shader GRADIENT_MASK;
   private static final Shader ROUNDED_TEXTURE;
   private static final HashMap<Integer, Integer> SHADOW_CACHE;
   public static Frustum FRUSTUM;
   private static int PROFILE_ID;

   public static Vec3d vectorTo2D(int scaleFactor, double x, double y, double z) {
      float xPos = (float) x;
      float yPos = (float) y;
      float zPos = (float) z;
      IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
      FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
      FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
      FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
      GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
      GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
      GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
      if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector))
         return new Vec3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor),
                 vector.get(2));
      return null;
   }

   public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
      if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
         if (framebuffer != null) {
            framebuffer.deleteFramebuffer();
         }
         return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
      }
      return framebuffer;
   }

   public static void bindTexture(int texture) {
      glBindTexture(GL_TEXTURE_2D, texture);
   }

   public static void resetColor() {
      color(1, 1, 1, 1);
   }

   public static final void color(double red, double green, double blue, double alpha) {
      GL11.glColor4d(red, green, blue, alpha);
   }

   public static void blockEspFrame(BlockPos blockPos, double red, double green, double blue) {
      double d = blockPos.getX();
      Minecraft.getMinecraft().getRenderManager();
      double x = d - Minecraft.getMinecraft().getRenderManager().viewerPosX;
      double d2 = blockPos.getY();
      Minecraft.getMinecraft().getRenderManager();
      double y = d2 - Minecraft.getMinecraft().getRenderManager().viewerPosY;
      double d3 = blockPos.getZ();
      Minecraft.getMinecraft().getRenderManager();
      double z = d3 - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(1.0f);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      GL11.glColor4d(red, green, blue, 1);
      drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glColor4f(1, 1, 1, 1);
   }

   public static void drawRect(float x, float y, float x2, float y2, int color) {
      drawGradientRect(x, y, x2, y2, color, color, color, color);
   }

   public static void drawTriangle(int red, int green, int blue) {
      boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
      if (needBlend)
         GL11.glEnable(GL11.GL_BLEND);
      int alpha = 255;
      int red_2 = Math.max(red - 40, 0);
      int green_2 = Math.max(green - 40, 0);
      int blue_2 = Math.max(blue - 40, 0);
      float width = 6, height = 12;
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
      GL11.glShadeModel(GL11.GL_SMOOTH);
      GL11.glBegin(GL11.GL_POLYGON);
      GL11.glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
      GL11.glVertex2d(0, 0 - height);
      GL11.glVertex2d(0 - width, 0);
      GL11.glVertex2d(0, 0 - 3);
      GL11.glEnd();
      GL11.glBegin(GL11.GL_POLYGON);
      GL11.glColor4f(red_2 / 255f, green_2 / 255f, blue_2 / 255f, alpha / 255f);
      GL11.glVertex2d(0, 0 - height);
      GL11.glVertex2d(0, 0 - 3);
      GL11.glVertex2d(0 + width, 0);
      GL11.glEnd();
      GL11.glShadeModel(GL11.GL_FLAT);
      GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      if (needBlend)
         GL11.glDisable(GL11.GL_BLEND);
   }

   public static void drawTriangle(double x, double y, double width, double height, double rotation) {
      boolean needBlend = !GL11.glIsEnabled(GL11.GL_BLEND);
      if (needBlend)
         GL11.glEnable(GL11.GL_BLEND);
      int alpha = 255;
      int red_1 = 255;
      int green_1 = 255;
      int blue_1 = 255;
      int red_2 = Math.max(red_1 - 40, 0);
      int green_2 = Math.max(green_1 - 40, 0);
      int blue_2 = Math.max(blue_1 - 40, 0);
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
      GL11.glShadeModel(GL11.GL_SMOOTH);
      GL11.glBegin(GL11.GL_POLYGON);
      GL11.glColor4f(red_1 / 255f, green_1 / 255f, blue_1 / 255f, alpha / 255f);
      GL11.glVertex2d(x, y - height);
      GL11.glVertex2d(x - width, y);
      GL11.glVertex2d(x, y - 3);
      GL11.glEnd();
      GL11.glBegin(GL11.GL_POLYGON);
      GL11.glColor4f(red_2 / 255f, green_2 / 255f, blue_2 / 255f, alpha / 255f);
      GL11.glVertex2d(x, y - height);
      GL11.glVertex2d(x, y - 3);
      GL11.glVertex2d(x + width, y);
      GL11.glEnd();
      GL11.glShadeModel(GL11.GL_FLAT);
      GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      if (needBlend)
         GL11.glDisable(GL11.GL_BLEND);
   }

   public static void drawTriangle(float x, float y, float vector, float size, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, 0);
      GL11.glRotatef(180 + vector, 0F, 0F, 1.0F);
      float alpha = (float) (color >> 24 & 255) / 255.0F;
      float red = (float) (color >> 16 & 255) / 255.0F;
      float green = (float) (color >> 8 & 255) / 255.0F;
      float blue = (float) (color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
      GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
      buffer.pos(0, 3.1 * size, 0).endVertex();
      buffer.pos(2 * size, -1 * size, 0).endVertex();
      buffer.pos(-2.3 * size, -1 * size, 0).endVertex();
      tessellator.draw();
      GL11.glDisable(GL11.GL_LINE_SMOOTH);
      GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glRotatef(-180 - vector, 0F, 0F, 1.0F);
      GL11.glTranslated(-x, -y, 0);
      GlStateManager.resetColor();
      GL11.glPopMatrix();
   }

   public static void drawTriangle(float x, float y, float vector, int color) {
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, 0);
      GL11.glRotatef(180 + vector, 0F, 0F, 1.0F);
      float alpha = (float) (color >> 24 & 255) / 255.0F;
      float red = (float) (color >> 16 & 255) / 255.0F;
      float green = (float) (color >> 8 & 255) / 255.0F;
      float blue = (float) (color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
      GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      float size = 0.9f;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.getBuffer();
      buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
      buffer.pos(0, 3.1 * size, 0).endVertex();
      buffer.pos(2 * size, -1 * size, 0).endVertex();
      buffer.pos(-2.3 * size, -1 * size, 0).endVertex();
      tessellator.draw();
      GL11.glDisable(GL11.GL_LINE_SMOOTH);
      GL11.glDisable(GL11.GL_POLYGON_SMOOTH);

      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glRotatef(-180 - vector, 0F, 0F, 1.0F);
      GL11.glTranslated(-x, -y, 0);
      GlStateManager.resetColor();
      GL11.glPopMatrix();
   }

   public static void drawCircle(float x, float y, float radius, int color) {
      float f = (float) (color >> 24 & 255) / 255.0F;
      float f1 = (float) (color >> 16 & 255) / 255.0F;
      float f2 = (float) (color >> 8 & 255) / 255.0F;
      float f3 = (float) (color & 255) / 255.0F;
      boolean flag = GL11.glIsEnabled(GL11.GL_BLEND);
      boolean flag1 = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
      boolean flag2 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

      if (!flag) {
         GL11.glEnable(GL11.GL_BLEND);
      }

      if (!flag1) {
         GL11.glEnable(GL11.GL_LINE_SMOOTH);
      }

      if (flag2) {
         GL11.glDisable(GL11.GL_TEXTURE_2D);
      }
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);


      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(GL11.GL_POLYGON);

      for (int i = 0; i <= 360; ++i) {
         GL11.glVertex2d((double) x + Math.sin((double) i * Math.PI / 180.0D) * (double) radius, (double) y + Math.cos((double) i * Math.PI / 180.0D) * (double) radius);
      }

      GL11.glEnd();
      GL11.glDisable(GL11.GL_LINE_SMOOTH);

      if (flag2) {
         GL11.glEnable(GL11.GL_TEXTURE_2D);
      }

      if (!flag1) {
         GL11.glDisable(GL11.GL_LINE_SMOOTH);
      }

      if (!flag) {
         GL11.glDisable(GL11.GL_BLEND);
      }
      GlStateManager.resetColor();
   }

   private static ICamera camera = new Frustum();

   public static void drawBoxESP(final AxisAlignedBB pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final int outlineAlpha) {
      final AxisAlignedBB bb = new AxisAlignedBB(pos.minX - mc.getRenderManager().viewerPosX, pos.minY - mc.getRenderManager().viewerPosY, pos.minZ - mc.getRenderManager().viewerPosZ, pos.maxX - mc.getRenderManager().viewerPosX, pos.maxY - mc.getRenderManager().viewerPosY, pos.maxZ - mc.getRenderManager().viewerPosZ);
      camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
      if (camera.isBoundingBoxInFrustum(pos)) {
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GlStateManager.disableDepth();
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         GlStateManager.disableTexture2D();
         GlStateManager.depthMask(false);
         GL11.glEnable(2848);
         GL11.glHint(3154, 4354);
         GL11.glLineWidth(lineWidth);
         if (box) {
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, boxAlpha / 255.0f);
         }
         if (outline) {
            RenderGlobal.drawBoundingBox(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, outlineAlpha / 255.0f);
         }
         GL11.glDisable(2848);
         GlStateManager.depthMask(true);
         GlStateManager.enableDepth();
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
      }
   }

   public static void drawBlur(Runnable data, float radius) {
      StencilUtility.initStencilToWrite();
      data.run();
      StencilUtility.readStencilBuffer(1);
      GaussianBlur.renderBlur(radius);
      StencilUtility.uninitStencilBuffer();
   }

   public static void applyRound(float width, float height, float round, float alpha, Runnable runnable) {
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.disableAlpha();
      ROUNDED_TEXTURE.useProgram();
      ROUNDED_TEXTURE.setupUniform2f("size", (width - round) * 2.0F, (height - round) * 2.0F);
      ROUNDED_TEXTURE.setupUniform1f("round", round);
      ROUNDED_TEXTURE.setupUniform1f("alpha", alpha);
      runnable.run();
      ROUNDED_TEXTURE.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void drawCircle3D(double posX, double posY, double posZ, float radius, int color) {

      double[] position = {
              posX - Minecraft.getMinecraft().getRenderManager().viewerPosX,
              posY - Minecraft.getMinecraft().getRenderManager().viewerPosY,
              posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ
      };
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableCull();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.disableDepth();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      GL11.glLineWidth(4);
      GL11.glPushMatrix();

      GlStateManager.translate(position[0], position[1], position[2]);

      GL11.glBegin(2);
      glColor(color);

      for (int i = 0; i <= 360; i++) {
         GL11.glVertex3d(Math.sin(i * Math.PI / 180) * radius, 0, Math.cos(i * Math.PI / 180) * radius);
      }
      GL11.glEnd();
            /*
            GL11.glBegin(2);
            for(int i = 0; i <= 360; i++) {
                int[] colors = Client.instance.getClientColors((float) (i * 1.1f), 100);
                RenderUtils.glColor(colors[0]);
                GL11.glVertex3d(Math.sin(i * Math.PI / 180) * .3f * anim.getAnim2(), 0, Math.cos(i * Math.PI / 180) * .3f * anim.getAnim2());
            }
            GL11.glEnd();

             */

      GL11.glPopMatrix();
      GlStateManager.enableAlpha();
      GlStateManager.enableDepth();
      GlStateManager.enableTexture2D();
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GL11.glPopMatrix();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      glColor(-1);
      GL11.glLineWidth(1);
   }

   public static void glColor(final int hex) {
      final float alpha = (hex >> 24 & 0xFF) / 255F;
      final float red = (hex >> 16 & 0xFF) / 255F;
      final float green = (hex >> 8 & 0xFF) / 255F;
      final float blue = (hex & 0xFF) / 255F;

      GlStateManager.color(red, green, blue, alpha);
   }

   public static void drawProfile(float x, float y, float x2, float y2) {
      if (PROFILE_ID != -1337) {
         GlStateManager.bindTexture(PROFILE_ID);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         allocTextureRectangle(x, y, x2, y2);
         GlStateManager.bindTexture(0);
      } else {
         boolean isDark = NightWare.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
         drawRoundedRect(x, y, x2, y2, x2 - 1.0F, isDark ? (new Color(50, 50, 50)).getRGB() : (new Color(100, 100, 100)).getRGB());
         if (DiscordPresence.avatar != null) {
            PROFILE_ID = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), DiscordPresence.avatar, true, false);
         }
      }

   }

   public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
      drawRoundedGradientRect(x, y, x2, y2, round, 1.0F, color, color, color, color);
   }

   public static void drawCRoundedRect(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, int color) {
      CorneredGradient(x, y, x2, y2, round1, round2, round3, round4, 1.0F, color, color, color, color);
   }

   public static void drawRoundCircle(float x, float y, float size, float radius, int color) {
      drawRoundedRect(x - size / 2.0F, y - size / 2.0F, size, size, radius, color);
   }

   public static void applyGradientMask(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content) {
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GRADIENT_MASK.useProgram();
      GRADIENT_MASK.setupUniform2f("location", x * 2.0F, (float) Minecraft.getMinecraft().displayHeight - height * 2.0F - y * 2.0F);
      GRADIENT_MASK.setupUniform2f("rectSize", width * 2.0F, height * 2.0F);
      GRADIENT_MASK.setupUniform1f("alpha", alpha);
      GRADIENT_MASK.setupUniform3f("color1", (float) bottomLeft.getRed() / 255.0F, (float) bottomLeft.getGreen() / 255.0F, (float) bottomLeft.getBlue() / 255.0F);
      GRADIENT_MASK.setupUniform3f("color2", (float) topLeft.getRed() / 255.0F, (float) topLeft.getGreen() / 255.0F, (float) topLeft.getBlue() / 255.0F);
      GRADIENT_MASK.setupUniform3f("color3", (float) bottomRight.getRed() / 255.0F, (float) bottomRight.getGreen() / 255.0F, (float) bottomRight.getBlue() / 255.0F);
      GRADIENT_MASK.setupUniform3f("color4", (float) topRight.getRed() / 255.0F, (float) topRight.getGreen() / 255.0F, (float) topRight.getBlue() / 255.0F);
      content.run();
      GRADIENT_MASK.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void drawRectWithOutline(float x, float y, float width, float height, int color, int outlineColor) {
      drawRect(x - 0.5F, y - 0.5F, width + 1.0F, height + 1.0F, outlineColor);
      drawRect(x, y, width, height, color);
   }

   public static void drawRectNoWH(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float) (color >> 24 & 255) / 255.0F;
      float f = (float) (color >> 16 & 255) / 255.0F;
      float f1 = (float) (color >> 8 & 255) / 255.0F;
      float f2 = (float) (color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.color(f, f1, f2, f3);
      bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferbuilder.pos(left, bottom, 0.0D).endVertex();
      bufferbuilder.pos(right, bottom, 0.0D).endVertex();
      bufferbuilder.pos(right, top, 0.0D).endVertex();
      bufferbuilder.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawLine(double x, double y, double x1, double y1, float width, int color) {
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GL11.glEnable(2848);
      GL11.glLineWidth(width);
      ColorUtility.setColor(color);
      GL11.glBegin(1);
      GL11.glVertex2d(x, y);
      GL11.glVertex2d(x1, y1);
      GL11.glEnd();
      GlStateManager.resetColor();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }

   public static void drawLeftSideRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color2);
      float[] c3 = ColorUtility.getRGBAf(color3);
      float[] c4 = ColorUtility.getRGBAf(color4);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", 0.0F, 0.0F, round, round);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", value);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void drawBlockBox(BlockPos blockPos, Color color, int alpha) {
      double x = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * (double) mc.timer.field_194147_b;
      double y = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * (double) mc.timer.field_194147_b;
      double z = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * (double) mc.timer.field_194147_b;
      mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
      GL11.glPushMatrix();
      AntiAliasingUtility.hook(true, false, false);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(1.0F);
      GL11.glDisable(3553);
      GL11.glDisable(2929);
      GlStateManager.color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) alpha / 255.0F);
      drawSelectionBoundingBox(mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos).grow(0.0020000000949949026D).offset(-x, -y, -z));
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(3042);
      AntiAliasingUtility.unhook(true, false, false);
      GL11.glPopMatrix();
   }

   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder vertexbuffer = tessellator.getBuffer();
      vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
      tessellator.draw();
      vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
      tessellator.draw();
      vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
      vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
      vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
      tessellator.draw();
   }

   public static void drawLeftSideRoundedRect(float x, float y, float x2, float y2, float round, int color) {
      drawLeftSideRoundedGradientRect(x, y, x2, y2, round, 1.0F, color, color, color, color);
   }

   public static void drawHeader(float x, float y, float width, float height) {
      drawGradientRect(x + width / 2.0F, y, width / 2.0F, height, (new Color(11974326)).getRGB(), 3, 3, (new Color(11974326)).getRGB());
      drawGradientRect(x, y, width / 2.0F, height, 3, (new Color(11974326)).getRGB(), (new Color(11974326)).getRGB(), 3);
   }

   public static void drawRoundedRect(float x, float y, float x2, float y2, float round, float swapX, float swapY, int firstColor, int secondColor) {
      float[] c = ColorUtility.getRGBAf(firstColor);
      float[] c1 = ColorUtility.getRGBAf(secondColor);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED.useProgram();
      ROUNDED.setupUniform2f("size", (x2 - round) * 2.0F, (y2 - round) * 2.0F);
      ROUNDED.setupUniform1f("round", round);
      ROUNDED.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED.setupUniform2f("swap", swapX, swapY);
      ROUNDED.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
      ROUNDED.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void drawVGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
      float f = (float) (startColor >> 24 & 255) / 255.0F;
      float f1 = (float) (startColor >> 16 & 255) / 255.0F;
      float f2 = (float) (startColor >> 8 & 255) / 255.0F;
      float f3 = (float) (startColor & 255) / 255.0F;
      float f4 = (float) (endColor >> 24 & 255) / 255.0F;
      float f5 = (float) (endColor >> 16 & 255) / 255.0F;
      float f6 = (float) (endColor >> 8 & 255) / 255.0F;
      float f7 = (float) (endColor & 255) / 255.0F;
      GlStateManager.disableTexture2D();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.shadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d((double) left, (double) top);
      GL11.glVertex2d((double) right, (double) top);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d((double) right, (double) bottom);
      GL11.glVertex2d((double) left, (double) bottom);
      GL11.glEnd();
      GL11.glPopMatrix();
      GlStateManager.resetColor();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
   }

   public static void drawGradientRect(float x, float y, float width, float height, int color1, int color2, int color3, int color4) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color2);
      float[] c3 = ColorUtility.getRGBAf(color3);
      float[] c4 = ColorUtility.getRGBAf(color4);
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      BUILDER.begin(7, DefaultVertexFormats.POSITION_COLOR);
      BUILDER.pos((double) x, (double) (height + y), 0.0D).color(c1[0], c1[1], c1[2], c1[3]).endVertex();
      BUILDER.pos((double) (width + x), (double) (height + y), 0.0D).color(c2[0], c2[1], c2[2], c2[3]).endVertex();
      BUILDER.pos((double) (width + x), (double) y, 0.0D).color(c3[0], c3[1], c3[2], c3[3]).endVertex();
      BUILDER.pos((double) x, (double) y, 0.0D).color(c4[0], c4[1], c4[2], c4[3]).endVertex();
      TESSELLATOR.draw();
      GlStateManager.shadeModel(7424);
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color2);
      float[] c3 = ColorUtility.getRGBAf(color3);
      float[] c4 = ColorUtility.getRGBAf(color4);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", round, round, round, round);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", value);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void drawItemStack(ItemStack itemStack, int x, int y) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.enableDepth();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
      mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, x, y);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableDepth();
   }

   public static void CorneredGradient(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, float value, int color1, int color2, int color3, int color4) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color2);
      float[] c3 = ColorUtility.getRGBAf(color3);
      float[] c4 = ColorUtility.getRGBAf(color4);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", value);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void CorneredGradient(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, int color1, int color2, int color3, int color4) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color2);
      float[] c3 = ColorUtility.getRGBAf(color3);
      float[] c4 = ColorUtility.getRGBAf(color4);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", 1);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void Cornered(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, float value, int color1) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color1);
      float[] c3 = ColorUtility.getRGBAf(color1);
      float[] c4 = ColorUtility.getRGBAf(color1);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", value);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   public static void Cornered(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, int color1) {
      float[] c1 = ColorUtility.getRGBAf(color1);
      float[] c2 = ColorUtility.getRGBAf(color1);
      float[] c3 = ColorUtility.getRGBAf(color1);
      float[] c4 = ColorUtility.getRGBAf(color1);
      GlStateManager.color(0.0F, 0.0F, 0.0F, 0.0F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      ROUNDED_GRADIENT.useProgram();
      ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
      ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
      ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
      ROUNDED_GRADIENT.setupUniform1f("value", 1);
      ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
      ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
      ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
      ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
      allocTextureRectangle(x, y, x2, y2);
      ROUNDED_GRADIENT.unloadProgram();
      GlStateManager.disableBlend();
   }

   private static final HashMap<Integer, Integer> shadowCache = new HashMap<>();

   public static void drawGlow(float x, float y, float width, float height, int glowRadius, Color color) {
      if (Optimizer.a) {
         return;
      }
      BufferedImage original = null;
      GaussianFilter op = null;
      glPushMatrix();
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
      width = width + glowRadius * 2;
      height = height + glowRadius * 2;
      x = x - glowRadius;
      y = y - glowRadius;
      float _X = x - 0.25f;
      float _Y = y + 0.25f;
      int identifier = String.valueOf(width * height + width + 1000000000 * glowRadius + glowRadius).hashCode();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      glDisable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_ALPHA_TEST);
      GlStateManager.enableBlend();
      int texId = -1;
      if (shadowCache.containsKey(identifier)) {
         texId = shadowCache.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0) {
            width = 1;
         }
         if (height <= 0) {
            height = 1;
         }
         original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
         Graphics g = original.getGraphics();
         g.setColor(Color.white);
         g.fillRect(glowRadius, glowRadius, (int) (width - glowRadius * 2), (int) (height - glowRadius * 2));
         g.dispose();
         op = new GaussianFilter(glowRadius);
         BufferedImage blurred = op.filter(original, null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         shadowCache.put(identifier, texId);
      }
      ColorUtility.setColor(color);
      GL11.glBegin(GL11.GL_QUADS);
      GL11.glTexCoord2f(0, 0); // top left
      GL11.glVertex2f(_X, _Y);
      GL11.glTexCoord2f(0, 1); // bottom left
      GL11.glVertex2f(_X, _Y + height);
      GL11.glTexCoord2f(1, 1); // bottom right
      GL11.glVertex2f(_X + width, _Y + height);
      GL11.glTexCoord2f(1, 0); // top right
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
      GlStateManager.resetColor();
      glEnable(GL_CULL_FACE);
      glPopMatrix();
   }

   public static void drawFixedGlow(float x, float y, float width, float height, int glowRadius, int color) {
      if (Optimizer.a) {
         return;
      }
      BufferedImage original = null;
      GaussianFilter op = null;
      glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
              GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
              GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
      width = width + glowRadius * 2;
      height = height + glowRadius * 2;
      x = x - glowRadius;
      y = y - glowRadius;
      float _X = x - 0.25f;
      float _Y = y + 0.25f;
      int identifier = String.valueOf(width * height + width + Math.sin(glowRadius / width * height) * glowRadius + glowRadius).hashCode();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      glDisable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_ALPHA_TEST);
      GlStateManager.enableBlend();
      int texId = -1;
      if (shadowCache.containsKey(identifier)) {
         texId = shadowCache.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0) {
            width = 1;
         }
         if (height <= 0) {
            height = 1;
         }
         original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
         Graphics g = original.getGraphics();
         g.setColor(Color.white);
         g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
         g.dispose();
         op = new GaussianFilter(glowRadius);
         BufferedImage blurred = op.filter(original, null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         shadowCache.put(identifier, texId);
      }
      GL11.glBegin(GL11.GL_QUADS);
      ColorUtility.setColor(color);
      GL11.glTexCoord2f(0, 0); // top left
      GL11.glVertex2f(_X, _Y);
      ColorUtility.setColor(color);
      GL11.glTexCoord2f(0, 1); // bottom left
      GL11.glVertex2f(_X, _Y + height);
      ColorUtility.setColor(color);
      GL11.glTexCoord2f(1, 1); // bottom right
      GL11.glVertex2f(_X + width, _Y + height);
      ColorUtility.setColor(color);
      GL11.glTexCoord2f(1, 0); // top right
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.resetColor();
      glEnable(GL_CULL_FACE);
      glPopMatrix();
   }

   public static void drawGradientGlow(float x, float y, float width, float height, int glowRadius, int color1, int color2, int color3, int color4) {
      if (Optimizer.a) {
         return;
      }
      BufferedImage original = null;
      GaussianFilter op = null;
      glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
              GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
              GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
      width = width + glowRadius * 2;
      height = height + glowRadius * 2;
      x = x - glowRadius;
      y = y - glowRadius;
      float _X = x - 0.25f;
      float _Y = y + 0.25f;
      int identifier = String.valueOf(width * height + width + Math.sin(glowRadius / width * height) * glowRadius + glowRadius).hashCode();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      glDisable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_ALPHA_TEST);
      GlStateManager.enableBlend();
      int texId = -1;
      if (shadowCache.containsKey(identifier)) {
         texId = shadowCache.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0) {
            width = 1;
         }
         if (height <= 0) {
            height = 1;
         }
         original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
         Graphics g = original.getGraphics();
         g.setColor(Color.white);
         g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
         g.dispose();
         op = new GaussianFilter(glowRadius);
         BufferedImage blurred = op.filter(original, null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         shadowCache.put(identifier, texId);
      }
      GL11.glBegin(GL11.GL_QUADS);
      ColorUtility.setColor(color1);
      GL11.glTexCoord2f(0, 0); // top left
      GL11.glVertex2f(_X, _Y);
      ColorUtility.setColor(color2);
      GL11.glTexCoord2f(0, 1); // bottom left
      GL11.glVertex2f(_X, _Y + height);
      ColorUtility.setColor(color4);
      GL11.glTexCoord2f(1, 1); // bottom right
      GL11.glVertex2f(_X + width, _Y + height);
      ColorUtility.setColor(color3);
      GL11.glTexCoord2f(1, 0); // top right
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.resetColor();
      glEnable(GL_CULL_FACE);
      glPopMatrix();
   }

   public static void drawHorizontalGlow(float x, float y, float width, float height, int glowRadius, int color1, int color2) {
      if (Optimizer.a) {
         return;
      }
      BufferedImage original = null;
      GaussianFilter op = null;
      glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
              GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
              GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01f);
      width = width + glowRadius * 2;
      height = height + glowRadius * 2;
      x = x - glowRadius;
      y = y - glowRadius;
      float _X = x - 0.25f;
      float _Y = y + 0.25f;
      int identifier = Objects.hash(width, height, glowRadius);
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      glDisable(GL11.GL_CULL_FACE);
      GL11.glEnable(GL11.GL_ALPHA_TEST);
      GlStateManager.enableBlend();
      int texId = -1;
      if (shadowCache.containsKey(identifier)) {
         texId = shadowCache.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0) {
            width = 1;
         }
         if (height <= 0) {
            height = 1;
         }
         original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
         Graphics g = original.getGraphics();
         g.setColor(Color.white);
         g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
         g.dispose();

         op = new GaussianFilter(glowRadius);

         BufferedImage blurred = op.filter(original, null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         shadowCache.put(identifier, texId);
      }
      GL11.glBegin(GL11.GL_QUADS);
      ColorUtility.setColor(color1);
      GL11.glTexCoord2f(0, 0); // top left
      GL11.glVertex2f(_X, _Y);
      GL11.glTexCoord2f(0, 1); // bottom left
      GL11.glVertex2f(_X, _Y + height);
      ColorUtility.setColor(color2);
      GL11.glTexCoord2f(1, 1); // bottom right
      GL11.glVertex2f(_X + width, _Y + height);
      GL11.glTexCoord2f(1, 0); // top right
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.resetColor();
      glEnable(GL_CULL_FACE);
      glPopMatrix();
   }


   public static void drawGradientGlow(float x, float y, float width, float height, int glowRadius, Color color1, Color color2) {
      if (Optimizer.a) {
         return;
      }
      BufferedImage original = null;
      GaussianFilter op = null;
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      GlStateManager.alphaFunc(516, 0.01F);
      width += (float) (glowRadius * 2);
      height += (float) (glowRadius * 2);
      x -= (float) glowRadius;
      y -= (float) glowRadius;
      float _X = x - 0.25F;
      float _Y = y + 0.25F;
      int identifier = (int) (width * height * (float) glowRadius);
      GL11.glEnable(3553);
      GL11.glDisable(2884);
      GL11.glEnable(3008);
      GlStateManager.enableBlend();
      int texId;
      if (SHADOW_CACHE.containsKey(identifier)) {
         texId = (Integer) SHADOW_CACHE.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0.0F) {
            width = 1.0F;
         }

         if (height <= 0.0F) {
            height = 1.0F;
         }

         original = new BufferedImage((int) width, (int) height, 3);
         Graphics g = original.getGraphics();
         g.setColor(Color.white);
         g.fillRect(glowRadius, glowRadius, (int) width - glowRadius * 2, (int) height - glowRadius * 2);
         g.dispose();
         op = new GaussianFilter((float) glowRadius);
         BufferedImage blurred = op.filter(original, (BufferedImage) null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         SHADOW_CACHE.put(identifier, texId);
      }

      GlStateManager.resetColor();
      GL11.glBegin(7);
      ColorUtility.setColor(color1);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2f(_X, _Y);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2f(_X, _Y + height);
      ColorUtility.setColor(color2);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2f(_X + width, _Y + height);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.resetColor();
      GL11.glEnable(2884);
      GL11.glPopMatrix();
   }

   public static void drawGradientGlow(float x, float y, float width, float height, int glowRadius, Color color1, Color color2, Color color3, Color color4) {
      if (Optimizer.a) {
         return;
      }
      GL11.glPushMatrix();
      GlStateManager.disableAlpha();
      GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
      GlStateManager.shadeModel(7425);
      GlStateManager.alphaFunc(516, 0.01F);
      width += (float) (glowRadius * 2);
      height += (float) (glowRadius * 2);
      x -= (float) glowRadius;
      y -= (float) glowRadius;
      float _X = x - 0.25F;
      float _Y = y + 0.25F;
      int identifier = (int) (width * height + width + (float) (color1.hashCode() * glowRadius) + (float) glowRadius);
      GL11.glEnable(3553);
      GL11.glDisable(2884);
      GL11.glEnable(3008);
      GlStateManager.enableBlend();
      int texId;
      if (SHADOW_CACHE.containsKey(identifier)) {
         texId = (Integer) SHADOW_CACHE.get(identifier);
         GlStateManager.bindTexture(texId);
      } else {
         if (width <= 0.0F) {
            width = 1.0F;
         }

         if (height <= 0.0F) {
            height = 1.0F;
         }

         BufferedImage original = new BufferedImage((int) width, (int) height, 3);
         Graphics g = original.getGraphics();
         g.setColor(Color.WHITE);
         g.fillRect(glowRadius, glowRadius, (int) (width - (float) (glowRadius * 2)), (int) (height - (float) (glowRadius * 2)));
         g.dispose();
         GaussianFilter op = new GaussianFilter((float) glowRadius);
         BufferedImage blurred = op.filter(original, (BufferedImage) null);
         texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
         SHADOW_CACHE.put(identifier, texId);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glBegin(7);
      ColorUtility.setColor(color1);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex2f(_X, _Y);
      ColorUtility.setColor(color2);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex2f(_X, _Y + height);
      ColorUtility.setColor(color3);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex2f(_X + width, _Y + height);
      ColorUtility.setColor(color4);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex2f(_X + width, _Y);
      GL11.glEnd();
      GlStateManager.enableTexture2D();
      GlStateManager.shadeModel(7424);
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.resetColor();
      GL11.glEnable(2884);
      GL11.glPopMatrix();
   }

   public static void scaleStart(float x, float y, float scale) {
      GL11.glPushMatrix();
      GL11.glTranslatef(x, y, 0.0F);
      GL11.glScalef(scale, scale, 1.0F);
      GL11.glTranslatef(-x, -y, 0.0F);
   }

   public static void scaleEnd() {
      GL11.glPopMatrix();
   }

   public static void drawImage(ResourceLocation tex, float x, float y, float x2, float y2) {
      mc.getTextureManager().bindTexture(tex);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      allocTextureRectangle(x, y, x2, y2);
      GlStateManager.bindTexture(0);
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height, int color) {
      GlStateManager.disableDepth();
      GlStateManager.enableBlend();
      OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
      glColor(color);

      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

      GlStateManager.disableBlend();
      GlStateManager.enableDepth();
   }

   public static void allocTextureRectangle(float x, float y, float width, float height) {
      BUILDER.begin(7, DefaultVertexFormats.POSITION_TEX);
      BUILDER.pos((double) x, (double) y, 0.0D).tex(0.0D, 0.0D).endVertex();
      BUILDER.pos((double) x, (double) (y + height), 0.0D).tex(0.0D, 1.0D).endVertex();
      BUILDER.pos((double) (x + width), (double) (y + height), 0.0D).tex(1.0D, 1.0D).endVertex();
      BUILDER.pos((double) (x + width), (double) y, 0.0D).tex(1.0D, 0.0D).endVertex();
      TESSELLATOR.draw();
   }

   public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
      return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
   }

   public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
      return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
   }

   public static boolean isInViewFrustum(Entity entity) {
      return isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
   }

   private static boolean isInViewFrustum(AxisAlignedBB bb) {
      Entity current = mc.getRenderViewEntity();
      if (current != null) {
         FRUSTUM.setPosition(current.posX, current.posY, current.posZ);
      }

      return FRUSTUM.isBoundingBoxInFrustum(bb);
   }

   public static Vec3d project2D(int scaleFactor, double x, double y, double z) {
      float xPos = (float) x;
      float yPos = (float) y;
      float zPos = (float) z;
      IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
      FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
      FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
      FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
      GL11.glGetFloat(2982, modelview);
      GL11.glGetFloat(2983, projection);
      GL11.glGetInteger(2978, viewport);
      return GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector) ? new Vec3d((double) (vector.get(0) / (float) scaleFactor), (double) (((float) Display.getHeight() - vector.get(1)) / (float) scaleFactor), (double) vector.get(2)) : null;
   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   static {
      BUILDER = TESSELLATOR.getBuffer();
      ROUNDED_GRADIENT = new Shader("nightware/shaders/rounded_gradient.fsh", true);
      ROUNDED = new Shader("nightware/shaders/rounded.fsh", true);
      GRADIENT_MASK = new Shader("nightware/shaders/gradient_mask.fsh", true);
      ROUNDED_TEXTURE = new Shader("nightware/shaders/rounded_texture.fsh", true);
      SHADOW_CACHE = new HashMap();
      FRUSTUM = new Frustum();
      PROFILE_ID = -1337;
   }

   public static void renderItem(ItemStack itemStack, float x, float y) {
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.enableDepth();
      RenderHelper.enableGUIStandardItemLighting();
      Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);
      //Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
      GlStateManager.disableDepth();
   }

   public static void customScaledObject2D(float oXpos, float oYpos, float oWidth, float oHeight, float oScale) {
      GL11.glTranslated(oWidth / 2, oHeight / 2, 1);
      GL11.glTranslated(-oXpos * oScale + oXpos + oWidth / 2 * -oScale, -oYpos * oScale + oYpos + oHeight / 2 * -oScale, 1);
      GL11.glScaled(oScale, oScale, 0);
   }
}

package nightware.main.utility.render;

import com.google.common.collect.Lists;
import java.awt.Rectangle;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public final class SmartScissor {
   private static SmartScissor.State state = new SmartScissor.State();
   private static List<SmartScissor.State> stateStack = Lists.newArrayList();

   public static void push() {
      stateStack.add(state.clone());
      GL11.glPushAttrib(524288);
   }

   public static void pop() {
      state = (SmartScissor.State)stateStack.remove(stateStack.size() - 1);
      GL11.glPopAttrib();
   }

   public static void unset() {
      GL11.glDisable(3089);
      state.enabled = false;
   }

   public static void setFromComponentCoordinates(int x, int y, int width, int height) {
      new ScaledResolution(Minecraft.getMinecraft());
      int scaleFactor = 2;
      int screenX = x * scaleFactor;
      int screenY = y * scaleFactor;
      int screenWidth = width * scaleFactor;
      int screenHeight = height * scaleFactor;
      screenY = Minecraft.getMinecraft().displayHeight - screenY - screenHeight;
      set(screenX, screenY, screenWidth, screenHeight);
   }

   public static void setFromComponentCoordinates(double x, double y, double width, double height) {
      new ScaledResolution(Minecraft.getMinecraft());
      int scaleFactor = 2;
      int screenX = (int)(x * (double)scaleFactor);
      int screenY = (int)(y * (double)scaleFactor);
      int screenWidth = (int)(width * (double)scaleFactor);
      int screenHeight = (int)(height * (double)scaleFactor);
      screenY = Minecraft.getMinecraft().displayHeight - screenY - screenHeight;
      set(screenX, screenY, screenWidth, screenHeight);
   }

   public static void set(int x, int y, int width, int height) {
      Rectangle screen = new Rectangle(0, 0, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      Rectangle current;
      if (state.enabled) {
         current = new Rectangle(state.x, state.y, state.width, state.height);
      } else {
         current = screen;
      }

      Rectangle target = new Rectangle(x + state.transX, y + state.transY, width, height);
      Rectangle result = current.intersection(target);
      result = result.intersection(screen);
      if (result.width < 0) {
         result.width = 0;
      }

      if (result.height < 0) {
         result.height = 0;
      }

      state.enabled = true;
      state.x = result.x;
      state.y = result.y;
      state.width = result.width;
      state.height = result.height;
      GL11.glEnable(3089);
      GL11.glScissor(result.x, result.y, result.width, result.height);
   }

   public static void translate(int x, int y) {
      state.transX = x;
      state.transY = y;
   }

   public static void translateFromComponentCoordinates(int x, int y) {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      int totalHeight = res.getScaledHeight();
      int scaleFactor = res.getScaleFactor();
      int screenX = x * scaleFactor;
      int screenY = y * scaleFactor;
      screenY = totalHeight * scaleFactor - screenY;
      translate(screenX, screenY);
   }

   private static class State implements Cloneable {
      public boolean enabled;
      public int transX;
      public int transY;
      public int x;
      public int y;
      public int width;
      public int height;

      private State() {
      }

      public SmartScissor.State clone() {
         try {
            return (SmartScissor.State)super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new AssertionError(var2);
         }
      }

      // $FF: synthetic method
      State(Object x0) {
         this();
      }
   }
}

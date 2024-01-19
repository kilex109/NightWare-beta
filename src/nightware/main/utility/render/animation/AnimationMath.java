package nightware.main.utility.render.animation;

import nightware.main.utility.math.MathUtility;
import net.minecraft.client.Minecraft;

public class AnimationMath {
   public static double deltaTime() {
      return Minecraft.getDebugFPS() > 0 ? 1.0D / (double)Minecraft.getDebugFPS() : 1.0D;
   }

   public static float fast(float end, float start, float multiple) {
      return (1.0F - MathUtility.clamp((float)(deltaTime() * (double)multiple), 0.0F, 1.0F)) * end + MathUtility.clamp((float)(deltaTime() * (double)multiple), 0.0F, 1.0F) * start;
   }
}

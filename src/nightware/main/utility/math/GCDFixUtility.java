package nightware.main.utility.math;

import nightware.main.utility.Utility;

public class GCDFixUtility implements Utility {
   public static float getFixedRotation(float rot) {
      return getDeltaMouse(rot) * getGCDValue();
   }

   public static float getGCDValue() {
      return (float)((double)getGCD() * 0.15D);
   }

   public static float getGCD() {
      float f1;
      return (f1 = (float)((double)mc.gameSettings.mouseSensitivity * 0.6D + 0.2D)) * f1 * f1 * 8.0F;
   }

   public static float getDeltaMouse(float delta) {
      return (float)Math.round(delta / getGCDValue());
   }
}

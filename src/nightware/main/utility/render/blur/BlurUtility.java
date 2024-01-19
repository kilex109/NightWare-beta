package nightware.main.utility.render.blur;

import nightware.main.utility.Utility;
import nightware.main.utility.render.StencilUtility;

public class BlurUtility implements Utility {
   public static void drawBlur(float radius, Runnable data) {
      StencilUtility.initStencilToWrite();
      data.run();
      StencilUtility.readStencilBuffer(1);
      GaussianBlur.renderBlur(radius);
      StencilUtility.uninitStencilBuffer();
   }

   public static void drawBlurredScreen(float radius) {
      GaussianBlur.renderBlur(radius);
   }
}

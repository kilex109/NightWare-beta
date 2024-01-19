package nightware.main.utility.render.animation.impl;

import nightware.main.utility.render.animation.Animation;
import nightware.main.utility.render.animation.Direction;

public class DecelerateAnimation extends Animation {
   public DecelerateAnimation(int ms, float endPoint) {
      super(ms, endPoint);
   }

   public DecelerateAnimation(int ms, float endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected float getEquation(float x) {
      float x1 = x / (float)this.duration;
      return 1.0F - (x1 - 1.0F) * (x1 - 1.0F);
   }
}

package nightware.main.utility.math;

import nightware.main.utility.Utility;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.util.vector.Vector2f;

public class RayCastUtility implements Utility {
   public static Entity getPointedEntity(Vector2f rot, double dst, float scale, boolean walls, Entity target) {
      Entity entity = mc.player;
      RayTraceResult objectMouseOver = rayTrace(dst, rot.x, rot.y, walls);
      Vec3d vec3d = entity.getPositionEyes(1.0F);
      boolean flag = false;
      double d1 = dst;
      if (objectMouseOver != null) {
         d1 = objectMouseOver.hitVec.distanceTo(vec3d);
      }

      Vec3d vec3d1 = getLook(rot.x, rot.y);
      Vec3d vec3d2 = vec3d.add(vec3d1.x * dst, vec3d1.y * dst, vec3d1.z * dst);
      Entity pointedEntity = null;
      Vec3d vec3d3 = null;
      double d2 = d1;
      float widthPrev = target.width;
      float heightPrev = target.height;
      AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().grow((double)target.getCollisionBorderSize());
      RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
      if (axisalignedbb.contains(vec3d)) {
         if (d1 >= 0.0D) {
            pointedEntity = target;
            vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
            d2 = 0.0D;
         }
      } else if (raytraceresult != null) {
         double d3 = vec3d.distanceTo(raytraceresult.hitVec);
         if (d3 < d1 || d1 == 0.0D) {
            boolean flag1 = false;
            if (!flag1 && target.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
               if (d1 == 0.0D) {
                  pointedEntity = target;
                  vec3d3 = raytraceresult.hitVec;
               }
            } else {
               pointedEntity = target;
               vec3d3 = raytraceresult.hitVec;
               d2 = d3;
            }
         }
      }

      if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > dst) {
         pointedEntity = null;
         objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing)null, new BlockPos(vec3d3));
      }

      if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
         objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
      }

      return objectMouseOver != null ? (objectMouseOver.entityHit instanceof Entity ? objectMouseOver.entityHit : null) : null;
   }

   public static RayTraceResult rayTrace(double blockReachDistance, float yaw, float pitch, boolean walls) {
      if (!walls) {
         return null;
      } else {
         Vec3d vec3d = mc.player.getPositionEyes(1.0F);
         Vec3d vec3d1 = getLook(yaw, pitch);
         Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
         return mc.world.rayTraceBlocks(vec3d, vec3d2, true, true, true);
      }
   }

   static Vec3d getVectorForRotation(float pitch, float yaw) {
      float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
      float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
      float f2 = -MathHelper.cos(-pitch * 0.017453292F);
      float f3 = MathHelper.sin(-pitch * 0.017453292F);
      return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
   }

   static Vec3d getLook(float yaw, float pitch) {
      return getVectorForRotation(pitch, yaw);
   }
}

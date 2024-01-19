package nightware.main.module.impl.combat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class TargetResult {
    EntityLivingBase target;
    Vec3d pos;

    public TargetResult(EntityLivingBase target, Vec3d pos) {
        this.pos = pos;
        this.target = target;
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    public Vec3d getPos() {
        return pos;
    }
}

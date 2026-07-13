package noppes.npcs.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;

public class EntityAILeapAtTargetNpc extends EntityAIBase {
    private EntityNPCInterface npc;
    private EntityLivingBase target;
    private float leapMotionY;
    private int leapCooldown;

    public EntityAILeapAtTargetNpc(EntityNPCInterface npc, float leapMotionY) {
        this.npc = npc;
        this.leapMotionY = leapMotionY;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {
        target = npc.getAttackTarget();
        if (target == null) {
            return false;
        }
        if (leapCooldown > 0) {
            leapCooldown--;
            return false;
        }
        double dist = npc.getDistanceSqToEntity(target);
        return npc.onGround && dist > 4.0D && dist < 16.0D;
    }

    @Override
    public boolean continueExecuting() {
        return !npc.onGround;
    }

    @Override
    public void startExecuting() {
        double dx = target.posX - npc.posX;
        double dz = target.posZ - npc.posZ;
        float dist = (float)Math.sqrt(dx * dx + dz * dz);
        npc.motionX += dx / dist * 0.5D;
        npc.motionY = leapMotionY;
        npc.motionZ += dz / dist * 0.5D;
        leapCooldown = 40;
    }
}

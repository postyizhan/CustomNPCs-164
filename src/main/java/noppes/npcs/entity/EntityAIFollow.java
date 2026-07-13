package noppes.npcs.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;

public class EntityAIFollow extends EntityAIBase {
    private EntityNPCInterface npc;
    private EntityLivingBase target;
    private double speed;
    private float minDist;
    private float maxDist;

    public EntityAIFollow(EntityNPCInterface npc, double speed, float minDist, float maxDist) {
        this.npc = npc;
        this.speed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = npc.getAttackTarget();
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        double dist = npc.getDistanceSqToEntity(target);
        return dist > minDist * minDist && dist < maxDist * maxDist;
    }

    @Override
    public boolean continueExecuting() {
        if (target == null || !target.isEntityAlive()) {
            return false;
        }
        double dist = npc.getDistanceSqToEntity(target);
        return dist > minDist * minDist && dist < maxDist * maxDist;
    }

    @Override
    public void startExecuting() {
        target = npc.getAttackTarget();
    }

    @Override
    public void updateTask() {
        if (target != null) {
            npc.getNavigator().tryMoveToEntityLiving(target, speed);
            npc.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        }
    }
}

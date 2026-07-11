package noppes.npcs.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

public class EntityAIAttackTarget extends EntityAIBase
{
    World worldObj;
    EntityNPCInterface attacker;
    EntityLivingBase entityTarget;

    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    int attackTick;
    boolean field_75437_f;

    /** The PathEntity of our entity. */
    PathEntity entityPathEntity;
    private int field_75445_i;
    
    public EntityAIAttackTarget(EntityNPCInterface par1EntityLiving, boolean par3)
    {
        this.attackTick = 0;
        this.attacker = par1EntityLiving;
        this.worldObj = par1EntityLiving.worldObj;
        this.field_75437_f = par3;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else if (this.attacker.inventory.getProjectile() != null && this.attacker.ai.useRangeMelee == 0)
        {
     	   return false;
        }
        
        double var2 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY, entitylivingbase.posZ);
        double var3 = this.attacker.ai.distanceToMelee * this.attacker.ai.distanceToMelee;
        
        if (this.attacker.ai.useRangeMelee == 1 && var2 > var3)
        {
        	return false;
        }
        else
        {
        	this.entityTarget = entitylivingbase;
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
            return this.entityPathEntity != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	this.entityTarget = this.attacker.getAttackTarget();
    	

		if(entityTarget == null || attacker.getDistanceSqToEntity(entityTarget) > attacker.getDistanceSqToEntity(entityTarget))
			return false;
		
		if (this.attacker.ai.useRangeMelee == 1 && attacker.getDistanceSqToEntity(entityTarget) > (this.attacker.ai.distanceToMelee * this.attacker.ai.distanceToMelee))
			return false;
		
		return !entityTarget.isEntityAlive() ? false : (!this.field_75437_f ? !this.attacker.getNavigator().noPath() : this.attacker.func_110176_b(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ)));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.attacker.getNavigator().setPath(this.entityPathEntity, 1.0D);
        this.field_75445_i = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
    	this.entityPathEntity = null;
    	this.entityTarget = null;
    	this.attacker.setAttackTarget(null);
        this.attacker.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.attacker.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);

        if ((this.field_75437_f || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.field_75445_i <= 0)
        {
            this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
            this.attacker.getNavigator().tryMoveToEntityLiving(this.entityTarget, 1.0D);
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (this.attacker.getDistance(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= attacker.stats.attackRange && attacker.canEntityBeSeen(this.entityTarget))
        {
            if (this.attackTick <= 0)
            {
                this.attackTick = this.attacker.stats.attackSpeed;
                this.attacker.swingItem();
                this.attacker.attackEntityAsMob(this.entityTarget);
            }
        }
    }
}

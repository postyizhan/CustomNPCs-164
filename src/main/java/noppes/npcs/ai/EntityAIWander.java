package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;

public class EntityAIWander extends EntityAIBase
{
    private EntityNPCInterface entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;

    public EntityAIWander(EntityNPCInterface par1EntityNPCInterface)
    {
        this.entity = par1EntityNPCInterface;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.entity.getAge() >= 100)
        {
            return false;
        }
        else if (this.entity.getRNG().nextInt(80) != 0)
        {
            return false;
        }
        else
        {
        	Vec3 vec = getVec();
            if (vec == null)
            {
                return false;
            }
            else
            {
                this.xPosition = vec.xCoord;
                this.yPosition = vec.yCoord;
                this.zPosition = vec.zCoord;
                return true;
            }
        }
    }
    private Vec3 getVec(){
    	if(entity.ai.walkingRange > 0){
            double distance = this.entity.getDistanceSq(this.entity.getStartXPos(), this.entity.getStartYPos(), this.entity.getStartZPos());
            int range = (int) MathHelper.sqrt_double(this.entity.ai.walkingRange * this.entity.ai.walkingRange - distance);
            if(range > CustomNpcs.NpcNavRange)
            	range = CustomNpcs.NpcNavRange;
            if(range < 3){
                range = this.entity.ai.walkingRange;
                if(range > CustomNpcs.NpcNavRange)
                	range = CustomNpcs.NpcNavRange;
                Vec3 start = Vec3.createVectorHelper(this.entity.getStartXPos(), this.entity.getStartYPos(), this.entity.getStartZPos());
                return RandomPositionGeneratorAlt.findRandomTargetBlockTowards(this.entity, range / 2, range / 2 > 7?7:range / 2, start);
            }
            else{
                return RandomPositionGeneratorAlt.findRandomTarget(this.entity, range, range / 2 > 7?7:range / 2);
            }
    	}
    	return RandomPositionGeneratorAlt.findRandomTarget(this.entity, CustomNpcs.NpcNavRange, 7);
    }
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entity.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 0.7D);
    }
}

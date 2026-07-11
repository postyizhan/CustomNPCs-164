package noppes.npcs.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumMovingType;

public class EntityAIMovingPath extends EntityAIBase
{
    private EntityNPCInterface npc;
    private int[] pos;

    public EntityAIMovingPath(EntityNPCInterface par1EntityNPCInterface)
    {
        this.npc = par1EntityNPCInterface;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (npc.ai.movingType != EnumMovingType.MovingPath || npc.isAttacking() || npc.getRNG().nextInt(40) != 0 && npc.ai.movingPause)
            return false;
        
        List<int[]> list = npc.ai.getMovingPath();
        if(list.size() < 2)
        	return false;
        
        npc.ai.incrementMovingPath();
    	pos = npc.ai.getCurrentMovingPath();
        
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
    	if(npc.isAttacking()){
    		npc.ai.decreaseMovingPath();
    		return false;
    	}
        return !this.npc.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.npc.getNavigator().tryMoveToXYZ(pos[0] + 0.5, pos[1], pos[2] + 0.5, 1.0D);
    }
}

package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.MathHelper;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumMovingType;

public class EntityAIReturn extends EntityAIBase
{
    private final EntityNPCInterface npc;
    private int stuckTicks = 0;
    private int returnTicks = 0;
    private double posX;
    private double posY;
    private double posZ;
    private boolean wasAttacked = false;
    private double[] preAttackPos;

    public EntityAIReturn(EntityNPCInterface npc)
    {
        this.npc = npc;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if(npc.isFollowerWithOwner() || !npc.ai.returnToStart || npc.isKilled() || !npc.getNavigator().noPath()){
    		return false;
    	}
    	
    	if (npc.ai.findShelter == 0 && (!npc.worldObj.isDaytime() || npc.worldObj.isRaining()) && !npc.worldObj.provider.hasNoSky)
        {
    		if (npc.worldObj.canBlockSeeTheSky((int)npc.getStartXPos(), (int)npc.getStartYPos(), (int)npc.getStartZPos()) || npc.worldObj.getFullBlockLightValue((int)npc.getStartXPos(), (int)npc.getStartYPos(), (int)npc.getStartZPos()) <= 8)
            {
                return false;
            }
        }
    	
    	if (npc.ai.findShelter == 1 && npc.worldObj.isDaytime())
    	{
    		if (npc.worldObj.canBlockSeeTheSky((int)npc.getStartXPos(), (int)npc.getStartYPos(), (int)npc.getStartZPos()))
            {
                return false;
            }
    	}
    	
    	if(npc.isAttacking())
    	{
    		if(!wasAttacked){
	    		wasAttacked = true;
	    		preAttackPos = new double[]{npc.posX, npc.posY, npc.posZ};
    		}
    		return false;
    	}
    	if(!npc.isAttacking() && wasAttacked)
    	{
    		return true;
    	}
    	
    	if(npc.ai.movingType == EnumMovingType.Wandering)
    		return this.npc.getDistanceSq(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos()) > npc.ai.walkingRange * npc.ai.walkingRange;

        if(npc.ai.movingType == EnumMovingType.Standing)
        	return !this.npc.isVeryNearAssignedPlace();
        return false;
    }
    public boolean continueExecuting()
    {
    	if(wasAttacked && returnTicks >= 30 && (npc.getNavigator().noPath() || isTooFar()))
    		return false;
        return !npc.isFollowerWithOwner() && !npc.isKilled() && !npc.isAttacking() && stuckTicks > 0 && !npc.isVeryNearAssignedPlace();
    }
    public void updateTask()
    {
    	if(returnTicks < 30){ //Always try to walk back for 30 ticks
        	returnTicks++;
    		return;
    	}
    	else if(returnTicks == 30){ //After 30 ticks check how far away he is, if too far teleport
        	returnTicks++;
	    	if(isTooFar()){
    			npc.setPosition(posX, posY, posZ);
        		npc.getNavigator().clearPathEntity();
	    	}
    	}    	
    	else if(npc.getNavigator().noPath()){ //If cant find a way back wait for 30 ticks before teleporting
    		stuckTicks++;
    		if(stuckTicks == 30){
        		stuckTicks = 0;
    			npc.setPosition(posX, posY, posZ);
        		npc.getNavigator().clearPathEntity();
    		}
    	}
    	else
    		stuckTicks = 1;
    	
    }
    private boolean isTooFar(){
    	int allowedDistance = npc.stats.aggroRange * 2;
    	if(npc.ai.movingType == EnumMovingType.Wandering)
    		allowedDistance += npc.ai.walkingRange;
    	return npc.getDistance(posX, posY, posZ) > allowedDistance;
    }
    
    public void startExecuting() 
    {
    	if(wasAttacked){
    		posX = preAttackPos[0];
    		posY = preAttackPos[1];
    		posZ = preAttackPos[2];
    	}
    	else{
        	posX = npc.getStartXPos();
        	posY = npc.getStartYPos();
        	posZ = npc.getStartZPos();
    	}
        npc.getNavigator().clearPathEntity();
		npc.getNavigator().tryMoveToXYZ(posX, posY, posZ, 1.0D);
    	stuckTicks = 1;
    	returnTicks = 0;
    }
    public void resetTask(){
    	wasAttacked = false;
    }
}

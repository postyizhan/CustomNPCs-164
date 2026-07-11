package noppes.npcs.ai;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumJobType;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.roles.JobGuard;

public class AttackSelectorNPC implements IEntitySelector
{
	private EntityNPCInterface npc;
	
	public AttackSelectorNPC(EntityNPCInterface npc){
		this.npc = npc;
	}
    /**
     * Return whether the specified entity is applicable to this filter.
     */
	
    public boolean isEntityApplicable(Entity entity)
    {
    	if(entity == npc || !(entity instanceof EntityLivingBase) || npc.getDistanceToEntity(entity) > npc.stats.aggroRange)
    		return false;

        if (this.npc.ai.directLOS && !this.npc.getEntitySenses().canSee(entity))
        	return false;
        	
    	//prevent the npc from going on an endless killing spree
    	if(!npc.isFollowerWithOwner() && npc.ai.returnToStart){ 
	    	int allowedDistance = npc.stats.aggroRange * 2;
	    	if(npc.ai.movingType == EnumMovingType.Wandering)
	    		allowedDistance += npc.ai.walkingRange;
	    	if(entity.getDistance(npc.getStartXPos(), npc.getStartYPos(), npc.getStartZPos()) > allowedDistance)
	    		return false;
    	}
    	
    	if(npc.advanced.job == EnumJobType.Guard && ((JobGuard)npc.jobInterface).isEntityApplicable((EntityLivingBase)entity))
    		return true;

    	if(entity instanceof EntityPlayer){
    		return npc.getFaction().isAggressiveToPlayer((EntityPlayer) entity);
    	}

    	if(entity instanceof EntityNPCInterface){
    		if(((EntityNPCInterface)entity).isKilled())
    			return false;
    		if(npc.advanced.attackOtherFactions)
    			return npc.getFaction().isAggressiveToNpc((EntityNPCInterface)entity);
    	}
    	
        return false;
    }
}

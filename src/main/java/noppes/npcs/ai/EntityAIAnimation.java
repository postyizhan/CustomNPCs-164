package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumAnimation;
import noppes.npcs.constants.EnumMovingType;

public class EntityAIAnimation extends EntityAIBase
{
    private EntityNPCInterface npc;

    private boolean isAttacking = false;
    private boolean isDead = false;
    private boolean isAtStartpoint = false;
    private boolean hasPath = false;

    public EntityAIAnimation(EntityNPCInterface npc)
    {
        this.npc = npc;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	isDead = npc.isKilled();
    	if(isDead)
    		return npc.currentAnimation != EnumAnimation.LYING;

    	if(npc.ai.animationType == EnumAnimation.NONE)
    		return npc.currentAnimation != EnumAnimation.NONE;
    	    	
    	isAttacking = npc.isAttacking();
    	isAtStartpoint = npc.isVeryNearAssignedPlace();
    	hasPath = !npc.getNavigator().noPath();

    	boolean hasCorrectAnimation = npc.currentAnimation == npc.ai.animationType;
    	if(npc.ai.movingType == EnumMovingType.Standing && hasNavigation())
    		return hasCorrectAnimation;
    	return !hasCorrectAnimation;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
    	EnumAnimation type = npc.ai.animationType;
    	if(isDead || npc.isSleeping())
    		type = EnumAnimation.LYING;
    	else if(npc.ai.movingType == EnumMovingType.Standing && hasNavigation() && (npc.ai.animationType == EnumAnimation.SITTING || npc.ai.animationType == EnumAnimation.LYING))
    		type = EnumAnimation.NONE;
		setAnimation(type);
    }
    
    private void setAnimation(EnumAnimation animation){
    	npc.currentAnimation = animation;
    	npc.getDataWatcher().updateObject(14, animation.ordinal());
    	npc.updateHitbox();
    	npc.setPosition(npc.posX, npc.posY, npc.posZ);
    }
    
    private boolean hasNavigation() {
    	return (isAttacking || !isAtStartpoint || hasPath);
    }
}

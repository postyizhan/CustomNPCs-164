package noppes.npcs.ai;

import net.minecraft.entity.ai.EntityAIBase;
import noppes.npcs.EntityNPCInterface;

public class EntityAIWorldLines extends EntityAIBase {

	private EntityNPCInterface npc;
	public EntityAIWorldLines(EntityNPCInterface npc){
		this.npc = npc;
	}
	
	@Override
	public boolean shouldExecute() {
		return !npc.isAttacking() && !npc.isKilled() && npc.advanced.hasWorldLines() && npc.getRNG().nextInt(900) == 1;
	}
	
    public void startExecuting()
    {
    	npc.saySurrounding(npc.advanced.getWorldLine());
    }
    
}

package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;

public class JobHealer extends JobInterface{
	private long healTicks = 0;
	public int range = 5;
	public int speed = 5;

	public JobHealer(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("HealerRange", range);
		nbttagcompound.setInteger("HealerSpeed", speed);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		range = nbttagcompound.getInteger("HealerRange");
		speed = nbttagcompound.getInteger("HealerSpeed");
	}
	private List<EntityLivingBase> toHeal = new ArrayList<EntityLivingBase>();
	
	//TODO heal food, heal potion effects, heal more types of entities besides just the player and npcs
	public boolean aiShouldExecute() {
		healTicks++;
		if (healTicks < speed * 10) 
			return false;
		
		for(Object plObj:  npc.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, npc.boundingBox.expand(range, range/2, range))){
			EntityLivingBase entity = (EntityLivingBase) plObj;
			
			if(entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) entity;
				if(player.getHealth() < player.getMaxHealth() && !npc.getFaction().isAggressiveToPlayer(player))
					toHeal.add(player);
			}
			if(entity instanceof EntityNPCInterface){
				EntityNPCInterface npc = (EntityNPCInterface) entity;
				if(npc.getHealth() < npc.getMaxHealth() && !this.npc.getFaction().isAggressiveToNpc(npc))
					toHeal.add(npc);
			}
			
		}

		healTicks = 0;
		return !toHeal.isEmpty();
	}

	public void aiStartExecuting() {
		for(EntityLivingBase entity : toHeal){
			float heal = entity.getMaxHealth() / 20; //heal 5% of max health
			entity.heal(heal > 0?heal:1);
			NoppesUtilServer.spawnParticle(entity, "heal",entity.dimension);
		}
		toHeal.clear();
	}
}

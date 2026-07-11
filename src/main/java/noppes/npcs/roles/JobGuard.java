package noppes.npcs.roles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;

public class JobGuard extends JobInterface{

	public boolean attacksAnimals = false;
	public boolean attackHostileMobs = true;
	public boolean attackCreepers = false;
	public boolean attackAll = false;
	
	public JobGuard(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setBoolean("GuardAttackAnimals", attacksAnimals);
		nbttagcompound.setBoolean("GuardAttackMobs", attackHostileMobs);
		nbttagcompound.setBoolean("GuardAttackCreepers", attackCreepers);
		nbttagcompound.setBoolean("GuardAttackAll", attackAll);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		attacksAnimals = nbttagcompound.getBoolean("GuardAttackAnimals");
		attackHostileMobs = nbttagcompound.getBoolean("GuardAttackMobs");
		attackCreepers = nbttagcompound.getBoolean("GuardAttackCreepers");
		attackAll = nbttagcompound.getBoolean("GuardAttackAll");
	}
	
	public boolean isEntityApplicable(EntityLivingBase entity) {
    	if(entity.isDead || entity.getHealth() < 1 || entity instanceof EntityPlayer)
    		return false;
//    	if(npc.advanced.role == EnumRoleType.Follower){
//    		EntityPlayer player = ((RoleFollower)npc.roleInterface).getOwner();
//
//        	if(player != null && entity.getAttackTarget() == player){
//        		return true;
//    		}
//    	}
    	
    	if(entity instanceof EntityNPCInterface)
    		return false;
    	
    	if(entity instanceof EntityAnimal){
    		if(!attacksAnimals || entity instanceof EntityTameable && !((EntityTameable)entity).getOwnerName().isEmpty())
    			return false;
    		return true;
    	}
    	else if (entity instanceof EntityCreeper) {
    		if (!attackCreepers)
    			return false;
    		return true;
    	}
    	else if(entity instanceof IMob || entity instanceof EntityDragon){
    		if(!attackHostileMobs)
    			return false;
    		return true;
    	}
    	else if(attackAll){
    		return true;
    	}
		return false;
	}
}

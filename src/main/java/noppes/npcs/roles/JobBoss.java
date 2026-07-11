package noppes.npcs.roles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumJobType;

public class JobBoss extends JobInterface{
	public boolean hideName = false;
	public int resetTime = 300;

	private NBTTagCompound original;

	public NBTTagCompound compound9;
	public NBTTagCompound compound8;
	public NBTTagCompound compound7;
	public NBTTagCompound compound6;
	public NBTTagCompound compound5;
	public NBTTagCompound compound4;
	public NBTTagCompound compound3;
	public NBTTagCompound compound2;
	public NBTTagCompound compound1;
	
	private int type = 10;
	
	public JobBoss(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		compound.setBoolean("BossHideName", hideName);

		saveCompound(original, "BossOriginal", compound);
		saveCompound(compound1, "BossNBT1", compound);
		saveCompound(compound2, "BossNBT2", compound);
		saveCompound(compound3, "BossNBT3", compound);
		saveCompound(compound4, "BossNBT4", compound);
		saveCompound(compound5, "BossNBT5", compound);
		saveCompound(compound6, "BossNBT6", compound);
		saveCompound(compound7, "BossNBT7", compound);
		saveCompound(compound8, "BossNBT8", compound);
		saveCompound(compound9, "BossNBT9", compound);
	}

	private void saveCompound(NBTTagCompound save, String name, NBTTagCompound compound) {
		if(save != null)
			compound.setCompoundTag(name, save);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		hideName = compound.getBoolean("BossHideName");

		original = compound.getCompoundTag("BossOriginal");
		compound1 = compound.getCompoundTag("BossNBT1");
		compound2 = compound.getCompoundTag("BossNBT2");
		compound3 = compound.getCompoundTag("BossNBT3");
		compound4 = compound.getCompoundTag("BossNBT4");
		compound5 = compound.getCompoundTag("BossNBT5");
		compound6 = compound.getCompoundTag("BossNBT6");
		compound7 = compound.getCompoundTag("BossNBT7");
		compound8 = compound.getCompoundTag("BossNBT8");
		compound9 = compound.getCompoundTag("BossNBT9");
	}

	@Override
	public boolean aiShouldExecute() {
		if(type == 10 || npc.getAttackTarget() != null)
			return false;
		return false;
	}
	private long timeStart;
	public void aiStartExecuting() {
		timeStart = System.currentTimeMillis();
	}

	@Override
	public void aiUpdateTask() {
		if(timeStart - System.currentTimeMillis() < (resetTime * 1000))
			return;
		
		npc.isDead = true;
		type = 10;
		spawnEntity(original);
	}

	public boolean applyDamage(float damage) {
//		float max = npc.getMaxHealth();
//		int percent = (int)(max / (npc.getHealth() - damage))  + 1;
//		NBTTagCompound compound = null;
//		if(percent >= type){
//			return true;
//		}
//		int i = type - 1;
//		while(i >= percent){
//			compound = getNBT(i);
//			if(compound != null)
//				break;
//			i--;
//		}
//		if(compound == null || !compound.hasKey("ClonedName"))
//			return true;
//		type = i;
//		return !spawnEntity(compound);
		return false;
	}
	
	private NBTTagCompound getNBT(int i) {
		if(i == 9)
			return compound9;
		if(i == 8)
			return compound8;
		if(i == 7)
			return compound7;
		if(i == 6)
			return compound6;
		if(i == 5)
			return compound5;
		if(i == 4)
			return compound4;
		if(i == 3)
			return compound3;
		if(i == 2)
			return compound2;
		if(i == 1)
			return compound1;
		return null;
	}
	
	public void setNBT(int i, NBTTagCompound compound) {
		if(i == 9)
			compound9 = compound;
		if(i == 8)
			compound8 = compound;
		if(i == 7)
			compound7 = compound;
		if(i == 6)
			compound6 = compound;
		if(i == 5)
			compound5 = compound;
		if(i == 4)
			compound4 = compound;
		if(i == 3)
			compound3 = compound;
		if(i == 2)
			compound2 = compound;
		if(i == 1)
			compound1 = compound;
	}

	public void reset() {
		if(type == 10)
			return;
		type = 10;
		spawnEntity(original);
	}

	private boolean spawnEntity(NBTTagCompound compound) {
		Entity entity = EntityList.createEntityFromNBT(compound, npc.worldObj);
		if(entity == null || !(entity instanceof EntityNPCInterface))
			return false;
		
//		EntityNPCInterface newnpc = (EntityNPCInterface) entity;
//		
//		npc.isDead = true;
//		
//		newnpc.setPosition(npc.posX, npc.posY, npc.posZ);
//		newnpc.rotationYaw = npc.rotationYaw;
//		newnpc.prevRotationYaw = npc.prevRotationYaw;
//		newnpc.rotationPitch = npc.rotationPitch;
//		newnpc.prevRotationPitch = npc.prevRotationPitch;
//		
//		newnpc.stats.maxHealth = npc.stats.maxHealth;
//		newnpc.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(newnpc.stats.maxHealth);
//		newnpc.advanced.job = EnumJobType.Boss;
//		newnpc.jobInterface = this;
//		
//		newnpc.setHealth(npc.stats.maxHealth / 10 * type - 1);
//		newnpc.setAttackTarget(npc.getAttackTarget());
//		
//		npc.worldObj.spawnEntityInWorld(newnpc);
//		this.npc = newnpc;
		
		return true;
	}
}

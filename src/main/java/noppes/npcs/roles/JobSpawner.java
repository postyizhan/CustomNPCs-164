package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;

import org.apache.commons.lang3.RandomStringUtils;

public class JobSpawner extends JobInterface{
	public NBTTagCompound compound6;
	public NBTTagCompound compound5;
	public NBTTagCompound compound4;
	public NBTTagCompound compound3;
	public NBTTagCompound compound2;
	public NBTTagCompound compound1;
	
	private int number = 0;
	
	private List<EntityLivingBase> spawned  = new ArrayList<EntityLivingBase>();
	
	private String id = RandomStringUtils.random(8, true, true);
	public boolean doesntDie = false;
	
	public int spawnType = 0;
	
	public int xOffset = 0;
	public int yOffset = 0;
	public int zOffset = 0;
	
	private EntityLivingBase target;

	public JobSpawner(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		saveCompound(compound1, "SpawnerNBT1", compound);
		saveCompound(compound2, "SpawnerNBT2", compound);
		saveCompound(compound3, "SpawnerNBT3", compound);
		saveCompound(compound4, "SpawnerNBT4", compound);
		saveCompound(compound5, "SpawnerNBT5", compound);
		saveCompound(compound6, "SpawnerNBT6", compound);
		
		compound.setString("SpawnerId", id);
		compound.setBoolean("SpawnerDoesntDie", doesntDie);
		compound.setInteger("SpawnerType", spawnType);
		compound.setInteger("SpawnerXOffset", xOffset);
		compound.setInteger("SpawnerYOffset", yOffset);
		compound.setInteger("SpawnerZOffset", zOffset);
	}

	private void saveCompound(NBTTagCompound save, String name, NBTTagCompound compound) {
		if(save != null)
			compound.setCompoundTag(name, save);
	}
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		compound1 = compound.getCompoundTag("SpawnerNBT1");
		compound2 = compound.getCompoundTag("SpawnerNBT2");
		compound3 = compound.getCompoundTag("SpawnerNBT3");
		compound4 = compound.getCompoundTag("SpawnerNBT4");
		compound5 = compound.getCompoundTag("SpawnerNBT5");
		compound6 = compound.getCompoundTag("SpawnerNBT6");
		
		id = compound.getString("SpawnerId");
		doesntDie = compound.getBoolean("SpawnerDoesntDie");
		spawnType = compound.getInteger("SpawnerType");
		xOffset = compound.getInteger("SpawnerXOffset");
		yOffset = compound.getInteger("SpawnerYOffset");
		zOffset = compound.getInteger("SpawnerZOffset");
	}

    
    public void setJobCompound(int i, NBTTagCompound compound){
    	if(i == 1)
    		compound1 = compound;
    	if(i == 2)
    		compound2 = compound;
    	if(i == 3)
    		compound3 = compound;
    	if(i == 4)
    		compound4 = compound;
    	if(i == 5)
    		compound5 = compound;
    	if(i == 6)
    		compound6 = compound;
    }
    
	@Override
	public void aiUpdateTask() {
		if(spawned.isEmpty())
			return;
		Iterator<EntityLivingBase> iterator = spawned.iterator();
		while(iterator.hasNext()){
			EntityLivingBase spawn = iterator.next();
			if(npc.getDistanceToEntity(spawn) > 60 || spawn.isDead || spawn.getHealth() <= 0){
				spawn.isDead = true;
				iterator.remove();
			}
			else{
				if(spawn instanceof EntityLiving)
					((EntityLiving)spawn).setAttackTarget(target);
			}
		}
		
		if(spawnType == 0 && spawned.isEmpty()){
			if(!spawnEntity(number + 1) && !doesntDie)
				npc.setDead();
		}
		if(spawnType == 1 && spawned.isEmpty()){
			if(number >= 6 && !doesntDie)
				npc.setDead();
			else{
				spawnEntity(compound1);
				spawnEntity(compound2);
				spawnEntity(compound3);
				spawnEntity(compound4);
				spawnEntity(compound5);
				spawnEntity(compound6);
				number = 6;
			}
		}
		if(spawnType == 2 && spawned.isEmpty()){
			ArrayList<NBTTagCompound> list = new ArrayList<NBTTagCompound>();
			if(compound1 != null && compound1.hasKey("id"))
				list.add(compound1);
			if(compound2 != null && compound2.hasKey("id"))
				list.add(compound2);
			if(compound3 != null && compound3.hasKey("id"))
				list.add(compound3);
			if(compound4 != null && compound4.hasKey("id"))
				list.add(compound4);
			if(compound5 != null && compound5.hasKey("id"))
				list.add(compound5);
			if(compound6 != null && compound6.hasKey("id"))
				list.add(compound6);
			
			if(!list.isEmpty()){
				NBTTagCompound compound = list.get(npc.getRNG().nextInt(list.size()));
				spawnEntity(compound);
			}
		}
		
	}
	
	private EntityLivingBase getTarget() {
		EntityLivingBase target = npc.getAttackTarget();
		if(target == null || target.isDead || target.getHealth() <= 0)
			target = npc.getAITarget();
		if(target != null && !target.isDead && target.getHealth() > 0)
			return target;

		for(EntityLivingBase entity : spawned){
			if(entity instanceof EntityLiving){
				target = ((EntityLiving)entity).getAttackTarget();
				if(target != null && !target.isDead && target.getHealth() > 0)
					return target;
			}
			target = entity.getAITarget();
			if(target != null && !target.isDead && target.getHealth() > 0)
				return target;
		}
		return null;
	}

	private boolean isEmpty(){
		if(compound1 != null && compound1.hasKey("id"))
			return false;
		if(compound2 != null && compound2.hasKey("id"))
			return false;
		if(compound3 != null && compound3.hasKey("id"))
			return false;
		if(compound4 != null && compound4.hasKey("id"))
			return false;
		if(compound5 != null && compound5.hasKey("id"))
			return false;
		if(compound6 != null && compound6.hasKey("id"))
			return false;
		
		return true;
	}
	
	private void setTarget(EntityLivingBase base, EntityLivingBase target) {
		if (EntityList.getEntityString(base).equals("Pixelmon")	&& target instanceof EntityPlayer) {
			//TODO
		} 
		else if (base instanceof EntityLiving)
			((EntityLiving) base).setAttackTarget(target);
		else
			base.setRevengeTarget(target);
	}

	private void addEntityToList(ArrayList<Object> team, int i) {
		NBTTagCompound compound = getCompound(i);
		if(compound == null){
			npc.setDead();
			return;
		}
		
		Entity entity = EntityList.createEntityFromNBT(compound, npc.worldObj);
		if(entity != null && EntityList.getEntityString(entity).equals("Pixelmon"))
			team.add(entity);
		
	}

	@Override
	public boolean aiShouldExecute() {
		if(isEmpty() || npc.isKilled())
			return false;
		target = getTarget();
		if(npc.getRNG().nextInt(30) == 1){
			if(spawned.isEmpty())
				spawned = getNearbySpawned();
			if(target == null)
				reset();
		}
		return target != null;
	}

	public boolean aiContinueExecute() {
		return aiShouldExecute();
	}
	public void resetTask() {
		reset();
	}

	public void aiStartExecuting() {

		if(spawned.isEmpty()){
			spawned = getNearbySpawned();
			if(spawned.isEmpty() && !spawnEntity(1)){
				if(!doesntDie)
					npc.setDead();
			}
		}
		number = 0;
		for(EntityLivingBase entity : spawned){
			int i = entity.getEntityData().getInteger("NpcSpawnerNr");
			if(i > number)
				number = i;
			setTarget(entity, npc.getAttackTarget());
		}
	}

	@Override
	public void reset() {
		number = 0;
		if(!spawned.isEmpty()){
			for(EntityLivingBase entity : spawned)
				entity.isDead = true;
		}
		else{
			List<Entity> list = npc.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, npc.boundingBox.expand(40, 40, 40));
			for(Entity entity : list){
				if(entity.getEntityData().getString("NpcSpawnerId").equals(id))
					entity.isDead = true;
			}
		}
		spawned.clear();
	}
	@Override
	public void killed() {
		reset();
	}
	
	private boolean spawnEntity(int i){
		NBTTagCompound compound = getCompound(i);
		if(compound == null){
			return false;
		}
		spawnEntity(compound);
		return true;
	}
	private void spawnEntity(NBTTagCompound compound){
		if(compound == null || !compound.hasKey("id"))
			return;
		EntityLivingBase entity = (EntityLivingBase) EntityList.createEntityFromNBT(compound, npc.worldObj);
		if(entity == null)
			return;
		entity.getEntityData().setString("NpcSpawnerId", id);
		entity.getEntityData().setInteger("NpcSpawnerNr", number);
		setTarget(entity, npc.getAttackTarget());
		entity.setPosition(npc.posX + xOffset, npc.posY + yOffset, npc.posZ + zOffset);
		if(entity instanceof EntityNPCInterface){
			EntityNPCInterface snpc = (EntityNPCInterface) entity;
			snpc.stats.spawnCycle = 3;
			snpc.ai.returnToStart = false;
		}
		
		npc.worldObj.spawnEntityInWorld(entity);
		spawned.add(entity);
	}

	private NBTTagCompound getCompound(int i) {
		if(i <= 1 && compound1 != null && compound1.hasKey("id")){
			number = 1;
			return compound1;
		}
		if(i <= 2 && compound2 != null && compound2.hasKey("id")){
			number = 2;
			return compound2;
		}
		if(i <= 3 && compound3 != null && compound3.hasKey("id")){
			number = 3;
			return compound3;
		}
		if(i <= 4 && compound4 != null && compound4.hasKey("id")){
			number = 4;
			return compound4;
		}
		if(i <= 5 && compound5 != null && compound5.hasKey("id")){
			number = 5;
			return compound5;
		}
		if(i <= 6 && compound6 != null && compound6.hasKey("id")){
			number = 6;
			return compound6;
		}
		return null;
	}

	
	private List<EntityLivingBase> getNearbySpawned(){
		List<EntityLivingBase> spawnList = new ArrayList<EntityLivingBase>();
		List<EntityLivingBase> list = npc.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, npc.boundingBox.expand(40, 40, 40));
		for(EntityLivingBase entity : list){
			if(entity.getEntityData().getString("NpcSpawnerId").equals(id) && !entity.isDead)
				spawnList.add(entity);
		}
		return spawnList;
	}
}

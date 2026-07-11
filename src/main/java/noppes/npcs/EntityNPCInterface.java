package noppes.npcs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.StepSound;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import noppes.npcs.ai.AttackSelectorNPC;
import noppes.npcs.ai.EntityAIAmbushTarget;
import noppes.npcs.ai.EntityAIAnimation;
import noppes.npcs.ai.EntityAIAttackTarget;
import noppes.npcs.ai.EntityAIAvoidTarget;
import noppes.npcs.ai.EntityAIBustDoor;
import noppes.npcs.ai.EntityAIClosestTarget;
import noppes.npcs.ai.EntityAIDodgeShoot;
import noppes.npcs.ai.EntityAIFindShade;
import noppes.npcs.ai.EntityAIJob;
import noppes.npcs.ai.EntityAILook;
import noppes.npcs.ai.EntityAIMoveIndoors;
import noppes.npcs.ai.EntityAIMovingPath;
import noppes.npcs.ai.EntityAIOccupyBed;
import noppes.npcs.ai.EntityAIOrbitTarget;
import noppes.npcs.ai.EntityAIPanic;
import noppes.npcs.ai.EntityAIRangedAttack;
import noppes.npcs.ai.EntityAIReturn;
import noppes.npcs.ai.EntityAIRole;
import noppes.npcs.ai.EntityAISprintToTarget;
import noppes.npcs.ai.EntityAIStalkTarget;
import noppes.npcs.ai.EntityAIWander;
import noppes.npcs.ai.EntityAIWatchClosest;
import noppes.npcs.ai.EntityAIWaterNav;
import noppes.npcs.ai.EntityAIWorldLines;
import noppes.npcs.ai.EntityAIZigZagTarget;
import noppes.npcs.constants.EnumAnimation;
import noppes.npcs.constants.EnumJobType;
import noppes.npcs.constants.EnumModelType;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPotionType;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.constants.EnumStandingType;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobBoss;
import noppes.npcs.roles.JobInterface;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RoleInterface;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityNPCInterface extends EntityCreature implements IEntityAdditionalSpawnData, ICommandSender, IRangedAttackMob, IBossDisplayData{

	public DataDisplay display;
	public DataStats stats;
	public DataAI ai;
	public DataAdvanced advanced;
	public DataInventory inventory;
	
	public int[] startPos;
	public float scaleX, scaleY, scaleZ;
	public float labelOffset;
	private boolean wasKilled = false;
	public RoleInterface roleInterface;
	public JobInterface jobInterface;
	public HashMap<Integer, DialogOption> dialogs;
	public boolean hasDied = false;
	public long killedtime = 0;
	private int taskCount = 1;
	private boolean isSleeping = false;
	
	private EntityAIRangedAttack aiRange;

	public ResourceLocation textureLocation = null;
	public ResourceLocation textureGlowLocation = null;
	public ResourceLocation textureCloakLocation = null;
	
	public EnumAnimation currentAnimation = EnumAnimation.NONE;
	
	public int npcVersion = VersionCompatibility.ModRev;
	public IChatMessages messages;
	

	public EntityNPCInterface(World world) {
		super(world);
		display = new DataDisplay(this);
		stats = new DataStats(this);
		ai = new DataAI(this);		
		advanced = new DataAdvanced(this);
		inventory = new DataInventory(this);
		
		dialogs = new HashMap<Integer, DialogOption>();
		advanced.interactLines.lines.put(0, new Line("Hello {player}"));
		experienceValue = 0;
		labelOffset = 0;
		scaleX = scaleY = scaleZ = 0.9375f;
        
		updateHitbox();
		setFaction(getFaction().id);
		
		this.updateTasks();
	}
	protected void entityInit() {
		super.entityInit();
		//this.dataWatcher.addObject(12, Integer.valueOf(0));
		//this.dataWatcher.addObject(11, String.valueOf(""));//role
		//this.dataWatcher.addObject(12, String.valueOf(""));//job
		this.dataWatcher.addObject(13, String.valueOf(""));//faction
		this.dataWatcher.addObject(14, Integer.valueOf(0)); // Animation
		this.dataWatcher.addObject(15, Integer.valueOf(0)); // isWalking
		//this.dataWatcher.addObject(16, Integer.valueOf(20));//health
		this.dataWatcher.addObject(23, Byte.valueOf((byte) 0)); // offsets
		this.dataWatcher.addObject(24, Integer.valueOf(0)); // isKilled
		//this.dataWatcher.addObject(26, String.valueOf("")); //cloack
	}
    protected boolean isAIEnabled()
    {
        return true;
    }
    
    //Prevents npcs from being leashed
    @Override
    public boolean getLeashed()
    {
        return false;
    }

    @Override
    public boolean attackEntityAsMob(Entity par1Entity)
    {

        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
    	
    	if (stats.attackSpeed < 10)
        {
        	par1Entity.hurtResistantTime = 0;
        }
    	
        boolean var4 = par1Entity.attackEntityFrom(new NpcDamageSource("mob", this), f);

        if (var4)
        {
            if (stats.knockback > 0)
            {
                par1Entity.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)stats.knockback * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)stats.knockback * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }
        }

        if (stats.potionType != EnumPotionType.None)
        {
        	if (stats.potionType != EnumPotionType.Fire)
        	{
        		((EntityLivingBase)par1Entity).addPotionEffect(new PotionEffect(this.getPotionEffect(stats.potionType), stats.potionDuration * 20, stats.potionAmp));
        	}
        	else
        	{
        		par1Entity.setFire(stats.potionDuration);
        	}
        }
        return var4;
    }

    @Override
    public void onLivingUpdate()
    {
    	if(CustomNpcs.FreezeNPCs)
    		return;
        this.updateArmSwingProgress();
		if(!worldObj.isRemote){
	    	if(!isKilled() && !isAttacking() && this.ticksExisted % 20 == 0){
	    		if(stats.healthRegen && this.getHealth() < this.getMaxHealth()){
		    		float heal = getMaxHealth() / 20;
		    		heal(heal > 0?heal:1);
	    		}
	    		if(getFaction().getsAttacked){
	    			List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, this.boundingBox.expand(16, 16, 16));
	    			for(EntityMob mob : list)
	    			{
	    				if(mob.getAttackTarget() == null && this.canEntityBeSeen(mob)){
		    	    		if(mob instanceof EntityZombie && !mob.getEntityData().hasKey("AttackNpcs")){
		    	    	        mob.tasks.addTask(2, new EntityAIAttackOnCollide(mob, EntityLivingBase.class, 1.0D, false));
		    	    	        mob.getEntityData().setBoolean("AttackNpcs", true);
		    	    		}
	    					mob.setAttackTarget(this);
	    					break;
	    				}
	    			}
	    		}
	    	}
			if(getHealth() <= 0){
				clearActivePotions();
				dataWatcher.updateObject(24, 1);//used for isKilled
			}
			dataWatcher.updateObject(23, (byte)(this.getAttackTarget() != null?1:0)); //used for isAttacking
			dataWatcher.updateObject(15, getNavigator().noPath()?0:1); //used for isWalking
		}
		
		if(wasKilled != isKilled() && wasKilled){
			reset();
		}
		
		wasKilled = isKilled();
		
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && this.stats.burnInSun)
        {
            float f = this.getBrightness(1.0F);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
            {
                this.setFire(8);
            }
        }
		
        super.onLivingUpdate();
        
        if (worldObj.isRemote){
        	if(!display.cloakTexture.isEmpty())
        		cloakUpdate();
			if(currentAnimation.ordinal() != dataWatcher.getWatchableObjectInt(14)){
				currentAnimation = EnumAnimation.values()[dataWatcher.getWatchableObjectInt(14)];
				updateHitbox();
			}
			if(advanced.job == EnumJobType.Bard)
				((JobBard)jobInterface).onLivingUpdate();
        }
    }
    
	@Override
	public boolean interact(EntityPlayer player) {
		if(worldObj.isRemote)
			return false;
		
		ItemStack currentItem = player.inventory.getCurrentItem();
		if (currentItem != null) {
			if (currentItem.itemID == CustomItems.cloner.itemID || currentItem.itemID == CustomItems.wand.itemID) {
				setAttackTarget(null);
				setRevengeTarget(null);
				return true;
			}
			if (currentItem.itemID == CustomItems.moving.itemID) {
				setAttackTarget(null);
				if(currentItem.stackTagCompound == null)
					currentItem.stackTagCompound = new NBTTagCompound();
				
				currentItem.stackTagCompound.setInteger("NPCID",this.entityId);
				player.sendChatToPlayer(ChatMessageComponent.createFromText("Registered " + this.getEntityName() + " to your NPC Pather"));
				return true;
			}
		}
		if(isAttacking() || isKilled() || getFaction().isAggressiveToPlayer(player))
			return false;
		
		Dialog dialog = getDialog(player);
		PlayerQuestData playerdata = PlayerDataController.instance.getQuestData(player);
		QuestData data = playerdata.getQuestCompletion(player, this);
		if (data != null){
			NoppesUtilServer.sendData(player, EnumPacketType.QuestCompletion, data.quest.writeToNBT(new NBTTagCompound()));
		}
		else if (dialog != null){
			NoppesUtilServer.openDialog(player, this, dialog);
		}
		else if(roleInterface != null)
			roleInterface.interact(player);
		else
			say(player, advanced.getInteractLine());
		
		return true;
	}

	private Dialog getDialog(EntityPlayer player) {
		for (DialogOption option : dialogs.values()) {
			if (option == null)
				continue;
			if (!option.hasDialog())
				continue;
			Dialog dialog = option.getDialog();
			if (dialog.availability.isAvailable(player)){
				return dialog;
			}
		}
		return null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float i) {
        if (this.worldObj.isRemote || isKilled() || CustomNpcs.FreezeNPCs)
        {
            return false;
        }
        i = stats.resistances.applyResistance(damagesource, i);
                
        if(advanced.job == EnumJobType.Boss && ((JobBoss)jobInterface).applyDamage(i))
        	return false;
        
		Entity entity = damagesource.getEntity();
		if (entity instanceof EntityPlayer && getFaction().isFriendlyToPlayer((EntityPlayer) entity) || damagesource.damageType.equals("inWall"))
			return false;

		EntityLivingBase attackingEntity = null;
		if (entity instanceof EntityLivingBase) {
			attackingEntity = (EntityLivingBase) entity;
		}
		if ((entity instanceof EntityArrow) && ((EntityArrow) entity).shootingEntity != null) {
			attackingEntity = (EntityLivingBase) ((EntityArrow) entity).shootingEntity;
		}
		int faction = getFaction().id;
		if (attackingEntity instanceof EntityNPCInterface && ((EntityNPCInterface) attackingEntity).getFaction().id == faction)
			return false;
		
		if(attackingEntity == null)
			return super.attackEntityFrom(damagesource, i);

		if (isAttacking()){
			if(getAttackTarget() != null && attackingEntity != null && this.getDistanceSqToEntity(getAttackTarget()) > this.getDistanceSqToEntity(attackingEntity)){
				setAttackTarget(attackingEntity);
			}
			return super.attackEntityFrom(damagesource, i);
		}
		
		if (i > 0) {
			List<EntityNPCInterface> inRange = worldObj.getEntitiesWithinAABB(EntityNPCInterface.class, this.boundingBox.expand(32D, 16D, 32D));
			for (EntityNPCInterface npc : inRange) {
				if (npc.isKilled() || !npc.advanced.defendFaction || npc.getFaction().id != faction)
					continue;
				
				if (npc.canEntityBeSeen(this) || npc.canEntityBeSeen(attackingEntity))
					npc.onAttack(attackingEntity);
			}
			setAttackTarget(attackingEntity);
		}
		return super.attackEntityFrom(damagesource, i);
	}
	public void onAttack(EntityLivingBase entity) {
		if (entity == null || entity == this || isAttacking() || ai.onAttack == 3)
			return;
		super.setAttackTarget(entity);
	}
	
	@Override
    public void setAttackTarget(EntityLivingBase entity)
    {
    	if(entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage)
    		return;
    	
		if (entity != null && entity != this && ai.onAttack != 3 && !isAttacking() && !isRemote())
			saySurrounding(advanced.getAttackLine());
		
		super.setAttackTarget(entity);
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float f) {
        ItemStack proj = inventory.getProjectile();
        if(proj == null)
        {
    		updateTasks();
        	return;
        }

        EntityProjectile projectile = new EntityProjectile(this.worldObj, this, proj.copy(), true);
        if (this.display.modelType == EnumModelType.Golem)
        	projectile.setLocationAndAngles(projectile.posX, this.posY + 1.45F, projectile.posZ, projectile.rotationYaw, projectile.rotationPitch);
        double varX = entitylivingbase.posX - this.posX;
		double varY = entitylivingbase.boundingBox.minY + (double)(entitylivingbase.height / 2.0F) - (this.posY + this.getEyeHeight());
		double varZ = entitylivingbase.posZ - this.posZ;
		float varF = projectile.hasGravity() ? MathHelper.sqrt_double(varX * varX + varZ * varZ) : 0.0F;
		float angle = projectile.getAngleForXYZ(varX, varY, varZ, varF, f == 1 && this.ai.canFireIndirect);
		float accuracy = 20.0F - MathHelper.floor_float((float)stats.accuracy / 5.0F);
        projectile.setThrowableHeading(varX, varY, varZ, angle, accuracy);
        this.playSound(this.stats.fireSound, 2.0F, 1.0f);
        this.worldObj.spawnEntityInWorld(projectile);
        
    }
	
	private void clearTasks(EntityAITasks tasks){
        Iterator iterator = tasks.taskEntries.iterator();

        while (iterator.hasNext())
        {
            EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)iterator.next();
            EntityAIBase entityaibase1 = entityaitaskentry.action;

            if (tasks.executingTaskEntries.contains(entityaitaskentry))
            	entityaibase1.resetTask();
        }
        tasks.taskEntries = new ArrayList<EntityAITaskEntry>();
        tasks.executingTaskEntries = new ArrayList<EntityAITaskEntry>();
	}
	public void updateTasks() {
		if (worldObj == null || worldObj.isRemote)
			return;
		clearTasks(tasks);
		clearTasks(targetTasks);
		IEntitySelector attackEntitySelector = new AttackSelectorNPC(this);
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new EntityAIClosestTarget(this, EntityLivingBase.class, 0, this.ai.directLOS, false, attackEntitySelector));
		
		this.tasks.addTask(0, new EntityAIWaterNav(this));
		this.taskCount = 1;
		this.doorInteractType();
		this.seekShelter();
		this.setResponse();
		this.setCanSleep();
		this.setMoveType();
		this.addRegularEntries();
	}
	/*
	 * Branch task function for setting how an NPC responds to a threat
	 */
	public void setResponse(){		
		aiRange = null;
        
        if (this.ai.onAttack == 1) 
        	this.tasks.addTask(this.taskCount++, new EntityAIPanic(this, 1.4F));
        
        else if (this.ai.onAttack == 2)  {
        	this.tasks.addTask(this.taskCount++, new EntityAIAvoidTarget(this));
        	this.setCanSprint();
        }
        
        else if (this.ai.onAttack == 0) {
        	this.setCanLeap();
        	this.setCanSprint();
        	if (this.inventory.getProjectile() == null || this.ai.useRangeMelee == 2)
        	{
	        	switch(this.ai.tacticalVariant)
	        	{
	        		case Dodge : this.tasks.addTask(this.taskCount++, new EntityAIZigZagTarget(this, 1.0D, this.ai.tacticalRadius)); break;
	        		case Surround : this.tasks.addTask(this.taskCount++, new EntityAIOrbitTarget(this, 1.0D, this.ai.tacticalRadius, true)); break;
	        		case Ambush : this.tasks.addTask(this.taskCount++, new EntityAIAmbushTarget(this, 1.2D, this.ai.tacticalRadius, false)); break;
	        		case Stalk : this.tasks.addTask(this.taskCount++, new EntityAIStalkTarget(this, this.ai.tacticalRadius)); break;
	        	}
        	}
        	else
        	{
        		switch(this.ai.tacticalVariant)
        		{
        			case Dodge : this.tasks.addTask(this.taskCount++, new EntityAIDodgeShoot(this)); break;
        			case Surround : this.tasks.addTask(this.taskCount++, new EntityAIOrbitTarget(this, 1.0D, stats.rangedRange, false)); break;
        			case Ambush : this.tasks.addTask(this.taskCount++, new EntityAIAmbushTarget(this, 1.2D, this.ai.tacticalRadius, false)); break;
        			case Stalk : this.tasks.addTask(this.taskCount++, new EntityAIStalkTarget(this, this.ai.tacticalRadius)); break;
        		}
        	}
        	this.tasks.addTask(this.taskCount, new EntityAIAttackTarget(this, true));
        	this.tasks.addTask(this.taskCount++, aiRange = new EntityAIRangedAttack(this));
        }
    }

	/*
	 * Branch task function for setting if an NPC wanders or not
	 */
	public void setMoveType(){	
		if (ai.movingType == EnumMovingType.Wandering)
		{
			this.tasks.addTask(this.taskCount++, new EntityAIWander(this));
		}
		
		if (ai.movingType == EnumMovingType.MovingPath)
		{
			this.tasks.addTask(this.taskCount++, new EntityAIMovingPath(this));
		}
	}
	/*
	 * Branch task function for adjusting NPC door interactivity
	 */
	public void doorInteractType(){			
		EntityAIBase aiDoor = null;
		if (this.ai.doorInteract == 1)
		{
			this.tasks.addTask(this.taskCount++, aiDoor = new EntityAIOpenDoor(this, true));
		}
		else if (this.ai.doorInteract == 0)
		{
			this.tasks.addTask(this.taskCount++, aiDoor = new EntityAIBustDoor(this));
		}
		this.getNavigator().setBreakDoors(aiDoor != null);
	}
	
	/*
	 * Branch task function for finding shelter under the appropriate conditions
	 */
	public void seekShelter() {
		if (this.ai.findShelter == 0)
		{
			this.tasks.addTask(this.taskCount++, new EntityAIMoveIndoors(this));
		}
		else if (this.ai.findShelter == 1)
		{
			this.tasks.addTask(this.taskCount++, new EntityAIRestrictSun(this));
			this.tasks.addTask(this.taskCount++, new EntityAIFindShade(this));
		}
	}
	
	/*
	 * Branch task function for setting ability to sleep
	 */
	public void setCanSleep() {
		if (this.ai.canSleep)
			this.tasks.addTask(this.taskCount++, new EntityAIOccupyBed(this));
	}
	
	/*
	 * Branch task function for leaping
	 */
	public void setCanLeap() {
		if (this.ai.canLeap)
			this.tasks.addTask(this.taskCount++, new EntityAILeapAtTarget(this, 0.4F));
	}
	
	/*
	 * Branch task function for sprinting
	 */
	public void setCanSprint() {
		if (this.ai.canSprint)
			this.tasks.addTask(this.taskCount++, new EntityAISprintToTarget(this));
	}
	
	/*
	 * Add immutable task entries.
	 */
	public void addRegularEntries() {
		this.tasks.addTask(this.taskCount++, new EntityAIReturn(this));
		this.tasks.addTask(this.taskCount++, new EntityAILook(this));
		if (this.ai.standingType != EnumStandingType.NoRotation && this.ai.standingType != EnumStandingType.HeadRotation)
			this.tasks.addTask(this.taskCount++, new EntityAIWatchClosest(this, EntityLiving.class, 5.0F));
		this.tasks.addTask(this.taskCount++, new EntityAIWorldLines(this));
		this.tasks.addTask(this.taskCount++, new EntityAIJob(this));
		this.tasks.addTask(this.taskCount++, new EntityAIRole(this));
		this.tasks.addTask(this.taskCount++, new EntityAIAnimation(this));
	}
	
	/*
	 * Function for getting proper move speeds. This way we don't have to modify them every time we use them.
	 */
	public float getSpeed() {
		return (float)stats.moveSpeed / 20.0F;
	}
	
	public float getBlockPathWeight(int par1, int par2, int par3)
    {
        return this.worldObj.getLightBrightness(par1, par2, par3) - 0.5F;
    }
	/*
	 * Used for getting the applied potion effect from dataStats.
	 */
	private int getPotionEffect(EnumPotionType p) {
		switch(p)
		{
		case Poison : return Potion.poison.id;
		case Hunger : return Potion.hunger.id;
		case Weakness : return Potion.weakness.id;
		case Slowness : return Potion.moveSlowdown.id;
		case Nausea : return Potion.confusion.id;
		case Blindness : return Potion.blindness.id;
		case Wither : return Potion.wither.id;
		default : return 0;
		}
	}
	
	protected int decreaseAirSupply(int par1)
    {
		if (!this.stats.canDrown)
			return par1;
        return super.decreaseAirSupply(par1);
    }
	
	public EnumCreatureAttribute getCreatureAttribute()
    {
        return this.stats.creatureType;
    }

    public String getEntityName()
    {
		return display.name;
    }
    
    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return this.isEntityAlive() ? this.getAttackTarget() != null && !advanced.angrySound.equals("") ? this.advanced.angrySound : this.advanced.idleSound : null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return this.advanced.hurtSound;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return this.advanced.deathSound;
    }
    
    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
    	if (!this.advanced.stepSound.equals(""))
    	{
    		this.playSound(this.advanced.stepSound, 0.15F, 1.0F);
    	}
    	else
    	{
    		StepSound stepsound = Block.blocksList[par4].stepSound;

            if (this.worldObj.getBlockId(par1, par2 + 1, par3) == Block.snow.blockID)
            {
                stepsound = Block.snow.stepSound;
                this.playSound(stepsound.getStepSound(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
            }
            else if (!Block.blocksList[par4].blockMaterial.isLiquid())
            {
                this.playSound(stepsound.getStepSound(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
            }
    	}
    }

	public void saySurrounding(Line line) {
		if (line == null)
			return;
		List<EntityPlayer> inRange = worldObj.getEntitiesWithinAABB(
				EntityPlayer.class, this.boundingBox.expand(20D, 20D, 20D));
		for (EntityPlayer player : inRange)
			say(player, line);
	}

	public void say(EntityPlayer player, Line line) {
		if (line == null || !this.canEntityBeSeen(player))
			return;		
		if(!line.sound.isEmpty()){
			NoppesUtilServer.sendData(player, EnumPacketType.PlaySound, line.sound, (float)posX, (float)posY, (float)posZ);
		}
		NoppesUtilServer.sendData(player, EnumPacketType.CHATBUBBLE, this.entityId, line.text);
	}


	@Override
	public void addVelocity(double d, double d1, double d2) {
		if (isWalking() && !isKilled())
			super.addVelocity(d, d1, d2);
	}


	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		npcVersion = compound.getInteger("ModRev");
		VersionCompatibility.CheckNpcCompatibility(this, compound);
		
		display.readToNBT(compound);
		stats.readToNBT(compound);
		ai.readToNBT(compound);
		advanced.readToNBT(compound);
		inventory.readEntityFromNBT(compound);
		
		killedtime = compound.getLong("KilledTime");
		
		startPos = NBTTags.getIntArray(compound.getTagList("StartPos"));
		if (startPos == null || startPos.length != 3){
			startPos = new int[] { 
				MathHelper.floor_double(posX),
				MathHelper.floor_double(posY),
				MathHelper.floor_double(posZ) };
		}
		
		dialogs = getDialogs(compound.getTagList("NPCDialogOptions"));		
		
		textureLocation = null;
		textureGlowLocation = null;
		textureCloakLocation = null;
        
		this.updateTasks();
	}


	private HashMap<Integer, DialogOption> getDialogs(NBTTagList tagList) {
		HashMap<Integer, DialogOption> map = new HashMap<Integer, DialogOption>();
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.tagAt(i);
			int slot = nbttagcompound.getInteger("DialogSlot");
			DialogOption option = new DialogOption();
			option.readNBT(nbttagcompound.getCompoundTag("NPCDialog"));
			map.put(slot, option);

		}
		return map;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		display.writeToNBT(compound);
		stats.writeToNBT(compound);
		ai.writeToNBT(compound);
		advanced.writeToNBT(compound);
		inventory.writeEntityToNBT(compound);

		compound.setLong("KilledTime", killedtime);
		if (startPos == null){
			startPos = new int[] { 
				MathHelper.floor_double(posX),
				MathHelper.floor_double(posY),
				MathHelper.floor_double(posZ) };
		}
		
		compound.setTag("StartPos", NBTTags.nbtIntArray(startPos));
		compound.setTag("NPCDialogOptions", nbtDialogs(dialogs));
		compound.setInteger("ModRev", npcVersion);
		
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(stats.maxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(CustomNpcs.NpcNavRange);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(this.getSpeed());
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(stats.attackStrength);
	}

	private NBTTagList nbtDialogs(HashMap<Integer, DialogOption> dialogs2) {
		NBTTagList nbttaglist = new NBTTagList();
		for (int slot : dialogs2.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("DialogSlot", slot);
			nbttagcompound.setCompoundTag("NPCDialog", dialogs2.get(slot)
					.writeNBT());
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public void updateHitbox() {
		
		if(currentAnimation == EnumAnimation.LYING){
			width = height = 0.2f;
		}
		else if (currentAnimation == EnumAnimation.SITTING){
			width = 0.6f;
			height = 1.3f;
		}
		else{
			width = 0.6f;
			height = 1.8f;
		}
		width = (width / 5f) * display.modelSize;
		height = (height / 5f) * display.modelSize;
	}

	public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		if (itemstack == null) {
			return;
		}
		EntityItem entityitem = new EntityItem(worldObj, posX,
				(posY - 0.30000001192092896D) + (double) getEyeHeight(), posZ,
				itemstack);
		entityitem.delayBeforeCanPickup = 40;
		if (flag) {
			float f2 = rand.nextFloat() * 0.5F;
			float f4 = rand.nextFloat() * 3.141593F * 2.0F;
			entityitem.motionX = -MathHelper.sin(f4) * f2;
			entityitem.motionZ = MathHelper.cos(f4) * f2;
			entityitem.motionY = 0.20000000298023224D;
		} else {
			float f1 = 0.3F;
			entityitem.motionX = -MathHelper
					.sin((rotationYaw / 180F) * 3.141593F)
					* MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
			entityitem.motionZ = MathHelper
					.cos((rotationYaw / 180F) * 3.141593F)
					* MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
			entityitem.motionY = -MathHelper
					.sin((rotationPitch / 180F) * 3.141593F) * f1 + 0.1F;
			f1 = 0.02F;
			float f3 = rand.nextFloat() * 3.141593F * 2.0F;
			f1 *= rand.nextFloat();
			entityitem.motionX += Math.cos(f3) * (double) f1;
			entityitem.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			entityitem.motionZ += Math.sin(f3) * (double) f1;
		}
		worldObj.spawnEntityInWorld(entityitem);
	}
	@Override
	public void onDeathUpdate(){
		if(stats.spawnCycle == 3){
			super.onDeathUpdate();
			return;
		}
		
		++this.deathTime;
		if(worldObj.isRemote)
			return;
		if(!hasDied){
			setDead();
		}
		killedtime--;
		if (killedtime <= 0) {
			if (stats.spawnCycle == 0 || (this.worldObj.isDaytime() && stats.spawnCycle == 1) || (!this.worldObj.isDaytime() && stats.spawnCycle == 2)) {
				reset();
			}
		}
	}
	
	public void reset() {
		hasDied = false;
		setHealth(getMaxHealth());
		dataWatcher.updateObject(24, 0); // iskilled false
		dataWatcher.updateObject(14, 0); // animation Normal
		dataWatcher.updateObject(15, 0); // is walking false
		this.setAttackTarget(null);
		this.setRevengeTarget(null);
		this.deathTime = 0;
		//fleeingTick = 0;
		if(startPos != null)
			setLocationAndAngles(getStartXPos(), getStartYPos() + 1, getStartZPos(), rotationYaw, rotationPitch);
		killedtime = 0;
		extinguish();
		this.clearActivePotions();
		moveEntityWithHeading(0,0);
		distanceWalkedModified = 0;
		getNavigator().clearPathEntity();
		currentAnimation = EnumAnimation.NONE;
		updateHitbox();
		this.updateTasks();
		ai.movingPos = 0;
		
		if(jobInterface != null)
			jobInterface.reset();
	}
	
	
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;

	public void cloakUpdate() {
		field_20066_r = field_20063_u;
		field_20065_s = field_20062_v;
		field_20064_t = field_20061_w;
		double d = posX - field_20063_u;
		double d1 = posY - field_20062_v;
		double d2 = posZ - field_20061_w;
		double d3 = 10D;
		if (d > d3) {
			field_20066_r = field_20063_u = posX;
		}
		if (d2 > d3) {
			field_20064_t = field_20061_w = posZ;
		}
		if (d1 > d3) {
			field_20065_s = field_20062_v = posY;
		}
		if (d < -d3) {
			field_20066_r = field_20063_u = posX;
		}
		if (d2 < -d3) {
			field_20064_t = field_20061_w = posZ;
		}
		if (d1 < -d3) {
			field_20065_s = field_20062_v = posY;
		}
		field_20063_u += d * 0.25D;
		field_20061_w += d2 * 0.25D;
		field_20062_v += d1 * 0.25D;
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public ItemStack getHeldItem() {
		if (isAttacking())
			return inventory.getWeapon();
		else if (jobInterface != null && jobInterface.overrideMainHand)
			return jobInterface.mainhand;
		else
			return inventory.getWeapon();
	}

	public ItemStack getOffHand() {
		if (isAttacking())
			return inventory.getOffHand();
		else if (jobInterface != null && jobInterface.overrideOffHand)
			return jobInterface.offhand;
		else
			return inventory.getOffHand();
	}
	
	@Override
	public void onDeath(DamageSource par1DamageSource)
	{
		inventory.dropStuff(this.recentlyHit > 0);
		if(!isRemote())
			saySurrounding(advanced.getKilledLine());
		super.onDeath(par1DamageSource);
	}
	
	@Override
	public void setDead() {
		hasDied = true;
		if(worldObj.isRemote || stats.spawnCycle == 3){
			this.spawnExplosionParticle();
			delete();
		}
		else {
			setHealth(-1);
			killedtime = stats.respawnTime * 10;
			
			if (advanced.role != EnumRoleType.None && roleInterface != null)
				roleInterface.killed();
			if (advanced.job != EnumJobType.None && jobInterface != null)
				jobInterface.killed();
		}
	}

	public void delete() {
		if (advanced.role != EnumRoleType.None && roleInterface != null)
			roleInterface.delete();
		if (advanced.job != EnumJobType.None && jobInterface != null)
			jobInterface.delete();
		super.setDead();
	}
	
	public float getStartXPos(){
		return startPos[0] + ai.bodyOffsetX / 10;
	}
	
	public float getStartZPos(){
		return startPos[2] + ai.bodyOffsetZ / 10;
	}

	public boolean isVeryNearAssignedPlace() {
		double xx = posX - getStartXPos();
		double zz = posZ - getStartZPos();
		if (xx < -0.2 || xx > 0.2)
			return false;
		if (zz < -0.2 || zz > 0.2)
			return false;
		return true;
	}

	public Icon getItemIcon(ItemStack par1ItemStack, int par2)
    {
		EntityPlayer player = CustomNpcs.proxy.getPlayer();
		if(player == null)
			return super.getItemIcon(par1ItemStack, par2);
		return player.getItemIcon(par1ItemStack, par2);
    }

	public double getStartYPos() {
		int i = startPos[0];
		int j = startPos[1];
		int k = startPos[2];
		double yy = 0;
		for (int ii = j; ii >= 0; ii--) {
			int id = worldObj.getBlockId(i, ii, k);
			if (id == 0)
				continue;
			Block block = Block.blocksList[id];
			AxisAlignedBB bb = block.getCollisionBoundingBoxFromPool(worldObj,
					i, ii, k);
			if (bb == null)
				continue;
			yy = bb.maxY;
			break;
		}
		if (yy == 0)
			setDead();
		yy += 0.5;
		return yy;
	}

	public void givePlayerItem(EntityPlayer player, ItemStack item) {
		if (worldObj.isRemote) {
			return;
		}
		item = item.copy();
		float f = 0.7F;
		double d = (double) (worldObj.rand.nextFloat() * f)
				+ (double) (1.0F - f);
		double d1 = (double) (worldObj.rand.nextFloat() * f)
				+ (double) (1.0F - f);
		double d2 = (double) (worldObj.rand.nextFloat() * f)
				+ (double) (1.0F - f);
		EntityItem entityitem = new EntityItem(worldObj, posX + d, posY + d1,
				posZ + d2, item);
		entityitem.delayBeforeCanPickup = 2;
		worldObj.spawnEntityInWorld(entityitem);

		int i = item.stackSize;

		if (player.inventory.addItemStackToInventory(item)) {
			worldObj.playSoundAtEntity(
					entityitem,
					"random.pop",
					0.2F,
					((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(entityitem, i);

			if (item.stackSize <= 0) {
				entityitem.setDead();
			}
		}
	}
	
	@Override
	public boolean isPlayerSleeping() {
		return currentAnimation == EnumAnimation.LYING;
	}

	@Override
	public boolean isRiding() {
		return currentAnimation == EnumAnimation.SITTING;
	}

	public boolean isWalking() {
		return ai.movingType != EnumMovingType.Standing || isAttacking() || isFollowerWithOwner() || dataWatcher.getWatchableObjectInt(15) == 1;
	}

	public boolean isSneaking() {
		return currentAnimation == EnumAnimation.SNEAKING;
	}
	@Override
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
        this.isAirBorne = true;
        float f1 = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
        float f2 = 0.5F *  (2 - stats.resistances.knockback);
        this.motionX /= 2.0D;
        this.motionY /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= par3 / (double)f1 * (double)f2;
        this.motionY += 0.2 + f2 / 2;
        this.motionZ -= par5 / (double)f1 * (double)f2;

        if (this.motionY > 0.4000000059604645D)
        {
            this.motionY = 0.4000000059604645D;
        }
    }
    
	public Faction getFaction() {
		String[] split = dataWatcher.getWatchableObjectString(13).split(":");
		int faction = 0;
		if(worldObj == null || split.length <= 1 && worldObj.isRemote)
			return new Faction();
		if(split.length > 1)
			faction = Integer.parseInt(split[0]);
		if(worldObj.isRemote){
			Faction fac = new Faction();
			fac.id = faction;
			fac.color = Integer.parseInt(split[1]);
			fac.name = split[2];
			return fac;
		}
		else{
			Faction fac = FactionController.getInstance().getFaction(faction);
			if (fac == null) {
				faction = FactionController.getInstance().getFirstFactionId();
				fac = FactionController.getInstance().getFaction(faction);
			}
			return fac;
		}
	}
	public boolean isRemote(){
		return worldObj == null || worldObj.isRemote;
	}
	public void setFaction(int integer) {
		if(integer < 0|| isRemote())
			return;
		Faction faction = FactionController.getInstance().getFaction(integer);
		if(faction == null)
			return;
		String str = faction.id + ":" + faction.color + ":" + faction.name;
		if(str.length() > 64)
			str = str.substring(0, 64);
		dataWatcher.updateObject(13, str);
	}
	public boolean isFollowerWithOwner() {
		return advanced.role == EnumRoleType.Follower
				&& ((RoleFollower) roleInterface).getDaysLeft() > 0;
	}
	
	public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        return (this.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD && par1PotionEffect.getPotionID() == Potion.poison.id) ? false : super.isPotionApplicable(par1PotionEffect);
    }

	public NBTTagCompound copy() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBTOptional(nbttagcompound);
		nbttagcompound.setInteger("EntityId", this.entityId);
		return nbttagcompound;
	}

	public boolean inNormalState() {
		//TODO not fleeing
		return !isAttacking() && !isFollowerWithOwner();
	}

	public boolean isAttacking() {
		return dataWatcher.getWatchableObjectByte(23) == 1;
	}

	public boolean isKilled() {
		return dataWatcher.getWatchableObjectInt(24) == 1;
	}

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		NBTTagCompound compound = new NBTTagCompound();
		this.writeEntityToNBT(compound);
		if(advanced.role == EnumRoleType.Trader){
			compound.removeTag("TraderCurrency");
			compound.removeTag("TraderSold");
		}
		if(advanced.job == EnumJobType.Boss){
			compound.removeTag("BossOriginal");
			compound.removeTag("BossNBT1");
			compound.removeTag("BossNBT2");
			compound.removeTag("BossNBT3");
			compound.removeTag("BossNBT4");
			compound.removeTag("BossNBT5");
			compound.removeTag("BossNBT6");
			compound.removeTag("BossNBT7");
			compound.removeTag("BossNBT8");
			compound.removeTag("BossNBT9");
		}
		if(advanced.job == EnumJobType.Spawner){
			compound.removeTag("SpawnerNBT1");
			compound.removeTag("SpawnerNBT2");
			compound.removeTag("SpawnerNBT3");
			compound.removeTag("SpawnerNBT4");
			compound.removeTag("SpawnerNBT5");
			compound.removeTag("SpawnerNBT6");
		}
		try {
			CompressedStreamTools.write(compound, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		try {
			NBTTagCompound compound = CompressedStreamTools.read(data);
			this.readEntityFromNBT(compound);
		} catch (Exception e) {
		} 
	}

	@Override
	public String getCommandSenderName() {
		return display.name;
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		if(CustomNpcs.NpcUseOpCommands)
	        return true;
        return var1 <= 2;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
        return new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
	}
	
	@Override
	public boolean canAttackClass(Class par1Class)
    {
        return EntityBat.class != par1Class;
    }
	public void setImmuneToFire(boolean immuneToFire) {
		this.isImmuneToFire = immuneToFire;
		stats.immuneToFire = immuneToFire;
	}
	
	public void setAvoidWater(boolean avoidWater) {
		this.getNavigator().setAvoidsWater(avoidWater);
		ai.avoidsWater = avoidWater;
	}
	public void setSleeping(boolean b) {
		this.isSleeping = b;
		
	}
	public boolean isSleeping() {
		return this.isSleeping;
	}
	
	protected void fall(float par1) {
		if (!this.stats.noFallDamage)
			super.fall(par1);
	}
	
	@Override
	public boolean canBeCollidedWith(){
		return !isKilled();
	}
	
	protected void applyEntityAttributes()
    {
		super.applyEntityAttributes();
        this.getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage);
    }
	
	public EntityAIRangedAttack getRangedTask()
	{
		return this.aiRange;
	}
	
	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
		
	}    
	
	public ItemStack func_130225_q(int i)
    {
        return inventory.armorItemInSlot(i);
    }
	
	@Override
	public void setInPortal(){
		//prevent npcs from walking into portals
	}
	@Override
	public World getEntityWorld() {
		return worldObj;
	}

	@Override
    public boolean isInvisibleToPlayer(EntityPlayer player){
        return display.visible == 1 && (player.getHeldItem() == null || player.getHeldItem().getItem() != CustomItems.wand);
    }
	
	public boolean isInvisible(){
		return display.visible != 0;
	}
}

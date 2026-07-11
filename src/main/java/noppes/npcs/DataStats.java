package noppes.npcs;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumParticleType;
import noppes.npcs.constants.EnumPotionType;

public class DataStats {
	
	public int attackStrength = 5, attackSpeed = 20, attackRange = 2, knockback = 0;
	public int fireDelay = 20, delayVariance = 40, rangedRange = 15, fireRate = 5, burstCount = 1, accuracy = 90;
	public int moveSpeed = 6;
	public int aggroRange = 16;
	
	public EnumPotionType potionType = EnumPotionType.None;
	public int potionDuration = 5; //20 = 1 second
	public int potionAmp = 0;

	public int maxHealth = 20;
	public int respawnTime = 20;
	public int spawnCycle = 0;
	public boolean hideKilledBody = false;
	
	public Resistances resistances = new Resistances();
	
	protected boolean immuneToFire = false;
	public boolean canDrown = true;
	public boolean burnInSun = false;
	public boolean noFallDamage = false;
	public boolean healthRegen = true;
	
	
	public int pDamage = 4, pImpact = 0, pSize = 5, pSpeed = 10, pArea = 0, pDur = 5;
    public boolean pPhysics = true, pXlr8 = false, pGlows = false, pExplode = false;
    public boolean pRender3D = false, pSpin = false, pStick = false;
    public EnumPotionType pEffect = EnumPotionType.None;
    public EnumParticleType pTrail = EnumParticleType.None;
    public int pEffAmp = 0;
    public String fireSound = "random.bow";
	
	public EnumCreatureAttribute creatureType = EnumCreatureAttribute.UNDEFINED;
	
	private EntityNPCInterface npc;
	
	public DataStats(EntityNPCInterface npc){
		this.npc = npc;
	}
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{	
		compound.setCompoundTag("Resistances", resistances.writeToNBT());
		compound.setInteger("MaxHealth", maxHealth);
		compound.setInteger("AggroRange", aggroRange);
		compound.setBoolean("HideBodyWhenKilled", hideKilledBody);
		compound.setInteger("RespawnTime", respawnTime);
		compound.setInteger("SpawnCycle", spawnCycle);
		compound.setInteger("CreatureType", creatureType.ordinal());
		compound.setInteger("MoveSpeed", moveSpeed);
		compound.setBoolean("HealthRegen", healthRegen);
		
		compound.setInteger("AttackStrenght", attackStrength);
		compound.setInteger("AttackRange", attackRange);
		compound.setInteger("AttackSpeed", attackSpeed);
		compound.setInteger("KnockBack", knockback);
		compound.setInteger("PotionEffect", potionType.ordinal());
		compound.setInteger("PotionDuration", potionDuration);
		compound.setInteger("PotionAmp", potionAmp);
		
		compound.setInteger("MaxFiringRange", rangedRange);
		compound.setInteger("FireRate", fireRate);
		compound.setInteger("FiringDelay", fireDelay);
		compound.setInteger("DelayVariance", delayVariance);
		compound.setInteger("BurstCount", burstCount);
		compound.setInteger("Accuracy", accuracy);
		
		compound.setInteger("pDamage", pDamage);
		compound.setInteger("pImpact", pImpact);
		compound.setInteger("pSize", pSize);
		compound.setInteger("pSpeed", pSpeed);
		compound.setInteger("pArea", pArea);
		compound.setInteger("pDur", pDur);
		compound.setBoolean("pPhysics", pPhysics);
		compound.setBoolean("pXlr8", pXlr8);
		compound.setBoolean("pGlows", pGlows);
		compound.setBoolean("pExplode", pExplode);
		compound.setBoolean("pRender3D", pRender3D);
		compound.setBoolean("pSpin", pSpin);
		compound.setBoolean("pStick", pStick);
		compound.setInteger("pEffect", pEffect.ordinal());
		compound.setInteger("pTrail", pTrail.ordinal());
		compound.setInteger("pEffAmp", pEffAmp);
		compound.setString("FiringSound", fireSound);
		
		compound.setBoolean("ImmuneToFire", immuneToFire);
		compound.setBoolean("CanDrown", canDrown);
		compound.setBoolean("BurnInSun", burnInSun);
		compound.setBoolean("NoFallDamage", noFallDamage);
		
		return compound;
	}

	public void readToNBT(NBTTagCompound compound)
	{
		resistances.readToNBT(compound.getCompoundTag("Resistances"));
		maxHealth = compound.getInteger("MaxHealth");
		hideKilledBody = compound.getBoolean("HideBodyWhenKilled");
		aggroRange = compound.getInteger("AggroRange");
		respawnTime = compound.getInteger("RespawnTime");
		spawnCycle = compound.getInteger("SpawnCycle");
		creatureType = EnumCreatureAttribute.values()[compound.getInteger("CreatureType") % EnumPotionType.values().length];
		moveSpeed = compound.getInteger("MoveSpeed");
		healthRegen = compound.getBoolean("HealthRegen");
		
		attackStrength = compound.getInteger("AttackStrenght");
		attackSpeed = compound.getInteger("AttackSpeed");
		attackRange = compound.getInteger("AttackRange");
		knockback = compound.getInteger("KnockBack");
		potionType = EnumPotionType.values()[compound.getInteger("PotionEffect") % EnumPotionType.values().length];
		potionDuration = compound.getInteger("PotionDuration");
		potionAmp = compound.getInteger("PotionAmp");
		
		rangedRange = compound.getInteger("MaxFiringRange");
		fireRate = compound.getInteger("FireRate");
		fireDelay = compound.getInteger("FiringDelay");
		delayVariance = compound.getInteger("DelayVariance");
		burstCount = compound.getInteger("BurstCount");
		accuracy = compound.getInteger("Accuracy");	
		
		pDamage = compound.getInteger("pDamage");
		pImpact = compound.getInteger("pImpact");
		pSize = compound.getInteger("pSize");
		pSpeed = compound.getInteger("pSpeed");
		pArea = compound.getInteger("pArea");
		pDur = compound.getInteger("pDur");
		pPhysics = compound.getBoolean("pPhysics");
		pXlr8 = compound.getBoolean("pXlr8");
		pGlows = compound.getBoolean("pGlows");
		pExplode = compound.getBoolean("pExplode");
		pRender3D = compound.getBoolean("pRender3D");
		pSpin = compound.getBoolean("pSpin");
		pStick = compound.getBoolean("pStick");
		pEffect = EnumPotionType.values()[compound.getInteger("pEffect") % EnumPotionType.values().length];
		pTrail = EnumParticleType.values()[compound.getInteger("pTrail") % EnumParticleType.values().length];
		pEffAmp = compound.getInteger("pEffAmp");
		fireSound = compound.getString("FiringSound");
		
		immuneToFire = compound.getBoolean("ImmuneToFire");		
		canDrown = compound.getBoolean("CanDrown");
		burnInSun = compound.getBoolean("BurnInSun");
		noFallDamage = compound.getBoolean("NoFallDamage");
		
		npc.setImmuneToFire(immuneToFire);
		npc.updateHitbox();
	}
}

package noppes.npcs;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumJobType;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.controllers.FactionOptions;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.Lines;
import noppes.npcs.roles.JobBard;
import noppes.npcs.roles.JobBoss;
import noppes.npcs.roles.JobConversation;
import noppes.npcs.roles.JobGuard;
import noppes.npcs.roles.JobHealer;
import noppes.npcs.roles.JobItemGiver;
import noppes.npcs.roles.JobSpawner;
import noppes.npcs.roles.RoleBank;
import noppes.npcs.roles.RoleFollower;
import noppes.npcs.roles.RolePostman;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;

public class DataAdvanced {
	public Lines interactLines = new Lines();
	public Lines worldLines = new Lines();
	public Lines attackLines = new Lines();
	public Lines killedLines = new Lines();
	
	public String idleSound = "";
	public String angrySound = "";
	public String hurtSound = "damage.hit";
	public String deathSound = "damage.hit";
	public String stepSound = "";
	
	private EntityNPCInterface npc;
	public FactionOptions factions = new FactionOptions();
	
	public EnumRoleType role = EnumRoleType.None;
	public EnumJobType job = EnumJobType.None;

	public boolean attackOtherFactions = false;
	public boolean defendFaction = false;
	
	public DataAdvanced(EntityNPCInterface npc){
		this.npc = npc;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setCompoundTag("NpcLines", worldLines.writeToNBT());
		compound.setCompoundTag("NpcKilledLines", killedLines.writeToNBT());
		compound.setCompoundTag("NpcInteractLines",	interactLines.writeToNBT());
		compound.setCompoundTag("NpcAttackLines", attackLines.writeToNBT());
		
		compound.setString("NpcIdleSound", idleSound);
		compound.setString("NpcAngrySound", angrySound);
		compound.setString("NpcHurtSound", hurtSound);
		compound.setString("NpcDeathSound", deathSound);
		compound.setString("NpcStepSound", stepSound);
		
		compound.setInteger("FactionID", npc.getFaction().id);
		compound.setBoolean("AttackOtherFactions", attackOtherFactions);
		compound.setBoolean("DefendFaction", defendFaction);

		compound.setInteger("Role", role.ordinal());
		compound.setInteger("NpcJob", job.ordinal());
		compound.setCompoundTag("FactionPoints", factions.writeToNBT(new NBTTagCompound()));

		if (role != EnumRoleType.None && npc.roleInterface != null)
			npc.roleInterface.writeEntityToNBT(compound);
		if (job != EnumJobType.None && npc.jobInterface != null)
			npc.jobInterface.writeEntityToNBT(compound);
		
		return compound;
	}

	public void readToNBT(NBTTagCompound compound) {
		interactLines.readNBT(compound.getCompoundTag("NpcInteractLines"));
		worldLines.readNBT(compound.getCompoundTag("NpcLines"));
		attackLines.readNBT(compound.getCompoundTag("NpcAttackLines"));
		killedLines.readNBT(compound.getCompoundTag("NpcKilledLines"));
		
		idleSound = compound.getString("NpcIdleSound");
		angrySound = compound.getString("NpcAngrySound");
		hurtSound = compound.getString("NpcHurtSound");
		deathSound = compound.getString("NpcDeathSound");
		stepSound = compound.getString("NpcStepSound");

		npc.setFaction(compound.getInteger("FactionID"));
		attackOtherFactions = compound.getBoolean("AttackOtherFactions");
		defendFaction = compound.getBoolean("DefendFaction");
		
		setRole(compound.getInteger("Role"));
		setJob(compound.getInteger("NpcJob"));

		factions.readFromNBT(compound.getCompoundTag("FactionPoints"));

		if (role != EnumRoleType.None&& npc.roleInterface != null)
			npc.roleInterface.readEntityFromNBT(compound);
		if (job != EnumJobType.None && npc.jobInterface != null)
			npc.jobInterface.readEntityFromNBT(compound);
	}

	public Line getInteractLine() {
		return interactLines.getLine();
	}
	public Line getAttackLine() {
		return attackLines.getLine();
	}
	public Line getKilledLine() {
		return killedLines.getLine();
	}
	public Line getWorldLine() {
		return worldLines.getLine();
	}

	public void setRole(int i) {
		if (EnumRoleType.values().length <= i)
			i -= 2;
		role = EnumRoleType.values()[i];

		if (role == EnumRoleType.None)
			npc.roleInterface = null;
		else if (role == EnumRoleType.Trader && !(npc.roleInterface instanceof RoleTrader))
			npc.roleInterface = new RoleTrader(npc);
		else if (role == EnumRoleType.Follower && !(npc.roleInterface instanceof RoleFollower))
			npc.roleInterface = new RoleFollower(npc);
		else if (role == EnumRoleType.Bank && !(npc.roleInterface instanceof RoleBank))
			npc.roleInterface = new RoleBank(npc);
		else if (role == EnumRoleType.Transporter && !(npc.roleInterface instanceof RoleTransporter))
			npc.roleInterface = new RoleTransporter(npc);
		else if (role == EnumRoleType.Postman && !(npc.roleInterface instanceof RolePostman))
			npc.roleInterface = new RolePostman(npc);
	}

	public void setJob(int i) {
		job = EnumJobType.values()[i % EnumJobType.values().length];
		
		if (job == EnumJobType.None)
			npc.jobInterface = null;
		else if (job == EnumJobType.Bard && !(npc.jobInterface instanceof JobBard))
			npc.jobInterface = new JobBard(npc);
		else if (job == EnumJobType.Healer && !(npc.jobInterface instanceof JobHealer))
			npc.jobInterface = new JobHealer(npc);
		else if (job == EnumJobType.Guard && !(npc.jobInterface instanceof JobGuard))
			npc.jobInterface = new JobGuard(npc);
		else if (job == EnumJobType.ItemGiver && !(npc.jobInterface instanceof JobItemGiver))
			npc.jobInterface = new JobItemGiver(npc);
		else if (job == EnumJobType.Boss && !(npc.jobInterface instanceof JobBoss))
			npc.jobInterface = new JobBoss(npc);
		else if (job == EnumJobType.Spawner && !(npc.jobInterface instanceof JobSpawner))
			npc.jobInterface = new JobSpawner(npc);
		else if (job == EnumJobType.Conversation && !(npc.jobInterface instanceof JobConversation))
			npc.jobInterface = new JobConversation(npc);
	}

	public boolean hasWorldLines() {
		return !worldLines.isEmpty();
	}
}

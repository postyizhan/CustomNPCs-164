package noppes.npcs.controllers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ICompatibilty;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFaction;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumDayTime;

public class Availability implements ICompatibilty {
	public int version = VersionCompatibility.ModRev;
	
	public EnumAvailabilityDialog dialogAvailable = EnumAvailabilityDialog.Always;
	public EnumAvailabilityQuest questAvailable = EnumAvailabilityQuest.Always;

	public int questId = -1;
	public int dialogId = -1;
	
	public EnumAvailabilityDialog dialog2Available = EnumAvailabilityDialog.Always;
	public EnumAvailabilityQuest quest2Available = EnumAvailabilityQuest.Always;

	public int quest2Id = -1;
	public int dialog2Id = -1;

	public EnumDayTime daytime = EnumDayTime.Always;
	
	public int factionId = -1;
	public int faction2Id = -1;
	
	public EnumAvailabilityFactionType factionAvailable = EnumAvailabilityFactionType.Always;
	public EnumAvailabilityFactionType faction2Available = EnumAvailabilityFactionType.Always;

	public EnumAvailabilityFaction factionStance = EnumAvailabilityFaction.Friendly;
	public EnumAvailabilityFaction faction2Stance = EnumAvailabilityFaction.Friendly;

	public int minPlayerLevel = 0;
	
	
    public void readFromNBT(NBTTagCompound compound)
    {
		version = compound.getInteger("ModRev");
		VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
		
    	setDialogAvailability(compound.getInteger("AvailabilityDialog"));
    	setQuestAvailability(compound.getInteger("AvailabilityQuest"));
    	setFactionAvailability(compound.getInteger("AvailabilityFaction"));
    	setFactionAvailabilityStance(compound.getInteger("AvailabilityFactionStance"));

    	questId = compound.getInteger("AvailabilityQuestId");
    	dialogId = compound.getInteger("AvailabilityDialogId");
    	factionId = compound.getInteger("AvailabilityFactionId");
    	
    	setDialog2Availability(compound.getInteger("AvailabilityDialog2"));
    	setQuest2Availability(compound.getInteger("AvailabilityQuest2"));
    	setFaction2Availability(compound.getInteger("AvailabilityFaction2"));
    	setFaction2AvailabilityStance(compound.getInteger("AvailabilityFaction2Stance"));

    	quest2Id = compound.getInteger("AvailabilityQuest2Id");
    	dialog2Id = compound.getInteger("AvailabilityDialog2Id");
    	faction2Id = compound.getInteger("AvailabilityFaction2Id");
    	
    	daytime = EnumDayTime.values()[compound.getInteger("AvailabilityDayTime")];

    	minPlayerLevel = compound.getInteger("AvailabilityMinPlayerLevel");
    }
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		compound.setInteger("ModRev", version);
		
		compound.setInteger("AvailabilityDialog", dialogAvailable.ordinal());
		compound.setInteger("AvailabilityQuest", questAvailable.ordinal());
		compound.setInteger("AvailabilityFaction", factionAvailable.ordinal());
		compound.setInteger("AvailabilityFactionStance", factionStance.ordinal());
		
		compound.setInteger("AvailabilityQuestId", questId);
		compound.setInteger("AvailabilityDialogId", dialogId);
		compound.setInteger("AvailabilityFactionId", factionId);

		compound.setInteger("AvailabilityDialog2", dialog2Available.ordinal());
		compound.setInteger("AvailabilityQuest2", quest2Available.ordinal());
		compound.setInteger("AvailabilityFaction2", faction2Available.ordinal());
		compound.setInteger("AvailabilityFaction2Stance", faction2Stance.ordinal());
		
		compound.setInteger("AvailabilityQuest2Id", quest2Id);
		compound.setInteger("AvailabilityDialog2Id", dialog2Id);
		compound.setInteger("AvailabilityFaction2Id", faction2Id);

		compound.setInteger("AvailabilityDayTime", daytime.ordinal());
		compound.setInteger("AvailabilityMinPlayerLevel", minPlayerLevel);
		return compound;
    }
    public void setDialogAvailability(int integer) {
    	dialogAvailable =  EnumAvailabilityDialog.values()[integer];
	}
    public void setQuestAvailability(int integer) {
    	questAvailable = EnumAvailabilityQuest.values()[integer];
	}
    public void setDialog2Availability(int integer) {
    	dialog2Available =  EnumAvailabilityDialog.values()[integer];
	}
    public void setQuest2Availability(int integer) {
    	quest2Available = EnumAvailabilityQuest.values()[integer];
	}
	public void setFactionAvailability(int value) {
    	factionAvailable =  EnumAvailabilityFactionType.values()[value];
	}
	public void setFaction2Availability(int value) {
    	faction2Available =  EnumAvailabilityFactionType.values()[value];
	}
	public void setFactionAvailabilityStance(int integer) {
		factionStance = EnumAvailabilityFaction.values()[integer];
	}
	public void setFaction2AvailabilityStance(int integer) {
		faction2Stance = EnumAvailabilityFaction.values()[integer];
	}
	public boolean isAvailable(EntityPlayer player){
		if(daytime == EnumDayTime.Day){
			long time = player.worldObj.getWorldTime() % 24000;
			if(time > 12000)
				return false;
		}
		if(daytime == EnumDayTime.Night){
			long time = player.worldObj.getWorldTime() % 24000;
			if(time < 12000)
				return false;
		}
		
		if(!dialogAvailable(dialogId, dialogAvailable, player))
			return false;
		
		if(!dialogAvailable(dialog2Id, dialog2Available, player))
			return false;

		if(!questAvailable(questId, questAvailable, player))
			return false;

		if(!questAvailable(quest2Id, quest2Available, player))
			return false;

		if(!factionAvailable(factionId, factionStance, factionAvailable, player))
			return false;

		if(!factionAvailable(faction2Id, faction2Stance, faction2Available, player))
			return false;
		
		if(player.experienceLevel < minPlayerLevel)
			return false;
		
		return true;
	}
	
	private boolean factionAvailable(int id, EnumAvailabilityFaction stance, EnumAvailabilityFactionType available, EntityPlayer player) {
		if(available == EnumAvailabilityFactionType.Always)
			return true;
		
		Faction faction = FactionController.getInstance().getFaction(id);
		if(faction == null)
			return true;
		
		PlayerFactionData data = PlayerDataController.instance.getFactionData(player);
		int points = data.getFactionPoints(id);
		
		EnumAvailabilityFaction current = EnumAvailabilityFaction.Neutral;
		if(faction.neutralPoints >= points)
			current = EnumAvailabilityFaction.Hostile;
		if(faction.friendlyPoints < points)
			current = EnumAvailabilityFaction.Friendly;

		if(available == EnumAvailabilityFactionType.Is && stance == current){
			return true;
		}
		if(available == EnumAvailabilityFactionType.IsNot && stance != current){
			return true;
		}
		
		return false;
	}
	
	public boolean dialogAvailable(int id, EnumAvailabilityDialog en, EntityPlayer player){
		if(en == EnumAvailabilityDialog.Always)
			return true;
		boolean hasRead = PlayerDataController.instance.hasDialogBeenRead(id, player);
		if(hasRead && en == EnumAvailabilityDialog.After)
			return true;
		else if(!hasRead && en == EnumAvailabilityDialog.Before)
			return true;
		return false;
	}
	
	public boolean questAvailable(int id, EnumAvailabilityQuest en, EntityPlayer player){
		if(en == EnumAvailabilityQuest.Always)
			return true;
		else if(en == EnumAvailabilityQuest.After && PlayerQuestController.isQuestFinished(player, id))
			return true;
		else if(en == EnumAvailabilityQuest.Before && !PlayerQuestController.isQuestFinished(player, id))
			return true;
		else if(en == EnumAvailabilityQuest.Active && PlayerQuestController.isQuestActive(player, id))
			return true;
		else if(en == EnumAvailabilityQuest.NotActive && !PlayerQuestController.isQuestActive(player, id))
			return true;
		return false;
	}
	@Override
	public int getVersion() {
		return version;
	}
	@Override
	public void setVersion(int version) {
		this.version = version;
	}
}

package noppes.npcs.controllers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.ICompatibilty;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.VersionCompatibility;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.quests.QuestDialog;
import noppes.npcs.quests.QuestInterface;
import noppes.npcs.quests.QuestItem;
import noppes.npcs.quests.QuestKill;
import noppes.npcs.quests.QuestLocation;

public class Quest implements ICompatibilty {
	public int version = VersionCompatibility.ModRev;
	public int id = -1;
	public EnumQuestType type = EnumQuestType.Item;
	public EnumQuestRepeat repeat = EnumQuestRepeat.None;
	public EnumQuestCompletion completion = EnumQuestCompletion.Npc;
	public String title = "default";
	public QuestCategory category;
	public String logText = "";
	public String completeText = "";
	public String completerNpc = "";
	public int nextQuestid = -1;
	public String nextQuestTitle = "";
	public PlayerMail mail = new PlayerMail();
	
	public QuestInterface questInterface = new QuestItem();
	
	public int rewardExp = 0;
	public NpcMiscInventory rewardItems = new NpcMiscInventory(9);
	public FactionOptions factionOptions = new FactionOptions();
	
	public void readNBT(NBTTagCompound compound) {
    	version = compound.getInteger("ModRev");
		VersionCompatibility.CheckAvailabilityCompatibility(this, compound);
		
		id = compound.getInteger("Id");
		setType(EnumQuestType.values()[compound.getInteger("Type")]);
		title = compound.getString("Title");
		logText = compound.getString("Text");
		completeText = compound.getString("CompleteText");
		completerNpc = compound.getString("CompleterNpc");
		nextQuestid = compound.getInteger("NextQuestId");
		nextQuestTitle = compound.getString("NextQuestTitle");
		if(hasNewQuest())
			nextQuestTitle = getNextQuest().title;
		else
			nextQuestTitle = "";
		
		rewardExp = compound.getInteger("RewardExp");
		rewardItems.setFromNBT(compound.getCompoundTag("Rewards"));
		
		completion = EnumQuestCompletion.values()[compound.getInteger("QuestCompletion")];
		repeat = EnumQuestRepeat.values()[compound.getInteger("QuestRepeat")];
		
		questInterface.readEntityFromNBT(compound);
		
		factionOptions.readFromNBT(compound.getCompoundTag("QuestFactionPoints"));
		
		mail.readNBT(compound.getCompoundTag("QuestMail"));
	}

	public void setType(EnumQuestType questType) {
		type = questType;
		if(type == EnumQuestType.Item)
			questInterface = new QuestItem();
		else if(type == EnumQuestType.Dialog)
			questInterface = new QuestDialog();
		else if(type == EnumQuestType.Kill)
			questInterface = new QuestKill();
		else if(type == EnumQuestType.Location)
			questInterface = new QuestLocation();
		
		if(questInterface != null)
			questInterface.questId = id;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("ModRev", version);
		compound.setInteger("Id", id);
		compound.setInteger("Type", type.ordinal());
		compound.setString("Title", title);
		compound.setString("Text", logText);
		compound.setString("CompleteText", completeText);
		compound.setString("CompleterNpc", completerNpc);
		compound.setInteger("NextQuestId", nextQuestid);
		compound.setString("NextQuestTitle", nextQuestTitle);
		compound.setInteger("RewardExp", rewardExp);
		compound.setCompoundTag("Rewards", rewardItems.getToNBT());

		compound.setInteger("QuestCompletion", completion.ordinal());
		compound.setInteger("QuestRepeat", repeat.ordinal());
		
		this.questInterface.writeEntityToNBT(compound);
		compound.setCompoundTag("QuestFactionPoints", factionOptions.writeToNBT(new NBTTagCompound()));
		compound.setCompoundTag("QuestMail", mail.writeNBT());
		
		return compound;
	}
	
	public boolean hasNewQuest()
	{
		return getNextQuest() != null;
	}
	public Quest getNextQuest()
	{
		return QuestController.instance == null?null:QuestController.instance.quests.get(nextQuestid);
	}

	public boolean complete(EntityPlayer player, QuestData data) {
		if(completion == EnumQuestCompletion.Instant){
			NoppesUtilServer.sendData(player, EnumPacketType.QuestCompletion, data.quest.writeToNBT(new NBTTagCompound()));
			return true;
		}
		return false;
	}
	public Quest copy(){
		Quest quest = new Quest();
		quest.readNBT(this.writeToNBT(new NBTTagCompound()));
		return quest;
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

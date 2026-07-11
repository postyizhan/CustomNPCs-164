package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.NpcMiscInventory;

public class PlayerMail {
	public NpcMiscInventory items = new NpcMiscInventory(9);
	public String subject = "";
	public String sender = "";
	public NBTTagCompound message = new NBTTagCompound();
	public long time = 0;
	public boolean beenRead = false;
	public int questId = -1;
	public String questTitle = "";
	
	public long timePast;
	
	public void readNBT(NBTTagCompound compound) {
		items.setFromNBT(compound.getCompoundTag("Items"));
		subject = compound.getString("Subject");
		sender = compound.getString("Sender");
		time = compound.getLong("Time");
		beenRead = compound.getBoolean("BeenRead");
		message = compound.getCompoundTag("Message");
		timePast = compound.getLong("TimePast");
		if(compound.hasKey("MailQuest"))
			questId = compound.getInteger("MailQuest");
		questTitle = compound.getString("MailQuestTitle");
		
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setCompoundTag("Items", items.getToNBT());
		compound.setString("Subject", subject);
		compound.setString("Sender", sender);
		compound.setLong("Time", time);
		compound.setBoolean("BeenRead", beenRead);
		compound.setCompoundTag("Message", message);
		compound.setLong("TimePast", System.currentTimeMillis() - time);
		compound.setInteger("MailQuest", questId);

		if(hasQuest())
			compound.setString("MailQuestTitle", getQuest().title);
		return compound;
	}
	
	public boolean isValid(){
		return !subject.isEmpty() && !message.hasNoTags() && !sender.isEmpty();
	}

	public boolean hasQuest() {
		return getQuest() != null;
	}
	public Quest getQuest() {
		return  QuestController.instance != null?QuestController.instance.quests.get(questId):null;
	}
}

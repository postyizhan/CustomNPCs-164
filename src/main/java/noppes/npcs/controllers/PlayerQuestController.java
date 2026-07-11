package noppes.npcs.controllers;

import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.quests.QuestDialog;

public class PlayerQuestController {

	public static boolean hasActiveQuests(EntityPlayer player){
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		return !data.activeQuests.isEmpty();
	}
	
	public static boolean isQuestActive(EntityPlayer player, int quest){
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		return data.activeQuests.containsKey(quest);
	}
	
	public static boolean isQuestFinished(EntityPlayer player, int questid){
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		return data.finishedQuests.containsKey(questid);
	}

	public static void addActiveQuest(Quest quest, EntityPlayer player) {
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		if(canQuestBeAccepted(quest, player)){
			data.activeQuests.put(quest.id,new QuestData(quest));	
			NoppesUtilServer.sendData(player, EnumPacketType.Message, "quest.newquest", quest.title);
			NoppesUtilServer.sendData(player, EnumPacketType.Chat, "quest.newquest", ": ", quest.title);
			PlayerDataController.instance.savePlayerData(player, data);
		}
	}
	
	public static void setQuestFinished(Quest quest, EntityPlayer player){
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		data.activeQuests.remove(quest.id);
		data.finishedQuests.put(quest.id,player.worldObj.getWorldTime());
		if(quest.repeat != EnumQuestRepeat.None && quest.type == EnumQuestType.Dialog){
			QuestDialog questdialog = (QuestDialog) quest.questInterface;
			PlayerDialogData dialogdata = PlayerDataController.instance.getDialogData(player);
			for(int dialog : questdialog.dialogs.values()){
				dialogdata.dialogsRead.remove(dialog);
			}
		}
		
		PlayerDataController.instance.savePlayerData(player, data);
	}
	public static boolean canQuestBeAccepted(Quest quest, EntityPlayer player){
		if(quest == null)
			return false;
		
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		if(data.activeQuests.containsKey(quest.id))
			return false;
		
		if(!data.finishedQuests.containsKey(quest.id) || quest.repeat == EnumQuestRepeat.Repeatable)
			return true;
		if(quest.repeat == EnumQuestRepeat.None)
			return false;
		
		long questTime = data.finishedQuests.get(quest.id);
		long worldTime = player.worldObj.getWorldTime();
		
		if(worldTime < questTime)
			return true;

		long timePassed = worldTime - questTime;

		if(quest.repeat == EnumQuestRepeat.Daily){
			return timePassed >= 24000;
		}
		if(quest.repeat == EnumQuestRepeat.Weekly){
			return timePassed >= 168000;
		}
		return false;
	}
	public static Vector<Quest> getActiveQuests(EntityPlayer player)
	{
		Vector<Quest> quests = new Vector<Quest>();
		PlayerQuestData data = PlayerDataController.instance.getQuestData(player);
		for(QuestData questdata: data.activeQuests.values()){
			if(questdata.quest == null)
				continue;
			quests.add(questdata.quest);
		}
		return quests;
	}
}

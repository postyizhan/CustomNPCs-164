package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.quests.QuestInterface;

public class PlayerQuestData implements IPlayerData{
	public HashMap<Integer,QuestData> activeQuests = new HashMap<Integer,QuestData>();
	public HashMap<Integer,Long> finishedQuests = new HashMap<Integer,Long>();
	
	public void readNBT(NBTTagCompound mainCompound) {
		if(mainCompound == null)
			return;
		NBTTagCompound compound = mainCompound.getCompoundTag("QuestData");
		
        NBTTagList list = compound.getTagList("CompletedQuests");
        if(list != null){
        	HashMap<Integer,Long> finishedQuests = new HashMap<Integer,Long>();
            for(int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(i);
                finishedQuests.put(nbttagcompound.getInteger("Quest"),nbttagcompound.getLong("Date"));
            }
            this.finishedQuests = finishedQuests;
        }
		
        NBTTagList list2 = compound.getTagList("ActiveQuests");
        if(list2 != null){
        	HashMap<Integer,QuestData> activeQuests = new HashMap<Integer,QuestData>();
            for(int i = 0; i < list2.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)list2.tagAt(i);
                int id = nbttagcompound.getInteger("Quest");
                Quest quest = QuestController.instance.quests.get(id);
                if(quest == null)
                	continue;
                QuestData data = new QuestData(quest);
                data.readEntityFromNBT(nbttagcompound);
                activeQuests.put(id,data);
            }
            this.activeQuests = activeQuests;
        }
        
	}

	public NBTTagCompound writeNBT(NBTTagCompound maincompound) {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(int quest : finishedQuests.keySet()){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			nbttagcompound.setLong("Date", finishedQuests.get(quest));
			list.appendTag(nbttagcompound);
		}
		
		compound.setTag("CompletedQuests", list);
		
		NBTTagList list2 = new NBTTagList();
		for(int quest : activeQuests.keySet()){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Quest", quest);
			activeQuests.get(quest).writeEntityToNBT(nbttagcompound);
			list2.appendTag(nbttagcompound);
		}
		
		compound.setTag("ActiveQuests", list2);
		
		maincompound.setCompoundTag("QuestData", compound);
		return maincompound;
	}

	public QuestData getQuestCompletion(EntityPlayer player,EntityNPCInterface npc) {
		for(QuestData data : activeQuests.values()){
			Quest quest = data.quest;
			if(quest != null && quest.completion == EnumQuestCompletion.Npc && quest.completerNpc.equals(npc.getEntityName()) && quest.questInterface.isCompleted(player)){
				return data;
			}
		}
		return null;
	}

	public boolean checkQuestCompletion(EntityPlayer player,EnumQuestType type) {
		boolean bo = false;
		for(QuestData data : this.activeQuests.values()){
			if(data.quest.type != type && type != null)
				continue;
			
			QuestInterface inter =  data.quest.questInterface;
			
			if(inter.isCompleted(player)){
				if(!data.isCompleted){
					if(!data.quest.complete(player,data)){
						NoppesUtilServer.sendData(player, EnumPacketType.Message, "quest.completed", data.quest.title);
						NoppesUtilServer.sendData(player, EnumPacketType.Chat, "quest.completed",": ",data.quest.title);
					}
					data.isCompleted = true;
					bo = true;
				}
			}
			else
				data.isCompleted = false;
		}
		if(bo)
			PlayerDataController.instance.savePlayerData(player, this);
		return bo;
		
	}
	
}

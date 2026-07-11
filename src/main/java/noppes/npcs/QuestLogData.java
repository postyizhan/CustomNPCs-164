package noppes.npcs;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.Quest;

public class QuestLogData {
	public HashMap<String,Vector<String>> categories = new HashMap<String,Vector<String>>();
	public String selectedQuest = "";
	public String selectedCategory = "";
	public HashMap<String,String> questText = new HashMap<String,String>();
	public HashMap<String,Vector<String>> questStatus = new HashMap<String,Vector<String>>();
	
	public NBTTagCompound writeNBT(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("Categories", NBTTags.nbtVectorMap(categories));
		compound.setTag("Logs", NBTTags.nbtStringStringMap(questText));
		compound.setTag("Status", NBTTags.nbtVectorMap(questStatus));
		return compound;
	}

	public void readNBT(NBTTagCompound compound){
		categories = NBTTags.getVectorMap(compound.getTagList("Categories"));
		questText = NBTTags.getStringStringMap(compound.getTagList("Logs"));
		questStatus = NBTTags.getVectorMap(compound.getTagList("Status"));
		
	}
	public void setData(EntityPlayer player){

        for(Quest quest : PlayerQuestController.getActiveQuests(player))
        {
    		String category = quest.category.title;
    		if(!categories.containsKey(category))
    			categories.put(category, new Vector<String>());
    		Vector<String> list = categories.get(category);
    		list.add(quest.title);
    		
    		questText.put(category + ":" + quest.title, quest.logText);

    		questStatus.put(category + ":" + quest.title, quest.questInterface.getQuestLogStatus(player));
        }
		
	}

	public boolean hasSelectedQuest() {
		return !selectedQuest.isEmpty();
	}

	public String getQuestText() {
		return questText.get(selectedCategory + ":" + selectedQuest);
	}

	public Vector<String> getQuestStatus() {
		return questStatus.get(selectedCategory + ":" + selectedQuest);
	}
}

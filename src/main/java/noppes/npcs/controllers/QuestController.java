package noppes.npcs.controllers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;

public class QuestController {
	public HashMap<Integer,QuestCategory> categories = new HashMap<Integer, QuestCategory>();
	public HashMap<Integer,Quest> quests = new HashMap<Integer, Quest>();

	public static QuestController instance;
	
	private int lastUsedID = 0;
	
	public QuestController(){
		instance = this;
		loadCategories();
	}
	
	private void loadCategories(){
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		try {
	        File file = new File(saveDir, "quests.dat");
	        if(file.exists()){
	        	loadCategories(file);
	        }
		} catch (Exception e) {
			try {
		        File file = new File(saveDir, "quests.dat_old");
		        if(file.exists()){
		        	loadCategories(file);
		        }
		        
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}
	private void loadCategories(File file) throws Exception{
		HashMap<Integer,QuestCategory> categories = new HashMap<Integer,QuestCategory>();
		HashMap<Integer,Quest> quests = new HashMap<Integer, Quest>();
		
        NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
        lastUsedID = nbttagcompound1.getInteger("lastID");
        NBTTagList list = nbttagcompound1.getTagList("Data");
        if(list != null){
	        for(int i = 0; i < list.tagCount(); i++)
	        {
	            QuestCategory category = new QuestCategory();
	            category.readNBT((NBTTagCompound)list.tagAt(i));
	            categories.put(category.id,category);
	            for(Quest quest : category.quests.values())
	            	quests.put(quest.id, quest);
	        }
        }
        
		this.quests = quests;
		this.categories = categories;
	}
	public void saveCategories(){
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();

	        NBTTagList list = new NBTTagList();
	        for(QuestCategory category : categories.values()){
	            NBTTagCompound nbtfactions = new NBTTagCompound();
	        	category.writeNBT(nbtfactions);
	        	list.appendTag(nbtfactions);
	        }
	        NBTTagCompound nbttagcompound = new NBTTagCompound();
	        nbttagcompound.setInteger("lastID", lastUsedID);
	        nbttagcompound.setTag("Data", list);
            File file = new File(saveDir, "quests.dat_new");
            File file1 = new File(saveDir, "quests.dat_old");
            File file2 = new File(saveDir, "quests.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeCategory(DataInputStream dis) throws IOException{
		int category = dis.readInt();
		QuestCategory cat = categories.get(category);
		if(cat == null)
			return;
		for(int dia : cat.quests.keySet())
			quests.remove(dia);
		categories.remove(category);
		saveCategories();
	}

	public void removeQuest(Quest quest) {
		for(QuestCategory category : categories.values())
			category.quests.remove(quest.id);
		quests.remove(quest.id);
		quest.category.quests.remove(quest.id);
		saveCategories();
	}
	
	public void saveCategory(QuestCategory category){
		if(category.id < 0){
			if(lastUsedID == 0){
				for(int catid : categories.keySet())
					if(catid > lastUsedID)
						lastUsedID = catid;
			}
			lastUsedID++;
			category.id = lastUsedID;
		}
		if(categories.containsKey(category.id)){
			QuestCategory currentcategory = categories.get(category.id);
			if(!currentcategory.title.equals(category.title)){
				while(containsCategoryName(category.title))
					category.title += "_";
			}
		}
		else{
			while(containsCategoryName(category.title))
				category.title += "_";
		}
		categories.put(category.id, category);
		saveCategories();
	}
	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for(QuestCategory cat : categories.values()){
			if(cat.title.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	private boolean containsQuestName(QuestCategory category, String name) {
		name = name.toLowerCase();
		for(Quest quest : category.quests.values()){
			if(quest.title.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	public Quest saveQuest(DataInputStream dis) throws IOException {
		QuestCategory category = categories.get(dis.readInt());
		if(category == null)
			return null;
		
		Quest quest = new Quest();
		quest.readNBT(CompressedStreamTools.read(dis));
		quest.category = category;
    	if(quest.id < 0){
			int id = 0;
    		for(int key : quests.keySet()){
    			if(key > id){
    				id = key;;
    			}
    		}
    		id++;
    		quest.id = id;
    		while(containsQuestName(quest.category, quest.title))
    			quest.title += "_";
    	}
    	quests.put(quest.id, quest);
    	quest.category.quests.put(quest.id, quest);
    	saveCategories();
    	return quest;
	}
}

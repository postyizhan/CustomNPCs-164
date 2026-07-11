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
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.constants.EnumOptionType;

public class DialogController {
	public HashMap<Integer,DialogCategory> categories = new HashMap<Integer, DialogCategory>();
	public HashMap<Integer,Dialog> dialogs = new HashMap<Integer, Dialog>();
	public static DialogController instance;
	private static final String lock = "lock";

	private int lastUsedID = 0;
	
	public DialogController(){
		instance = this;
		loadCategories();
	}
	
	private void loadCategories(){
		boolean loadDefault = false;
		synchronized (lock) {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			try {
		        File file = new File(saveDir, "dialog.dat");
		        if(file.exists()){
			        loadCategories(file);
		        }
		        else
		        	loadDefault = true;
			} catch (Exception e) {
				try {
			        File file = new File(saveDir, "dialog.dat_old");
			        if(file.exists()){
			        	loadCategories(file);
			        }
			        
				} catch (Exception ee) {
					e.printStackTrace();
				}
			}
		}
		if(loadDefault)
    		loadDefaultDialogs();
	}
	private void loadCategories(File file) throws Exception{
        NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
        lastUsedID = nbttagcompound1.getInteger("lastID");
        NBTTagList list = nbttagcompound1.getTagList("Data");
        HashMap<Integer,DialogCategory> categories = new HashMap<Integer, DialogCategory>();
        HashMap<Integer,Dialog> dialogs = new HashMap<Integer, Dialog>();
        if(list != null){
            for(int i = 0; i < list.tagCount(); i++)
            {
                DialogCategory category = new DialogCategory();
                category.readNBT((NBTTagCompound)list.tagAt(i));
                categories.put(category.id,category);
                for(Dialog dialog : category.dialogs.values())
                	dialogs.put(dialog.id, dialog);
            }
        }
        this.categories = categories;
        this.dialogs = dialogs;
	}
	public void saveCategories(){
		synchronized (lock) {
			try {
				File saveDir = CustomNpcs.getWorldSaveDirectory();
	
		        NBTTagList list = new NBTTagList();
		        for(DialogCategory category : categories.values()){
		            NBTTagCompound nbtfactions = new NBTTagCompound();
		            category.writeNBT(nbtfactions);
		        	list.appendTag(nbtfactions);
		        }
		        NBTTagCompound nbttagcompound = new NBTTagCompound();
		        nbttagcompound.setInteger("lastID", lastUsedID);
		        nbttagcompound.setTag("Data", list);
	            File file = new File(saveDir, "dialog.dat_new");
	            File file1 = new File(saveDir, "dialog.dat_old");
	            File file2 = new File(saveDir, "dialog.dat");
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
	}

	private void loadDefaultDialogs() {
		if(categories.isEmpty())
		{
			if(categories.isEmpty()){
				DialogCategory cat = new DialogCategory();
				cat.id = 1;
				cat.title = "Villager";
				
				Dialog dia1 = new Dialog();
				dia1.id = 1;
				dia1.category = cat;
				dia1.title = "Start";
				dia1.text = "Hello {player}, "+'\n'+'\n'+"Welcome to our village. I hope you enjoy your stay";
				
				Dialog dia2 = new Dialog();
				dia2.id = 2;
				dia2.category = cat;
				dia2.title = "Ask about village";
				dia2.text = "This village has been around for ages. Enjoy your stay here.";
				
				Dialog dia3 = new Dialog();
				dia3.id = 3;
				dia3.category = cat;
				dia3.title = "Who are you";
				dia3.text = "I'm a villager here. I have lived in this village my whole life.";
				
				cat.dialogs.put(dia1.id, dia1);
				cat.dialogs.put(dia2.id, dia2);
				cat.dialogs.put(dia3.id, dia3);
				

				DialogOption option = new DialogOption();
				option.title = "Tell me something about this village";
				option.dialogId = 2;
				option.optionType = EnumOptionType.DialogOption;
				
				DialogOption option2 = new DialogOption();
				option2.title = "Who are you?";
				option2.dialogId = 3;
				option2.optionType = EnumOptionType.DialogOption;

				DialogOption option3 = new DialogOption();
				option3.title = "Goodbye";
				option3.optionType = EnumOptionType.QuitOption;
				
				dia1.options.put(0, option2);
				dia1.options.put(1, option);
				dia1.options.put(2, option3);
				

				DialogOption option4 = new DialogOption();
				option4.title = "Back";
				option4.dialogId = 1;

				dia2.options.put(1, option4);
				dia3.options.put(1, option4);
				
				categories.put(cat.id, cat);
				dialogs.put(dia1.id, dia1);
				dialogs.put(dia2.id, dia2);
				dialogs.put(dia3.id, dia3);
				
				lastUsedID = 3;
				saveCategories();
			}
		}
	}
	public void removeDialog(Dialog dialog) {
		DialogCategory category = dialog.category;
		category.dialogs.remove(dialog.id);
		dialogs.remove(dialog.id);
		saveCategories();
	}
	
	public void saveCategory(DialogCategory category) throws IOException {
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
			DialogCategory currentCategory = categories.get(category.id);
			if(!currentCategory.title.equals(category.title)){
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
	public void removeCategory(DataInputStream dis) throws IOException{
		int category = dis.readInt();
		DialogCategory cat = categories.get(category);
		if(cat == null)
			return;
		for(int dia : cat.dialogs.keySet())
			dialogs.remove(dia);
		categories.remove(category);
		saveCategories();
	}
	
	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for(DialogCategory cat : categories.values()){
			if(cat.title.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	private boolean containsDialogName(DialogCategory category, String name) {
		name = name.toLowerCase();
		for(Dialog dia : category.dialogs.values()){
			if(dia.title.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	public void saveDialog(int categoryId,Dialog dialog) throws IOException {
		DialogCategory category = categories.get(categoryId);
		if(category == null)
			return;
		
		dialog.category = category;
    	if(dialog.id < 0){
			int id = 0;
    		for(int key : dialogs.keySet()){
    			if(key > id){
    				id = key;;
    			}
    		}
    		id++;
    		dialog.id = id;
    		while(containsDialogName(dialog.category, dialog.title))
    			dialog.title += "_";
    	}
    	dialogs.put(dialog.id, dialog);
    	dialog.category.dialogs.put(dialog.id, dialog);
    	saveCategories();
	}
	public boolean hasDialog(int dialogId) {
		return dialogs.containsKey(dialogId);
	}
}

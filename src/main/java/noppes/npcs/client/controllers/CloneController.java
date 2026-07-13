package noppes.npcs.client.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;

public class CloneController {
	public static Entity toClone = null;

	// 文件夹注册表持久化（解决空文件夹不显示问题）
	private static TreeSet<String> registeredFolders = null;

	private static TreeSet<String> loadRegisteredFolders(){
		if(registeredFolders != null)
			return registeredFolders;
		registeredFolders = new TreeSet<String>();
		registeredFolders.add("Default");
		try {
			File file = new File(CustomNpcs.Dir, "clonefolders.dat");
			if(file.exists()){
				NBTTagCompound compound = CompressedStreamTools.readCompressed(new FileInputStream(file));
				NBTTagList list = compound.getTagList("Folders");
				if(list != null){
					for(int i = 0; i < list.tagCount(); i++){
						NBTTagCompound tag = (NBTTagCompound)list.tagAt(i);
						registeredFolders.add(tag.getString("Name"));
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Failed to load clone folders: " + e.getMessage());
		}
		return registeredFolders;
	}

	private static void saveRegisteredFolders(){
		try {
			File file = new File(CustomNpcs.Dir, "clonefolders.dat");
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagList list = new NBTTagList();
			for(String folder : loadRegisteredFolders()){
				if(!folder.equals("Default")){
					NBTTagCompound tag = new NBTTagCompound();
					tag.setString("Name", folder);
					list.appendTag(tag);
				}
			}
			compound.setTag("Folders", list);
			CompressedStreamTools.writeCompressed(compound, new FileOutputStream(file));
		} catch (Exception e) {
			System.err.println("Failed to save clone folders: " + e.getMessage());
		}
	}

	public static void registerFolder(String folder){
		if(folder != null && !folder.isEmpty()){
			loadRegisteredFolders().add(folder);
			saveRegisteredFolders();
		}
	}

	public static void unregisterFolder(String folder){
		if(!folder.equals("Default")){
			loadRegisteredFolders().remove(folder);
			saveRegisteredFolders();
		}
	}

	private static ArrayList<NBTTagCompound> loadClones(){
		try {
	        File file = new File(CustomNpcs.Dir, "clonednpcs.dat");
	        if(!file.exists()){
				return new ArrayList<NBTTagCompound>();
	        }
	        return loadClones(file);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		try {
			File file = new File(CustomNpcs.Dir, "clonednpcs.dat_old");
	        return loadClones(file);
	        
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return new ArrayList<NBTTagCompound>();
	}
	private static ArrayList<NBTTagCompound> loadClones(File file) throws Exception{
		ArrayList<NBTTagCompound> clones = new ArrayList<NBTTagCompound>();
        NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
        NBTTagList list = nbttagcompound1.getTagList("Data");
        if(list == null){
        	return clones;
        }
        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(i);
            if(!nbttagcompound.hasKey("ClonedDate")){
        		nbttagcompound.setLong("ClonedDate", System.currentTimeMillis());
        		nbttagcompound.setString("ClonedName", nbttagcompound.getString("Name"));
            }
            clones.add(nbttagcompound);
        }
		return clones;
	}
	public static void saveClones(Collection<NBTTagCompound> clones){
		try {
	        NBTTagList list = new NBTTagList();
	        for(NBTTagCompound nbt: clones){
	        	list.appendTag(nbt);
	        }
	        NBTTagCompound nbttagcompound = new NBTTagCompound();
	        nbttagcompound.setTag("Data", list);
            File file = new File(CustomNpcs.Dir, "clonednpcs.dat_new");
            File file1 = new File(CustomNpcs.Dir, "clonednpcs.dat_old");
            File file2 = new File(CustomNpcs.Dir, "clonednpcs.dat");
            
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
			System.err.println(e.getMessage());
		}
	}
	public static ArrayList<NBTTagCompound> getClones(){
		return loadClones();
	}
	public static ArrayList<NBTTagCompound> getClones(String folder){
		ArrayList<NBTTagCompound> clones = new ArrayList<NBTTagCompound>();
		for(NBTTagCompound nbt : loadClones()){
			if(getFolderOf(nbt).equals(folder))
				clones.add(nbt);
		}
		return clones;
	}
	public static ArrayList<String> getFolders(){
		TreeSet<String> folders = new TreeSet<String>(loadRegisteredFolders());
		// 也包含从 clone 反推的文件夹（兼容旧存档）
		for(NBTTagCompound nbt : loadClones()){
			String folder = getFolderOf(nbt);
			if(!folders.contains(folder)){
				folders.add(folder);
				registerFolder(folder); // 自动注册旧文件夹
			}
		}
		return new ArrayList<String>(folders);
	}
	public static String getFolderOf(NBTTagCompound nbt){
		if(nbt.hasKey("ClonedFolder"))
			return nbt.getString("ClonedFolder");
		if(nbt.hasKey("ClonedTab"))
			return "Tab " + nbt.getInteger("ClonedTab");
		return "Default";
	}
	public static void addClone(Entity entity, String name, String folder) {
		ArrayList<NBTTagCompound> clones = getClones();

		NBTTagCompound nbttagcompound = new NBTTagCompound();
		entity.writeToNBTOptional(nbttagcompound);
		//nbttagcompound.setInteger("EntityId", entity.entityId);
		nbttagcompound.setLong("ClonedDate", System.currentTimeMillis());
		nbttagcompound.setString("ClonedName", name);
		nbttagcompound.setString("ClonedFolder", folder);


		clones.add(nbttagcompound);
		saveClones(clones);
		registerFolder(folder); // 确保文件夹被注册
	}
	public static void renameFolder(String old, String neu){
		ArrayList<NBTTagCompound> clones = getClones();
		for(NBTTagCompound nbt : clones){
			if(getFolderOf(nbt).equals(old))
				nbt.setString("ClonedFolder", neu);
		}
		saveClones(clones);
		unregisterFolder(old);
		registerFolder(neu);
	}
	public static void deleteFolder(String folder){
		ArrayList<NBTTagCompound> clones = getClones();
		for(NBTTagCompound nbt : clones){
			if(getFolderOf(nbt).equals(folder))
				nbt.setString("ClonedFolder", "Default");
		}
		saveClones(clones);
		unregisterFolder(folder);
	}
}

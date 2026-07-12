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
		TreeSet<String> folders = new TreeSet<String>();
		folders.add("Default");
		for(NBTTagCompound nbt : loadClones()){
			folders.add(getFolderOf(nbt));
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
	}
	public static void renameFolder(String old, String neu){
		ArrayList<NBTTagCompound> clones = getClones();
		for(NBTTagCompound nbt : clones){
			if(getFolderOf(nbt).equals(old))
				nbt.setString("ClonedFolder", neu);
		}
		saveClones(clones);
	}
	public static void deleteFolder(String folder){
		ArrayList<NBTTagCompound> clones = getClones();
		for(NBTTagCompound nbt : clones){
			if(getFolderOf(nbt).equals(folder))
				nbt.setString("ClonedFolder", "Default");
		}
		saveClones(clones);
	}
}

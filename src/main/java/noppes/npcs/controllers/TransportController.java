package noppes.npcs.controllers;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.roles.RoleTransporter;

public class TransportController {
	private HashMap<Integer,TransportLocation> locations = new HashMap<Integer, TransportLocation>();
	public HashMap<Integer,TransportCategory> categories = new HashMap<Integer, TransportCategory>();

	private int lastUsedID = 0;
	
	private static TransportController instance;
	public TransportController(){
		instance = this;
		loadCategories();
		if(categories.isEmpty()){
			TransportCategory cat = new TransportCategory();
			cat.id = 1;
			cat.title = "Default";
			categories.put(cat.id, cat);
		}
	}
	private void loadCategories(){
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		if(saveDir == null)
			return;
		try {
	        File file = new File(saveDir, "transport.dat");
	        if(!file.exists()){
				return;
	        }
	        loadCategoriesFile(file);
		} catch (IOException e) {
			try {
		        File file = new File(saveDir, "transport.dat_old");
		        if(!file.exists()){
					return;
		        }
		        loadCategoriesFile(file);
		        
			} catch (IOException ee) {
			}
		}
	}
	private void loadCategoriesFile(File file) throws IOException{
        DataInputStream var1 = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new FileInputStream(file))));
        loadCategoriesStream(var1);
		var1.close();
	}
	public void loadCategoriesStream(DataInputStream stream) throws IOException{
		HashMap<Integer,TransportLocation> locations = new HashMap<Integer,TransportLocation>();
		HashMap<Integer,TransportCategory> categories = new HashMap<Integer,TransportCategory>();
        NBTTagCompound nbttagcompound1 = CompressedStreamTools.read(stream);
        lastUsedID = nbttagcompound1.getInteger("lastID");
        NBTTagList list = nbttagcompound1.getTagList("NPCTransportCategories");
        if(list == null){
        	return;
        }
        for(int i = 0; i < list.tagCount(); i++)
        {
        	TransportCategory category = new TransportCategory();
        	NBTTagCompound compound = (NBTTagCompound)list.tagAt(i);
        	category.readNBT(compound);
        	
            for(TransportLocation location : category.locations.values())
            	locations.put(location.id,location);
            
            categories.put(category.id, category);
        }
        this.locations = locations;
        this.categories = categories;
	}
	
	public NBTTagCompound getNBT(){
        NBTTagList list = new NBTTagList();
        for(TransportCategory category : categories.values()){
        	NBTTagCompound compound = new NBTTagCompound();
        	category.writeNBT(compound);
        	list.appendTag(compound);
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setInteger("lastID", lastUsedID);
        nbttagcompound.setTag("NPCTransportCategories", list);
        return nbttagcompound;
	}
	
	public void saveCategories(){
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
            File file = new File(saveDir, "transport.dat_new");
            File file1 = new File(saveDir, "transport.dat_old");
            File file2 = new File(saveDir, "transport.dat");
            CompressedStreamTools.writeCompressed(getNBT(), new FileOutputStream(file));
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
	public TransportLocation getTransport(int transportId) {
		return locations.get(transportId);
	}
	public TransportLocation getTransport(String name) {
		for(TransportLocation loc : locations.values()){
			if(loc.name.equals(name))
				return loc;
		}
		
		return null;
	}
	private int getUniqueIdLocation() {
		if(lastUsedID == 0){
			for(int catid : locations.keySet())
				if(catid > lastUsedID)
					lastUsedID = catid;
		}
		lastUsedID++;
		return lastUsedID;
	}
	private int getUniqueIdCategory() {
		int id = 0;
		for(int catid : categories.keySet())
			if(catid > id)
				id = catid;
		id++;
		return id;
	}
	public void setLocation(TransportLocation location) {
		if(locations.containsKey(location.id)){
			for(TransportCategory cat : categories.values())
				cat.locations.remove(location.id);
		}
		locations.put(location.id, location);
		location.category.locations.put(location.id, location);
	}
	public void removeLocation(int location) {
		TransportLocation loc = locations.get(location);
		if(loc == null)
			return;
		loc.category.locations.remove(location);
		locations.remove(location);
		
		saveCategories();
	}
	private boolean containsCategoryName(String name) {
		name = name.toLowerCase();
		for(TransportCategory cat : categories.values()){
			if(cat.title.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	public void saveCategory(DataInputStream dis) throws IOException {
		String name = dis.readUTF();
		int id = dis.readInt();
		if(id < 0){
			id = getUniqueIdCategory();
		}
		if(categories.containsKey(id)){
			TransportCategory category = categories.get(id);
			if(!category.title.equals(name)){
				while(containsCategoryName(name))
					name += "_";
				categories.get(id).title = name;
			}
		}
		else{
			while(containsCategoryName(name))
				name += "_";
			TransportCategory category = new TransportCategory();
			category.id = id;
			category.title = name;
			categories.put(id, category);
		}
		saveCategories();
	}
	public void removeCategory(int id){
		if(categories.size() == 1)
			return;
		TransportCategory cat = categories.get(id);
		if(cat == null)
			return;
		for(int i : cat.locations.keySet())
			locations.remove(i);
		categories.remove(id);
		
		saveCategories();
	}
	public boolean containsLocationName(String name) {
		name = name.toLowerCase();
		for(TransportLocation loc : locations.values()){
			if(loc.name.toLowerCase().equals(name))
				return true;
		}
		return false;
	}
	public static TransportController getInstance(){
		return instance;
	}
	public TransportLocation saveLocation(int categoryId, DataInputStream dis, EntityPlayerMP player, EntityNPCInterface npc) throws IOException {
		TransportCategory category = categories.get(categoryId);
		if(category == null || npc.advanced.role != EnumRoleType.Transporter)
			return null;
		RoleTransporter role = (RoleTransporter) npc.roleInterface;
		TransportLocation location = new TransportLocation();
		location.readNBT(CompressedStreamTools.read(dis));
		location.category = category;
		if(role.hasTransport())
			location.id = role.transportId;
		if(location.id < 0 || !locations.get(location.id).name.equals(location.name)){
			while(containsLocationName(location.name))
				location.name += "_";
		}
		if(location.id < 0)
			location.id = getUniqueIdLocation();
		
		category.locations.put(location.id, location);
		locations.put(location.id, location);
		saveCategories();
		
		return location;
	}

}

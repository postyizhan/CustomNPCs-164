package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;
import noppes.npcs.controllers.data.SpawnData;

public class SpawnController {
	public static SpawnController Instance;
	public HashMap<Integer, SpawnData> spawns = new HashMap<Integer, SpawnData>();

	private static final Object lock = new Object();
	private int lastUsedID = 0;

	public SpawnController() {
		Instance = this;
		loadSpawns();
	}

	private void loadSpawns() {
		synchronized(lock) {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
			if(saveDir == null)
				return;

			File file = new File(saveDir, "spawns.dat");
			File oldFile = new File(saveDir, "spawns.dat_old");

			if(!file.exists() && !oldFile.exists())
				return;

			try {
				if(file.exists())
					loadSpawns(file);
				else
					loadSpawns(oldFile);
			} catch(Exception exception) {
				exception.printStackTrace();
				if(!file.exists())
					return;
				try {
					if(oldFile.exists())
						loadSpawns(oldFile);
					else
						exception.printStackTrace();
				} catch(Exception oldException) {
					exception.printStackTrace();
					oldException.printStackTrace();
				}
			}
		}
	}

	private void loadSpawns(File file) throws Exception {
		FileInputStream stream = new FileInputStream(file);
		NBTTagCompound compound;
		try {
			compound = CompressedStreamTools.readCompressed(stream);
		} finally {
			stream.close();
		}

		lastUsedID = compound.getInteger("lastID");
		NBTTagList list = compound.getTagList("Data");
		for(int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound nbtSpawn = (NBTTagCompound) list.tagAt(i);
			SpawnData spawn = new SpawnData();
			spawn.readNBT(nbtSpawn);
			spawns.put(spawn.id, spawn);
		}
	}

	public void saveSpawns() {
		synchronized(lock) {
			try {
				File saveDir = CustomNpcs.getWorldSaveDirectory();
				if(saveDir == null)
					return;

				NBTTagList list = new NBTTagList();
				for(SpawnData spawn : spawns.values()) {
					NBTTagCompound nbtSpawn = new NBTTagCompound();
					spawn.writeNBT(nbtSpawn);
					list.appendTag(nbtSpawn);
				}
				NBTTagCompound compound = new NBTTagCompound();
				compound.setInteger("lastID", lastUsedID);
				compound.setTag("Data", list);

				File newFile = new File(saveDir, "spawns.dat_new");
				File oldFile = new File(saveDir, "spawns.dat_old");
				File file = new File(saveDir, "spawns.dat");

				CompressedStreamTools.writeCompressed(compound, new FileOutputStream(newFile));

				if(oldFile.exists())
					oldFile.delete();
				if(file.exists())
					file.renameTo(oldFile);
				if(!newFile.renameTo(file)) {
					if(!file.exists() && oldFile.exists())
						oldFile.renameTo(file);
				}
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public SpawnData addSpawn(String name) {
		synchronized(lock) {
			do {
				lastUsedID++;
			} while(spawns.containsKey(lastUsedID));

			SpawnData spawn = new SpawnData();
			spawn.id = lastUsedID;
			spawn.name = name == null ? "" : name;
			spawns.put(spawn.id, spawn);
			saveSpawns();
			return spawn;
		}
	}

	public void removeSpawn(int id) {
		synchronized(lock) {
			if(spawns.remove(id) != null)
				saveSpawns();
		}
	}

	public SpawnData getSpawn(int id) {
		synchronized(lock) {
			return spawns.get(id);
		}
	}
}

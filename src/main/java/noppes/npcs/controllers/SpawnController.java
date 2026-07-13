package noppes.npcs.controllers;

import java.util.Collection;
import java.util.HashMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.Server;
import noppes.npcs.controllers.data.SpawnData;

public class SpawnController {
	private static final String SPAWN_DATA_PATH = "customnpcs/spawns";
	private static final String SPAWN_TAG_PREFIX = "Spawn";

	public static SpawnController Instance;

	private HashMap<Integer, SpawnData> spawns = new HashMap<Integer, SpawnData>();
	private int lastUsedId = 0;

	public SpawnController() {
		Instance = this;
		loadSpawns();
	}

	public static SpawnController getInstance() {
		if(Instance == null)
			Instance = new SpawnController();
		return Instance;
	}

	public void loadSpawns() {
		NBTTagCompound root = Server.readWorldData(SPAWN_DATA_PATH);
		if(root == null)
			return;

		HashMap<Integer, SpawnData> loadedSpawns = new HashMap<Integer, SpawnData>();
		int loadedLastId = 0;
		Collection<?> tags = root.getTags();
		for(Object object : tags) {
			if(!(object instanceof NBTTagCompound))
				continue;
			NBTBase tag = (NBTBase)object;
			String tagName = tag.getName();
			if(tagName == null || !tagName.startsWith(SPAWN_TAG_PREFIX))
				continue;
			try {
				int tagId = Integer.parseInt(tagName.substring(SPAWN_TAG_PREFIX.length()));
				SpawnData spawn = new SpawnData();
				spawn.readNBT(root.getCompoundTag(tagName));
				if(spawn.id < 0)
					spawn.id = tagId;
				loadedSpawns.put(spawn.id, spawn);
				if(spawn.id > loadedLastId)
					loadedLastId = spawn.id;
			} catch(NumberFormatException ignored) {
			}
		}

		spawns = loadedSpawns;
		lastUsedId = loadedLastId;
		if(root.hasNoTags())
			saveSpawns();
	}

	public void saveSpawns() {
		NBTTagCompound root = new NBTTagCompound();
		for(SpawnData spawn : spawns.values()) {
			if(spawn == null)
				continue;
			root.setTag(SPAWN_TAG_PREFIX + spawn.id, spawn.writeNBT(new NBTTagCompound()));
		}
		Server.writeWorldData(SPAWN_DATA_PATH, root);
	}

	public void addSpawn(SpawnData spawn) {
		if(spawn == null)
			return;
		if(spawn.id < 0)
			spawn.id = getUnusedId();
		else if(spawn.id > lastUsedId)
			lastUsedId = spawn.id;
		spawns.put(spawn.id, spawn);
		saveSpawns();
	}

	public void removeSpawn(int id) {
		if(spawns.remove(id) != null)
			saveSpawns();
	}

	public SpawnData getSpawn(int id) {
		return spawns.get(id);
	}

	public HashMap<Integer, SpawnData> getAllSpawns() {
		return new HashMap<Integer, SpawnData>(spawns);
	}

	private int getUnusedId() {
		do {
			lastUsedId++;
		} while(spawns.containsKey(lastUsedId));
		return lastUsedId;
	}
}

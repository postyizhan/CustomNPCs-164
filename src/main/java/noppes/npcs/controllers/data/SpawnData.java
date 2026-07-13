package noppes.npcs.controllers.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomItem;
import noppes.npcs.NBTTags;

public class SpawnData extends WeightedRandomItem {
	public static final int DESPAWN_FORCE_NATURAL = 0;
	public static final int DESPAWN_PRESERVE_TEMPLATE = 1;
	public static final int DESPAWN_FORCE_PERSISTENT = 2;

	private static final String SPAWN_COMPOUND_PREFIX = "SpawnCompound";

	public int id = -1;
	public String name = "";
	public List<String> biomes = new ArrayList<String>();
	public HashSet<Integer> dimensions = new HashSet<Integer>();
	public HashMap<Integer, NBTTagCompound> spawnCompounds = new HashMap<Integer, NBTTagCompound>();
	public boolean animalSpawning = true;
	public boolean monsterSpawning = false;
	public boolean liquidSpawning = false;
	public boolean airSpawning = false;
	public int spawnHeightMin = 0;
	public int spawnHeightMax = 100;
	public int maxAlive = 0;
	public int cooldownTicks = 0;
	public int attemptsPerCycle = 1;
	public int playerMinDistance = 24;
	public int despawnMode = DESPAWN_FORCE_NATURAL;

	public SpawnData() {
		super(10);
	}

	public void readNBT(NBTTagCompound compound) {
		if(compound == null)
			return;

		id = compound.hasKey("SpawnId") ? compound.getInteger("SpawnId") : -1;
		name = compound.hasKey("SpawnName") ? compound.getString("SpawnName") : "";
		setItemWeight(compound.hasKey("SpawnWeight") ? compound.getInteger("SpawnWeight") : 10);

		biomes = NBTTags.getStringList(compound.getTagList("SpawnBiomes"));
		dimensions = NBTTags.getIntegerSet(compound.getTagList("SpawnDimensions"));

		spawnCompounds.clear();
		Collection<?> tags = compound.getTags();
		for(Object object : tags) {
			if(!(object instanceof NBTBase))
				continue;
			NBTBase tag = (NBTBase)object;
			String tagName = tag.getName();
			if(tagName == null || !tagName.startsWith(SPAWN_COMPOUND_PREFIX) || !(tag instanceof NBTTagCompound))
				continue;
			try {
				int slot = Integer.parseInt(tagName.substring(SPAWN_COMPOUND_PREFIX.length()));
				spawnCompounds.put(slot, compound.getCompoundTag(tagName));
			} catch(NumberFormatException ignored) {
			}
		}

		animalSpawning = !compound.hasKey("AnimalSpawning") || compound.getBoolean("AnimalSpawning");
		monsterSpawning = compound.hasKey("MonsterSpawning") && compound.getBoolean("MonsterSpawning");
		liquidSpawning = compound.hasKey("LiquidSpawning") && compound.getBoolean("LiquidSpawning");
		if(compound.hasKey("CaveSpawning"))
			airSpawning = compound.getBoolean("CaveSpawning");
		else
			airSpawning = compound.hasKey("AirSpawning") && compound.getBoolean("AirSpawning");

		spawnHeightMin = compound.hasKey("HeightMin") ? compound.getInteger("HeightMin") : 0;
		spawnHeightMax = compound.hasKey("HeightMax") ? compound.getInteger("HeightMax") : 100;
		setMaxAlive(compound.hasKey("MaxAlive") ? compound.getInteger("MaxAlive") : 0);
		setCooldownTicks(compound.hasKey("CooldownTicks") ? compound.getInteger("CooldownTicks") : 0);
		setAttemptsPerCycle(compound.hasKey("AttemptsPerCycle") ? compound.getInteger("AttemptsPerCycle") : 1);
		setPlayerMinDistance(compound.hasKey("PlayerMinDistance") ? compound.getInteger("PlayerMinDistance") : 24);
		setDespawnMode(compound.hasKey("DespawnMode") ? compound.getInteger("DespawnMode") : DESPAWN_FORCE_NATURAL);
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		if(compound == null)
			compound = new NBTTagCompound();

		compound.setInteger("SpawnId", id);
		compound.setString("SpawnName", name == null ? "" : name);
		compound.setInteger("SpawnWeight", itemWeight);

		List<String> savedBiomes = new ArrayList<String>();
		if(biomes != null) {
			for(String biome : biomes) {
				if(biome != null)
					savedBiomes.add(biome);
			}
		}
		compound.setTag("SpawnBiomes", NBTTags.nbtStringList(savedBiomes));
		compound.setTag("SpawnDimensions", NBTTags.nbtIntegerSet(dimensions));

		List<String> oldSpawnTags = new ArrayList<String>();
		for(Object object : compound.getTags()) {
			if(object instanceof NBTBase) {
				String tagName = ((NBTBase)object).getName();
				if(tagName != null && tagName.startsWith(SPAWN_COMPOUND_PREFIX))
					oldSpawnTags.add(tagName);
			}
		}
		for(String tagName : oldSpawnTags)
			compound.removeTag(tagName);

		if(spawnCompounds != null) {
			for(Map.Entry<Integer, NBTTagCompound> entry : spawnCompounds.entrySet()) {
				if(entry.getKey() == null)
					continue;
				NBTTagCompound spawnCompound = entry.getValue();
				compound.setTag(SPAWN_COMPOUND_PREFIX + entry.getKey(), spawnCompound == null ? new NBTTagCompound() : spawnCompound);
			}
		}

		compound.setBoolean("AnimalSpawning", animalSpawning);
		compound.setBoolean("MonsterSpawning", monsterSpawning);
		compound.setBoolean("LiquidSpawning", liquidSpawning);
		compound.setBoolean("CaveSpawning", airSpawning);
		compound.setInteger("HeightMin", spawnHeightMin);
		compound.setInteger("HeightMax", spawnHeightMax);
		compound.setInteger("MaxAlive", maxAlive);
		compound.setInteger("CooldownTicks", cooldownTicks);
		compound.setInteger("AttemptsPerCycle", attemptsPerCycle);
		compound.setInteger("PlayerMinDistance", playerMinDistance);
		compound.setInteger("DespawnMode", despawnMode);
		return compound;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? "" : name;
	}

	public List<String> getBiomes() {
		return biomes;
	}

	public void setBiomes(List<String> biomes) {
		this.biomes = biomes == null ? new ArrayList<String>() : new ArrayList<String>(biomes);
	}

	public HashSet<Integer> getDimensions() {
		return dimensions;
	}

	public void setDimensions(HashSet<Integer> dimensions) {
		this.dimensions = dimensions == null ? new HashSet<Integer>() : new HashSet<Integer>(dimensions);
	}

	public int getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(int itemWeight) {
		if(itemWeight < 1)
			itemWeight = 1;
		else if(itemWeight > 100)
			itemWeight = 100;
		this.itemWeight = itemWeight;
	}

	public int getWeight() {
		return getItemWeight();
	}

	public void setWeight(int weight) {
		setItemWeight(weight);
	}

	public HashMap<Integer, NBTTagCompound> getSpawnCompounds() {
		return spawnCompounds;
	}

	public void setSpawnCompounds(HashMap<Integer, NBTTagCompound> spawnCompounds) {
		this.spawnCompounds = spawnCompounds == null ? new HashMap<Integer, NBTTagCompound>() : new HashMap<Integer, NBTTagCompound>(spawnCompounds);
	}

	public boolean isAnimalSpawning() {
		return animalSpawning;
	}

	public boolean getAnimalSpawning() {
		return animalSpawning;
	}

	public void setAnimalSpawning(boolean animalSpawning) {
		this.animalSpawning = animalSpawning;
	}

	public boolean isMonsterSpawning() {
		return monsterSpawning;
	}

	public boolean getMonsterSpawning() {
		return monsterSpawning;
	}

	public void setMonsterSpawning(boolean monsterSpawning) {
		this.monsterSpawning = monsterSpawning;
	}

	public boolean isLiquidSpawning() {
		return liquidSpawning;
	}

	public boolean getLiquidSpawning() {
		return liquidSpawning;
	}

	public void setLiquidSpawning(boolean liquidSpawning) {
		this.liquidSpawning = liquidSpawning;
	}

	public boolean isAirSpawning() {
		return airSpawning;
	}

	public boolean getAirSpawning() {
		return airSpawning;
	}

	public void setAirSpawning(boolean airSpawning) {
		this.airSpawning = airSpawning;
	}

	public int getSpawnHeightMin() {
		return spawnHeightMin;
	}

	public void setSpawnHeightMin(int spawnHeightMin) {
		this.spawnHeightMin = spawnHeightMin;
	}

	public int getSpawnHeightMax() {
		return spawnHeightMax;
	}

	public void setSpawnHeightMax(int spawnHeightMax) {
		this.spawnHeightMax = spawnHeightMax;
	}

	public int getMaxAlive() {
		return maxAlive;
	}

	public void setMaxAlive(int maxAlive) {
		this.maxAlive = Math.max(0, maxAlive);
	}

	public int getCooldownTicks() {
		return cooldownTicks;
	}

	public void setCooldownTicks(int cooldownTicks) {
		this.cooldownTicks = Math.max(0, cooldownTicks);
	}

	public int getAttemptsPerCycle() {
		return attemptsPerCycle;
	}

	public void setAttemptsPerCycle(int attemptsPerCycle) {
		this.attemptsPerCycle = Math.max(1, attemptsPerCycle);
	}

	public int getPlayerMinDistance() {
		return playerMinDistance;
	}

	public void setPlayerMinDistance(int playerMinDistance) {
		this.playerMinDistance = Math.max(0, playerMinDistance);
	}

	public int getDespawnMode() {
		return despawnMode;
	}

	public void setDespawnMode(int despawnMode) {
		if(despawnMode < DESPAWN_FORCE_NATURAL || despawnMode > DESPAWN_FORCE_PERSISTENT)
			despawnMode = DESPAWN_FORCE_NATURAL;
		this.despawnMode = despawnMode;
	}
}

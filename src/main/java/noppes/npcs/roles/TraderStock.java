package noppes.npcs.roles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.constants.EnumStockReset;

public class TraderStock {

	public boolean enableStock = false;
	public boolean perPlayer = false;
	public EnumStockReset resetType = EnumStockReset.NONE;
	public long customResetTime = 0;
	public long lastResetTime = 0;
	public int[] maxStock = new int[18];
	public int[] currentStock = new int[18];
	public HashMap<String, int[]> playerStock = new HashMap<String, int[]>();

	public TraderStock() {
		for(int i = 0; i < 18; i++) {
			maxStock[i] = -1;
			currentStock[i] = -1;
		}
	}

	public synchronized int getAvailableStock(int slot, String playerName) {
		if(!enableStock || slot < 0 || slot >= 18 || maxStock[slot] < 0)
			return Integer.MAX_VALUE;

		if(perPlayer) {
			int[] purchasedAmounts = playerStock.get(playerName);
			if(purchasedAmounts == null)
				return maxStock[slot];
			return Math.max(0, maxStock[slot] - purchasedAmounts[slot]);
		}

		if(currentStock[slot] < 0)
			return maxStock[slot];
		return Math.max(0, currentStock[slot]);
	}

	public boolean hasStock(int slot, String playerName, int amount) {
		return getAvailableStock(slot, playerName) >= amount;
	}

	public synchronized boolean consumeStock(int slot, String playerName, int amount) {
		if(!enableStock || slot < 0 || slot >= 18 || maxStock[slot] < 0)
			return true;

		if(perPlayer) {
			int[] purchasedAmounts = playerStock.get(playerName);
			if(purchasedAmounts == null) {
				purchasedAmounts = new int[18];
				playerStock.put(playerName, purchasedAmounts);
			}
			if(maxStock[slot] - purchasedAmounts[slot] >= amount) {
				purchasedAmounts[slot] += amount;
				return true;
			}
			return false;
		}

		if(currentStock[slot] < 0)
			currentStock[slot] = maxStock[slot];
		if(currentStock[slot] >= amount) {
			currentStock[slot] -= amount;
			return true;
		}
		return false;
	}

	public boolean shouldReset(long currentTime) {
		if(resetType == EnumStockReset.NONE)
			return false;
		long resetInterval = resetType == EnumStockReset.MCCUSTOM || resetType == EnumStockReset.RLCUSTOM ? customResetTime : resetType.getDefaultInterval();
		return currentTime - lastResetTime >= resetInterval;
	}

	public synchronized void resetStock(long currentTime) {
		lastResetTime = currentTime;
		for(int i = 0; i < 18; i++)
			currentStock[i] = maxStock[i];
		playerStock.clear();
	}

	public boolean validateStock() {
		boolean changed = false;
		for(int i = 0; i < 18; i++) {
			if(maxStock[i] >= 0) {
				if(currentStock[i] < 0 || currentStock[i] > maxStock[i]) {
					currentStock[i] = maxStock[i];
					changed = true;
				}
			}
		}

		for(int[] purchasedAmounts : playerStock.values()) {
			for(int i = 0; i < 18; i++) {
				if(purchasedAmounts[i] < 0) {
					purchasedAmounts[i] = 0;
					changed = true;
				}
				else if(maxStock[i] >= 0 && purchasedAmounts[i] > maxStock[i]) {
					purchasedAmounts[i] = maxStock[i];
					changed = true;
				}
			}
		}
		return changed;
	}

	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("EnableStock"))
			enableStock = compound.getBoolean("EnableStock");
		if(compound.hasKey("PerPlayer"))
			perPlayer = compound.getBoolean("PerPlayer");
		if(compound.hasKey("ResetType")) {
			int resetOrdinal = compound.getInteger("ResetType");
			if(resetOrdinal >= 0 && resetOrdinal < EnumStockReset.values().length)
				resetType = EnumStockReset.values()[resetOrdinal];
		}
		if(compound.hasKey("CustomResetTime"))
			customResetTime = compound.getLong("CustomResetTime");
		if(compound.hasKey("MaxStock")) {
			int[] loadedMax = compound.getIntArray("MaxStock");
			if(loadedMax != null && loadedMax.length == 18)
				maxStock = loadedMax;
		}
		if(compound.hasKey("CurrentStock")) {
			int[] loadedCurrent = compound.getIntArray("CurrentStock");
			if(loadedCurrent != null && loadedCurrent.length == 18)
				currentStock = loadedCurrent;
		}
		if(compound.hasKey("LastResetTime"))
			lastResetTime = compound.getLong("LastResetTime");
		if(compound.hasKey("PlayerStock")) {
			playerStock.clear();
			NBTTagList playerList = compound.getTagList("PlayerStock");
			for(int i = 0; i < playerList.tagCount(); i++) {
				NBTTagCompound playerTag = (NBTTagCompound)playerList.tagAt(i);
				int[] purchasedAmounts = playerTag.getIntArray("Purchased");
				if(purchasedAmounts != null && purchasedAmounts.length == 18)
					playerStock.put(playerTag.getString("Player"), purchasedAmounts);
			}
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setBoolean("EnableStock", enableStock);
		compound.setBoolean("PerPlayer", perPlayer);
		compound.setInteger("ResetType", resetType.ordinal());
		compound.setLong("CustomResetTime", customResetTime);
		compound.setIntArray("MaxStock", maxStock);
		compound.setIntArray("CurrentStock", currentStock);
		compound.setLong("LastResetTime", lastResetTime);

		NBTTagList playerList = new NBTTagList();
		for(Map.Entry<String, int[]> entry : playerStock.entrySet()) {
			NBTTagCompound playerTag = new NBTTagCompound();
			playerTag.setString("Player", entry.getKey());
			playerTag.setIntArray("Purchased", entry.getValue());
			playerList.appendTag(playerTag);
		}
		compound.setTag("PlayerStock", playerList);
		return compound;
	}
}

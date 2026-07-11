package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerFactionData implements IPlayerData{
	public HashMap<Integer,Integer> factionData = new HashMap<Integer,Integer>();
	
	
	public void readNBT(NBTTagCompound compound) {
		HashMap<Integer,Integer> factionData = new HashMap<Integer,Integer>();
		if(compound == null)
			return;
        NBTTagList list = compound.getTagList("FactionData");
        if(list == null || list.tagCount() == 0){
        	return;
        }

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(i);
            factionData.put(nbttagcompound.getInteger("Faction"),nbttagcompound.getInteger("Points"));
        }
        this.factionData = factionData;
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(int faction : factionData.keySet()){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Faction", faction);
			nbttagcompound.setInteger("Points", factionData.get(faction));
			list.appendTag(nbttagcompound);
		}
		
		compound.setTag("FactionData", list);
		return compound;
	}

	public int getFactionPoints(int id) {
		if(!factionData.containsKey(id)){
			Faction faction = FactionController.getInstance().getFaction(id);
			factionData.put(id, faction == null? -1 : faction.defaultPoints);
		}
		return factionData.get(id);
	}

	public void increasePoints(int factionId, int points) {
		if(!factionData.containsKey(factionId)){
			Faction faction = FactionController.getInstance().getFaction(factionId);
			factionData.put(factionId, faction == null? -1 : faction.defaultPoints);
		}
		factionData.put(factionId, factionData.get(factionId) + points);
	}
	
	public NBTTagCompound getPlayerGuiData(){
		NBTTagCompound compound = new NBTTagCompound();
		writeNBT(compound);
		
		NBTTagList list = new NBTTagList();
		for(int id : factionData.keySet()){
			Faction faction = FactionController.getInstance().getFaction(id);
			if(faction == null || faction.hideFaction)
				continue;
			NBTTagCompound com = new NBTTagCompound();
			faction.writeNBT(com);
			list.appendTag(com);
		}
		compound.setTag("FactionList", list);
		
		return compound;
	}
}

package noppes.npcs.controllers;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerTransportData implements IPlayerData{
	public HashSet<Integer> transports = new HashSet<Integer>();
	public void readNBT(NBTTagCompound compound) {
		HashSet<Integer> dialogsRead = new HashSet<Integer>();
		if(compound == null)
			return;
        NBTTagList list = compound.getTagList("TransportData");
        if(list == null || list.tagCount() == 0){
        	return;
        }

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(i);
            dialogsRead.add(nbttagcompound.getInteger("Transport"));
        }
        this.transports = dialogsRead;
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(int dia : transports){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Transport", dia);
			list.appendTag(nbttagcompound);
		}
		
		compound.setTag("TransportData", list);
		return compound;
	}
	
}

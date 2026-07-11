package noppes.npcs.controllers;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerDialogData implements IPlayerData{
	public HashSet<Integer> dialogsRead = new HashSet<Integer>();
	public void readNBT(NBTTagCompound compound) {
		HashSet<Integer> dialogsRead = new HashSet<Integer>();
		if(compound == null)
			return;
        NBTTagList list = compound.getTagList("DialogData");
        if(list == null || list.tagCount() == 0){
        	return;
        }

        for(int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(i);
            dialogsRead.add(nbttagcompound.getInteger("Dialog"));
        }
        this.dialogsRead = dialogsRead;
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(int dia : dialogsRead){
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setInteger("Dialog", dia);
			list.appendTag(nbttagcompound);
		}
		
		compound.setTag("DialogData", list);
		return compound;
	}
	
}

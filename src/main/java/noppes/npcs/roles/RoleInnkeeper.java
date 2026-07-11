package noppes.npcs.roles;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.controllers.InnDoorData;

public class RoleInnkeeper extends RoleInterface{
	
	private String innName = "Inn";
	private HashMap<String,InnDoorData> doors = new HashMap<String,InnDoorData>();
	
	public RoleInnkeeper(EntityNPCInterface npc) {
		super(npc);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("InnName", innName);
		nbttagcompound.setTag("InnDoors", nbtInnDoors(doors));
	}

	private NBTBase nbtInnDoors(HashMap<String, InnDoorData> doors1) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(doors1 == null)
    		return nbttaglist;
        HashMap<String,InnDoorData> doors2 = doors1;
        for(String name : doors2.keySet())
        {
        	InnDoorData door = doors2.get(name);
        	if(door == null)
        		continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Name", name);
            nbttagcompound.setInteger("posX", door.x);
            nbttagcompound.setInteger("posY", door.y);
            nbttagcompound.setInteger("posZ", door.z);
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		innName = nbttagcompound.getString("InnName");
		doors = getInnDoors(nbttagcompound.getTagList("InnDoors"));
	}

	private HashMap<String, InnDoorData> getInnDoors(NBTTagList tagList) {
		HashMap<String, InnDoorData> list = new HashMap<String, InnDoorData>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            String name = nbttagcompound.getString("Name");
            
            InnDoorData door = new InnDoorData();
            door.x = nbttagcompound.getInteger("posX");
            door.y = nbttagcompound.getInteger("posY");
            door.z = nbttagcompound.getInteger("posZ");
            list.put(name, door);
        }
		return list;
	}

	@Override
	public boolean interact(EntityPlayer player) {
		npc.say(player, npc.advanced.getInteractLine());
		if(doors.isEmpty()){
			player.addChatMessage("No Rooms available");
			return true;
		}
		return false;
	}

}

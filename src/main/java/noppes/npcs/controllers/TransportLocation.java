package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;

public class TransportLocation {
	public int id = -1;
	public String name = "default name";
	public double posX;
	public double posY;
	public double posZ;
	
	public int npcX;
	public int npcY;
	public int npcZ;

	public int type = 0;
	public int dimension = 0;
	
	public TransportCategory category;
	
	public void readNBT(NBTTagCompound compound) {
		if(compound == null)
			return;
		id = compound.getInteger("Id");
		posX = compound.getDouble("PosX");
		posY = compound.getDouble("PosY");
		posZ = compound.getDouble("PosZ");
		npcX = compound.getInteger("PosNpcX");
		npcY = compound.getInteger("PosNpcY");
		npcZ = compound.getInteger("PosNpcZ");
		type = compound.getInteger("Type");
		dimension = compound.getInteger("Dimension");
		name = compound.getString("Name");
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("Id", id);
		compound.setDouble("PosX", posX);
		compound.setDouble("PosY", posY);
		compound.setDouble("PosZ", posZ);
		compound.setInteger("PosNpcX", npcX);
		compound.setInteger("PosNpcY", npcY);
		compound.setInteger("PosNpcZ", npcZ);
		compound.setInteger("Type", type);
		compound.setInteger("Dimension", dimension);
		compound.setString("Name", name);
		return compound;
	}

	public boolean isNpc(EntityNPCInterface npc) {
		return npc.startPos[0] == npcX && npc.startPos[1] == npcY && npc.startPos[2] == npcZ;
	}

	public boolean isDefault() {
		return type == 1;
	}
}

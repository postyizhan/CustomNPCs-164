package noppes.npcs.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.TextBlock;

public class TileBigSign extends TileEntity {
	
	public int rotation;
	public boolean canEdit = true;
	public boolean hasChanged = true;

    private String signText = "";
    public TextBlock block;

    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        rotation = compound.getInteger("SignRotation");
        setText(compound.getString("SignText"));
    }

    public void writeToNBT(NBTTagCompound compound){
    	super.writeToNBT(compound);
    	compound.setInteger("SignRotation", rotation);
    	compound.setString("SignText", signText);
    }
	
    public boolean canUpdate(){
        return false;
    }

    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt){
    	NBTTagCompound compound = pkt.data;
    	readFromNBT(compound);
    }
    
    public void setText(String text){
    	this.signText = text;
    	hasChanged = true;
    }
    
    public String getText(){
    	return signText;
    }
    
    @Override
    public Packet getDescriptionPacket(){
    	NBTTagCompound compound = new NBTTagCompound();
    	writeToNBT(compound);
    	Packet132TileEntityData packet = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, compound);
    	return packet;
    }
}


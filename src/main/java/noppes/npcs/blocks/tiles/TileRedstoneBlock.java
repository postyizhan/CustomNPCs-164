package noppes.npcs.blocks.tiles;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.controllers.Availability;

public class TileRedstoneBlock extends TileEntity {
	public int onRangeX = 6;
	public int onRangeY = 6;
	public int onRangeZ = 6;
	public int offRangeX = 10;
	public int offRangeY = 10;
	public int offRangeZ = 10;

	public Availability availability = new Availability();
	
	public boolean isActivated = false;
	
	public TileRedstoneBlock(){
		
	}
	private int ticks = 10;
	@Override
	public void updateEntity(){
		if(worldObj.isRemote)
			return;
		ticks--;
		if(ticks > 0)
			return;
		ticks = 20;
		
		Block block = Block.blocksList[worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
		if(block == null || block instanceof BlockNpcRedstone == false){
			worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
			return;
		}

		if(CustomNpcs.FreezeNPCs){
			if(isActivated)
				setActive(block,false);
			return;
		}
		
		if(!isActivated){
			List<EntityPlayer> list = getPlayerList(onRangeX,onRangeY,onRangeZ);
			if(list.isEmpty())
				return;
			for(EntityPlayer player : list){
				if(availability.isAvailable(player)){
					setActive(block,true);
					return;
				}
			}
		}
		else{
			List<EntityPlayer> list = getPlayerList(offRangeX,offRangeY,offRangeZ);
			for(EntityPlayer player : list){
				if(availability.isAvailable(player))
					return;
			}
			setActive(block,false);
		}
	
	}
	private void setActive(Block block, boolean bo){
		isActivated = bo;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, isActivated?1:0,2);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		if(hasWorldObj())
			block.onBlockAdded(worldObj, xCoord, yCoord, zCoord);
	}
	private List<EntityPlayer> getPlayerList(int x, int y, int z){
		return worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(x, y, z));	
	}
	
	@Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	onRangeX = par1NBTTagCompound.getInteger("BlockOnRangeX");
    	onRangeY = par1NBTTagCompound.getInteger("BlockOnRangeY");
    	onRangeZ = par1NBTTagCompound.getInteger("BlockOnRangeZ");
    	offRangeX = par1NBTTagCompound.getInteger("BlockOffRangeX");
    	offRangeY = par1NBTTagCompound.getInteger("BlockOffRangeY");
    	offRangeZ = par1NBTTagCompound.getInteger("BlockOffRangeZ");
    	
    	isActivated = par1NBTTagCompound.getBoolean("BlockActivated");
    	
    	availability.readFromNBT(par1NBTTagCompound);
		if(hasWorldObj())
			setActive(getBlockType(), isActivated);
    }

    /**
     * Writes a tile entity to NBT.
     */
	@Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("BlockOnRangeX", onRangeX);
    	par1NBTTagCompound.setInteger("BlockOnRangeY", onRangeY);
    	par1NBTTagCompound.setInteger("BlockOnRangeZ", onRangeZ);
    	par1NBTTagCompound.setInteger("BlockOffRangeX", offRangeX);
    	par1NBTTagCompound.setInteger("BlockOffRangeY", offRangeY);
    	par1NBTTagCompound.setInteger("BlockOffRangeZ", offRangeZ);

    	par1NBTTagCompound.setBoolean("BlockActivated", isActivated);
    	
    	availability.writeToNBT(par1NBTTagCompound);
    }
}

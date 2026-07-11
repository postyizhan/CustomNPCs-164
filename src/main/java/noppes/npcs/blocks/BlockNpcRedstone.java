package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;

public class BlockNpcRedstone extends BlockContainer
{
    public BlockNpcRedstone(int par1)
    {
        super(par1, Material.circuits);
        setCreativeTab(CustomItems.tab);
    }

    @Override    
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	if(par1World.isRemote)
    		return false;
		ItemStack currentItem = player.inventory.getCurrentItem();
		if (currentItem != null	&& currentItem.itemID == CustomItems.wand.itemID) {
			TileEntity tile = par1World.getBlockTileEntity(i, j, k);
			NBTTagCompound compound = new NBTTagCompound();
			tile.writeToNBT(compound);
			NoppesUtilServer.sendData(player, EnumPacketType.RedstoneBlockSave, compound);
        	return true;
		}
		return false;
    }
    @Override
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, blockID);
        par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, blockID);
        par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, blockID);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, blockID);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack item){
    	if(entityliving instanceof EntityPlayer && world.isRemote){
    		CustomNpcs.proxy.openGui(i, j, k, EnumGuiType.RedstoneBlock, (EntityPlayer) entityliving);
    	}
    }
    /**
     * Called whenever the block is removed.
     */
    @Override
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) 
    {
    	onBlockAdded(par1World, par2, par3, par4);
    }

    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	if(isActivated(par1IBlockAccess, par2, par3, par4) > 0)
    		return 0xFF6B68;
    	else
    		return super.colorMultiplier(par1IBlockAccess, par2, par3, par4);
    }
    /**
     * Is this block indirectly powering the block on the specified side
     */
    @Override
    public int isProvidingStrongPower(IBlockAccess par1World, int par2, int par3, int par4, int par5)
    {
        return isActivated(par1World, par2, par3, par4);
    }
    @Override
    public int isProvidingWeakPower(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return isActivated(par1IBlockAccess, par2, par3, par4);
    }
    @Override
    public boolean canProvidePower()
    {
        return true;
    }
	@Override
    public TileEntity createNewTileEntity(World var1){
		return new TileRedstoneBlock();
	}
	
	public int isActivated(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
		return par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1?15 : 0;
	}
}

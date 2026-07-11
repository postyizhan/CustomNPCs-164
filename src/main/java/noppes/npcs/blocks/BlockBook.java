package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.permissions.CustomNpcsPermissions;

public class BlockBook extends BlockRotated{

	public BlockBook() {
        super(Block.planks);
        setBlockBounds(0, 0, 0, 1, 0.2f, 1);
	}
	    
    @Override    
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9){
    	if(par1World.isRemote)
    		return true;
    	TileEntity tile = par1World.getBlockTileEntity(i, j, k);
    	if(!(tile instanceof TileBook))
    		return false;
		ItemStack currentItem = player.inventory.getCurrentItem();
		if (currentItem != null	&& currentItem.getItem() == CustomItems.wand) {
			((TileBook)tile).book.itemID = Item.writableBook.itemID;
		}
		NBTTagCompound compound = ((TileBook)tile).book.writeToNBT(new NBTTagCompound());
    	NoppesUtilServer.sendData((EntityPlayerMP)player, EnumPacketType.OpenBook, i, j, k, compound);
    	return true;
    }
    
    @Override   
    public String getUnlocalizedName(){
    	return "item.book";
    }

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileBook();
	}
}

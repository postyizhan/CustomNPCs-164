package noppes.npcs.blocks.tiles;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class TileBook extends TileColorable {
	public ItemStack book = new ItemStack(Item.writableBook);
	
	@Override
    public void readFromNBT(NBTTagCompound compound){
        super.readFromNBT(compound);
        book = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Items"));
        if(book == null)
        	book = new ItemStack(Item.writableBook);
    }

	@Override
    public void writeToNBT(NBTTagCompound compound){
    	super.writeToNBT(compound);
    	compound.setTag("Items", book.writeToNBT(new NBTTagCompound()));
    }
}

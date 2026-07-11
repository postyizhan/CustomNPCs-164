package noppes.npcs;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NpcMiscInventory implements IInventory {
	public HashMap<Integer,ItemStack> items = new HashMap<Integer,ItemStack>();
	public int stackLimit = 64;

	public NBTTagCompound getToNBT(){
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setTag("NpcMiscInv", NBTTags.nbtItemStackList(items));
		return nbttagcompound;
	}
	public void setFromNBT(NBTTagCompound nbttagcompound){
		items = NBTTags.getItemStackList(nbttagcompound.getTagList("NpcMiscInv"));
	}
	private int size;
	public NpcMiscInventory(int size){
		this.size = size;
	}
	@Override
	public int getSizeInventory() {
		return size;
	}

	@Override
	public ItemStack getStackInSlot(int var1) {
		return items.get(var1);
	}

	@Override
	public ItemStack decrStackSize(int par1, int par2) {
        ItemStack var4 = null;
        if (items.get(par1) != null)
        {

            if (items.get(par1).stackSize <= par2)
            {
                var4 = items.get(par1);
                items.put(par1,null);
            }
            else
            {
                var4 = items.get(par1).splitStack(par2);

                if (items.get(par1).stackSize == 0)
                {
                	items.put(par1,null);
                }
            }
        }
		return var4;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1) {
        if (items.get(var1) != null)
        {
            ItemStack var3 = items.get(var1);
            items.put(var1,null);
            return var3;
        }
        return null;
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2) {
        items.put(var1,var2);
	}

	@Override
	public String getInvName() {
		return "Npc Misc Inventory";
	}

	@Override
	public int getInventoryStackLimit() {
		return stackLimit;
	}

	@Override
	public void onInventoryChanged() {
		
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openChest() {
		
	}

	@Override
	public void closeChest() {
		
	}
	@Override
	public boolean isInvNameLocalized() {
		return true;
	}
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

}

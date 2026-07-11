// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

// Referenced classes of package net.minecraft.src:
//            IInventory, ItemStack, IInvBasic, EntityPlayer

public class InventoryNpcTrader
    implements IInventory
{

    private String inventoryTitle;
    private int slotsCount;
    private ItemStack inventoryContents[];
    private ContainerNPCTrader con;

    public InventoryNpcTrader(String s, int i,ContainerNPCTrader con)
    {
    	this.con = con;
        inventoryTitle = s;
        slotsCount = i;
        inventoryContents = new ItemStack[i];
    }
    
    public ItemStack getStackInSlot(int i)
    {
        ItemStack toBuy = inventoryContents[i];
        if(toBuy == null)
        	return null;
        
        return ItemStack.copyItemStack(toBuy);
    }

    public ItemStack decrStackSize(int i, int j)
    {
        if(inventoryContents[i] != null)
        {
            ItemStack itemstack = inventoryContents[i];
            return  ItemStack.copyItemStack(itemstack);
        } else
        {
            return null;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
    	if(itemstack != null)
    		inventoryContents[i] = itemstack.copy();
        onInventoryChanged();
        
    }

    public int getSizeInventory()
    {
        return slotsCount;
    }

    public String getInvName()
    {
        return inventoryTitle;
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public void onInventoryChanged()
    {
    	con.onCraftMatrixChanged(this);
    }

    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    public void openChest()
    {
    }

    public void closeChest()
    {
    }

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
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

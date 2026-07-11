// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

// Referenced classes of package net.minecraft.src:
//            IInventory, ItemStack, IInvBasic, EntityPlayer

public class InventoryNPC
    implements IInventory
{

    private String inventoryTitle;
    private int slotsCount;
    private ItemStack inventoryContents[];
    private Container con;

    public InventoryNPC(String s, int i,Container con)
    {
    	this.con = con;
        inventoryTitle = s;
        slotsCount = i;
        inventoryContents = new ItemStack[i];
    }
    @Override
    public ItemStack getStackInSlot(int i)
    {
        return inventoryContents[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if(inventoryContents[i] != null)
        {
            if(inventoryContents[i].stackSize <= j)
            {
                ItemStack itemstack = inventoryContents[i];
                inventoryContents[i] = null;
                //onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = inventoryContents[i].splitStack(j);
            if(inventoryContents[i].stackSize == 0)
            {
                inventoryContents[i] = null;
            }
            //onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        inventoryContents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        //onInventoryChanged();
    }

    @Override
    public int getSizeInventory()
    {
        return slotsCount;
    }

    @Override
    public String getInvName()
    {
        return inventoryTitle;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void onInventoryChanged()
    {
    	con.onCraftMatrixChanged(this);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return false;
    }

    @Override
    public void openChest()
    {
    }

    @Override
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

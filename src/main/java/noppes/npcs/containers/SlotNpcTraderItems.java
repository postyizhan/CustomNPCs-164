// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;


// Referenced classes of package net.minecraft.src:
//            Slot, ItemStack, ItemArmor, Item, 
//            Block, ContainerPlayer, IInventory

class SlotNpcTraderItems extends Slot
{


    public SlotNpcTraderItems( IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }
    public void onPickupFromSlot(ItemStack itemstack)
    {
    	if(itemstack == null)
    		return;
    	if(getStack() == null)
    		return;
    	if(itemstack.itemID != getStack().itemID)
    		return;
    	itemstack.stackSize--;
        //onSlotChanged();
    }

    public int getSlotStackLimit()
    {
        return 64;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
        
    }
}

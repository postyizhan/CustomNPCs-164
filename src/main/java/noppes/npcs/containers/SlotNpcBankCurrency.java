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

public class SlotNpcBankCurrency extends Slot
{

    public ItemStack item; 

    public SlotNpcBankCurrency(ContainerNPCBankInterface containerplayer, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
    }

    public int getSlotStackLimit()
    {
        return 64;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
    	if(item == null)
    		return false;
		if(item.itemID == itemstack.itemID){
			if(!item.getHasSubtypes() || item.getItemDamage() == itemstack.getItemDamage())
				return true;
		}
		return false;
    }
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.roles.RoleFollower;


// Referenced classes of package net.minecraft.src:
//            Slot, ItemStack, ItemArmor, Item, 
//            Block, ContainerPlayer, IInventory

class SlotNpcMercenaryCurrency extends Slot
{

	RoleFollower role; /* synthetic field */

    public SlotNpcMercenaryCurrency(RoleFollower role, IInventory inv, int i, int j, int k)
    {
        super(inv, i, j, k);
        this.role = role;
    }

    public int getSlotStackLimit()
    {
        return 64;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
    	int id = itemstack.itemID;
    	for(ItemStack is : role.inventory.items.values()){
    		if(id == is.itemID){
    			if(itemstack.getHasSubtypes() && itemstack.getItemDamage() != is.getItemDamage())
    				continue;
    			return true;
    		}
    	}
        return false;
        
    }
}

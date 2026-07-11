// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;


// Referenced classes of package net.minecraft.src:
//            Slot, ItemStack, ItemArmor, Item, 
//            Block, ContainerPlayer, IInventory

class SlotNPCArmor extends Slot
{

    final int armorType; /* synthetic field */
    final ContainerNPCInv inventory; /* synthetic field */

    SlotNPCArmor(ContainerNPCInv containerplayer, IInventory iinventory, int i, int j, int k, int l)
    {
        super(iinventory, i, j, k);
        inventory = containerplayer;
        armorType = l;
    }

    public int getSlotStackLimit()
    {
        return 1;
    }
    public Icon getBackgroundIconIndex()
    {
        return ItemArmor.func_94602_b(this.armorType);
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if(itemstack.getItem() instanceof ItemArmor)
        {
            return ((ItemArmor)itemstack.getItem()).armorType == armorType;
        }
        if(itemstack.getItem() instanceof ItemBlock)
        {
            return armorType == 0;
        } else
        {
            return false;
        }
    }
}

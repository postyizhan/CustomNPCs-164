// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EntityNPCInterface;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerNPCInv extends Container
{    
    public ContainerNPCInv(EntityNPCInterface npc,EntityPlayer player)
    {
        for(int l = 0; l < 4; l++)
        {
        	addSlotToContainer(new SlotNPCArmor(this,npc.inventory, l, 9, 22 + l * 18,l));
        }

        addSlotToContainer(new Slot(npc.inventory, 4, 81, 22));
        addSlotToContainer(new Slot(npc.inventory, 5, 81, 40));
        addSlotToContainer(new Slot(npc.inventory, 6, 81, 58));

        for(int l = 0; l < 8; l++)
        {
        	addSlotToContainer(new Slot(npc.inventory,  l + 7 , 191 , 17 + l * 22));
        }

        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
            	addSlotToContainer(new Slot(player.inventory, l1 + i1 * 9 + 9, l1 * 18 + 8, 113 + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
        	addSlotToContainer(new Slot(player.inventory, j1, j1 * 18 + 8, 171));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i)
    {
        return null;
    }
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}

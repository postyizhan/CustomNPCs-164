// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.roles.RoleFollower;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerNPCFollower extends Container
{
    public InventoryNPC currencyMatrix;
	public RoleFollower role;

    public ContainerNPCFollower(EntityNPCInterface npc,EntityPlayer player)
    {
        role = (RoleFollower) npc.roleInterface;

    	currencyMatrix = new InventoryNPC("currency", 1,this);
    	addSlotToContainer(new SlotNpcMercenaryCurrency(role,currencyMatrix, 0, 26, 9));
        
        for(int j1 = 0; j1 < 9; j1++)
        {
        	addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
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
    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        if (!entityplayer.worldObj.isRemote){
	        ItemStack itemstack = currencyMatrix.getStackInSlotOnClosing(0);
	        if(itemstack != null && !entityplayer.worldObj.isRemote)
	        {
	            entityplayer.entityDropItem(itemstack,0f);
	        }
        }

    }
}

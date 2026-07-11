// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerNPCBankUpgrade extends ContainerNPCBankInterface
{
    public ContainerNPCBankUpgrade(EntityPlayer player, int slot, int bankid)
    {
    	super(player,slot,bankid);
    }
    public boolean isAvailable(){
    	return true;
    }
    public boolean canBeUpgraded(){
    	return true;
    }
    public int xOffset(){
    	return 54;
    }
    public int getRowNumber() {
		return 3;
	}
}

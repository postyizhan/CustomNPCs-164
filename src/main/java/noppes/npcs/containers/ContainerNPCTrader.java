// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.roles.RoleTrader;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerNPCTrader extends Container
{

    public InventoryNPC currencyMatrix;
	public RoleTrader role;

    public ContainerNPCTrader(EntityNPCInterface npc,EntityPlayer player)
    {
        role = (RoleTrader) npc.roleInterface;
    	
    	currencyMatrix = new InventoryNPC("currency", 1,this);
    	addSlotToContainer(new SlotNpcTraderCurrency(this,currencyMatrix, 0, 15, 39));

        for(int i = 0; i < 18; i++)
        {
        	int x =  62;
        	x += i%3 * 45;
        	int y = 9;
        	y += i/3 * 22;
        	addSlotToContainer(new Slot(role.inventorySold, i, x, y));
        }

        for(int i1 = 0; i1 < 3; i1++)
        {
            for(int l1 = 0; l1 < 9; l1++)
            {
            	addSlotToContainer(new Slot(player.inventory, l1 + i1 * 9 + 9, 8 + l1 * 18, 144  + i1 * 18));
            }

        }

        for(int j1 = 0; j1 < 9; j1++)
        {
        	addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 202));
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
        ItemStack itemstack = currencyMatrix.getStackInSlot(0);
        if(itemstack != null && !entityplayer.worldObj.isRemote)
        {
            currencyMatrix.setInventorySlotContents(0, null);
            entityplayer.entityDropItem(itemstack,0f);
        }

    }
    @Override
    public ItemStack slotClick(int i, int j, int par3, EntityPlayer entityplayer)
    {
    	if(par3 == 6)
    		par3 = 0;
    	if(i <= 0 || i >= 19)
        	return super.slotClick(i, j, par3, entityplayer);
    	
		if( j ==1 )
			return null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot == null || slot.getStack() == null)
        	return null;
        ItemStack item = slot.getStack();
        if(!canGivePlayer(item, entityplayer))
        	return null;
		if(role.stock.enableStock && !role.stock.hasStock(i-1, entityplayer.username, 1))
			return null;
        if(!canBuy(role.inventoryCurrency.items.get(i-1),entityplayer))
        	return null;
        ItemStack soldItem = item.copy();
        givePlayer(soldItem, entityplayer);
		if(role.stock.enableStock)
			role.stock.consumeStock(i-1, entityplayer.username, 1);
        return soldItem;
    	
    }
    private boolean canBuy(ItemStack item, EntityPlayer entityplayer) {
		ItemStack currency = currencyMatrix.getStackInSlot(0);
		if(currency == null || !NoppesUtilPlayer.compareItems(currency,item,false))
			return false;
		int price = item.stackSize;
		if(currency.stackSize < price)
			return false;
		if(currency.stackSize - price == 0)
			currencyMatrix.setInventorySlotContents(0, null);
		else
			currency = currency.splitStack(price);
		return true;
	}

	private boolean canGivePlayer(ItemStack item,EntityPlayer entityplayer){
        ItemStack itemstack3 = entityplayer.inventory.getItemStack();
        if(itemstack3 == null){
        	return true;
        }
        else if(NoppesUtilPlayer.compareItems(itemstack3,item,false)){
            int k1 = item.stackSize;
            if(k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize())
            {
                return true;
            }
        }
        return false;
    }
    private void givePlayer(ItemStack item,EntityPlayer entityplayer){
        ItemStack itemstack3 = entityplayer.inventory.getItemStack();
        if(itemstack3 == null){
        	entityplayer.inventory.setItemStack(item);
        }
        else if(NoppesUtilPlayer.compareItems(itemstack3,item,false)){

            int k1 = item.stackSize;
            if(k1 > 0 && k1 + itemstack3.stackSize <= itemstack3.getMaxStackSize())
            {
                itemstack3.stackSize += k1;
            }
        }
    }
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerDataController;

// Referenced classes of package net.minecraft.src:
//            Container, InventoryCrafting, InventoryCraftResult, SlotCrafting, 
//            InventoryPlayer, Slot, CraftingManager, IInventory, 
//            World, EntityPlayer, Block, ItemStack

public class ContainerNPCBankInterface extends Container
{
    public InventoryNPC currencyMatrix;
    private EntityPlayer player;
    public SlotNpcBankCurrency currency;
    public int slot = 0;
    public int bankid;
    private PlayerBankData data;
    public ContainerNPCBankInterface(EntityPlayer player, int slot, int bankid)
    {
    	super();
    	this.bankid = bankid;
    	this.slot = slot;
    	this.player = player;
    	    	
        currencyMatrix = new InventoryNPC("currency", 1, this);
        if(!isAvailable() || canBeUpgraded()){
        	currency = new SlotNpcBankCurrency(this, currencyMatrix, 0, 80, 29);
        	addSlotToContainer(currency);
        }
        
        NpcMiscInventory items = new NpcMiscInventory(54);
        if(!player.worldObj.isRemote){
    		data = PlayerDataController.instance.getBankData(player,bankid);
        	items = data.getBankOrDefault(bankid).itemSlots.get(slot);
        }
        
        int xOffset = xOffset();
        for (int j = 0; j < getRowNumber(); j++)
        {
            for (int i1 = 0; i1 < 9; i1++)
            {
            	int id = i1 + j * 9;
            	addSlotToContainer(new Slot(items, id, 8 + i1 * 18, 17 + xOffset + j * 18));
            }
        }
        
        if(isUpgraded())
        	xOffset += 54;
        for (int k = 0; k < 3; k++)
        {
            for (int j1 = 0; j1 < 9; j1++)
            {
            	addSlotToContainer(new Slot(player.inventory, j1 + k * 9 + 9, 8 + j1 * 18, 86 + xOffset + k * 18));
            }
        }

        for (int l = 0; l < 9; l++)
        {
        	addSlotToContainer(new Slot(player.inventory, l, 8 + l * 18, 144 + xOffset ));
        }
    }
    public int getRowNumber() {
		return 0;
	}
	public int xOffset(){
    	return 0;
    }
    @Override
    public void onCraftMatrixChanged(IInventory inv){
    	
    }
    public boolean isAvailable(){
    	return false;
    }
    public boolean isUpgraded(){
    	return false;
    }
    public boolean canBeUpgraded(){
    	return false;
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
            ItemStack var3 = this.currencyMatrix.getStackInSlot(0);
            currencyMatrix.setInventorySlotContents(0, null);
            if (var3 != null)
            {
            	entityplayer.dropPlayerItem(var3);
            }
    		PlayerDataController.instance.savePlayerData(player, data);
        }
    }
}

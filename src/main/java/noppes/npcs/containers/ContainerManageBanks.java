package noppes.npcs.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import noppes.npcs.controllers.Bank;

public class ContainerManageBanks extends Container
{
	public Bank bank;
	
    public ContainerManageBanks(EntityPlayer player)
    {
   		bank = new Bank();
   		
        for(int i = 0; i < 6; i++)
        {
        	int x =  36;
        	int y = 21;
        	y += i * 22;
        	addSlotToContainer(new Slot(bank.currencyInventory, i, x, y));
        }   
        
        for(int i = 0; i < 6; i++)
        {
        	int x =  142;
        	int y = 21;
        	y += i * 22;
        	addSlotToContainer(new Slot(bank.upgradeInventory, i, x, y));
        }   

        for(int j1 = 0; j1 < 9; j1++)
        {
        	addSlotToContainer(new Slot(player.inventory, j1, 8 + j1 * 18, 154));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int i)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(i);
        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(i >= 0 && i < 7)
            {
                if(!mergeItemStack(itemstack1, 7, 43, true))
                {
                    return null;
                }
            } 
            else if(i >= 20 && i < 47)
            {
                if(!mergeItemStack(itemstack1, 36, 43, false))
                {
                    return null;
                }
            } 
            else if(i >= 47 && i < 56)
            {
                if(!mergeItemStack(itemstack1, 7, 34, false))
                {
                    return null;
                }
            } 
            else if(!mergeItemStack(itemstack1, 7, 43, false))
            {
                return null;
            }
            
            if(itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            } 
            else
            {
                slot.onSlotChanged();
            }
            
            if(itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(par1EntityPlayer,itemstack1);
            } 
            else
            {
                return null;
            }
        }
        return itemstack;
    }
    
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

	public void setBank(Bank bank2) {
		for(int i = 0; i< 6; i++){
			bank.currencyInventory.setInventorySlotContents(i, bank2.currencyInventory.getStackInSlot(i));
			bank.upgradeInventory.setInventorySlotContents(i, bank2.upgradeInventory.getStackInSlot(i));
		}
	}
}


package noppes.npcs.roles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;

public class RoleTrader extends RoleInterface{

	public NpcMiscInventory inventoryCurrency;
	public NpcMiscInventory inventorySold;
	public TraderStock stock;
	
	public RoleTrader(EntityNPCInterface npc) {
		super(npc);
		inventoryCurrency = new NpcMiscInventory(18);
		inventorySold = new NpcMiscInventory(18);
		stock = new TraderStock();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    	nbttagcompound.setCompoundTag("TraderCurrency", inventoryCurrency.getToNBT());
    	nbttagcompound.setCompoundTag("TraderSold", inventorySold.getToNBT());
		nbttagcompound.setCompoundTag("Stock", stock.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		inventoryCurrency.setFromNBT(nbttagcompound.getCompoundTag("TraderCurrency"));
		inventorySold.setFromNBT(nbttagcompound.getCompoundTag("TraderSold"));
		if(nbttagcompound.hasKey("Stock"))
			stock.readFromNBT(nbttagcompound.getCompoundTag("Stock"));
	}
	
	@Override
	public boolean interact(EntityPlayer player) {
		npc.say(player, npc.advanced.getInteractLine());
		long now = stock.resetType.isRealTime() ? System.currentTimeMillis() : npc.worldObj.getTotalWorldTime();
		if(stock.enableStock && stock.shouldReset(now))
			stock.resetStock(now);
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTrader, npc);
		return false;
	}

	public boolean hasStock(int slot, String playerName, int amount) {
		return stock.hasStock(slot, playerName, amount);
	}

	public boolean consumeStock(int slot, String playerName, int amount) {
		return stock.consumeStock(slot, playerName, amount);
	}

	public int getAvailableStock(int slot, String playerName) {
		return stock.getAvailableStock(slot, playerName);
	}

	public boolean hasCurrency(ItemStack itemstack) {
		if(itemstack == null)
			return false;
		for(ItemStack item : inventoryCurrency.items.values()){
			if(item != null && NoppesUtilPlayer.compareItems(item, itemstack,false)){
				return true;
			}
		}
		return false;
	}

}

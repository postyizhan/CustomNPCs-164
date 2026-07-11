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
	
	public RoleTrader(EntityNPCInterface npc) {
		super(npc);
		inventoryCurrency = new NpcMiscInventory(18);
		inventorySold = new NpcMiscInventory(18);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {

    	nbttagcompound.setCompoundTag("TraderCurrency", inventoryCurrency.getToNBT());
    	nbttagcompound.setCompoundTag("TraderSold", inventorySold.getToNBT());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		inventoryCurrency.setFromNBT(nbttagcompound.getCompoundTag("TraderCurrency"));
		inventorySold.setFromNBT(nbttagcompound.getCompoundTag("TraderSold"));
	}
	
	@Override
	public boolean interact(EntityPlayer player) {
		npc.say(player, npc.advanced.getInteractLine());
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerTrader, npc);
		return false;
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

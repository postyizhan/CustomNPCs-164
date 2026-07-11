package noppes.npcs.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.NpcMiscInventory;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.controllers.PlayerDataController;

public class RolePostman extends RoleInterface{

	public NpcMiscInventory inventory = new NpcMiscInventory(1);
	private List<EntityPlayer> recentlyChecked = new ArrayList<EntityPlayer>();
	private List<EntityPlayer> toCheck;
	
	public RolePostman(EntityNPCInterface npc) {
		super(npc);
	}

	public boolean aiShouldExecute() {
		if(npc.ticksExisted % 20 != 0)
			return false;

		toCheck = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox.expand(10, 10, 10));
		toCheck.removeAll(recentlyChecked);

		List<EntityPlayer> listMax = npc.worldObj.getEntitiesWithinAABB(EntityPlayer.class, npc.boundingBox.expand(20, 20, 20));
		recentlyChecked.retainAll(listMax);
		recentlyChecked.addAll(toCheck);
		
		for(EntityPlayer player : toCheck){
			if(PlayerDataController.instance.hasMail(player))
				player.addChatMessage("You've got mail");			
		}
		return false;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    	nbttagcompound.setCompoundTag("PostInv", inventory.getToNBT());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		inventory.setFromNBT(nbttagcompound.getCompoundTag("PostInv"));
	}


	@Override
	public boolean interact(EntityPlayer player) {
		NoppesUtilServer.sendOpenGui(player, EnumGuiType.PlayerMailman, npc);
		return true;
	}

}

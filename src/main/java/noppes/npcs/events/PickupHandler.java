package noppes.npcs.events;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import cpw.mods.fml.common.IPickupNotifier;

public class PickupHandler implements IPickupNotifier{

	@Override
	public void notifyPickup(EntityItem item, EntityPlayer player) {
		if(player.worldObj.isRemote)
			return;
		PlayerQuestData playerdata = PlayerDataController.instance.getQuestData(player);
		playerdata.checkQuestCompletion(player,EnumQuestType.Item);
	}

}

package noppes.npcs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.items.ItemShield;

public class ServerEventHandler {

	
	@ForgeSubscribe
	public void invoke(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		int block = player.worldObj.getBlockId(event.x, event.y, event.z);

		if((block == CustomItems.banner.blockID || block == CustomItems.wallBanner.blockID || block == CustomItems.sign.blockID)  && event.action == Action.RIGHT_CLICK_BLOCK){
			ItemStack item = player.inventory.getCurrentItem();
			if(item == null || item.getItem() == null)
				return;
			int y = event.y;
			int meta = player.worldObj.getBlockMetadata(event.x, event.y, event.z);
			if(meta >= 7)
				y--;
			TileBanner tile = (TileBanner)player.worldObj.getBlockTileEntity(event.x, y, event.z);
			if(!tile.canEdit()){
		        if(item.getItem() == CustomItems.wand){
		        	tile.time = System.currentTimeMillis();
		        }
				return;
			}

        	if(!player.worldObj.isRemote){
				tile.icon = item.copy();
		    	player.worldObj.markBlockForUpdate(event.x, y, event.z);
		    	event.setCanceled(true);
        	}
	    	
		}
	}
}

package noppes.npcs.events;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.client.controllers.CloneController;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.permissions.CustomNpcsPermissions;


public class ItemInteractEvent{
	
	public static EntityVillager Merchant;

	@ForgeSubscribe
	public void invoke(EntityInteractEvent event) {
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if(item == null)
			return;
		boolean isRemote = event.entityPlayer.worldObj.isRemote;
		boolean npcInteracted = event.target instanceof EntityNPCInterface;

		if(item.itemID == CustomItems.wand.itemID && npcInteracted && !isRemote){
			if (!CustomNpcsPermissions.Instance.hasPermission(event.entityPlayer.username,"customnpcs.npc.edit")){
				return;
			}
			event.setCanceled(true);
			NoppesUtilServer.sendOpenGui(event.entityPlayer, EnumGuiType.MainMenuDisplay, (EntityNPCInterface) event.target);
		}
		else if(item.itemID == CustomItems.cloner.itemID && isRemote){
	    	CloneController.toClone = event.target;
			CustomNpcs.proxy.openGui(0,0,0, EnumGuiType.MobSpawnerAdd, event.entityPlayer);
		}
		else if(item.itemID == CustomItems.wand.itemID && event.target instanceof EntityVillager){
			event.setCanceled(true);
			Merchant = (EntityVillager)event.target;
			
			if(!isRemote){
				EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
				player.openGui(CustomNpcs.instance, EnumGuiType.MerchantAdd.ordinal(), player.worldObj, 0, 0, 0);
		        MerchantRecipeList merchantrecipelist = Merchant.getRecipes(player);
	
		        if (merchantrecipelist != null)
		        {
	            	NoppesUtilServer.sendData(player, EnumPacketType.MerchantAdd, merchantrecipelist);
		        }
			}
		}
		
	}

}

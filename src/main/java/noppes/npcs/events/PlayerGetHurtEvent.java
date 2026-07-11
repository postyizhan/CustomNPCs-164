package noppes.npcs.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import noppes.npcs.items.ItemShield;


public class PlayerGetHurtEvent{

	@ForgeSubscribe
	public void invoke(LivingHurtEvent event) {
		if(event.entityLiving instanceof EntityPlayer){
			playerhurt((EntityPlayer) event.entityLiving,event);
		}
	}

	private void playerhurt(EntityPlayer player, LivingHurtEvent hurt) {
		if(hurt.source.isUnblockable() || hurt.source.isFireDamage())
			return;
		if(!player.isBlocking())
			return;
		ItemStack item = player.getCurrentEquippedItem();
		if(item == null || !(item.getItem() instanceof ItemShield))
			return;
		float damage = item.getItemDamage() + hurt.ammount;
		
		item.damageItem((int) hurt.ammount, player);

		if(damage > item.getMaxDamage())
			hurt.ammount = damage - item.getMaxDamage();
		else{
			hurt.ammount = 0;
			hurt.setCanceled(true);
		}
	}

}

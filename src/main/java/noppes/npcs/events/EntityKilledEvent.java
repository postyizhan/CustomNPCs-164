package noppes.npcs.events;

import java.util.HashMap;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import noppes.npcs.CustomItems;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.items.ItemExcalibur;
import noppes.npcs.quests.QuestKill;


public class EntityKilledEvent{

	@ForgeSubscribe
	public void invoke(LivingDeathEvent event) {
		if(!event.entityLiving.worldObj.isRemote){
			entityDied(event.entityLiving,event);
		}
	}

	private void entityDied(EntityLivingBase entity, LivingDeathEvent hurt) {
		
		if(hurt.source.getEntity() == null || !(hurt.source.getEntity() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) hurt.source.getEntity();
		
		doExcalibur(player,entity);
		
		doQuest(player, entity);
		
		if(entity instanceof EntityNPCInterface)
			doFactionPoints(player,(EntityNPCInterface)entity);
	}

	private void doExcalibur(EntityPlayer player, EntityLivingBase entity) {
		ItemStack item = player.getCurrentEquippedItem();
		if(item == null || item.getItem() != CustomItems.excalibur)
			return;
		NoppesUtilServer.sendData(player, EnumPacketType.PlayMusic, "Failboat103 - Excalibuuur");
		player.addChatMessage("<" + item.getItem().getStatName() + "> " + ItemExcalibur.quotes[player.getRNG().nextInt(ItemExcalibur.quotes.length)]);
	}

	private void doFactionPoints(EntityPlayer player, EntityNPCInterface npc) {
		npc.advanced.factions.addPoints(player);
	}

	private void doQuest(EntityPlayer player, EntityLivingBase entity) {
		PlayerQuestData playerdata = PlayerDataController.instance.getQuestData(player);
		boolean change = false;
		String entityName = EntityList.getEntityString(entity);		
		
		for(QuestData data : playerdata.activeQuests.values()){
			if(data.quest.type != EnumQuestType.Kill)
				continue;
			String name = entityName;
			QuestKill quest = (QuestKill) data.quest.questInterface;
			if(quest.targets.containsKey(entity.getEntityName()))
				name = entity.getEntityName();
			else if(!quest.targets.containsKey(name))
				continue;
			HashMap<String, Integer> killed = quest.getKilled(data);
			if(killed.containsKey(name) && killed.get(name) >= quest.targets.get(name))
				continue;
			int amount = 0;
			if(killed.containsKey(name))
				amount = killed.get(name);
			killed.put(name, amount + 1);
			quest.setKilled(data, killed);
			change = true;
		}
		if(!change)
			return;
		
		playerdata.checkQuestCompletion(player,EnumQuestType.Kill);
		PlayerDataController.instance.savePlayerData(player, playerdata);
	}

}

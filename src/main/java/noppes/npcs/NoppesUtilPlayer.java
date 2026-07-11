package noppes.npcs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumOptionType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.containers.ContainerNPCBankInterface;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.BankData;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Line;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.QuestData;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.roles.RoleFollower;
import cpw.mods.fml.common.network.PacketDispatcher;

public class NoppesUtilPlayer {

	public static void changeFollowerState(EntityPlayerMP player, EntityNPCInterface npc) {
		if(npc.advanced.role != EnumRoleType.Follower)
			return;
		
		RoleFollower role = (RoleFollower) npc.roleInterface;
		EntityPlayer owner = role.getOwner();
		if(owner == null || !owner.username.equals(player.username))
			return;
		
		role.isFollowing = !role.isFollowing;
	}
	public static void hireFollower(EntityPlayerMP player, EntityNPCInterface npc) {
		if(npc.advanced.role != EnumRoleType.Follower)
			return;
		Container con = player.openContainer;
		if(con == null || !(con instanceof ContainerNPCFollowerHire))
			return;
		
		ContainerNPCFollowerHire container = (ContainerNPCFollowerHire) con;
		RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}
	public static void extendFollower(EntityPlayerMP player, EntityNPCInterface npc) {
		if(npc.advanced.role != EnumRoleType.Follower)
			return;
		Container con = player.openContainer;
		if(con == null || !(con instanceof ContainerNPCFollower))
			return;
		
		ContainerNPCFollower container = (ContainerNPCFollower) con;
		RoleFollower role = (RoleFollower) npc.roleInterface;
		followerBuy(role, container.currencyMatrix, player, npc);
	}
	public static void transport(EntityPlayerMP player, EntityNPCInterface npc, String location){
		TransportLocation loc = TransportController.getInstance().getTransport(location);
        PlayerTransportData playerdata = PlayerDataController.instance.getTransportData(player);

		if(loc == null || !loc.isDefault() && !playerdata.transports.contains(loc.id))
			return;
		if(player.dimension != loc.dimension){
			System.out.println(loc.dimension + " transfering");
			int dim = player.dimension;
			MinecraftServer server = MinecraftServer.getServer();
            WorldServer wor = server.worldServerForDimension(player.dimension);
	        player.playerNetServerHandler.setPlayerLocation(loc.posX, loc.posY, loc.posZ, player.rotationYaw, player.rotationPitch);
			server.getConfigurationManager().transferPlayerToDimension(player, loc.dimension, new CustomTeleporter(wor));

	        if(dim == 1){
	            if (player.isEntityAlive())
	            {
	                player.setLocationAndAngles(loc.posX, loc.posY, loc.posZ, player.rotationYaw, player.rotationPitch);
	                wor.spawnEntityInWorld(player);
	            }
	        }
	        player.worldObj.updateEntityWithOptionalForce(player, false);
		}
		else{
			player.playerNetServerHandler.setPlayerLocation(loc.posX, loc.posY, loc.posZ, player.rotationYaw, player.rotationPitch);
	    	player.worldObj.updateEntityWithOptionalForce(player, false);
		}
	}
	private static void followerBuy(RoleFollower role,IInventory currencyInv,EntityPlayerMP player, EntityNPCInterface npc){
    	ItemStack currency = currencyInv.getStackInSlot(0);
		if(currency == null)
    		return;
    	HashMap<ItemStack,Integer> cd = new HashMap<ItemStack,Integer>();
    	for(int i : role.inventory.items.keySet()){
    		ItemStack is = role.inventory.items.get(i);
    		if(is == null || is.itemID != currency.itemID || is.getHasSubtypes() && is.getItemDamage() != currency.getItemDamage())
    			continue;
    		int days = 1;
    		if(role.rates.containsKey(i))
    			days = role.rates.get(i);
    		
    		cd.put(is,days);
    	}
    	if(cd.size() == 0)
    		return;
    	int stackSize = currency.stackSize;
    	int days = 0;
    	
    	int possibleDays = 0;
    	int possibleSize = stackSize;
    	while(true){
        	for(ItemStack item : cd.keySet()){
        		int rDays = cd.get(item);
        		int rValue = item.stackSize;
        		if(rValue > stackSize)
        			continue;
        		int newStackSize = stackSize % rValue;
        		int size = stackSize - newStackSize;
        		int posDays = (size / rValue) * rDays;
        		if(possibleDays <= posDays){
        			possibleDays = posDays;
        			possibleSize = newStackSize;
        		}
        	}
        	if(stackSize == possibleSize)
        		break;
        	stackSize = possibleSize;
        	days += possibleDays;
        	possibleDays = 0;
    	}
    	if(days == 0)
    		return;
    	if(stackSize <= 0)
    		currencyInv.setInventorySlotContents(0, null);
    	else
    		currency = currency.splitStack(stackSize);
    	
    	npc.say(player, new Line(role.dialogHire.replaceAll("\\{days\\}", days+"")));
    	role.setOwner(player.username);
    	role.addDays(days);
	}

	public static void bankUpgrade(EntityPlayerMP player, EntityNPCInterface npc) {
		if(npc.advanced.role != EnumRoleType.Bank)
			return;
		Container con = player.openContainer;
		if(con == null || !(con instanceof ContainerNPCBankInterface))
			return;
		
		ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		Bank bank = BankController.getInstance().getBank(container.bankid);
		ItemStack item = bank.upgradeInventory.getStackInSlot(container.slot);
		if(item == null)
			return;

		int price = item.stackSize;
		ItemStack currency = container.currencyMatrix.getStackInSlot(0);
		if(currency == null || price > currency.stackSize)
			return;
		if(currency.stackSize - price == 0)
			container.currencyMatrix.setInventorySlotContents(0, null);
		else
			currency = currency.splitStack(price);
		PlayerBankData data = PlayerDataController.instance.getBankData(player,bank.id);
        BankData bankData = data.getBank(bank.id);
		bankData.upgradedSlots.put(container.slot, true);
		PlayerDataController.instance.savePlayerData(player, data);

		bankData.openBankGui(player, npc, bank.id, container.slot);
	}
	public static void bankUnlock(EntityPlayerMP player, EntityNPCInterface npc) {
		if(npc.advanced.role != EnumRoleType.Bank)
			return;
		Container con = player.openContainer;
		if(con == null || !(con instanceof ContainerNPCBankInterface))
			return;
		
		ContainerNPCBankInterface container = (ContainerNPCBankInterface) con;
		Bank bank = BankController.getInstance().getBank(container.bankid);
		
		ItemStack item = bank.currencyInventory.getStackInSlot(container.slot);
		if(item == null)
			return;
		
		int price = item.stackSize;
		ItemStack currency = container.currencyMatrix.getStackInSlot(0);
		if(currency == null || price > currency.stackSize)
			return;
		if(currency.stackSize - price == 0)
			container.currencyMatrix.setInventorySlotContents(0, null);
		else
			currency = currency.splitStack(price);
		
		PlayerBankData data = PlayerDataController.instance.getBankData(player,bank.id);
        BankData bankData = data.getBank(bank.id);
		if(bankData.unlockedSlots + 1 <= bank.maxSlots)
			bankData.unlockedSlots++;
		
		PlayerDataController.instance.savePlayerData(player,data);
		
		bankData.openBankGui(player, npc, bank.id, container.slot);
	}


	public static void sendData(EnumPlayerPacket enu, Object... obs) {
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = NoppesUtil.getDataOutputStream(bytes);
			out.writeInt(enu.ordinal());
			for(Object ob : obs){
				if(ob instanceof Integer)
					out.writeInt((Integer) ob);
				else if(ob instanceof String)
					out.writeUTF((String) ob);
				else if(ob instanceof Long)
					out.writeLong((Long) ob);
				else if(ob instanceof Boolean)
					out.writeBoolean((Boolean)ob);
				else if(ob instanceof NBTTagCompound)
					CompressedStreamTools.write((NBTTagCompound) ob, out);
				
			}
			out.close();
	        PacketDispatcher.sendPacketToServer(new Packet250CustomPayload("CNPCs Player",bytes.toByteArray()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void dialogSelected(int dialogId, int optionId,EntityPlayer player,EntityNPCInterface npc) {
		Dialog dialog = DialogController.instance.dialogs.get(dialogId);
		if(dialog == null || !dialog.hasDialogs(player) && !dialog.hasOtherOptions())
			return;
    	DialogOption option = dialog.options.get(optionId);
    	if(option == null || option.optionType == EnumOptionType.DialogOption && (!option.isAvailable(player) || !option.hasDialog()) || option.optionType == EnumOptionType.Disabled)
    		return;
    	if(option.optionType == EnumOptionType.RoleOption){
    		if(npc.roleInterface != null)
    			npc.roleInterface.interact(player);
    	}
    	else if(option.optionType == EnumOptionType.DialogOption){
    		NoppesUtilServer.openDialog(player, npc, option.getDialog());
    	}
    	else if(option.optionType == EnumOptionType.CommandBlock){
            String command = option.command.replaceAll("@dp", player.username);
            TileEntityCommandBlock tile = new TileEntityCommandBlock();
            tile.worldObj = npc.worldObj;
            tile.setCommand(command);
            tile.setCommandSenderName("@"+npc.getCommandSenderName());
            tile.xCoord = MathHelper.floor_double(npc.posX);
            tile.yCoord = MathHelper.floor_double(npc.posY);
            tile.zCoord = MathHelper.floor_double(npc.posZ);
            tile.executeCommandOnPowered(npc.worldObj);
    	}
	}
	public static void sendQuestLogData(EntityPlayerMP player) {
        if(!PlayerQuestController.hasActiveQuests(player)){
        	return;
        }
        QuestLogData data = new QuestLogData();
        data.setData(player);
		NoppesUtilServer.sendData(player, EnumPacketType.GuiData, data.writeNBT());
	}
	public static void questCompletion(EntityPlayerMP player, int questId) {
		PlayerQuestData playerdata = PlayerDataController.instance.getQuestData(player);
		QuestData data = playerdata.activeQuests.get(questId);
		if(data == null)
			return;
		
		if(!data.quest.questInterface.isCompleted(player))
			return;
		

		data.quest.questInterface.handleComplete(player);
		if(data.quest.rewardExp > 0){
			player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 0.5F * ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.8F));
            
            player.addExperience(data.quest.rewardExp);
		}
		data.quest.factionOptions.addPoints(player);
		if(data.quest.mail.isValid()){
			PlayerDataController.instance.addPlayerMessage(player.username, data.quest.mail);
		}
		for(ItemStack item : data.quest.rewardItems.items.values()){
			CustomNpcs.GivePlayerItem(player, player, item);
		}
		PlayerQuestController.setQuestFinished(data.quest, player);
		if(data.quest.hasNewQuest()) PlayerQuestController.addActiveQuest(data.quest.getNextQuest(), player);
	}
	
	public static boolean compareItems(ItemStack var9, ItemStack var10, boolean ignoreDamage){

        if (var10 == null && var9 != null || var10 != null && var9 == null)
        {
            return false;
        }

        if (var9.itemID != var10.itemID)
        {
            return false;
        }

        if (var9.getItemDamage() != -1 && var9.getItemDamage() != var10.getItemDamage() && !ignoreDamage)
        {
            return false;
        }
        if(var9.stackTagCompound != null && (var10.stackTagCompound == null || !var9.stackTagCompound.equals(var10.stackTagCompound)))
        {
            return false;
        }
		return true;
	}

}

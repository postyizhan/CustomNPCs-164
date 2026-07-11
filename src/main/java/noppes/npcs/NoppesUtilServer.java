package noppes.npcs;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.WeightedRandomMinecart;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.WorldServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogCategory;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.PlayerBankData;
import noppes.npcs.controllers.PlayerData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerDialogData;
import noppes.npcs.controllers.PlayerFactionData;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.PlayerQuestData;
import noppes.npcs.controllers.PlayerTransportData;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestCategory;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.TransportCategory;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.roles.RoleTransporter;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;



public class NoppesUtilServer {
	private static HashMap<String,EntityNPCInterface> selectedNpcs = new HashMap<String,EntityNPCInterface>();
	private static HashMap<String,Quest> editingQuests = new HashMap<String,Quest>();


	public static byte[] CompoundToBytes(NBTTagCompound compound,EnumPacketType type){
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(type.ordinal());
			if(compound != null)
				CompressedStreamTools.write(compound, out);
			out.close();
			return bytes.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
   
    public static void setEditingNpc(EntityPlayer player, EntityNPCInterface npc){
    	selectedNpcs.put(player.username, npc);
    	if(npc != null)
    		sendData(player, EnumPacketType.EditingNpc, npc.entityId);
    }
    public static EntityNPCInterface getEditingNpc(EntityPlayer player){
    	return selectedNpcs.get(player.username);
    }

	public static void setEditingQuest(EntityPlayer player, Quest quest) {
		editingQuests.put(player.username, quest);
	}
    public static Quest getEditingQuest(EntityPlayer player){
    	return editingQuests.get(player.username);
    }

    private static void sendRoleData(EntityPlayer player, EntityNPCInterface npc){
    	if(npc.advanced.role == EnumRoleType.None)
    		return;
    	NBTTagCompound comp = new NBTTagCompound();
    	npc.roleInterface.writeEntityToNBT(comp);
    	comp.setInteger("EntityId", npc.entityId);
    	comp.setInteger("Role", npc.advanced.role.ordinal());
		byte[] data = CompoundToBytes(comp,EnumPacketType.SaveRole);
        PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("CNPCs Client",data),(Player) player);
    }

	public static void sendFactionDataAll(EntityPlayerMP player) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(Faction faction : FactionController.getInstance().factions.values()){
			map.put(faction.name, faction.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}
	
	public static void sendBankDataAll(EntityPlayerMP player) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(Bank bank : BankController.getInstance().banks.values()){
			map.put(bank.name, bank.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}
	
	public static void consumeItemStack(int i, EntityPlayer player){
		ItemStack item = player.inventory.getCurrentItem();
		if(player.capabilities.isCreativeMode || item == null)
			return;
		
        --item.stackSize;
        if (item.stackSize <= 0)
        	player.destroyCurrentEquippedItem();
	}
	
	public static void openDialog(EntityPlayer player, EntityNPCInterface npc, Dialog dia){
		Dialog dialog = dia.copy(player);
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		try {
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(EnumPacketType.Dialog.ordinal());
			out.writeInt(npc.entityId);
			CompressedStreamTools.write(dialog.writeToNBT(new NBTTagCompound()), out);
			out.close();
	        PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("CNPCs Client",bytes.toByteArray()),(Player) player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dia.factionOptions.addPoints(player);
        if(dialog.hasQuest())
        	PlayerQuestController.addActiveQuest(dialog.getQuest(),player);
        if(!dialog.command.isEmpty()){
            String command = dialog.command.replaceAll("@dp", player.username);
            TileEntityCommandBlock tile = new TileEntityCommandBlock();
            tile.worldObj = npc.worldObj;
            tile.setCommand(command);
            tile.setCommandSenderName("@"+npc.getCommandSenderName());
            tile.xCoord = MathHelper.floor_double(npc.posX);
            tile.yCoord = MathHelper.floor_double(npc.posY);
            tile.zCoord = MathHelper.floor_double(npc.posZ);
            tile.executeCommandOnPowered(npc.worldObj);
        }
        if(dialog.mail.isValid())
        	PlayerDataController.instance.addPlayerMessage(player.username, dialog.mail);
		PlayerDataController.instance.setDialogRead(dialog.id, player);
		setEditingNpc(player, npc);
	}
	
	public static DataOutputStream getDataOutputStream(ByteArrayOutputStream stream) throws IOException{
        return new DataOutputStream(new GZIPOutputStream(stream));
	}
	public static void sendOpenGui(EntityPlayer player,
			EnumGuiType gui, EntityNPCInterface npc) {
		sendOpenGui(player, gui, npc, 0, 0, 0);
	}
	public static void sendOpenGui(EntityPlayer player,
			EnumGuiType gui, EntityNPCInterface npc, int i, int j, int k) {
		if(!(player instanceof EntityPlayerMP))
			return;
				
		setEditingNpc(player, npc);
		sendExtraData(player, npc,gui, i, j, k);
		if(CustomNpcs.proxy.getServerGuiElement(gui.ordinal(), player, player.worldObj, i, j, k) != null){
			player.openGui(CustomNpcs.instance, gui.ordinal(), player.worldObj, i, j, k);
			return;
		}
		else{
			try {
			    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				DataOutputStream out = getDataOutputStream(bytes);
				out.writeInt(EnumPacketType.Gui.ordinal());
				out.writeInt(gui.ordinal());
				out.writeInt(i);
				out.writeInt(j);
				out.writeInt(k);
				out.close();
		        PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("CNPCs Client",bytes.toByteArray()),(Player) player);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<String> list = getScrollData(player, gui, npc);
		if(list == null || list.isEmpty())
			return;
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(EnumPacketType.ScrollList.ordinal());
			for(String line : list)
				out.writeUTF(line);
			out.close();
	        PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("CNPCs Client",bytes.toByteArray()),(Player) player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private static void sendExtraData(EntityPlayer player, EntityNPCInterface npc, EnumGuiType gui, int i, int j, int k) {
		if(gui == EnumGuiType.PlayerFollower || gui == EnumGuiType.PlayerFollowerHire || gui == EnumGuiType.PlayerTrader || gui == EnumGuiType.PlayerTransporter){
			sendRoleData(player, npc);
		}
	}

	private static ArrayList<String> getScrollData(EntityPlayer player, EnumGuiType gui, EntityNPCInterface npc) {
		if(gui == EnumGuiType.PlayerTransporter){
			RoleTransporter role = (RoleTransporter) npc.roleInterface;
			ArrayList<String> list = new ArrayList<String>();
	        TransportLocation location = role.getLocation();
	        String name = role.getLocation().name;
	        for(TransportLocation loc: location.category.getDefaultLocations()){
	        	if(!list.contains(loc.name)){
	        		list.add(loc.name);
	        	}
	        }
	        PlayerTransportData playerdata = PlayerDataController.instance.getTransportData(player);
	        for(int i : playerdata.transports){
	        	TransportLocation loc = TransportController.getInstance().getTransport(i);
	        	if(loc != null && location.category.locations.containsKey(loc.id)){
		        	if(!list.contains(loc.name)){
		        		list.add(loc.name);
		        	}
	        	}
	        }
	        list.remove(name);
	        return list;
		}
		return null;
	}
	public static void spawnParticle(Entity entity,String particle,int dimension){
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	try {
			DataOutputStream stream = getDataOutputStream(bytes);
			stream.writeInt(EnumPacketType.Particle.ordinal());
			stream.writeDouble(entity.posX);
			stream.writeDouble(entity.posY);
			stream.writeDouble(entity.posZ);
			stream.writeFloat(entity.height);
			stream.writeFloat(entity.width);
			stream.writeFloat(entity.yOffset);
			
			stream.writeUTF(particle);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Packet250CustomPayload packet = new Packet250CustomPayload("CNPCs Client",bytes.toByteArray());
        PacketDispatcher.sendPacketToAllAround(entity.posX, entity.posY, entity.posZ, 60, dimension, packet);
    }


	public static void deleteNpc(EntityNPCInterface npc,EntityPlayer player) {
    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    	try {
			DataOutputStream stream = getDataOutputStream(bytes);
			stream.writeInt(EnumPacketType.Delete.ordinal());
			stream.writeInt(npc.entityId);
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        Packet250CustomPayload packet = new Packet250CustomPayload("CNPCs Client",bytes.toByteArray());
        PacketDispatcher.sendPacketToAllAround(npc.posX, npc.posY, npc.posZ, 256, player.dimension, packet);
	}

	public static void createMobSpawner(DataInputStream dis,EntityPlayer player) {
		try {
			int x = dis.readInt();
			int y = dis.readInt();
			int z = dis.readInt();
			NBTTagCompound comp = CompressedStreamTools.read(dis);
			comp.removeTag("Pos");

    		if(comp.getString("id").equalsIgnoreCase("entityhorse")){
    			player.sendChatToPlayer(ChatMessageComponent.createFromText("Currently you cant create horse spawner, its a minecraft bug"));
    			return;
    		}
			
    		player.worldObj.setBlock(x, y, z, Block.mobSpawner.blockID);
    		TileEntityMobSpawner tile = (TileEntityMobSpawner) player.worldObj.getBlockTileEntity(x, y, z);
    		MobSpawnerBaseLogic logic = tile.getSpawnerLogic();
    		logic.setRandomMinecart(new WeightedRandomMinecart(logic, comp, comp.getString("id")));
    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendDialogCategoryData(EntityPlayerMP player) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(DialogCategory category : DialogController.instance.categories.values()){
			map.put(category.title, category.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendQuestCategoryData(EntityPlayerMP player) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(QuestCategory category : QuestController.instance.categories.values()){
			map.put(category.title, category.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendPlayerData(EnumPlayerData type, EntityPlayerMP player, String name) throws IOException {
		Map<String,Integer> map = new HashMap<String,Integer>();
        
		if(type == EnumPlayerData.Players){
	        File loc = PlayerDataController.instance.getSaveDir();
	        for(File file : loc.listFiles()){
	        	if(file.isDirectory() || !file.getName().endsWith(".dat"))
	        		continue;
	        	map.put(file.getName().substring(0, file.getName().length() - 4), 0);
	        }
		}
		else{
			PlayerData playerdata = new PlayerData(PlayerDataController.instance.loadPlayerData(name));
			if(type == EnumPlayerData.Dialog){
				PlayerDialogData data = playerdata.dialogData;
				
		        for(int questId : data.dialogsRead){
		        	Dialog dialog = DialogController.instance.dialogs.get(questId);
		        	if(dialog == null)
		        		continue;
		        	map.put(dialog.category.title + ": " + dialog.title,questId);
		        }
			}
			else if(type == EnumPlayerData.Quest){
				PlayerQuestData data = playerdata.questData;
	
		        for(int questId : data.activeQuests.keySet()){
		        	Quest quest = QuestController.instance.quests.get(questId);
		        	if(quest == null)
		        		continue;
		        	map.put(quest.category.title + ": " + quest.title + "(Active quest)",questId);
		        }
		        for(int questId : data.finishedQuests.keySet()){
		        	Quest quest = QuestController.instance.quests.get(questId);
		        	if(quest == null)
		        		continue;
		        	map.put(quest.category.title + ": " + quest.title + "(Finished quest)",questId);
		        }
			}
			else if(type == EnumPlayerData.Transport){
				PlayerTransportData data = playerdata.transportData;
	
		        for(int questId : data.transports){
		        	TransportLocation location = TransportController.getInstance().getTransport(questId);
		        	if(location == null)
		        		continue;
		        	map.put(location.category.title + ": " + location.name,questId);
		        }
			}
			else if(type == EnumPlayerData.Bank){
				PlayerBankData data = playerdata.bankData;
	
		        for(int bankId : data.banks.keySet()){
		        	Bank bank = BankController.getInstance().banks.get(bankId);
		        	if(bank == null)
		        		continue;
		        	map.put(bank.name,bankId);
		        }
			}
			else if(type == EnumPlayerData.Factions){
				PlayerFactionData data = playerdata.factionData;
		        for(int factionId : data.factionData.keySet()){
		        	Faction faction = FactionController.getInstance().factions.get(factionId);
		        	if(faction == null)
		        		continue;
		        	map.put(faction.name + "(" + data.getFactionPoints(factionId) + ")" ,factionId);
		        }
			}
		}
		HashMap<String, Integer> toSend = new HashMap<String,Integer>();
		for(String s : map.keySet()){
			if(toSend.size() == 1000){
				sendData(player, EnumPacketType.ScrollData, toSend);
				toSend = new HashMap<String,Integer>();
			}
			toSend.put(s, map.get(s));
		}
		
		sendData(player, EnumPacketType.ScrollData, toSend);
	}
	
	public static void removePlayerData(DataInputStream dis, EntityPlayerMP player) throws IOException {
		int id = dis.readInt();
		if(EnumPlayerData.values().length <= id)
			return;
		String name = dis.readUTF();
		EnumPlayerData type = EnumPlayerData.values()[id];
		PlayerData playerdata = new PlayerData(PlayerDataController.instance.loadPlayerData(name));

        if(type == EnumPlayerData.Players){
            File file = new File(PlayerDataController.instance.getSaveDir(),name + ".dat");
            if(!file.exists())
            	return;
        	file.delete();
        }
        if(type == EnumPlayerData.Quest){
        	PlayerQuestData data = playerdata.questData;
        	int questId = dis.readInt();
        	data.activeQuests.remove(questId);
        	data.finishedQuests.remove(questId);
        	PlayerDataController.instance.savePlayerData(name, playerdata.getNBT());
        }
        if(type == EnumPlayerData.Dialog){
        	PlayerDialogData data = playerdata.dialogData;
        	data.dialogsRead.remove(dis.readInt());
        	PlayerDataController.instance.savePlayerData(name, playerdata.getNBT());
        }
        if(type == EnumPlayerData.Transport){
        	PlayerTransportData data = playerdata.transportData;
        	data.transports.remove(dis.readInt());
        	PlayerDataController.instance.savePlayerData(name, playerdata.getNBT());
        }
        if(type == EnumPlayerData.Bank){
        	PlayerBankData data = playerdata.bankData;
        	data.banks.remove(dis.readInt());
        	PlayerDataController.instance.savePlayerData(name, playerdata.getNBT());
        }
        if(type == EnumPlayerData.Factions){
        	PlayerFactionData data = playerdata.factionData;
        	data.factionData.remove(dis.readInt());
        	PlayerDataController.instance.savePlayerData(name, playerdata.getNBT());
        }
        EntityPlayer pl = player.worldObj.getPlayerEntityByName(name);
        if(pl != null)
        	pl.getEntityData().removeTag("CustomNpcsId");
        sendPlayerData(type, player, name);
	}
	
	public static void sendRecipeData(EntityPlayerMP player, int size) {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		if(size == 3){
			for(RecipeCarpentry recipe : RecipeController.instance.globalRecipes.values()){
				map.put(recipe.name, recipe.id);
			}
		}
		else{
			for(RecipeCarpentry recipe : RecipeController.instance.anvilRecipes.values()){
				map.put(recipe.name, recipe.id);
			}
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendDialogData(EntityPlayerMP player, DialogCategory category) {
		if(category == null)
			return;
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(Dialog dialog : category.dialogs.values()){
			map.put(dialog.title, dialog.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendQuestData(EntityPlayerMP player, QuestCategory category) {
		if(category == null)
			return;
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(Quest quest : category.quests.values()){
			map.put(quest.title, quest.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendTransportCategoryData(EntityPlayerMP player) {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(TransportCategory category : TransportController.getInstance().categories.values()){
			map.put(category.title, category.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendTransportData(EntityPlayerMP player, int categoryid) {
		TransportCategory category = TransportController.getInstance().categories.get(categoryid);
		if(category == null)
			return;
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(TransportLocation transport : category.locations.values()){
			map.put(transport.name, transport.id);
		}
		sendData(player, EnumPacketType.ScrollData, map);
	}
	public static void sendData(EntityPlayer player, EnumPacketType enu, Object... obs) {
        PacketDispatcher.sendPacketToPlayer(getPacket(enu, obs),(Player) player);
	}
	public static void sendDataToAll(Entity entity, EnumPacketType enu, Object... obs) {
        Packet packet = getPacket(enu, obs);
		((WorldServer)entity.worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(entity, packet);
	}
	
	public static Packet250CustomPayload getPacket(EnumPacketType enu, Object... obs){
		try {
		    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			DataOutputStream out = getDataOutputStream(bytes);
			out.writeInt(enu.ordinal());
			for(Object ob : obs){
				if(ob == null)
					continue;
				if(ob instanceof Map){
					Map<String,Integer> map = (Map<String, Integer>) ob;
					for(String key : map.keySet()){
						int value = map.get(key);
						out.writeInt(value);
						out.writeUTF(key);
					}
				}
				else if(ob instanceof Enum)
					out.writeInt(((Enum<?>) ob).ordinal());
				else if(ob instanceof Double)
					out.writeDouble((Double) ob);
				else if(ob instanceof Float)
					out.writeFloat((Float) ob);
				else if(ob instanceof Integer)
					out.writeInt((Integer) ob);
				else if(ob instanceof String)
					out.writeUTF((String) ob);
				else if(ob instanceof NBTTagCompound)
					CompressedStreamTools.write((NBTTagCompound) ob, out);
				else if(ob instanceof MerchantRecipeList)
					((MerchantRecipeList)ob).writeRecipiesToStream(out);
				
			}
			out.close();
			bytes.close();
	        return new Packet250CustomPayload("CNPCs Client",bytes.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void sendNpcDialogs(EntityPlayer player) {
		EntityNPCInterface npc = getEditingNpc(player);
		if(npc == null)
			return;
		for(int pos : npc.dialogs.keySet()){
			DialogOption option = npc.dialogs.get(pos);
			if(option == null || !option.hasDialog())
				continue;
				
			NBTTagCompound compound = option.writeNBT();
			compound.setInteger("Position", pos);
			sendData(player, EnumPacketType.GuiData, compound);
			
		}
	}

	public static DialogOption setNpcDialog(int slot, int dialogId, EntityPlayer player) throws IOException {
		EntityNPCInterface npc = getEditingNpc(player);
		if(npc == null)
			return null;
		if(!npc.dialogs.containsKey(slot))
			npc.dialogs.put(slot, new DialogOption());
		
		DialogOption option = npc.dialogs.get(slot);
		option.dialogId = dialogId;
		if(option.hasDialog())
			option.title = option.getDialog().title;
		
		return option;
	}

	public static void saveTileEntity(EntityPlayerMP player, DataInputStream dis) throws IOException {
		NBTTagCompound compound = CompressedStreamTools.read(dis);
		int x = compound.getInteger("x");
		int y = compound.getInteger("y");
		int z = compound.getInteger("z");
		
		TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
		if(tile != null)
			tile.readFromNBT(compound);
		
	}

	public static void setRecipeGui(EntityPlayerMP player, RecipeCarpentry recipe){
		if(recipe == null)
			return;
		if(!(player.openContainer instanceof ContainerManageRecipes))
			return;
		
		ContainerManageRecipes container = (ContainerManageRecipes) player.openContainer;
		container.setRecipe(recipe);
		
		sendData(player, EnumPacketType.GuiData, recipe.writeNBT());
	}

	public static void sendBank(EntityPlayerMP player,Bank bank) {
		NBTTagCompound compound = new NBTTagCompound();
		bank.writeEntityToNBT(compound);
		NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);

		if(player.openContainer instanceof ContainerManageBanks){
			((ContainerManageBanks)player.openContainer).setBank(bank);
		}
		player.sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
	}

	public static void sendNearbyNpcs(EntityPlayerMP player) {
		List<EntityNPCInterface> npcs = player.worldObj.getEntitiesWithinAABB(EntityNPCInterface.class, player.boundingBox.expand(64, 64, 64));
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(EntityNPCInterface npc : npcs){
			if(npc.isDead)
				continue;
			float distance = player.getDistanceToEntity(npc);
	        DecimalFormat df = new DecimalFormat("#.#");
	        String s = df.format(distance);
	        if(distance < 10)
	        	s = "0" + s;
			map.put(s + " : " + npc.display.name, npc.entityId);
		}
		
		sendData(player, EnumPacketType.ScrollData, map);
	}

	public static void sendGuiError(EntityPlayer player, int i) {
		sendData(player, EnumPacketType.GuiError, i, new NBTTagCompound());
	}

	public static void sendGuiClose(EntityPlayerMP player, int i, NBTTagCompound comp) {
		sendData(player, EnumPacketType.GuiClose, i, comp);
		
	}

}

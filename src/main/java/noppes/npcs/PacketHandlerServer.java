package noppes.npcs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.village.MerchantRecipeList;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPlayerData;
import noppes.npcs.constants.EnumRoleType;
import noppes.npcs.controllers.Bank;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogCategory;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.DialogOption;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestCategory;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeCarpentry;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.TransportLocation;
import noppes.npcs.permissions.CustomNpcsPermissions;
import noppes.npcs.roles.RoleTrader;
import noppes.npcs.roles.RoleTransporter;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerServer implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(!packet.channel.equals("CNPCs Server"))
			return;
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(packet.data))));
			server(dis,(EntityPlayerMP) player,EnumPacketType.values()[dis.readInt()]);
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	private void server(DataInputStream dis, EntityPlayerMP player, EnumPacketType type) throws IOException{
		if(CustomNpcs.OpsOnly && !MinecraftServer.getServer().getConfigurationManager().getOps().contains(player.username.toLowerCase())){
			MinecraftServer.getServer().logWarning(player.username + ": tried to use custom npcs without being an op");
			return;
		}
		
		if(type.hasPermission() && !CustomNpcsPermissions.Instance.hasPermission(player.username, type.permission))
			return;
		
		EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
		if(type.needsNpc && npc == null)
			return;
		if(type == EnumPacketType.Delete){
			npc.delete();
			NoppesUtilServer.deleteNpc(npc,player);
		}
		else if(type == EnumPacketType.GetTileEntity){
			TileEntity tile = player.worldObj.getBlockTileEntity(dis.readInt(), dis.readInt(), dis.readInt());
			NBTTagCompound compound = new NBTTagCompound();
			tile.writeToNBT(compound);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
		}
		else if(type == EnumPacketType.ChangeModel){
			npc.delete();
			NoppesUtilServer.deleteNpc(npc,player);

			int x = dis.readInt();
			int y = dis.readInt();
			int z = dis.readInt();
			npc = (EntityNPCInterface)EntityList.createEntityFromNBT(CompressedStreamTools.read(dis), player.worldObj);

        	npc.startPos = new int[]{x,y,z};
    		npc.setPosition(x + 0.5F, npc.getStartYPos(), z + 0.5F);
    		npc.setHealth(npc.getMaxHealth());
    		
			player.worldObj.spawnEntityInWorld(npc);
			NoppesUtilServer.setEditingNpc(player, npc);
		}
		else if(type == EnumPacketType.Bank){
			BankController.getInstance().loadBanks(dis);
			BankController.getInstance().saveBanks();
		}
		else if(type == EnumPacketType.BanksGet){
			NoppesUtilServer.sendBankDataAll(player);
		}
		else if(type == EnumPacketType.BankGet){
			Bank bank = BankController.getInstance().getBank(dis.readInt());
			NoppesUtilServer.sendBank(player,bank);
		}
		else if(type == EnumPacketType.BankSave){
			Bank bank = new Bank();
			bank.readEntityFromNBT(CompressedStreamTools.read(dis));
			BankController.getInstance().saveBank(bank);
			NoppesUtilServer.sendBankDataAll(player);
			NoppesUtilServer.sendBank(player,bank);
		}
		else if(type == EnumPacketType.BankRemove){
			BankController.getInstance().removeBank(dis.readInt());			
			NoppesUtilServer.sendBankDataAll(player);
			NoppesUtilServer.sendBank(player,new Bank());
		}
		else if(type == EnumPacketType.SaveNpc){
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			npc.readFromNBT(compound);
			npc.reset();
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.RemoteMainMenu){
			Entity entity = player.worldObj.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			NoppesUtilServer.sendOpenGui(player, EnumGuiType.MainMenuDisplay, (EntityNPCInterface) entity);
		}
		else if(type == EnumPacketType.RemoteDelete){
			Entity entity = player.worldObj.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			npc = (EntityNPCInterface) entity;
			npc.delete();
			NoppesUtilServer.deleteNpc(npc,player);
			NoppesUtilServer.sendNearbyNpcs(player);
		}
		else if(type == EnumPacketType.RemoteNpcsGet){
			NoppesUtilServer.sendNearbyNpcs(player);
			NoppesUtilServer.sendData(player, EnumPacketType.ScrollSelected, CustomNpcs.FreezeNPCs?"Unfreeze Npcs":"Freeze Npcs");
		}
		else if(type == EnumPacketType.RemoteFreeze){
			CustomNpcs.FreezeNPCs = !CustomNpcs.FreezeNPCs;
			NoppesUtilServer.sendData(player, EnumPacketType.ScrollSelected, CustomNpcs.FreezeNPCs?"Unfreeze Npcs":"Freeze Npcs");
		}
		else if(type == EnumPacketType.RemoteReset){
			Entity entity = player.worldObj.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			npc = (EntityNPCInterface) entity;
			npc.reset();
		}
		else if(type == EnumPacketType.RemoteTpToNpc){
			Entity entity = player.worldObj.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityNPCInterface))
				return;
			npc = (EntityNPCInterface) entity;
			player.playerNetServerHandler.setPlayerLocation(npc.posX, npc.posY, npc.posZ, 0, 0);
		}
		else if(type == EnumPacketType.SpawnMob){
			Entity entity = EntityList.createEntityFromNBT(CompressedStreamTools.read(dis), player.worldObj);
			player.worldObj.spawnEntityInWorld(entity);
		}
		else if(type == EnumPacketType.SpawnNpc){
			int x = dis.readInt();
			int y = dis.readInt();
			int z = dis.readInt();
			npc = (EntityNPCInterface)EntityList.createEntityFromNBT(CompressedStreamTools.read(dis), player.worldObj);

        	npc.startPos = new int[]{x,y,z};
    		npc.setPosition(x + 0.5F, npc.getStartYPos(), z + 0.5F);
    		npc.setHealth(npc.getMaxHealth());
    		
			player.worldObj.spawnEntityInWorld(npc);
			NoppesUtilServer.setEditingNpc(player, npc);
		}
		else if(type == EnumPacketType.MobSpawner){
			NoppesUtilServer.createMobSpawner(dis,player);
		}
		else if(type == EnumPacketType.Gui){
			EnumGuiType gui = EnumGuiType.values()[dis.readInt()];
			int i = dis.readInt();
			int j = dis.readInt();
			int k = dis.readInt();
			NoppesUtilServer.sendOpenGui(player, gui, NoppesUtilServer.getEditingNpc(player), i, j, k);
		}
		else if(type == EnumPacketType.RecipesGet){
			NoppesUtilServer.sendRecipeData(player,dis.readInt());
		}
		else if(type == EnumPacketType.RecipeGet){
			RecipeCarpentry recipe = RecipeController.instance.getRecipe(dis.readInt());
			NoppesUtilServer.setRecipeGui(player,recipe);
		}
		else if(type == EnumPacketType.RecipeRemove){
			RecipeCarpentry recipe = RecipeController.instance.removeRecipe(dis.readInt());
			NoppesUtilServer.sendRecipeData(player, recipe.isGlobal?3:4);
			NoppesUtilServer.setRecipeGui(player,new RecipeCarpentry());
		}
		else if(type == EnumPacketType.RecipeSave){
			RecipeCarpentry recipe = RecipeController.instance.saveRecipe(dis);
			NoppesUtilServer.sendRecipeData(player, recipe.isGlobal?3:4);
			NoppesUtilServer.setRecipeGui(player,recipe);
		}
		else if(type == EnumPacketType.DialogCategoriesGet){
			NoppesUtilServer.sendDialogCategoryData(player);
		}
		else if(type == EnumPacketType.DialogCategorySave){
			DialogCategory category = new DialogCategory();
			category.readNBT(CompressedStreamTools.read(dis));
			DialogController.instance.saveCategory(category);
			NoppesUtilServer.sendDialogCategoryData(player);
		}
		else if(type == EnumPacketType.DialogCategoryRemove){
			DialogController.instance.removeCategory(dis);
			NoppesUtilServer.sendDialogCategoryData(player);
		}
		else if(type == EnumPacketType.DialogCategoryGet){
			DialogCategory category = DialogController.instance.categories.get(dis.readInt());
			if(category != null)
				NoppesUtilServer.sendData(player, EnumPacketType.GuiData, category.writeNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.DialogsGet){
			NoppesUtilServer.sendDialogData(player,DialogController.instance.categories.get(dis.readInt()));
		}
		else if(type == EnumPacketType.DialogGet){
			Dialog dialog = DialogController.instance.dialogs.get(dis.readInt());
			if(dialog != null){
				NBTTagCompound compound = dialog.writeToNBT(new NBTTagCompound());
				Quest quest = QuestController.instance.quests.get(dialog.quest);
				if(quest != null)
					compound.setString("DialogQuestName", quest.title);
				NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
			}
		}
		else if(type == EnumPacketType.DialogsGetFromDialog){
			Dialog dialog = DialogController.instance.dialogs.get(dis.readInt());
			if(dialog == null)
				return;
			NoppesUtilServer.sendDialogData(player,dialog.category);
		}
		else if(type == EnumPacketType.DialogSave){
			int category = dis.readInt();
			Dialog dialog = new Dialog();
			dialog.readNBT(CompressedStreamTools.read(dis));
			DialogController.instance.saveDialog(category,dialog);
			if(dialog.category != null)
				NoppesUtilServer.sendDialogData(player,dialog.category);
		}
		else if(type == EnumPacketType.QuestOpenGui){
			Quest quest = new Quest();
			int gui = dis.readInt();
			quest.readNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.setEditingQuest(player,quest);
			player.openGui(CustomNpcs.instance, gui , player.worldObj, 0, 0, 0);
		}
		else if(type == EnumPacketType.DialogEdit){
			Dialog dialog = DialogController.instance.dialogs.get(dis.readInt());
			if(dialog != null)
				NoppesUtilServer.sendData(player,EnumPacketType.GuiData, dialog.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.DialogRemove){
			Dialog dialog = DialogController.instance.dialogs.get(dis.readInt());
			if(dialog != null && dialog.category != null){
				DialogController.instance.removeDialog(dialog);
				NoppesUtilServer.sendDialogData(player,dialog.category);
			}
		}
		else if(type == EnumPacketType.DialogNpcGet){
			NoppesUtilServer.sendNpcDialogs(player);
		}
		else if(type == EnumPacketType.DialogNpcSet){
			int slot = dis.readInt();
			int dialog = dis.readInt();
			DialogOption option = NoppesUtilServer.setNpcDialog(slot,dialog,player);
			if(option != null && option.hasDialog()){
				NBTTagCompound compound = option.writeNBT();
				compound.setInteger("Position", slot);
				NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
			}
		}
		else if(type == EnumPacketType.DialogNpcRemove){
			npc.dialogs.remove(dis.readInt());
		}
		else if(type == EnumPacketType.QuestGet){
			Quest quest = QuestController.instance.quests.get(dis.readInt());
			if(quest != null){
				NBTTagCompound compound = new NBTTagCompound();
				if(quest.hasNewQuest())
					compound.setString("NextQuestTitle", quest.getNextQuest().title);
				NoppesUtilServer.sendData(player, EnumPacketType.GuiData, quest.writeToNBT(compound));
			}
		}
		else if(type == EnumPacketType.QuestCategoryGet){
			QuestCategory category = QuestController.instance.categories.get(dis.readInt());
			if(category != null)
				NoppesUtilServer.sendData(player, EnumPacketType.GuiData, category.writeNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.QuestCategorySave){
			QuestCategory category = new QuestCategory();
			category.readNBT(CompressedStreamTools.read(dis));
			QuestController.instance.saveCategory(category);
			NoppesUtilServer.sendQuestCategoryData(player);
		}
		else if(type == EnumPacketType.QuestCategoriesGet){
			NoppesUtilServer.sendQuestCategoryData(player);
		}
		else if(type == EnumPacketType.QuestCategoryRemove){
			QuestController.instance.removeCategory(dis);
			NoppesUtilServer.sendQuestCategoryData(player);
		}
		else if(type == EnumPacketType.QuestsGet){
			QuestCategory category = QuestController.instance.categories.get(dis.readInt());
			NoppesUtilServer.sendQuestData(player,category);
		}
		else if(type == EnumPacketType.QuestsGetFromQuest){
			Quest quest = QuestController.instance.quests.get(dis.readInt());
			if(quest == null)
				return;
			NoppesUtilServer.sendQuestData(player,quest.category);
		}
		else if(type == EnumPacketType.QuestEdit){
			Quest quest = QuestController.instance.quests.get(dis.readInt());
			if(quest != null)
				NoppesUtilServer.sendData(player,EnumPacketType.GuiData, quest.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.QuestSave){
			Quest quest = QuestController.instance.saveQuest(dis);
			if(quest.category != null)
				NoppesUtilServer.sendQuestData(player,quest.category);
		}
		else if(type == EnumPacketType.QuestRemove){
			Quest quest = QuestController.instance.quests.get(dis.readInt());
			if(quest != null){
				QuestController.instance.removeQuest(quest);
				NoppesUtilServer.sendQuestData(player,quest.category);
			}
		}
		else if(type == EnumPacketType.TransportCategoriesGet){
			NoppesUtilServer.sendTransportCategoryData(player);
		}
		else if(type == EnumPacketType.TransportCategorySave){
			TransportController.getInstance().saveCategory(dis);
		}
		else if(type == EnumPacketType.TransportCategoryRemove){
			TransportController.getInstance().removeCategory(dis.readInt());
			NoppesUtilServer.sendTransportCategoryData(player);
		}
		else if(type == EnumPacketType.TransportRemove){
			int id = dis.readInt();
			TransportController.getInstance().removeLocation(id);
			NoppesUtilServer.sendTransportData(player,id);
		}
		else if(type == EnumPacketType.TransportsGet){
			NoppesUtilServer.sendTransportData(player,dis.readInt());
		}
		else if(type == EnumPacketType.TransportSave){
			int cat = dis.readInt();
			TransportLocation location = TransportController.getInstance().saveLocation(cat,dis,player,NoppesUtilServer.getEditingNpc(player));
			if(location != null){
				if(npc.advanced.role != EnumRoleType.Transporter)
					return;
				((RoleTransporter)npc.roleInterface).setTransport(location);
				NoppesUtilServer.sendTransportData(player, cat);
				NoppesUtilServer.sendData(player,EnumPacketType.GuiData,location.writeNBT());
			}
		}
		else if(type == EnumPacketType.SaveTileEntity){
			NoppesUtilServer.saveTileEntity(player,dis);
		}
		else if(type == EnumPacketType.TransportGetLocation){
			if(npc.advanced.role != EnumRoleType.Transporter)
				return;
			RoleTransporter role = (RoleTransporter) npc.roleInterface;
			if(role.hasTransport()){
				NoppesUtilServer.sendData(player,EnumPacketType.GuiData,role.getLocation().writeNBT());
				NoppesUtilServer.sendData(player, EnumPacketType.ScrollSelected,role.getLocation().category.title);
			}
		}
		else if(type == EnumPacketType.FactionsGet){
			NoppesUtilServer.sendFactionDataAll(player);
		}
		else if(type == EnumPacketType.FactionSet){
			npc.setFaction(dis.readInt());
		}
		else if(type == EnumPacketType.FactionSave){
			Faction faction = new Faction();
			faction.readNBT(CompressedStreamTools.read(dis));
			FactionController.getInstance().saveFaction(faction);
			NoppesUtilServer.sendFactionDataAll(player);
			NBTTagCompound compound = new NBTTagCompound();
			faction.writeNBT(compound);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
		}
		else if(type == EnumPacketType.FactionRemove){
			FactionController.getInstance().removeFaction(dis.readInt());			
			NoppesUtilServer.sendFactionDataAll(player);
			NBTTagCompound compound = new NBTTagCompound();
			(new Faction()).writeNBT(compound);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
		}
		else if(type == EnumPacketType.FactionGet){
			NBTTagCompound compound = new NBTTagCompound();
			Faction faction = FactionController.getInstance().getFaction(dis.readInt());
			faction.writeNBT(compound);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, compound);
		}
		else if(type == EnumPacketType.PlayerDataGet){
			int id = dis.readInt();
			if(EnumPlayerData.values().length <= id)
				return;
			String name = null;
			EnumPlayerData datatype = EnumPlayerData.values()[id];
			if(datatype != EnumPlayerData.Players)
				name = dis.readUTF();
			NoppesUtilServer.sendPlayerData(datatype,player,name);
		}
		else if(type == EnumPacketType.PlayerDataRemove){
			NoppesUtilServer.removePlayerData(dis,player);
		}
		else if(type == EnumPacketType.MainmenuDisplayGet){
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, npc.display.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.MainmenuDisplaySave){
			npc.display.readToNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MainmenuStatsGet){
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, npc.stats.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.MainmenuStatsSave){
			npc.stats.readToNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MainmenuInvGet){
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, npc.inventory.writeEntityToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.MainmenuInvSave){
			npc.inventory.readEntityFromNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MainmenuAIGet){
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, npc.ai.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.MainmenuAISave){
			npc.ai.readToNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MainmenuAISave){
			npc.inventory.readEntityFromNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MainmenuAdvancedGet){
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, npc.advanced.writeToNBT(new NBTTagCompound()));
		}
		else if(type == EnumPacketType.MainmenuAdvancedSave){
			npc.advanced.readToNBT(CompressedStreamTools.read(dis));
			NoppesUtilServer.sendDataToAll(npc,EnumPacketType.UpdateNpc, npc.copy());
		}
		else if(type == EnumPacketType.MerchantUpdate){
			Entity entity = player.worldObj.getEntityByID(dis.readInt());
			if(entity == null || !(entity instanceof EntityVillager))
				return;
			MerchantRecipeList list = MerchantRecipeList.readRecipiesFromStream(dis);
			((EntityVillager)entity).setRecipes(list);
		}
		else if(type == EnumPacketType.TraderStockSave){
			NBTTagCompound compound = CompressedStreamTools.read(dis);
			((RoleTrader)npc.roleInterface).stock.readFromNBT(compound);
			npc.reset();
			NoppesUtilServer.sendDataToAll(npc, EnumPacketType.UpdateNpc, npc.copy());
		}
		
	}
}

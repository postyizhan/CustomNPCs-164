package noppes.npcs;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.BankData;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.PlayerFactionData;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.PlayerMailData;
import noppes.npcs.controllers.PlayerQuestController;
import noppes.npcs.controllers.PlayerQuestData;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandlerPlayer implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(!packet.channel.equals("CNPCs Player"))
			return;
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(packet.data))));
			player(dis,(EntityPlayerMP) player,EnumPlayerPacket.values()[dis.readInt()]);
			dis.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void player(DataInputStream dis, EntityPlayerMP player, EnumPlayerPacket type) throws IOException {
		if(type == EnumPlayerPacket.FollowerHire){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.hireFollower(player,npc);
		}
		else if(type == EnumPlayerPacket.FollowerExtend){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.extendFollower(player,npc);
		}
		else if(type == EnumPlayerPacket.FollowerState){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.changeFollowerState(player,npc);
		}
		else if(type == EnumPlayerPacket.Transport){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.transport(player, npc, dis.readUTF());
		}
		else if(type == EnumPlayerPacket.BankUpgrade){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.bankUpgrade(player, npc);
		}
		else if(type == EnumPlayerPacket.BankUnlock){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.bankUnlock(player, npc);
		}
		else if(type == EnumPlayerPacket.BankSlotOpen){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			int slot = dis.readInt();
			int bankId = dis.readInt();
			BankData data = PlayerDataController.instance.getBankData(player,bankId).getBankOrDefault(bankId);
			data.openBankGui(player, npc, bankId, slot);
		}
		else if(type == EnumPlayerPacket.Dialog){
			EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(player);
			if(npc == null)
				return;
			NoppesUtilPlayer.dialogSelected(dis.readInt(),dis.readInt(),player,npc);
		}
		else if(type == EnumPlayerPacket.CheckQuestCompletion){
			PlayerQuestData playerdata = PlayerDataController.instance.getQuestData(player);
			playerdata.checkQuestCompletion(player,null);
		}
		else if(type == EnumPlayerPacket.QuestLog){
			NoppesUtilPlayer.sendQuestLogData(player);
		}
		else if(type == EnumPlayerPacket.QuestCompletion){
			NoppesUtilPlayer.questCompletion(player,dis.readInt());
		}
		else if(type == EnumPlayerPacket.FactionsGet){
			PlayerFactionData data = PlayerDataController.instance.getFactionData(player);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, data.getPlayerGuiData());
		}
		else if(type == EnumPlayerPacket.MailGet){
			PlayerMailData data = PlayerDataController.instance.getMailData(player);
			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, data.writeNBT(new NBTTagCompound()));
		}
		else if(type == EnumPlayerPacket.MailDelete){
			long time = dis.readLong();
			String username = dis.readUTF();
			PlayerMailData data = PlayerDataController.instance.getMailData(player);
			
			Iterator<PlayerMail> it = data.playermail.iterator();
			while(it.hasNext()){
				PlayerMail mail = it.next();
				if(mail.time == time && mail.sender.equals(username)){
					it.remove();
				}
			}
			PlayerDataController.instance.savePlayerData(player, data);

			NoppesUtilServer.sendData(player, EnumPacketType.GuiData, data.writeNBT(new NBTTagCompound()));
		}
		else if(type == EnumPlayerPacket.MailSend){
			String username = PlayerDataController.instance.hasPlayer(dis.readUTF());
			if(username.isEmpty()){
				NoppesUtilServer.sendGuiError(player, 0);
				return;
			}
			
			PlayerMail mail = new PlayerMail();
			mail.readNBT(CompressedStreamTools.read(dis));

			if(mail.subject.isEmpty()){
				NoppesUtilServer.sendGuiError(player, 1);
				return;
			}
			PlayerDataController.instance.addPlayerMessage(username, mail);
			
			NBTTagCompound comp = new NBTTagCompound();
			comp.setString("username", username);
			NoppesUtilServer.sendGuiClose(player, 1,comp);
		}
		else if(type == EnumPlayerPacket.MailRead){
			long time = dis.readLong();
			String username = dis.readUTF();
			PlayerMailData data = PlayerDataController.instance.getMailData(player);
			
			Iterator<PlayerMail> it = data.playermail.iterator();
			while(it.hasNext()){
				PlayerMail mail = it.next();
				if(mail.time == time && mail.sender.equals(username)){
					mail.beenRead = true;
					if(mail.hasQuest())
						PlayerQuestController.addActiveQuest(mail.getQuest(), player);
				}
			}
			PlayerDataController.instance.savePlayerData(player, data);
		}

		else if(type == EnumPlayerPacket.SignSave){
			int x = dis.readInt(), y = dis.readInt(), z = dis.readInt();
			TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
			if(tile == null || !(tile instanceof TileBigSign))
				return;
			TileBigSign sign = (TileBigSign) tile;
			if(sign.canEdit){
				sign.setText(dis.readUTF());
				sign.canEdit = false;
				player.worldObj.markBlockForUpdate(x, y, z);
			}
		}
		else if(type == EnumPlayerPacket.SaveBook){
			int x = dis.readInt(), y = dis.readInt(), z = dis.readInt();
			TileEntity tileentity = player.worldObj.getBlockTileEntity(x, y, z);
			if(!(tileentity instanceof TileBook))
				return;
			TileBook tile = (TileBook) tileentity;
			if(tile.book.getItem() == Item.writtenBook)
				return;
			boolean sign = dis.readBoolean();
			ItemStack book = ItemStack.loadItemStackFromNBT(CompressedStreamTools.read(dis));
			if(book == null)
				return;
			if(book.getItem() == Item.writableBook && !sign && ItemWritableBook.validBookTagPages(book.getTagCompound())){
				tile.book.setTagInfo("pages", book.getTagCompound().getTagList("pages"));
			}
			if(book.getItem() == Item.writtenBook && sign && ItemEditableBook.validBookTagContents(book.getTagCompound())){
				tile.book.setTagInfo("author", new NBTTagString("author", player.getCommandSenderName()));
				tile.book.setTagInfo("title", new NBTTagString("title", book.getTagCompound().getString("title")));
                tile.book.setTagInfo("pages", book.getTagCompound().getTagList("pages"));
                tile.book.itemID = Item.writtenBook.itemID;
			}
		}
	}
}

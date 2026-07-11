package noppes.npcs.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.CustomNpcs;

import org.apache.commons.lang3.RandomStringUtils;

public class PlayerDataController {	
	private static final String id = RandomStringUtils.random(12, true, true);
	
	public static PlayerDataController instance;
	
	public PlayerDataController(){
		instance = this;
	}
	public File getSaveDir(){
		try{
			File file = new File(CustomNpcs.getWorldSaveDirectory(),"playerdata");
			if(!file.exists())
				file.mkdir();
			return file;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public NBTTagCompound loadPlayerData(String player){
		File saveDir = getSaveDir();
		String filename = player;
		if(filename.isEmpty())
			filename = "noplayername";
		filename += ".dat";
		try {
	        File file = new File(saveDir, filename);
	        if(!file.exists()){
				return new NBTTagCompound();
	        }
	        return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		try {
	        File file = new File(saveDir, filename+"_old");
	        return CompressedStreamTools.readCompressed(new FileInputStream(file));
	        
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return new NBTTagCompound();
	}
	
	public void savePlayerData(EntityPlayer player, IPlayerData data){
		savePlayerData(player.username, data.writeNBT(getData(player)));
	}
	
	public void savePlayerData(String username, NBTTagCompound compound){
		String filename = username;
		if(filename.isEmpty())
			filename = "noplayername";
		else{
			EntityPlayer player = MinecraftServer.getServer().worldServers[0].getPlayerEntityByName(username);
			if(player != null){
				player.getEntityData().setString("CustomNpcsId", id);
				player.getEntityData().setCompoundTag("CustomNpcsData", compound);
			}
		}
		filename += ".dat";
		try {
			File saveDir = getSaveDir();
            File file = new File(saveDir, filename+"_new");
            File file1 = new File(saveDir, filename+"_old");
            File file2 = new File(saveDir, filename);
            CompressedStreamTools.writeCompressed(compound, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public PlayerBankData getBankData(EntityPlayer player) {
		PlayerBankData data = new PlayerBankData();
		data.readNBT(getData(player));
		return data;
	}
	
	public PlayerBankData getBankData(EntityPlayer player, int bankId) {
		Bank bank = BankController.getInstance().getBank(bankId);
		PlayerBankData data = getBankData(player);
		if(!data.hasBank(bank.id)){
			data.loadNew(bank.id);
			savePlayerData(player, data);
		}
		return data;
	}
	
	public PlayerDialogData getDialogData(EntityPlayer player){
		PlayerDialogData data = new PlayerDialogData();
		data.readNBT(getData(player));
		return data;
	}

	public PlayerQuestData getQuestData(EntityPlayer player) {
		PlayerQuestData data = new PlayerQuestData();
		data.readNBT(getData(player));
		return data;
	}
	
	public PlayerTransportData getTransportData(EntityPlayer player) {
		PlayerTransportData data = new PlayerTransportData();
		data.readNBT(getData(player));
		return data;
	}
	public PlayerItemGiverData getItemGiverData(EntityPlayer player) {
		PlayerItemGiverData data = new PlayerItemGiverData();
		data.readNBT(getData(player));
		return data;
	}
	
	public void setDialogRead(int dialog, EntityPlayer player){
		PlayerDialogData data = getDialogData(player);
		if(!data.dialogsRead.contains(dialog)){
			data.dialogsRead.add(dialog);
			savePlayerData(player, data);
		}
	}
	public boolean hasDialogBeenRead(int dialog, EntityPlayer player){
		PlayerDialogData data = getDialogData(player);
		return data.dialogsRead.contains(dialog);
	}
	public void addTransport(int transportId, EntityPlayer player) {
		PlayerTransportData data = getTransportData(player);
		data.transports.add(transportId);
		savePlayerData(player, data);		
	}
	public PlayerFactionData getFactionData(EntityPlayer player) {
		PlayerFactionData data = new PlayerFactionData();
		data.readNBT(getData(player));
		return data;	
	}

	private NBTTagCompound getData(EntityPlayer player) {

		NBTTagCompound compound = player.getEntityData();
		if(compound.getString("CustomNpcsId").equals(id) && compound.getString("CustomNpcsUsername").equals(player.username))
			return compound.getCompoundTag("CustomNpcsData");

		compound.setString("CustomNpcsId", id);
		compound.setString("CustomNpcsUsername", player.username);
		compound.setCompoundTag("CustomNpcsData", this.loadPlayerData(player.username));

		return compound;
		
	}
	public String hasPlayer(String username) {
        for(String file : getSaveDir().list()){
        	if(file.equalsIgnoreCase(username + ".dat"))
        		return file.substring(0, file.length() - 4);
        }
		
		return "";
	}
	public void addPlayerMessage(String username, PlayerMail mail) {
		mail.time = System.currentTimeMillis();
		PlayerData data = new PlayerData(loadPlayerData(username));
		data.mailData.playermail.add(mail);
		savePlayerData(username, data.getNBT());
	}
	public boolean hasMail(EntityPlayer player) {
		return getMailData(player).hasMail();
	}
	public PlayerMailData getMailData(EntityPlayer player) {
		PlayerMailData data = new PlayerMailData();
		data.readNBT(getData(player));
		return data;
	}
}

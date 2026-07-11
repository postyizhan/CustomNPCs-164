package noppes.npcs.controllers;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
	public PlayerDialogData dialogData = new PlayerDialogData();
	public PlayerBankData bankData = new PlayerBankData();
	public PlayerQuestData questData = new PlayerQuestData();
	public PlayerTransportData transportData = new PlayerTransportData();
	public PlayerFactionData factionData = new PlayerFactionData();
	public PlayerItemGiverData itemgiverData = new PlayerItemGiverData();
	public PlayerMailData mailData = new PlayerMailData();
	
	public PlayerData(NBTTagCompound compound){
		dialogData.readNBT(compound);
		bankData.readNBT(compound);
		questData.readNBT(compound);
		transportData.readNBT(compound);
		factionData.readNBT(compound);
		itemgiverData.readNBT(compound);
		mailData.readNBT(compound);
	}

	public NBTTagCompound getNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		dialogData.writeNBT(compound);
		bankData.writeNBT(compound);
		questData.writeNBT(compound);
		transportData.writeNBT(compound);
		factionData.writeNBT(compound);
		itemgiverData.writeNBT(compound);
		mailData.writeNBT(compound);
		return compound;
	}
}

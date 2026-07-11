package noppes.npcs.controllers;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerMailData implements IPlayerData{
	public ArrayList<PlayerMail> playermail = new ArrayList<PlayerMail>();
	
	public void readNBT(NBTTagCompound compound) {
		ArrayList<PlayerMail> newmail = new ArrayList<PlayerMail>();
		NBTTagList list = compound.getTagList("MailData");
		
		for(int i = 0; i < list.tagCount(); i++){
			PlayerMail mail = new PlayerMail();
			mail.readNBT((NBTTagCompound) list.tagAt(i));
			newmail.add(mail);
		}
		playermail = newmail;
	}

	public NBTTagCompound writeNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		
		for(PlayerMail mail : playermail){
			list.appendTag(mail.writeNBT());
		}
		
		compound.setTag("MailData", list);
		return compound;
	}

	public boolean hasMail() {
		for(PlayerMail mail : playermail)
			if(!mail.beenRead)
				return true;
		return false;
	}	
}

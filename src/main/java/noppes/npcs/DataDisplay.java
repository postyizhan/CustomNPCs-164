package noppes.npcs;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.constants.EnumModelType;

public class DataDisplay {
	EntityNPCInterface npc;
	
	public String name;
	public String title = "";

	public byte skinType = 0; //0:normal, 1:player, 2:url
	public String url = "";
	public String skinUsername = "";
	public String texture = "customnpcs:textures/entity/humanmale/Steve.png";;
	public String cloakTexture = "";
	public String glowTexture = "";
	
	public int visible = 0;

	public EnumModelType modelType = EnumModelType.HumanMale;
	
	public int modelSize = 5;

	public int showName = 0;
	public int skinColor = 0xFFFFFF;
	
	public boolean NoLivingAnimation = false;

	
	public DataDisplay(EntityNPCInterface npc){
		this.npc = npc;
		String[] names = { "Noppes", "Noppes", "Noppes", "Noppes", "Atesson",
				"Rothcersul", "Achdranys", "Pegato", "Chald", "Gareld",
				"Nalworche", "Ineald", "Tia'kim", "Torerod", "Turturdar",
				"Ranler", "Dyntan", "Oldrake", "Gharis", "Elmn", "Tanal",
				"Waran-ess", "Ach-aldhat", "Athi", "Itageray", "Tasr",
				"Ightech", "Gakih", "Adkal", "Qua'an", "Sieq", "Urnp", "Rods",
				"Vorbani", "Smaik", "Fian", "Hir", "Ristai", "Kineth", "Naif",
				"Issraya", "Arisotura", "Honf", "Rilfom", "Estz", "Ghatroth",
				"Yosil", "Darage", "Aldny", "Tyltran", "Armos", "Loxiku",
				"Burhat", "Tinlt", "Ightyd", "Mia",
				"Ken", "Karla", "Lily", "Carina", "Daniel", "Slater", "Zidane", "Valentine", "Eirina", 
				"Carnow", "Grave", "Shadow", "Drakken", "Kaoz", "Silk", "Drake", "Oldam", "Lynxx", "Lenyx", 
				"Winter", "Seth", "Apolitho", "Amethyst", "Ankin", "Seinkan", "Ayumu", "Sakamoto", "Divina", 
				"Div", "Magia", "Magnus", "Tiakono", "Ruin", "Hailinx", "Ethan", "Wate", "Carter", "William", 
				"Brion", "Sparrow", "Basrrelen", "Gyaku", "Claire", "Crowfeather", "Blackwell", "Raven", "Farcri",
				"Lucas", "Bangheart", "Kamoku", "Kyoukan", "Blaze", "Benjamin", "Larianne", "Kakaragon", 
				"Melancholy", "Epodyno", "Thanato", "Mika", "Dacks", "Ylander", "Neve", "Meadow", "Cuero", 
				"Embrera", "Eldamore", "Faolan", "Chim", "Nasu", "Kathrine", "Ariel", "Arei", "Demytrix", 
				"Kora", "Ava", "Larson", "Leonardo", "Wyrl", "Sakiama", "Lambton", "Kederath", "Malus", "Riplette", 
				"Andern", "Ezall", "Lucien", "Droco", "Cray", "Tymen", "Zenix", "Entranger", 
				"Saenorath", "Chris", "Christine", "Marble", "Mable", "Ross", "Rose", "Xalgan ", "Kennet", "NationsGlory.fr"
		};
		name = names[new Random().nextInt(names.length)];
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("Name", name);
		nbttagcompound.setString("Title", title);
		nbttagcompound.setString("SkinUsername", skinUsername);
		nbttagcompound.setString("SkinUrl", url);
		nbttagcompound.setString("Texture", texture);
		nbttagcompound.setString("CloakTexture", cloakTexture);
		nbttagcompound.setString("GlowTexture", glowTexture);
		nbttagcompound.setByte("UsingSkinUrl", skinType);
		
		nbttagcompound.setInteger("ModelType", modelType.ordinal());
		nbttagcompound.setInteger("Size", modelSize);

		nbttagcompound.setInteger("ShowName", showName);
		nbttagcompound.setInteger("SkinColor", skinColor);
		nbttagcompound.setInteger("NpcVisible", visible);

		nbttagcompound.setBoolean("NoLivingAnimation", NoLivingAnimation);

		return nbttagcompound;
	}
	public void readToNBT(NBTTagCompound nbttagcompound) {
		name = nbttagcompound.getString("Name");
		title = nbttagcompound.getString("Title");
		skinUsername = nbttagcompound.getString("SkinUsername");
		texture = nbttagcompound.getString("Texture");
		url = nbttagcompound.getString("SkinUrl");
		cloakTexture = nbttagcompound.getString("CloakTexture");
		glowTexture = nbttagcompound.getString("GlowTexture");
		skinType = nbttagcompound.getByte("UsingSkinUrl");
		
		modelType = EnumModelType.values()[nbttagcompound
				.getInteger("ModelType") % EnumModelType.values().length];
		
		modelSize = nbttagcompound.getInteger("Size");

		showName = nbttagcompound.getInteger("ShowName");
		skinColor = nbttagcompound.getInteger("SkinColor");
		visible = nbttagcompound.getInteger("NpcVisible");
		
		NoLivingAnimation = nbttagcompound.getBoolean("NoLivingAnimation");
	}
	public boolean showName() {
		if(npc.isKilled())
			return false;
		return showName == 0 || (showName == 2 && npc.isAttacking());
	}
}

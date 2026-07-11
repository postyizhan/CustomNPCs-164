package noppes.npcs;

import net.minecraft.entity.player.EntityPlayer;

public class NoppesStringUtils {
	
	public static String formatText(String text, Object... obs) {
		for(Object ob : obs){
			if(ob instanceof EntityPlayer){
				String username = ((EntityPlayer)ob).getDisplayName();
				text = text.replace("{player}", username);
				text = text.replace("@p", username);
			}
			else if(ob instanceof EntityNPCInterface)
				text = text.replace("@npc", ((EntityNPCInterface)ob).getCommandSenderName());
				
		}
		text = text.replace("&", Character.toChars(167)[0] + "");
		return text;
	}
}

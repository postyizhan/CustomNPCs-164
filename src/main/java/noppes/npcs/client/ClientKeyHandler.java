package noppes.npcs.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import noppes.npcs.client.gui.player.GuiQuestLog;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class ClientKeyHandler extends KeyHandler{

	public ClientKeyHandler(KeyBinding[] keyBindings, boolean[] repeatings) {
		super(keyBindings, repeatings);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if(kb.keyDescription.equals("Quest Log") && tickEnd){
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.currentScreen == null)
				NoppesUtil.openGUI(mc.thePlayer, new GuiQuestLog(mc.thePlayer));
			else if(mc.currentScreen instanceof GuiQuestLog)
				mc.setIngameFocus();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
	}

}

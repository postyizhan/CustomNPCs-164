package noppes.npcs.client;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.player.GuiFaction;
import noppes.npcs.client.gui.player.GuiQuestLog;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.IButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ClientTickHandler implements ITickHandler{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub
		
	}
	private World prevWorld;
	private boolean otherContainer = false;
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.CLIENT)){
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.thePlayer != null && mc.thePlayer.openContainer instanceof ContainerPlayer){
				if(otherContainer){
			    	NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion);
					otherContainer = false;
				}
			}
			else
				otherContainer = true;
			
			GuiScreen guiscreen = mc.currentScreen;
			if(CustomNpcs.InventoryGuiEnabled && guiscreen instanceof GuiInventory && !guiHasButtons(guiscreen)){
				GuiInventory guiinv = (GuiInventory) guiscreen;
				
				IButtonListener listener = new IButtonListener(){

					@Override
					public void actionPerformed(GuiButton button) {
						Minecraft mc = Minecraft.getMinecraft();
						if(button.id == 2)
							mc.displayGuiScreen(new GuiQuestLog(mc.thePlayer));
						if(button.id == 3)
							mc.displayGuiScreen(new GuiFaction());
					}
					
				};
		        GuiMenuTopButton inv = new GuiMenuTopButton(1, guiinv.guiLeft + 3, guiinv.guiTop - 17, "menu.inventory");
		        GuiMenuTopButton quests = new GuiMenuTopButton(2, inv, "quest.quests", listener);
		        GuiMenuTopButton factions = new GuiMenuTopButton(3, quests, "menu.factions", listener);
		        inv.active = true;

		        guiinv.buttonList.add(inv);
		        guiinv.buttonList.add(quests);
		        guiinv.buttonList.add(factions);
			}
			CustomNpcs.ticks++;
			if(prevWorld != mc.theWorld){
				MusicController.Instance.playing = "";
				prevWorld = mc.theWorld;
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT, TickType.WORLD);
	}

	public boolean guiHasButtons(GuiScreen guiscreen){
		for(Object ob : guiscreen.buttonList){
			if(ob instanceof GuiMenuTopButton)
				return true;
		}
		return false;
	}
	
	@Override
	public String getLabel() {
		return "CNPCs ClientTickHandler";
	}

}

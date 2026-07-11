package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.StatCollector;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;

import org.lwjgl.input.Keyboard;

public class GuiNpcMenu {
	
	private GuiScreen parent;
	private GuiMenuTopButton[] topButtons;

	private int activeMenu;
	private EntityNPCInterface npc;

	public GuiNpcMenu(GuiScreen parent, int activeMenu, EntityNPCInterface npc) {
		this.parent = parent;
		this.activeMenu = activeMenu;
		this.npc = npc;
	}

    public void initGui(int guiLeft, int guiTop, int width)
    {
        Keyboard.enableRepeatEvents(true);

        GuiMenuTopButton display = new GuiMenuTopButton(1,guiLeft + 4, guiTop - 17, "menu.display");
        GuiMenuTopButton stats = new GuiMenuTopButton(2,display.xPosition + display.getWidth() , guiTop - 17, "menu.stats");
        GuiMenuTopButton ai = new GuiMenuTopButton(6,stats.xPosition + stats.getWidth() , guiTop - 17, "menu.ai");
        GuiMenuTopButton inv = new GuiMenuTopButton(3,ai.xPosition + ai.getWidth() , guiTop - 17,  "menu.inventory");
        GuiMenuTopButton advanced = new GuiMenuTopButton(4,inv.xPosition + inv.getWidth() , guiTop - 17, "menu.advanced");
        GuiMenuTopButton global = new GuiMenuTopButton(5,advanced.xPosition + advanced.getWidth() , guiTop - 17, "menu.global");

        GuiMenuTopButton close = new GuiMenuTopButton(0,guiLeft + width - 22, guiTop - 17, "X");
        GuiMenuTopButton delete = new GuiMenuTopButton(66,guiLeft + width - 72, guiTop - 17,"selectWorld.deleteButton");
        delete.xPosition = close.xPosition - delete.getWidth();
        
        topButtons = new GuiMenuTopButton[]{display,stats,ai,inv,advanced,global,close,delete};
        
        for(GuiMenuTopButton button : topButtons)
        	button.active = button.id == activeMenu;
    }
    
    private void topButtonPressed(GuiMenuTopButton button) {
    	if(button.displayString.equals(activeMenu))
    		return;
    	Minecraft mc = Minecraft.getMinecraft();
    	mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
		if(button.id == 0){
			close();
			return;
		}
		else if(button.id == 66){
            GuiYesNo guiyesno = new GuiYesNo(parent, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
            mc.displayGuiScreen(guiyesno);
            return;
		}
		save();
		if(button.id == 1){
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuDisplay, 0, 0, 0);
		}
		else if(button.id == 2){
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuStats, 0, 0, 0);
		}
		else if(button.id == 3){
			NoppesUtil.requestOpenGUI(EnumGuiType.MainMenuInv, 0, 0, 0);
		}
		else if(button.id == 4){
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuAdvanced, 0, 0, 0);
		}
		else if(button.id == 5){
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuGlobal, 0, 0, 0);
		}
		else if(button.id == 6){
			CustomNpcs.proxy.openGui(npc, EnumGuiType.MainMenuAI, 0, 0, 0);
		}
		activeMenu = button.id;
	}

	private void save() {
    	GuiNpcTextField.unfocus();
    	
		if(parent instanceof GuiContainerNPCInterface2)
			((GuiContainerNPCInterface2)parent).save();
		
		if(parent instanceof GuiNPCInterface2)
			((GuiNPCInterface2)parent).save();
	} 

	private void close() {
		if(parent instanceof GuiContainerNPCInterface2)
			((GuiContainerNPCInterface2)parent).close();
		
		if(parent instanceof GuiNPCInterface2)
			((GuiNPCInterface2)parent).close();
		if(npc != null){
			npc.reset();
			NoppesUtil.sendData(EnumPacketType.RemoteReset,npc.entityId);
		}
	} 

	public void mouseClicked(int i, int j, int k) {
        if (k == 0)
        {
        	Minecraft mc = Minecraft.getMinecraft();
            for (GuiMenuTopButton button : topButtons)
            {
                if (button.mousePressed(mc, i, j))
                {
                    topButtonPressed(button);
                }
            }
        }
	}


	public void drawElements(FontRenderer fontRenderer, int i, int j,
			Minecraft mc, float f) {
        for(GuiMenuTopButton button: topButtons)
        	button.drawButton(mc, i, j);
        
	}
}

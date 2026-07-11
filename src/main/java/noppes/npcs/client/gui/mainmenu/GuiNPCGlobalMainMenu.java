// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNpcManagePlayerData;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumGuiType;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCGlobalMainMenu extends GuiNPCInterface2
{
    public GuiNPCGlobalMainMenu(EntityNPCInterface npc)
    {
    	super(npc,5);
    }

    public void initGui()
    {
    	super.initGui();
    	this.addButton(new GuiNpcButton(2, guiLeft + 85, guiTop + 20, "global.banks"));
    	this.addButton(new GuiNpcButton(3, guiLeft + 85, guiTop + 42, "menu.factions"));
    	this.addButton(new GuiNpcButton(4, guiLeft + 85, guiTop + 64, "dialog.dialogs"));
    	this.addButton(new GuiNpcButton(11, guiLeft + 85, guiTop + 86, "quest.quests"));
    	this.addButton(new GuiNpcButton(12, guiLeft + 85, guiTop + 108, "global.transport"));
    	this.addButton(new GuiNpcButton(13, guiLeft + 85, guiTop + 130, "global.playerdata"));
    	this.addButton(new GuiNpcButton(14, guiLeft + 85, guiTop + 152, "global.recipes"));
    	
    }
	protected void actionPerformed(GuiButton guibutton) {
        if(guibutton.id == 11)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageQuests);
        }
        if(guibutton.id == 2)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageBanks);
        }
        if(guibutton.id == 3)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageFactions);
        }
        if(guibutton.id == 4)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageDialogs);
        }
        if(guibutton.id == 12)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageTransport);
        }
        if(guibutton.id == 13)
        {
        	NoppesUtil.openGUI(player, new GuiNpcManagePlayerData(npc, this));
        }
        if(guibutton.id == 14)
        {
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes,4,0,0);
        }
	}
	@Override
	public void save() {
	}
    

}

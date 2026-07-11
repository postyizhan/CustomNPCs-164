// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCLinesMenu extends GuiNPCInterface2
{
    public GuiNPCLinesMenu(EntityNPCInterface npc)
    {
    	super(npc);
    }

    public void initGui()
    {
        super.initGui();
    	this.addButton(new GuiNpcButton(0, guiLeft + 85, guiTop + 20, "World Lines"));
    	this.addButton(new GuiNpcButton(1, guiLeft + 85, guiTop + 43, "Attack Lines"));
    	this.addButton(new GuiNpcButton(2, guiLeft + 85, guiTop + 66, "Interact Lines"));
    	this.addButton(new GuiNpcButton(5, guiLeft + 85, guiTop + 89, "Killed Lines"));
        
    }

	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 0)
        {
        	NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.worldLines));
        }
        if(guibutton.id == 1)
        {
        	NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.attackLines));
        }
        if(guibutton.id == 2)
        {
        	NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.interactLines));
        }
        if(guibutton.id == 5)
        {
        	NoppesUtil.openGUI(player, new GuiNPCLinesEdit(npc, npc.advanced.killedLines));
        }
    }
	public void save() {
	}


}

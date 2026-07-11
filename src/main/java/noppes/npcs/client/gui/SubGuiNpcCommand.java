// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcCommand extends SubGuiInterface implements ITextfieldListener
{
	public String command;
	
    public SubGuiNpcCommand(String command)
    {
    	this.command = command;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
    	this.addTextField(new GuiNpcTextField(4, this, this.fontRenderer, guiLeft + 4,  guiTop + 84, 248, 20, command));
    	this.getTextField(4).setMaxStringLength(32767);

    	this.addLabel(new GuiNpcLabel(4, "advMode.command", guiLeft + 4, guiTop + 110, 0x404040));
    	this.addLabel(new GuiNpcLabel(5, "advMode.nearestPlayer", guiLeft + 4, guiTop + 125, 0x404040));
    	this.addLabel(new GuiNpcLabel(6, "advMode.randomPlayer", guiLeft + 4, guiTop + 140, 0x404040));
    	this.addLabel(new GuiNpcLabel(7, "advMode.allPlayers", guiLeft + 4, guiTop + 155, 0x404040));
    	this.addLabel(new GuiNpcLabel(8, "dialogcommandoptionplayer", guiLeft + 4, guiTop + 170, 0x404040));

    	this.addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 190,98, 20, "gui.done"));
    	
    }

	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 66){
        	close();
        }
    }

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 4){
			command = textfield.getText();
		}
	}

}

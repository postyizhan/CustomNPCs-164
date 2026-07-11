// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumOptionType;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcDialogOptions extends SubGuiInterface
{
	private Dialog dialog;
	
    public SubGuiNpcDialogOptions(Dialog dialog)
    {
    	this.dialog = dialog;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
        this.addLabel(new GuiNpcLabel(66, "dialog.options", guiLeft, guiTop + 4, 0x404040));
        this.getLabel(66).center(xSize);
        
		for (int i = 0; i < 6; i++) {
			String optionString = "";
			DialogOption option = dialog.options.get(i);
			if(option != null && option.optionType != EnumOptionType.Disabled)
				optionString += option.title;

			this.addLabel(new GuiNpcLabel(i + 10, i + 1 + ": ", guiLeft + 4, guiTop + 16 + i * 32, 0x404040));
			this.addLabel(new GuiNpcLabel(i, optionString, guiLeft + 14, guiTop + 12 + i * 32, 0x404040));
	    	this.addButton(new GuiNpcButton(i, guiLeft + 13,  guiTop + 21 + i * 32, 60, 20, "selectServer.edit"));
			
		}

    	this.addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 194,98, 20, "gui.done"));
    	
    }

	protected void actionPerformed(GuiButton guibutton)
    {
		if(guibutton.id < 6){
        	if(!dialog.options.containsKey(guibutton.id ))
        		dialog.options.put(guibutton.id, new DialogOption());
        	changeSubGui(new SubGuiNpcDialogOption(dialog.options.get(guibutton.id)));
        }
        if(guibutton.id == 66)
        {
        	close();
        }
    }
}

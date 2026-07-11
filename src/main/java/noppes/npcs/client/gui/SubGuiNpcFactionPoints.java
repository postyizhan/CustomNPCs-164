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
import noppes.npcs.controllers.Faction;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcFactionPoints extends SubGuiInterface implements ITextfieldListener
{
	private Faction faction;
    public SubGuiNpcFactionPoints(Faction faction)
    {
    	this.faction = faction;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
    	this.addTextField(new GuiNpcTextField(2, this, fontRenderer, guiLeft + 48, guiTop + 48, 70, 20, faction.defaultPoints + ""));
    	addLabel(new GuiNpcLabel(2,"faction.default", guiLeft + 8, guiTop + 48 + 5, 0x404040));
    	getTextField(2).setMaxStringLength(6);
    	getTextField(2).numbersOnly = true;

    	addLabel(new GuiNpcLabel(3,"faction.unfriendly", guiLeft + 48, guiTop + 72, 0xFF0000));
    	addTextField(new GuiNpcTextField(3, this, fontRenderer, guiLeft + 48, guiTop + 82, 70, 20, faction.neutralPoints + ""));
    	addLabel(new GuiNpcLabel(4,"faction.neutral", guiLeft + 48, guiTop + 104, 0xF2FF00));
    	addTextField(new GuiNpcTextField(4, this, fontRenderer, guiLeft + 48, guiTop + 114, 70, 20, faction.friendlyPoints + ""));
    	addLabel(new GuiNpcLabel(5,"faction.friendly", guiLeft + 48, guiTop + 136, 0x00FF00));

    	getTextField(3).numbersOnly = true;
    	getTextField(4).numbersOnly = true;
    	
    	getLabel(3).center(70);
    	getLabel(4).center(70);
    	getLabel(5).center(70);

    	addButton(new GuiNpcButton(66, guiLeft + 20, guiTop + 192, 90, 20, "gui.done"));
    }

	public void unFocused(GuiNpcTextField textfield) {
		 if(textfield.id == 2) {
			faction.defaultPoints = textfield.getInteger();
		}else if(textfield.id == 3) {
			faction.neutralPoints = textfield.getInteger();
		}else if(textfield.id == 4) {
			faction.friendlyPoints = textfield.getInteger();
		}
	}
    
	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 66)
        {
        	close();
        }
    }

}

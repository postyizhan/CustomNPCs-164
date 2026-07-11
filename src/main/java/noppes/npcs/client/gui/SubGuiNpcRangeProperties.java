// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.DataStats;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcRangeProperties extends SubGuiInterface implements ITextfieldListener
{
	private DataStats stats;
	private GuiNpcSoundSelection gui;
	
    public SubGuiNpcRangeProperties(DataStats stats)
    {
    	this.stats = stats;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
        addLabel(new GuiNpcLabel(1,"stats.accuracy", guiLeft + 5, guiTop + 11, 0x404040));
        addTextField(new GuiNpcTextField(1,this, fontRenderer, guiLeft + 85, guiTop + 6, 50, 18, stats.accuracy+""));
        getTextField(1).numbersOnly = true;
        getTextField(1).setMinMaxDefault(0, 100, 90);
        addLabel(new GuiNpcLabel(2,"stats.rangedrange", guiLeft + 5, guiTop + 35, 0x404040));
        addTextField(new GuiNpcTextField(2,this, fontRenderer, guiLeft + 85, guiTop + 30, 50, 18, stats.rangedRange+""));
        getTextField(2).numbersOnly = true;
        getTextField(2).setMinMaxDefault(1, 64, 2);
        addLabel(new GuiNpcLabel(3,"stats.firedelay", guiLeft + 5, guiTop + 59, 0x404040));
        addTextField(new GuiNpcTextField(3,this, fontRenderer, guiLeft + 85, guiTop + 54, 50, 18, stats.fireDelay+""));
        getTextField(3).numbersOnly = true;
        getTextField(3).setMinMaxDefault(1, 1000, 20);
        addLabel(new GuiNpcLabel(4,"stats.delayvariance", guiLeft + 5, guiTop + 83, 0x404040));
        addTextField(new GuiNpcTextField(4,this, fontRenderer, guiLeft + 85, guiTop + 78, 50, 18, stats.delayVariance+""));
        getTextField(4).numbersOnly = true;
        getTextField(4).setMinMaxDefault(0, 100, 20);


        addLabel(new GuiNpcLabel(5, "stats.burstspeed", guiLeft + 5, guiTop + 107, 0x404040));
		addTextField(new GuiNpcTextField(5, this, fontRenderer, guiLeft + 85, guiTop + 102, 50, 18, stats.fireRate + ""));
		getTextField(5).numbersOnly = true;
		getTextField(5).setMinMaxDefault(0, 30, 0);
    	addLabel(new GuiNpcLabel(6, "stats.burstcount", guiLeft + 5, guiTop + 131, 0x404040));
    	addTextField(new GuiNpcTextField(6, this, fontRenderer, guiLeft + 85, guiTop + 126, 50, 18, stats.burstCount + ""));
    	getTextField(6).numbersOnly = true;
    	getTextField(6).setMinMaxDefault(1, 100, 20);
    	addLabel(new GuiNpcLabel(7, "stats.firesound:", guiLeft + 5, guiTop + 155, 0x404040));
    	addTextField(new GuiNpcTextField(7,this, fontRenderer, guiLeft + 85, guiTop + 150, 100, 20, stats.fireSound));
    	addButton(new GuiNpcButton(7, guiLeft + 187, guiTop + 150, 60, 20, "mco.template.button.select"));
    	
    	addButton(new GuiNpcButton(66, guiLeft + 190, guiTop + 190, 60, 20, "gui.done"));
    }

	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 1){
			stats.accuracy = textfield.getInteger();
		}
		else if(textfield.id == 2){
			stats.rangedRange = textfield.getInteger();
		}
		else if(textfield.id == 3){
			stats.fireDelay = textfield.getInteger();
		}
		else if(textfield.id == 4){
			stats.delayVariance = textfield.getInteger();
		}
		else if(textfield.id == 5){
			stats.fireRate = textfield.getInteger();
		}
		else if(textfield.id == 6){
			stats.burstCount = textfield.getInteger();
		}
		else if(textfield.id == 7){
			stats.fireSound = textfield.getText();
		}
	}
	
	@Override
	public void elementClicked(){
		getTextField(7).setText(gui.getSelected());
		unFocused(getTextField(7));
	}
    
	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 7)
        {
        	NoppesUtil.openGUI(player, gui = new GuiNpcSoundSelection(npc, parent, getTextField(7).getText()));
        }
        if(guibutton.id == 66)
        {
        	close();
        }
    }
}

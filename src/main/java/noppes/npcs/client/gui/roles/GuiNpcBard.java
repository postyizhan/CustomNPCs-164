// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.GuiNpcMusicSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.JobBard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcBard extends GuiNPCInterface2
{	
	private JobBard job;
	private GuiNpcMusicSelection gui;
	
    public GuiNpcBard(EntityNPCInterface npc)
    {
    	super(npc);    	
    	job = (JobBard) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();

    	this.addButton(new GuiNpcButton(1, guiLeft + 55, guiTop + 15,20,20, "X"));
        addLabel(new GuiNpcLabel(0,job.song, guiLeft + 80, guiTop + 20, 0x404040));
    	this.addButton(new GuiNpcButton(0, guiLeft + 75, guiTop + 50, "Select Music"));

    	this.addButton(new GuiNpcButton(2, guiLeft + 75, guiTop + 71, new String[]{"None","Banjo","Violin","Guitar","Harp"}, job.getInstrument().ordinal()));
    	this.addButton(new GuiNpcButton(3, guiLeft + 75, guiTop + 92, new String[]{"Play in background","Play as jukebox"}, job.isStreamer?1:0));

        
        addLabel(new GuiNpcLabel(2,"On Distance:", guiLeft + 60, guiTop + 143, 0x404040));
        addTextField(new GuiNpcTextField(2,this, this.fontRenderer, guiLeft+130, guiTop + 138, 40, 20, job.minRange + ""));
        getTextField(2).numbersOnly = true;
        getTextField(2).setMinMaxDefault(2, 64, 5);
        
        addLabel(new GuiNpcLabel(3,"Off Distance:", guiLeft + 60, guiTop + 166, 0x404040));
        addTextField(new GuiNpcTextField(3,this, this.fontRenderer, guiLeft+130, guiTop + 161, 40, 20, job.maxRange + ""));
        getTextField(3).numbersOnly = true;
        getTextField(3).setMinMaxDefault(2, 64, 10);

    	getLabel(3).enabled = job.isStreamer;
    	getTextField(3).enabled = job.isStreamer;
    }
    @Override
    public void elementClicked(){
    	job.song = gui.getSelected();
    }

    protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	gui = new GuiNpcMusicSelection(npc, this, job.song);
        	NoppesUtil.openGUI(player, gui);
        	job.song = "";
        	MusicController.Instance.stopMusic();
        }
        if(guibutton.id == 1)
        {
        	job.song = "";
        	getLabel(0).label = "";
        	MusicController.Instance.stopMusic();
        }
        if(guibutton.id == 2)
        {
        	job.setInstrument(button.getValue());
        }
        if(guibutton.id == 3)
        {
        	job.isStreamer = button.getValue() == 1;

        	getLabel(3).enabled = job.isStreamer;
        	getTextField(3).enabled = job.isStreamer;
        }
    	        
    }

    @Override
	public void save() {
    	job.minRange = getTextField(2).getInteger();
    	job.maxRange = getTextField(3).getInteger();
    	
    	if(job.minRange > job.maxRange)
    		job.maxRange = job.minRange;

    	MusicController.Instance.stopMusic();
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}


}

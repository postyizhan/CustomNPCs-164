// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNpcMobSpawnerSelector;
import noppes.npcs.client.gui.GuiNpcMusicSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.JobBoss;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcBoss extends GuiNPCInterface2
{	
	private JobBoss job;
	private GuiNpcMusicSelection gui;

	private int slot = -1;
	
    public GuiNpcBoss(EntityNPCInterface npc)
    {
    	super(npc);    	
    	job = (JobBoss) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();

        addLabel(new GuiNpcLabel(40, "Show name", guiLeft + 300, guiTop + 25, 0x404040));
    	this.addButton(new GuiNpcButton(40, guiLeft + 355, guiTop + 20,40,20, new String[]{"gui.no","gui.yes"}, job.hideName?0:1));
    	
    	int y = 6;

    	this.addButton(new GuiNpcButton(20, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(0, "90%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(0, guiLeft + 80, guiTop + y, getTitle(job.compound9)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(21, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(1, "80%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(1, guiLeft + 80, guiTop + y, getTitle(job.compound8)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(22, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(2, "70%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(2, guiLeft + 80, guiTop + y, getTitle(job.compound7)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(23, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(3, "60%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(3, guiLeft + 80, guiTop + y, getTitle(job.compound6)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(24, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(4, "50%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(4, guiLeft + 80, guiTop + y, getTitle(job.compound5)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(25, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(5, "40%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(5, guiLeft + 80, guiTop + y, getTitle(job.compound4)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(26, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(6, "30%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(6, guiLeft + 80, guiTop + y, getTitle(job.compound3)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(27, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(7, "20%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(7, guiLeft + 80, guiTop + y, getTitle(job.compound2)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(28, guiLeft + 55, guiTop + y,20,20, "X"));
        addLabel(new GuiNpcLabel(8, "10%", guiLeft + 4, guiTop + y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(8, guiLeft + 80, guiTop + y, getTitle(job.compound1)));
    }

    private String getTitle(NBTTagCompound compound) {
		if(compound != null && compound.hasKey("ClonedName"))
			return compound.getString("ClonedName");
		
		return "gui.selectnpc";
	}
    
    @Override
    public void elementClicked(){
    	gui.getSelected();
    }

    protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;     
    	if(button.id >= 0 && button.id < 9){
    		slot = 9 - button.id;
    		setSubGui(new GuiNpcMobSpawnerSelector());
    	}     
    	if(button.id >= 20 && button.id < 29){
			job.setNBT(29 - button.id, null); 
			initGui();
    	}
    	if(button.id == 40){
    		job.hideName = button.getValue() == 0;
    	}
    }

	public void closeSubGui(SubGuiInterface gui) {
		super.closeSubGui(gui);
		NBTTagCompound compound = ((GuiNpcMobSpawnerSelector)gui).getCompound();
		if(compound != null)
			job.setNBT(slot, compound); 
		initGui();
	}

    @Override
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}


}

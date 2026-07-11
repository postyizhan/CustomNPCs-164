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
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.JobSpawner;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcSpawner extends GuiNPCInterface2 implements ITextfieldListener
{	
	private JobSpawner job;
	private GuiNpcMusicSelection gui;
	
	private int slot = -1;
	
    public GuiNpcSpawner(EntityNPCInterface npc)
    {
    	super(npc);    	
    	job = (JobSpawner) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();

    	int y = guiTop + 6;

    	this.addButton(new GuiNpcButton(20, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(0, "1:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(0, guiLeft + 50, y, getTitle(job.compound1)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(21, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(1, "2:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(1, guiLeft + 50, y, getTitle(job.compound2)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(22, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(2, "3:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(2, guiLeft + 50, y, getTitle(job.compound3)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(23, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(3, "4:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(3, guiLeft + 50, y, getTitle(job.compound4)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(24, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(4, "5:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(4, guiLeft + 50, y, getTitle(job.compound5)));

    	y += 23; 
    	this.addButton(new GuiNpcButton(25, guiLeft + 25, y,20,20, "X"));
        addLabel(new GuiNpcLabel(5, "6:", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(5, guiLeft + 50, y, getTitle(job.compound6)));

    	y += 23; 
        addLabel(new GuiNpcLabel(6, "Dies after spawning", guiLeft + 4, y + 5, 0x404040));
    	this.addButton(new GuiNpcButton(26, guiLeft + 75, y,40,20, new String[]{"gui.yes","gui.no"}, job.doesntDie?1:0));

    	y += 23; 
        addLabel(new GuiNpcLabel(7,"Position Offset X:", guiLeft + 4, y + 5, 0x404040));
    	addTextField(new GuiNpcTextField(7,this, fontRenderer, guiLeft + 99, y, 24, 20, job.xOffset + ""));
    	getTextField(7).numbersOnly = true;
        getTextField(7).setMinMaxDefault(-9, 9, 0);
        addLabel(new GuiNpcLabel(8,"Y:", guiLeft + 125, y + 5, 0x404040));
    	addTextField(new GuiNpcTextField(8,this, fontRenderer, guiLeft + 135, y, 24, 20, job.yOffset + ""));
    	getTextField(8).numbersOnly = true;
        getTextField(8).setMinMaxDefault(-9, 9, 0);
        addLabel(new GuiNpcLabel(9,"Z:", guiLeft + 161, y + 5, 0x404040));
    	addTextField(new GuiNpcTextField(9,this, fontRenderer, guiLeft + 171, y, 24, 20, job.zOffset + ""));
    	getTextField(9).numbersOnly = true;
        getTextField(9).setMinMaxDefault(-9, 9, 0);  
        
    	y += 23; 
        addLabel(new GuiNpcLabel(10, "SpawnType", guiLeft + 4, y + 5, 0x404040));
        addButton(new GuiNpcButton(10, guiLeft + 80, y, 100, 20, new String[]{"One by One", "All", "Random"}, job.spawnType));
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
    	if(button.id >= 0 && button.id < 6){
    		slot = button.id + 1;
    		setSubGui(new GuiNpcMobSpawnerSelector());
    	}    
    	if(button.id >= 20 && button.id < 26){
			job.setJobCompound(button.id - 19, null); 
			initGui();
    	}
    	if(button.id == 26){
    		job.doesntDie = button.getValue() == 1;
    	}
    	if(button.id == 10){
    		job.spawnType = button.getValue();
    	}
    }

	public void closeSubGui(SubGuiInterface gui) {
		super.closeSubGui(gui);
		NBTTagCompound compound = ((GuiNpcMobSpawnerSelector)gui).getCompound();
		if(compound != null)
			job.setJobCompound(slot, compound); 
		initGui();
	}

    @Override
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 7){
			job.xOffset = textfield.getInteger();
		}
		if(textfield.id == 8){
			job.yOffset = textfield.getInteger();
		}
		if(textfield.id == 9){
			job.zOffset = textfield.getInteger();
		}
	}


}

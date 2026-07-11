// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.containers.ContainerNpcItemGiver;
import noppes.npcs.roles.JobItemGiver;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcItemGiver extends GuiContainerNPCInterface2
{	
	private JobItemGiver role;
    public GuiNpcItemGiver(EntityNPCInterface npc, ContainerNpcItemGiver container)
    {
    	super(npc,container);    	
    	role = (JobItemGiver) npc.jobInterface;
        ySize = 182;
    	setBackground("npcitemgiver.png");
    }

    public void initGui()
    {
    	super.initGui();
    	this.addButton(new GuiNpcButton(0, guiLeft + 6, guiTop + 6, 140,20, new String[]{"Random Item","All Items","Give Not Owned Items","Give When Doesnt Own Any","Chained"},role.givingMethod));
    	this.addButton(new GuiNpcButton(1, guiLeft + 6, guiTop + 29, 140,20, new String[]{"Timer","Give Only Once","Daily"},role.cooldownType));

    	addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 55, guiTop + 54, 90, 20, role.cooldown + ""));
    	getTextField(0).numbersOnly = true;
        addLabel(new GuiNpcLabel(0,"Cooldown:", guiLeft + 6, guiTop + 59, 0x404040));
        addLabel(new GuiNpcLabel(1,"Items to give", guiLeft + 46, guiTop + 79, 0x404040));
        
        getTextField(0).numbersOnly = true;

        int i = 0;
        for(String line : role.lines){
        	addTextField(new GuiNpcTextField(i+1, this, fontRenderer, guiLeft + 150, guiTop + 6 + i * 24, 236, 20,line));
        	i++;
        }
        for(;i <3; i++){
        	addTextField(new GuiNpcTextField(i+1, this, fontRenderer, guiLeft + 150, guiTop + 6 + i * 24, 236, 20,""));
        }
    	getTextField(0).enabled = role.isOnTimer();
    	getLabel(0).enabled = role.isOnTimer();
    }

    public void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	role.givingMethod = button.getValue();
        }
        if(guibutton.id == 1)
        {
        	role.cooldownType = button.getValue();
        	getTextField(0).enabled = role.isOnTimer();
        	getLabel(0).enabled = role.isOnTimer();
        }
    	        
    }

	public void save() {

		List<String> lines = new ArrayList<String>();
    	for(int i = 1; i < 4; i++){
    		GuiNpcTextField tf = getTextField(i);
    		if(!tf.isEmpty())
    			lines.add(tf.getText());
    	}
    	role.lines = lines;
		int cc = 10;
		if(!getTextField(0).isEmpty() && getTextField(0).isInteger())
			cc = getTextField(0).getInteger();

		role.cooldown = cc;

		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}

}

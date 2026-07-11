// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.JobHealer;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcHealer extends GuiNPCInterface2
{	
	private JobHealer job;
    public GuiNpcHealer(EntityNPCInterface npc)
    {
    	super(npc);    	
    	job = (JobHealer) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();

        addLabel(new GuiNpcLabel(1,"Healing Speed:", guiLeft + 60, guiTop + 110, 0x404040));
        addTextField(new GuiNpcTextField(1,this, this.fontRenderer, guiLeft+130, guiTop + 105, 40, 20, job.speed + ""));
        getTextField(1).numbersOnly = true;
        getTextField(1).setMinMaxDefault(1, 10, 8);
        
        addLabel(new GuiNpcLabel(2,"Range:", guiLeft + 60, guiTop + 133, 0x404040));
        addTextField(new GuiNpcTextField(2,this, this.fontRenderer, guiLeft+130, guiTop + 128, 40, 20, job.range + ""));
        getTextField(2).numbersOnly = true;
        getTextField(2).setMinMaxDefault(2, 20, 5);
        
    }
    @Override
    public void elementClicked(){
    	
    }


    @Override
	public void save() {
    	job.speed = getTextField(1).getInteger();
    	job.range = getTextField(2).getInteger();

		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}


}

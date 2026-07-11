// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.roles.JobGuard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcGuard extends GuiNPCInterface2
{	
	private JobGuard role;
    public GuiNpcGuard(EntityNPCInterface npc)
    {
    	super(npc);    	
    	role = (JobGuard) npc.jobInterface;
    }

    public void initGui()
    {
    	super.initGui();
    	this.addButton(new GuiNpcButton(0, guiLeft + 85, guiTop + 20, new String[]{"Dont Attack Animals","Attack Animals"},role.attacksAnimals?1:0));
    	this.addButton(new GuiNpcButton(1, guiLeft + 85, guiTop + 43, new String[]{"Dont Attack Monsters","Attack Monsters"},role.attackHostileMobs?1:0));
    	this.addButton(new GuiNpcButton(4, guiLeft + 85, guiTop + 66, new String[]{"Dont Attack Creepers","Attack Creepers"},role.attackCreepers?1:0));
    	this.addButton(new GuiNpcButton(2, guiLeft + 85, guiTop + 89, new String[]{"Dont Attack Other Mobs","Attack Other Mobs"},role.attackAll?1:0));

//    	getButton(4).enabled = role.attackHostileMobs;
    }

    protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	role.attacksAnimals = button.getValue()==1;
        }
        if(guibutton.id == 1)
        {
        	role.attackHostileMobs = button.getValue()==1;
//        	getButton(4).enabled = role.attackHostileMobs;
        }
        if(guibutton.id == 2)
        {
        	role.attackAll = button.getValue()==1;
        }
        if(guibutton.id == 4)
        {
        	role.attackCreepers = button.getValue()==1;
        }       
    }

	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumModelType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.entity.EntityNPCDwarfFemale;
import noppes.npcs.entity.EntityNPCDwarfMale;
import noppes.npcs.entity.EntityNPCElfFemale;
import noppes.npcs.entity.EntityNPCElfMale;
import noppes.npcs.entity.EntityNPCEnderman;
import noppes.npcs.entity.EntityNPCFurryFemale;
import noppes.npcs.entity.EntityNPCFurryMale;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNPCHumanFemale;
import noppes.npcs.entity.EntityNPCHumanMale;
import noppes.npcs.entity.EntityNPCOrcFemale;
import noppes.npcs.entity.EntityNPCOrcMale;
import noppes.npcs.entity.EntityNPCPony;
import noppes.npcs.entity.EntityNPCVillager;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcEnderchibi;
import noppes.npcs.entity.EntityNpcMonsterFemale;
import noppes.npcs.entity.EntityNpcMonsterMale;
import noppes.npcs.entity.EntityNpcNagaFemale;
import noppes.npcs.entity.EntityNpcNagaMale;
import noppes.npcs.entity.EntityNpcSkeleton;
import noppes.npcs.entity.EntityNpcSlime;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcModelSelection extends GuiNPCInterface
{
	private GuiNPCStringSlot slot;
	private GuiNPCInterface2 parent;
	private HashMap<String,EnumModelType> data = new HashMap<String,EnumModelType>();
	
    public GuiNpcModelSelection(EntityNPCInterface npc,GuiNPCInterface2 parent)
    {
    	super(npc);
    	drawDefaultBackground = false;
		title = "Select Npc Model";
    	this.parent = parent;
    }

    public void initGui()
    {
        super.initGui();
        Vector<String> list = new Vector<String>();
        for(EnumModelType type : EnumModelType.values()){
        	list.add(type.name);
        	data.put(type.name, type);
        }
        slot = new GuiNPCStringSlot(list,this,npc,false,18);
        slot.registerScrollButtons(4, 5);
        slot.selected = npc.display.modelType.name;
        
    	this.addButton(2,new GuiNpcButton(2, width / 2 -100, height - 41,98, 20, "gui.back"));
    	this.addButton(4,new GuiNpcButton(4, width / 2  + 2, height - 41,98, 20, "mco.template.button.select"));
    }


    public void drawScreen(int i, int j, float f)
    {
    	slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }

	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 2)
        {
        	NoppesUtil.openGUI(player, parent);
        }
        if(guibutton.id == 4)
        {
        	close();
        }
    }
	public void doubleClicked() {
    	close();
		
	}
	@Override
	public void save() {
		if(npc.display.modelType == data.get(slot.selected))
			return;
		npc.display.modelType = data.get(slot.selected);
    	EntityNPCInterface newnpc = null;
    	if(npc.display.modelType == EnumModelType.HumanMale)
    		newnpc = new EntityNPCHumanMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Villager)
    		newnpc = new EntityNPCVillager(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Pony)
    		newnpc = new EntityNPCPony(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.HumanFemale)
    		newnpc = new EntityNPCHumanFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.DwarfMale)
    		newnpc = new EntityNPCDwarfMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.FurryMale)
    		newnpc = new EntityNPCFurryMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.MonsterMale)
    		newnpc = new EntityNpcMonsterMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.MonsterFemale)
    		newnpc = new EntityNpcMonsterFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Skeleton)
    		newnpc = new EntityNpcSkeleton(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.DwarfFemale)
    		newnpc = new EntityNPCDwarfFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.FurryFemale)
    		newnpc = new EntityNPCFurryFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.OrcMale)
    		newnpc = new EntityNPCOrcMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.OrcFemale)
    		newnpc = new EntityNPCOrcFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.ElfMale)
    		newnpc = new EntityNPCElfMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.ElfFemale)
    		newnpc = new EntityNPCElfFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Crystal)
    		newnpc = new EntityNpcCrystal(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.EnderChibi)
    		newnpc = new EntityNpcEnderchibi(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.EnderMan)
    		newnpc = new EntityNPCEnderman(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.NagaFemale)
    		newnpc = new EntityNpcNagaFemale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.NagaMale)
    		newnpc = new EntityNpcNagaMale(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Dragon)
    		newnpc = new EntityNpcDragon(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Slime)
    		newnpc = new EntityNpcSlime(npc.worldObj);
    	if(npc.display.modelType == EnumModelType.Golem)
    		newnpc = new EntityNPCGolem(npc.worldObj);
    	
    	String texture = newnpc.display.texture;
    	NBTTagCompound nbttagcompound = npc.copy();
    	newnpc.readFromNBT(nbttagcompound);
    	
    	String faction = npc.getDataWatcher().getWatchableObjectString(13);
    	newnpc.getDataWatcher().updateObject(13, faction);
    	
    	newnpc.display.texture = texture;
    	NoppesUtil.sendData(EnumPacketType.ChangeModel, npc.startPos[0], npc.startPos[1], npc.startPos[2], newnpc.copy());
        
	}


}

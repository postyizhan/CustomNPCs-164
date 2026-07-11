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
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCFactionSetup extends GuiNPCInterface2 implements IScrollData,GuiCustomScrollActionListener
{
	private GuiCustomScroll scrollFactions;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	
    public GuiNPCFactionSetup(EntityNPCInterface npc)
    {
    	super(npc);
    	NoppesUtil.sendData(EnumPacketType.FactionsGet);
    }

    public void initGui()
    {
        super.initGui();

        this.addLabel(new GuiNpcLabel(0, "faction.attackHostile", guiLeft + 4, guiTop + 25, 0x404040));
        this.addButton(new GuiNpcButton(0, guiLeft + 144, guiTop + 20,40,20, new String[]{"gui.no","gui.yes"}, npc.advanced.attackOtherFactions?1:0));

        this.addLabel(new GuiNpcLabel(1, "faction.defend", guiLeft + 4, guiTop + 47, 0x404040));
        this.addButton(new GuiNpcButton(1, guiLeft + 144, guiTop + 42,40,20, new String[]{"gui.no","gui.yes"}, npc.advanced.defendFaction?1:0));

        this.addLabel(new GuiNpcLabel(12, "faction.ondeath", guiLeft + 4, guiTop + 69, 0x404040));
        addButton(new GuiNpcButton(12, guiLeft + 90, guiTop + 64, 80, 20, "faction.points"));
        
        
        scrollFactions = new GuiCustomScroll(this,0);
        scrollFactions.setWorldAndResolution(mc, 350, 250);
        scrollFactions.setSize(180, 200);
        scrollFactions.guiLeft = guiLeft + 200;
        scrollFactions.guiTop = guiTop + 4;
    }

    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(x, y, f);
        scrollFactions.drawScreen(x, y, f);
    }

    @Override
    public void buttonEvent(GuiButton guibutton) {
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	npc.advanced.attackOtherFactions = button.getValue() == 1;
        }
        if(guibutton.id == 1)
        {
        	npc.advanced.defendFaction = button.getValue() == 1;
        }
    	if(button.id == 12)
        	setSubGui(new SubGuiNpcFactionOptions(npc.advanced.factions));
    }
    
    }
	
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) 
	{
		String name = npc.getFaction().name;
		this.data = data;
		scrollFactions.setList(list);
		
		if(name != null)
			setSelected(name);
	}
	
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	if(k == 0 && scrollFactions != null)
    		scrollFactions.mouseClicked(i, j, k);
    }
	
	@Override
	public void setSelected(String selected) {
		scrollFactions.setSelected(selected);
	}
	
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			NoppesUtil.sendData(EnumPacketType.FactionSet, data.get(scrollFactions.getSelected()));
		}
	}
	
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}

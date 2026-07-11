// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiNpcFactionPoints;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.Faction;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCManageFactions extends GuiNPCInterface2 implements IScrollData,GuiCustomScrollActionListener,ITextfieldListener, IGuiData
{
	private GuiCustomScroll scrollFactions;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private Faction faction = new Faction();
	private String selected = null;
	
    public GuiNPCManageFactions(EntityNPCInterface npc)
    {
    	super(npc);
    	NoppesUtil.sendData(EnumPacketType.FactionsGet);
    }

    public void initGui()
    {
        super.initGui();
        
       	this.addButton(new GuiNpcButton(0,guiLeft + 368, guiTop + 8, 45, 20, "gui.add"));
    	this.addButton(new GuiNpcButton(1,guiLeft + 368, guiTop + 32, 45, 20, "gui.remove"));
        
    	if(scrollFactions == null){
	        scrollFactions = new GuiCustomScroll(this,0);
	        scrollFactions.setSize(143, 208);
	        scrollFactions.guiLeft = guiLeft + 220;
	        scrollFactions.guiTop = guiTop + 4;
    	}
    	addScroll(scrollFactions);
        
    	if (faction.id == -1)
    		return;
           	
    	this.addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 48, guiTop + 4, 165, 20, faction.name));
    	getTextField(0).setMaxStringLength(20);
    	addLabel(new GuiNpcLabel(0,"gui.name", guiLeft + 8, guiTop + 9, 0x404040));

    	String color = Integer.toHexString(faction.color);
    	while(color.length() < 6)
    		color = "0" + color;
    	this.addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft + 48, guiTop + 26, 165, 20, color));
    	addLabel(new GuiNpcLabel(1,"gui.color", guiLeft + 8, guiTop + 31, 0x404040));
    	getTextField(1).setMaxStringLength(6);
		getTextField(1).setTextColor(faction.color);

    	addLabel(new GuiNpcLabel(2,"faction.points", guiLeft + 8, guiTop + 53, 0x404040));
       	this.addButton(new GuiNpcButton(2,guiLeft + 80, guiTop + 48, 45, 20, "selectServer.edit"));

    	addLabel(new GuiNpcLabel(3,"faction.hidden", guiLeft + 8, guiTop + 75, 0x404040));
       	this.addButton(new GuiNpcButton(3,guiLeft + 80, guiTop + 70, 45, 20, new String[]{"gui.no","gui.yes"},faction.hideFaction?1:0));


    	addLabel(new GuiNpcLabel(4,"faction.attacked", guiLeft + 8, guiTop + 97, 0x404040));
       	this.addButton(new GuiNpcButton(4,guiLeft + 80, guiTop + 92, 45, 20, new String[]{"gui.no","gui.yes"},faction.getsAttacked?1:0));
    	
    	addLabel(new GuiNpcLabel(6,"faction.hostiles", guiLeft + 8, guiTop + 145, 0x404040));
    	
		ArrayList<String> hostileList = new ArrayList<String>(scrollFactions.getList());
		hostileList.remove(faction.name);

		HashSet<String> set = new HashSet<String>();
		for(String s : data.keySet()){
			if(!s.equals(faction.name) && faction.attackFactions.contains(data.get(s)))
				set.add(s);
		}
		
    	GuiCustomScroll scrollHostileFactions = new GuiCustomScroll(this,1,true);
        scrollHostileFactions.setSize(163, 58);
        scrollHostileFactions.guiLeft = guiLeft + 4;
        scrollHostileFactions.guiTop = guiTop + 154;
		scrollHostileFactions.setList(hostileList);
		scrollHostileFactions.setSelectedList(set);
        addScroll(scrollHostileFactions);
    }

	protected void actionPerformed(GuiButton guibutton)
    {
		GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	save();
        	String name = "New";
        	while(data.containsKey(name))
        		name += "_";
        	Faction faction = new Faction(-1, name, 0x00FF00, 1000);
        	
			NBTTagCompound compound = new NBTTagCompound();
			faction.writeNBT(compound);
        	NoppesUtil.sendData(EnumPacketType.FactionSave, compound);
        }
        if(guibutton.id == 1)
        {
        	if(data.containsKey(scrollFactions.getSelected())) {
        		NoppesUtil.sendData(EnumPacketType.FactionRemove, data.get(selected));
        		scrollFactions.clear();
        		faction = new Faction();
        		initGui();
        	}
        }
        if(guibutton.id == 2)
        {
        	this.setSubGui(new SubGuiNpcFactionPoints(faction));
        }
        if(guibutton.id == 3)
        {
        	faction.hideFaction = button.getValue() == 1;
        }
        if(guibutton.id == 4)
        {
        	faction.getsAttacked = button.getValue() == 1;
        }
    }

	@Override
	public void setGuiData(NBTTagCompound compound) {

		this.faction = new Faction();
		faction.readNBT(compound);
		
		setSelected(faction.name);
		initGui();
	}
	

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		String name = scrollFactions.getSelected();
		this.data = data;
		scrollFactions.setList(list);
		
		if(name != null)
			scrollFactions.setSelected(name);
	}
    
	@Override
	public void setSelected(String selected) {
		this.selected = selected;
		scrollFactions.setSelected(selected);
	}
    
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			save();
			selected = scrollFactions.getSelected();
			NoppesUtil.sendData(EnumPacketType.FactionGet, data.get(selected));
		}
		else if(guiCustomScroll.id == 1)
		{
			HashSet<Integer> set = new HashSet<Integer>();
			for(String s : guiCustomScroll.getSelectedList()){
				if(data.containsKey(s))
					set.add(data.get(s));
			}
			faction.attackFactions = set;
			save();
		}
	}
	
	public void save() {
		if(selected != null && data.containsKey(selected) && faction != null){
			NBTTagCompound compound = new NBTTagCompound();
			faction.writeNBT(compound);
    	
			NoppesUtil.sendData(EnumPacketType.FactionSave, compound);
		}
	}
		
	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(faction.id == -1) 
			return;
		
		if(guiNpcTextField.id == 0) {
			String name = guiNpcTextField.getText();
			if(!name.isEmpty() && !data.containsKey(name)){
				String old = faction.name;
				data.remove(faction.name);
				faction.name = name;
				data.put(faction.name, faction.id);
				selected = name;
				scrollFactions.replace(old,faction.name);
			}
		} else if(guiNpcTextField.id == 1) {
			int color = 0;
			try{
				color = Integer.parseInt(guiNpcTextField.getText(),16);
			}
			catch(NumberFormatException e){
				color = 0;
			}
	    	faction.color = color;
	    	guiNpcTextField.setTextColor(faction.color);
		} 
		
	}

}

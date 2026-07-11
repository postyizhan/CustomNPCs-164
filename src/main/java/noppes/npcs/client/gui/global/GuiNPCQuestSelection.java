// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCQuestSelection extends GuiNPCInterface implements IScrollData
{
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	private HashMap<String,Integer> data;
	private boolean selectCategory = true;
	public GuiSelectionListener listener;
	private int quest;
	
    public GuiNPCQuestSelection(EntityNPCInterface npc,GuiScreen parent,int quest)
    {
    	super(npc);
    	drawDefaultBackground = false;
		title = "Select Quest Category";
    	this.parent = parent;
    	data = new HashMap<String, Integer>();
    	this.quest = quest;
    	
    	if(quest >= 0){
    		NoppesUtil.sendData(EnumPacketType.QuestsGetFromQuest, quest);
    		selectCategory = false;
    		title = "Select Dialog";
    	}else
    		NoppesUtil.sendData(EnumPacketType.QuestCategoriesGet, quest);
    	
    	if(parent instanceof GuiSelectionListener)
    		listener = (GuiSelectionListener) parent;
    }

    public void initGui()
    {
        super.initGui();
        Vector<String> list = new Vector<String>();
        
        slot = new GuiNPCStringSlot(list,this,npc,false,18);
        slot.registerScrollButtons(4, 5);
        

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
        	if(selectCategory){
            	close();
            	NoppesUtil.openGUI(player, parent);
        	}else{
    			title = "Select Dialog Category";
        		selectCategory = true;
        		NoppesUtil.sendData(EnumPacketType.QuestCategoriesGet, quest);
        	}
        }
        if(guibutton.id == 4)
        {
        	if(slot.selected == null || slot.selected.isEmpty())
        		return;
        	doubleClicked();
        }
    }
	public String getSelected(){
		return slot.selected;
	}
	public void doubleClicked() {
    	if(slot.selected == null || slot.selected.isEmpty())
    		return;
		if(selectCategory){
			selectCategory = false;
			title = "Select Quest";
			NoppesUtil.sendData(EnumPacketType.QuestsGet, data.get(slot.selected));
		}
		else{
			quest = data.get(slot.selected);
			close();
			NoppesUtil.openGUI(player, parent);
		}
		
	}
	public void save() {
		if(quest >= 0){
			if(listener != null)
				listener.selected(quest);
		}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = data;
		this.slot.setList(list);
		if(quest >= 0)
			for(String name : data.keySet())
				if(data.get(name) == quest)
					slot.selected = name;
	}

	@Override
	public void setSelected(String selected) {
		
	}

}

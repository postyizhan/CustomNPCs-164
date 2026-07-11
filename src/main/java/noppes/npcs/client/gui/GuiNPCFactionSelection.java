// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

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

public class GuiNPCFactionSelection extends GuiNPCInterface implements IScrollData
{
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private int dialog;
	public GuiSelectionListener listener;
    public GuiNPCFactionSelection(EntityNPCInterface npc,GuiScreen parent,int dialog)
    {
    	super(npc);
    	drawDefaultBackground = false;
		title = "Select Dialog Category";
    	this.parent = parent;
    	this.dialog = dialog;
    	
		NoppesUtil.sendData(EnumPacketType.FactionsGet);
    	
    	if(parent instanceof GuiSelectionListener){
    		listener = (GuiSelectionListener) parent;
    	}
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
        	close();
        	NoppesUtil.openGUI(player, parent);
        }
        if(guibutton.id == 4)
        {
        	doubleClicked();
        }
    }
	public void doubleClicked() {
    	if(slot.selected == null || slot.selected.isEmpty())
    		return;
		dialog = data.get(slot.selected);
		close();
		NoppesUtil.openGUI(player, parent);
		
	}
	public void save() {
		if(dialog >= 0){
			if(listener != null)
				listener.selected(dialog);
		}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = data;
		this.slot.setList(list);
		if(dialog >= 0)
			for(String name : data.keySet())
				if(data.get(name) == dialog)
					slot.selected = name;
	}

	@Override
	public void setSelected(String selected) {
		
	}


}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.questtypes;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.controllers.Quest;
import noppes.npcs.quests.QuestKill;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcQuestTypeKill extends GuiNPCInterface implements ITextfieldListener, GuiCustomScrollActionListener
{
	private GuiScreen parent;
	private GuiCustomScroll scroll;
	
	private QuestKill quest;
	
	private GuiNpcTextField lastSelected;

    public GuiNpcQuestTypeKill(EntityNPCInterface npc, Quest q,
			GuiScreen parent) {
    	super(npc);
    	this.parent = parent;
    	title = "Quest Kill Setup";
    	
    	quest = (QuestKill) q.questInterface;
    	
	}

	public void initGui() {
		super.initGui();
		int i = 0;
		addLabel(new GuiNpcLabel(0, "You can fill in npc or player names too", guiLeft - 100, guiTop + 50, 0xffffff));
		for (String name : quest.targets.keySet()) {
			this.addTextField(new GuiNpcTextField(i, this, fontRenderer, guiLeft - 100, guiTop + 70 + i * 22, 180, 20, name));
			this.addTextField(new GuiNpcTextField(i + 3, this, fontRenderer, guiLeft + 84, guiTop + 70 + i * 22, 30, 20, quest.targets.get(name) + ""));
			this.getTextField(i+3).numbersOnly = true;
			this.getTextField(i+3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
			i++;
		}
		
		for(;i < 3; i++){
			this.addTextField(new GuiNpcTextField(i, this, fontRenderer, guiLeft - 100, guiTop + 70 + i * 22, 180, 20, ""));
			this.addTextField(new GuiNpcTextField(i + 3, this, fontRenderer, guiLeft + 84, guiTop + 70 + i * 22, 30, 20, "1"));
			this.getTextField(i+3).numbersOnly = true;
			this.getTextField(i+3).setMinMaxDefault(1, Integer.MAX_VALUE, 1);
		}
        Map<?,?> data = EntityList.stringToClassMapping;
        ArrayList<String> list = new ArrayList<String>();
        for(Object name : data.keySet()){
        	Class<?> c = (Class<?>) data.get(name);
        	try {
        		if(EntityLivingBase.class.isAssignableFrom(c) && !EntityNPCInterface.class.isAssignableFrom(c) && c.getConstructor(new Class[] {World.class}) != null && !Modifier.isAbstract(c.getModifiers()))
        				list.add(name.toString());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
			}
        }        
        scroll = new GuiCustomScroll(this,0);
        scroll.setList(list);
        scroll.setWorldAndResolution(mc, 350, 250);
        scroll.setSize(140, 190);
        scroll.guiLeft = guiLeft + 120;
        scroll.guiTop = guiTop + 14;
		this.addButton(new GuiNpcButton(0, guiLeft - 100, guiTop + 140, 98, 20, "gui.back"));
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		scroll.drawScreen(i, j, f);
	}
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			NoppesUtil.openGUI(player, parent);
		}
	}
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	scroll.mouseClicked(i, j, k);
    }
	public void save() {
	}

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(guiNpcTextField.id < 3)
			lastSelected = guiNpcTextField;

		saveTargets();
	}
	private void saveTargets(){
		HashMap<String,Integer> map = new HashMap<String,Integer>(); 
		for(int i = 0; i< 3; i++){
			String name = getTextField(i).getText();
			if(name.isEmpty())
				continue;
			map.put(name, getTextField(i+3).getInteger());
		}
		quest.targets = map;
	}
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(lastSelected == null)
			return;
		lastSelected.setText(guiCustomScroll.getSelected());
		saveTargets();
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import noppes.npcs.EntityNPCInterface;

// Referenced classes of package net.minecraft.src:
//            GuiSlot, GuiSelectWorld, GuiButton, SaveFormatComparator, 
//            MathHelper, StatCollector, Tessellator

public class GuiNPCStringSlot extends GuiSlot
{

	private List<String> list; /* synthetic field */
    public String selected;
    public HashSet<String> selectedList;
    private boolean multiSelect;
    private GuiNPCInterface parent;
    public int size;
    public GuiNPCStringSlot(List<String> list,GuiNPCInterface parent,EntityNPCInterface npc,boolean multiSelect, int size)
    {
        super(Minecraft.getMinecraft(), parent.width, parent.height, 32, parent.height - 64, size);
        selectedList = new HashSet<String>();
        this.parent = parent;
        Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        this.list = list;
        this.multiSelect = multiSelect;
        this.size = size;
    }
    public void setList(List<String> list){
        Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        this.list = list;
        selected = "";
    }

    protected int getSize()
    {
        return list.size();
    }
    private long prevTime = 0;
    protected void elementClicked(int i, boolean flag)
    {
//        GuiSelectWorld.onElementSelected(parentWorldGui, i);
//        boolean flag1 = GuiSelectWorld.getSelectedWorld(parentWorldGui) >= 0 && GuiSelectWorld.getSelectedWorld(parentWorldGui) < getSize();
//        GuiSelectWorld.getSelectButton(parentWorldGui).enabled = flag1;
//        GuiSelectWorld.getRenameButton(parentWorldGui).enabled = flag1;
//        GuiSelectWorld.getDeleteButton(parentWorldGui).enabled = flag1;
//        if(flag && flag1)
//        {
//            parentWorldGui.selectWorld(i);
//        }
    	long time = System.currentTimeMillis();
    	if(selected != null && selected.equals(list.get(i)) && time - prevTime < 400 )
    		parent.doubleClicked();
		selected = list.get(i);
		if(selectedList.contains(selected))
			selectedList.remove(selected);
		else
			selectedList.add(selected);
		parent.elementClicked();
		prevTime = time;
    }

    protected boolean isSelected(int i)
    {
    	if(!multiSelect){
	    	if(selected == null)
	    		return false;
	        return selected.equals(list.get(i));
    	}
    	else{
	        return selectedList.contains(list.get(i));
    	}
    }

    protected int getContentHeight()
    {
        return list.size() * size;
    }

    protected void drawBackground()
    {
        parent.drawDefaultBackground();
    }

    protected void drawSlot(int i, int j, int k, int l, Tessellator tessellator)
    {
    	String s = list.get(i);
    	if(!parent.drawSlot(i, j, k, l, tessellator, s))
    		parent.drawString(parent.getFontRenderer(), s, j + 50, k + 3, 0xFFFFFF);
    }

	public void clear() {
		list.clear();
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.Bank;
import noppes.npcs.roles.RoleBank;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcBankSetup extends GuiNPCInterface2 implements IScrollData,GuiCustomScrollActionListener
{
	private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private RoleBank role;

    public GuiNpcBankSetup(EntityNPCInterface npc)
    {
    	super(npc);
    	NoppesUtil.sendData(EnumPacketType.BanksGet);
    	role = (RoleBank) npc.roleInterface;
    }

    public void initGui()
    {
        super.initGui();

        scroll = new GuiCustomScroll(this,0);
        scroll.setWorldAndResolution(mc, 350, 250);
        scroll.setSize(200, 152);
        scroll.guiLeft = guiLeft + 85;
        scroll.guiTop = guiTop + 20;
    }

    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(x, y, f);
        scroll.drawScreen(x, y, f);
    }

	protected void actionPerformed(GuiButton guibutton)
    {
    }
	
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) 
	{
		String name = null;
		Bank bank = role.getBank();
		if(bank != null)
			name = bank.name;
		this.data = data;
		scroll.setList(list);
		
		if(name != null)
			setSelected(name);
	}
	
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	if(k == 0 && scroll != null)
    		scroll.mouseClicked(i, j, k);
    }
    
	@Override
	public void setSelected(String selected) {
		scroll.setSelected(selected);
	}
	
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			role.bankId = data.get(scroll.getSelected());
			save();
		}
	}
	
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}

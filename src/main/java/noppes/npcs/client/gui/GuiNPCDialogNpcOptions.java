// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.DialogOption;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCDialogNpcOptions extends GuiNPCInterface2 implements GuiSelectionListener,IGuiData{
	private GuiScreen parent;
	private HashMap<Integer, DialogOption> data = new HashMap<Integer,DialogOption>();

	public GuiNPCDialogNpcOptions(EntityNPCInterface npc, GuiScreen parent) {
		super(npc);
		this.parent = parent;
		this.drawDefaultBackground = true;
		NoppesUtil.sendData(EnumPacketType.DialogNpcGet);
	}

	public void initGui() {
		super.initGui();
		for (int i = 0; i < 12; i++) {
			int offset = i >=6 ?200:0;
			this.addButton(new GuiNpcButton(i + 20, guiLeft + 20 + offset, guiTop + 13 + i % 6 * 22, 20, 20, "X"));
			this.addLabel(new GuiNpcLabel(i, "" + i, guiLeft + 6 + offset, guiTop + 18 + i % 6 * 22, 0x000000));
			
			String title = "dialog.selectoption";
			if(data.containsKey(i))
				title = data.get(i).title;
			this.addButton(new GuiNpcButton(i, guiLeft + 44 + offset, guiTop + 13 +  i % 6 * 22, 140, 20, title));

		}
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
	}
	private int selectedSlot;
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 1) {
			NoppesUtil.openGUI(player, parent);
		}
		if (guibutton.id >= 0 && guibutton.id < 20) {
			close();
			selectedSlot = guibutton.id;
			int id = -1;
			if(data.containsKey(guibutton.id))
				id = data.get(guibutton.id).dialogId;
			NoppesUtil.openGUI(player, new GuiNPCDialogSelection(npc, this, id));
		}
		if (guibutton.id >= 20 && guibutton.id < 40) {
			int slot = guibutton.id - 20;
			data.remove(slot);
			NoppesUtil.sendData(EnumPacketType.DialogNpcRemove,slot);
			initGui();
		}
	}

	public void save() {
		return;
	}

	@Override
	public void selected(int id) {
		NoppesUtil.sendData(EnumPacketType.DialogNpcSet, selectedSlot, id);
	}


	@Override
	public void setGuiData(NBTTagCompound compound) {
		int pos = compound.getInteger("Position");
		
		DialogOption dialog = new DialogOption();
		dialog.readNBT(compound);
		
		data.put(pos, dialog);
		initGui();
	}

}

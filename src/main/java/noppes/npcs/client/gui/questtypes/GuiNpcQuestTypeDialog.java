// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCDialogSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.controllers.Quest;
import noppes.npcs.quests.QuestDialog;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcQuestTypeDialog extends GuiNPCInterface implements GuiSelectionListener
{
	private GuiScreen parent;
	
	private QuestDialog quest;

    public GuiNpcQuestTypeDialog(EntityNPCInterface npc, Quest q,
			GuiScreen parent) {
    	super(npc);
    	this.parent = parent;
    	title = "Quest Dialog Setup";
    	
    	quest = (QuestDialog) q.questInterface;
    	
	}

	public void initGui() {
		super.initGui();
		for (int i = 0; i < 3; i++) {
			String title = "dialog.selectoption";
			this.addButton(i + 9, new GuiNpcButton(i + 9, width / 2 - 100, 55 + i * 22, 20, 20, "X"));
			this.addButton(i + 3, new GuiNpcButton(i + 3, width / 2 - 78, 55 + i * 22, 140, 20, title));

		}
		this.addButton(0, new GuiNpcButton(0, width / 2 - 100, 215, 98, 20, "gui.back"));
	}

	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
	}
	private int selectedSlot;
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (guibutton.id == 0) {
			NoppesUtil.openGUI(player, parent);
		}
		if (guibutton.id >= 3 && guibutton.id < 9) {
			close();
			selectedSlot = guibutton.id - 3;
			int id = -1;
			if(quest.dialogs.containsKey(selectedSlot))
				id = quest.dialogs.get(selectedSlot);
			NoppesUtil.openGUI(player, new GuiNPCDialogSelection(npc, this, id));
		}
		if (guibutton.id >= 9 && guibutton.id < 15) {
			int slot = guibutton.id - 9;
			quest.dialogs.remove(slot);
			save();
			initGui();
		}
	}

	public void save() {
	}

	@Override
	public void selected(int id) {
		quest.dialogs.put(selectedSlot, id);
	}

}

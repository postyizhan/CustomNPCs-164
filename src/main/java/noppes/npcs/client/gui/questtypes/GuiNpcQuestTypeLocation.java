// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.controllers.Quest;
import noppes.npcs.quests.QuestLocation;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcQuestTypeLocation extends GuiNPCInterface implements ITextfieldListener
{
	private GuiScreen parent;
	
	private QuestLocation quest;

    public GuiNpcQuestTypeLocation(EntityNPCInterface npc, Quest q,
			GuiScreen parent) {
    	super(npc);
    	this.parent = parent;
    	title = "Quest Location Setup";
    	
    	quest = (QuestLocation) q.questInterface;
    	
	}

	public void initGui() {
		super.initGui();

		this.addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft - 100, guiTop + 70, 180, 20, quest.location));
		this.addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft - 100, guiTop + 92, 180, 20, quest.location2));
		this.addTextField(new GuiNpcTextField(2, this, fontRenderer, guiLeft - 100, guiTop + 114, 180, 20, quest.location3));
		this.addButton(new GuiNpcButton(0, guiLeft - 100, guiTop + 140, 98, 20, "gui.back"));
	}

	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton.id == 0) {
			NoppesUtil.openGUI(player, parent);
		}
	}
	
	public void save() {
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 0)
			quest.location = textfield.getText();
		if(textfield.id == 1)
			quest.location2 = textfield.getText();
		if(textfield.id == 2)
			quest.location3 = textfield.getText();
	}

}

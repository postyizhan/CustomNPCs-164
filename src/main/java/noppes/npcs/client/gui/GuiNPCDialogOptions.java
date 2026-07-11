// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.constants.EnumOptionType;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCDialogOptions extends GuiNPCInterface implements ITextfieldListener, GuiSelectionListener{
	private GuiScreen parent;
	private Dialog dialog;
	public HashMap<Integer,DialogOption> options = new HashMap<Integer,DialogOption>();

	public GuiNPCDialogOptions(EntityNPCInterface npc, GuiScreen parent, Dialog dialog) {
		super(npc);
		this.parent = parent;
		this.dialog = dialog;
		options = dialog.options;
		title = "";
		this.drawDefaultBackground = false;
	}

	public void initGui() {
		super.initGui();		
		for(int i= 0; i < 6;i++){
			if(!options.containsKey(i))
				options.put(i, new DialogOption());
		}
		for (int i = 0; i < 6; i++) {
			DialogOption option = options.get(i);
			int y = 6 + (i % 3) * 70;
			int x = (width - 420)/ 4 + width/2 + 1; 
			if(i > 2)
				x -= width/2;
	    	this.addButton(new GuiNpcButton(i, x,  y, 92, 20, new String[]{"dialog.quitoption","dialog.option"," gui.disabled","dialog.roleoption","dialogcommandoption"},option.optionType.ordinal()));
	        this.addTextField(new GuiNpcTextField(i, this, this.fontRenderer, x+2,  y + 22, 196, 20, option.title));
			
	        this.addButton(new GuiNpcButton(i + 6, x, y + 44, 196, 20, "dialog.selectoption"));
	        
	    	String color = Integer.toHexString(option.optionColor);
	    	while(color.length() < 6)
	    		color = 0 + color;
	    	
	        addLabel(new GuiNpcLabel(i, "gui.color", x + 98, y + 4, 0xffffff));
	    	this.addTextField(new GuiNpcTextField(i+6, this, this.fontRenderer, x+130, y, 45, 20, color));

	    	this.addTextField(new GuiNpcTextField(i + 12, this, this.fontRenderer, x+2,  y + 44, 196, 20, option.command));
	    	this.getTextField(i+12).enabled = option.optionType == EnumOptionType.CommandBlock;
	    	
	    	getButton(i + 6).enabled = getButton(i + 6).drawButton = option.optionType == EnumOptionType.DialogOption;

			getTextField(i).enabled = option.optionType != EnumOptionType.Disabled; 
			getTextField(i + 6).enabled = option.optionType != EnumOptionType.Disabled; 
			
			getLabel(i).enabled = option.optionType != EnumOptionType.Disabled;
		}
		this.addButton(new GuiNpcButton(66, width / 2 - 49, 215, 98, 20, "gui.back"));
	}

	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawVerticalLine(width / 2, 0, height, 0xFFFFFFFF);
		drawHorizontalLine(0, width, 74, 0xFFFFFFFF);
		drawHorizontalLine(0, width, 144, 0xFFFFFFFF);
		super.drawScreen(i, j, f);
	}
	private int selectedSlot;
	protected void actionPerformed(GuiButton guibutton) {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if (guibutton.id >= 0 && guibutton.id < 6) {
			DialogOption option = options.get(guibutton.id);
			option.optionType = EnumOptionType.values()[button.getValue()];
			initGui();
		}
		if (guibutton.id >= 6 && guibutton.id < 12) {
			save();
			selectedSlot = guibutton.id - 6;
			
			DialogOption option = options.get(selectedSlot);
			int id = -1;
			if(option != null)
				id = option.dialogId;
			NoppesUtil.openGUI(player, new GuiNPCDialogSelection(npc, this, id));
		}
		
		if (guibutton.id == 66) {
			save();
			NoppesUtil.openGUI(player, parent);
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if (textfield.id >= 0 && textfield.id < 6) {
			DialogOption option = options.get(textfield.id);
			if(textfield.isEmpty()){
				option.optionType = EnumOptionType.Disabled;
				initGui();
			}else{
				option.title = textfield.getText();
			}
		}
		if (textfield.id >= 6 && textfield.id < 12) {
			DialogOption option = options.get(textfield.id - 6);
			int color = 0xe0e0e0;
			try{
				color = Integer.parseInt(textfield.getText(),16);
			}
			catch(NumberFormatException e){
				color = 0xe0e0e0;
			}
			option.optionColor = color;

	    	String colors = Integer.toHexString(option.optionColor);
	    	while(colors.length() < 6)
	    		colors = 0 + colors;
			textfield.setText(colors);
		}
		if (textfield.id >= 12 && textfield.id < 18) {
			DialogOption option = options.get(textfield.id - 12);
			option.command = textfield.getText();
		}
	}

	public void save() {
		dialog.options = options;
	}

	@Override
	public void selected(int id) {

		if(!options.containsKey(selectedSlot))
			options.put(selectedSlot, new DialogOption());
		
		DialogOption option = options.get(selectedSlot);
		option.dialogId = id;
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumOptionType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcDialogOption extends SubGuiInterface implements IGuiData, ITextfieldListener, GuiSelectionListener
{
	private DialogOption option;
	
    public SubGuiNpcDialogOption(DialogOption option)
    {
    	this.option = option;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
        this.addLabel(new GuiNpcLabel(66, "dialog.editoption", guiLeft, guiTop + 4, 0x404040));
        this.getLabel(66).center(xSize);
        
        this.addLabel(new GuiNpcLabel(0, "gui.title", guiLeft + 4, guiTop + 20, 0x404040));
        this.addTextField(new GuiNpcTextField(0, this, this.fontRenderer, guiLeft + 40, guiTop + 15, 196, 20, option.title));
        
        String color = Integer.toHexString(option.optionColor);
    	while(color.length() < 6)
    		color = 0 + color;
        addLabel(new GuiNpcLabel(2, "gui.color", guiLeft + 4, guiTop + 45, 0x404040));
    	this.addTextField(new GuiNpcTextField(2, this, this.fontRenderer, guiLeft + 62, guiTop + 40, 92, 20, color));
    	this.getTextField(2).setTextColor(option.optionColor);
    	
        this.addLabel(new GuiNpcLabel(1, "dialog.optiontype", guiLeft + 4, guiTop + 67, 0x404040));
    	this.addButton(new GuiNpcButton(1, guiLeft + 62, guiTop + 62, 92, 20, new String[]{"gui.close","dialog.dialog","gui.disabled","menu.role","tile.commandBlock.name"},option.optionType.ordinal()));

    	if(option.optionType == EnumOptionType.DialogOption){
    		this.addButton(new GuiNpcButton(3, guiLeft + 4, guiTop + 84, "availability.selectdialog"));
    		if(option.dialogId >= 0)
    			NoppesUtil.sendData(EnumPacketType.DialogGet, option.dialogId);
    	}
    	if(option.optionType == EnumOptionType.CommandBlock){
	    	this.addTextField(new GuiNpcTextField(4, this, this.fontRenderer, guiLeft + 4,  guiTop + 84, 248, 20, option.command));
	    	this.getTextField(4).setMaxStringLength(32767);

	    	this.addLabel(new GuiNpcLabel(4, "advMode.command", guiLeft + 4, guiTop + 110, 0x404040));
	    	this.addLabel(new GuiNpcLabel(5, "advMode.nearestPlayer", guiLeft + 4, guiTop + 125, 0x404040));
	    	this.addLabel(new GuiNpcLabel(6, "advMode.randomPlayer", guiLeft + 4, guiTop + 140, 0x404040));
	    	this.addLabel(new GuiNpcLabel(7, "advMode.allPlayers", guiLeft + 4, guiTop + 155, 0x404040));
	    	this.addLabel(new GuiNpcLabel(8, "dialogcommandoptionplayer", guiLeft + 4, guiTop + 170, 0x404040));
    	}

    	this.addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 190,98, 20, "gui.done"));
    	
    }

	protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;

    	if(button.id == 1){
			option.optionType = EnumOptionType.values()[button.getValue()];
			initGui();
    	}
    	if(button.id == 3){
    		GuiNPCDialogSelection gui = new GuiNPCDialogSelection(npc, parent, option.dialogId);
    		gui.listener = this;
			NoppesUtil.openGUI(player, gui);
    	}
        if(guibutton.id == 66)
        {
        	close();
        }
    }

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 0){
			if(textfield.isEmpty())
				textfield.setText(option.title);
			else{
				option.title = textfield.getText();
			}
		}
		if(textfield.id == 2){
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
			textfield.setTextColor(color);
		}
		if(textfield.id == 4){
			option.command = textfield.getText();
		}
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		if(compound.hasKey("DialogId")){
			Dialog dialog = new Dialog();
			dialog.readNBT(compound);
			option.dialogId = dialog.id;
			if(this.getButton(3) != null)
				getButton(3).displayString = dialog.title;
		}
	}

	@Override
	public void selected(int ob) {
		option.dialogId = ob;
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNpcMusicSelection;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcAvailability;
import noppes.npcs.client.gui.SubGuiNpcCommand;
import noppes.npcs.client.gui.SubGuiNpcDialogOption;
import noppes.npcs.client.gui.SubGuiNpcDialogOptions;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogCategory;
import noppes.npcs.controllers.PlayerMail;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCManageDialogs extends GuiNPCInterface2 implements IScrollData, ISubGuiListener, GuiSelectionListener,GuiCustomScrollActionListener,ITextfieldListener, IGuiData
{
	private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private Dialog dialog = new Dialog();
	private DialogCategory category = new DialogCategory();
	private boolean categorySelection = true;

	private GuiNpcMusicSelection gui;
	
    public GuiNPCManageDialogs(EntityNPCInterface npc)
    {
    	super(npc);
		NoppesUtil.sendData(EnumPacketType.DialogCategoriesGet);
    }

    public void initGui()
    {
        super.initGui();
       	this.addButton(new GuiNpcButton(0,guiLeft + 358, guiTop + 8, 58, 20,categorySelection?"dialog.dialogs":"gui.categories"));
        
       	this.addButton(new GuiNpcButton(1,guiLeft + 358, guiTop + 38, 58, 20, "gui.add"));
    	this.addButton(new GuiNpcButton(2,guiLeft + 358, guiTop + 61, 58, 20, "gui.remove"));
    	
        if(scroll == null){
	        scroll = new GuiCustomScroll(this,0);
	        scroll.setSize(143, 208);
	        scroll.guiLeft = guiLeft + 214;
	        scroll.guiTop = guiTop + 4;
        }
        this.addScroll(scroll);
        
        if(categorySelection && category.id >= 0)
        	categoryGuiInit();
        if(!categorySelection && dialog.id >= 0)
        	dialogGuiInit();
    }

	private void dialogGuiInit() {
		addLabel(new GuiNpcLabel(1,"gui.title", guiLeft + 4, guiTop + 8, 0x404040));
		addTextField(new GuiNpcTextField(1, this, this.fontRenderer, guiLeft + 60, guiTop + 3, 140, 20, dialog.title));
		
		addLabel(new GuiNpcLabel(3, "dialog.dialogtext", guiLeft + 4, guiTop + 30, 0x404040));
    	addButton(new GuiNpcButton(3, guiLeft + 120, guiTop + 25, 50, 20, "selectServer.edit"));
    	
		addLabel(new GuiNpcLabel(4, "availability.options", guiLeft + 4, guiTop + 51, 0x404040));
		addButton(new GuiNpcButton(4, guiLeft + 120, guiTop + 46, 50, 20, "selectServer.edit"));
    	
		addLabel(new GuiNpcLabel(5, "faction.options", guiLeft + 4, guiTop + 72, 0x404040));
		addButton(new GuiNpcButton(5, guiLeft + 120, guiTop + 67, 50, 20, "selectServer.edit"));

		addLabel(new GuiNpcLabel(6, "dialog.options", guiLeft + 4, guiTop + 93, 0x404040));
		addButton(new GuiNpcButton(6, guiLeft + 120, guiTop + 89, 50, 20, "selectServer.edit"));

		addButton(new GuiNpcButton(7, guiLeft + 4, guiTop + 114, 144, 20, "availability.selectquest"));
		addButton(new GuiNpcButton(8, guiLeft + 150, guiTop + 114, 20, 20, "X"));

		addLabel(new GuiNpcLabel(9, "gui.selectSound", guiLeft + 4, guiTop + 138, 0x404040));
		addTextField(new GuiNpcTextField(2, this, fontRenderer, guiLeft + 4, guiTop + 148, 144, 20, dialog.sound));
		addButton(new GuiNpcButton(9, guiLeft + 150, guiTop + 148, 60, 20, "mco.template.button.select"));

		addButton(new GuiNpcButton(13, guiLeft + 4, guiTop + 169, 164, 20, "mailbox.setup"));
		addButton(new GuiNpcButton(14, guiLeft + 170, guiTop + 169, 20, 20, "X"));
		if(!dialog.mail.subject.isEmpty())
			getButton(13).displayString = dialog.mail.subject;

		addLabel(new GuiNpcLabel(10, "advMode.command", guiLeft + 4, guiTop + 195, 0x404040));
		addButton(new GuiNpcButton(10, guiLeft + 120, guiTop + 190, 50, 20, "selectServer.edit"));
	}

	private void categoryGuiInit() {
        addTextField(new GuiNpcTextField(0,this, this.fontRenderer, guiLeft+8, guiTop + 8, 160, 16, category.title));
        getTextField(0).setMaxStringLength(20);
	}

	@Override
	public void elementClicked() {
		getTextField(2).setText(gui.getSelected());
		unFocused(getTextField(2));
	}
	
	public void buttonEvent(GuiButton guibutton)
    {
        if(guibutton.id == 0)
        {
        	save();
        	if(categorySelection){
        		if(category.id < 0)
        			return;
        		NoppesUtil.sendData(EnumPacketType.DialogsGet, category.id);
        	}
        	else if(!categorySelection){
        		NoppesUtil.sendData(EnumPacketType.DialogCategoriesGet);
            	dialog = new Dialog();
            	category = new DialogCategory();
        	}
        	categorySelection = !categorySelection;
        	getButton(0).enabled = false;
    		scroll.clear();
    		data.clear();
        }
        if(guibutton.id == 1)
        {
        	save();
        	String name = "New";
        	while(data.containsKey(name))
        		name += "_";
        	if(categorySelection){
        		DialogCategory category = new DialogCategory();
        		category.title = name;
        		NoppesUtil.sendData(EnumPacketType.DialogCategorySave, category.writeNBT(new NBTTagCompound()));
        	}
        	else{
        		Dialog dialog = new Dialog();
        		dialog.title = name;
        		NoppesUtil.sendData(EnumPacketType.DialogSave, category.id, dialog.writeToNBT(new NBTTagCompound()));
        	}
        }
        if(guibutton.id == 2)
        {
        	if(data.containsKey(scroll.getSelected())) {
				if(categorySelection){
					NoppesUtil.sendData(EnumPacketType.DialogCategoryRemove, category.id);
					category = new DialogCategory();
				}
				else{
					NoppesUtil.sendData(EnumPacketType.DialogRemove, dialog.id);
					dialog = new Dialog();
				}
        		scroll.clear();
        	}
        }
        if(guibutton.id == 3 && dialog.id >= 0){
        	setSubGui(new SubGuiNpcTextArea(dialog.text));
        }
        if(guibutton.id == 4 && dialog.id >= 0){
        	setSubGui(new SubGuiNpcAvailability(dialog.availability));
        }
        if(guibutton.id == 5 && dialog.id >= 0){
        	setSubGui(new SubGuiNpcFactionOptions(dialog.factionOptions));
        }
        if(guibutton.id == 6 && dialog.id >= 0){
        	setSubGui(new SubGuiNpcDialogOptions(dialog));
        }
        if(guibutton.id == 7 && dialog.id >= 0){
			NoppesUtil.openGUI(player, new GuiNPCQuestSelection(npc, this, dialog.quest));
        }
        if(guibutton.id == 8 && dialog.id >= 0){
        	dialog.quest = -1;
        	initGui();
        }
        if(guibutton.id == 9 && dialog.id >= 0){
        	NoppesUtil.openGUI(player, gui = new GuiNpcMusicSelection(npc, this, getTextField(2).getText()));
        }
    	if(guibutton.id == 10){
    		setSubGui(new SubGuiNpcCommand(dialog.command));
    	}
    	if(guibutton.id == 13){
			setSubGui(new SubGuiMailmanSendSetup(dialog.mail, this));
    	}
        if(guibutton.id == 14)
        {
        	dialog.mail = new PlayerMail();
        	initGui();
        }
    }
	
	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(guiNpcTextField.id == 0) {
			if(category.id < 0)
				guiNpcTextField.setText("");
			else{
				String name = guiNpcTextField.getText();
				if(name.isEmpty() || data.containsKey(name)){
					guiNpcTextField.setText(category.title);
				}
				else if(categorySelection && category.id >= 0){
					String old = category.title;
					data.remove(category.title);
					category.title = name;
					data.put(category.title, category.id);
					scroll.replace(old,category.title);
				}
			}
		}
		if(guiNpcTextField.id == 1) {
			if(dialog.id < 0)
				guiNpcTextField.setText("");
			else{
				String name = guiNpcTextField.getText();
				if(name.isEmpty() || data.containsKey(name)){
					guiNpcTextField.setText(dialog.title);
				}
				else if(!categorySelection && dialog.id >= 0){
					String old = dialog.title;
					data.remove(dialog.title);
					dialog.title = name;
					data.put(dialog.title, dialog.id);
					scroll.replace(old,dialog.title);
				}
			}
		}
		if(guiNpcTextField.id == 2) {
			dialog.sound = guiNpcTextField.getText();
		}
		
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		if(categorySelection){
			category.readNBT(compound);
			setSelected(category.title);
			initGui();
		}
		else{
			dialog.readNBT(compound);
			setSelected(dialog.title);
			initGui();
			if(compound.hasKey("DialogQuestName"))
				getButton(7).displayString = compound.getString("DialogQuestName");
		}
	}

	@Override
	public void subGuiClosed(SubGuiInterface subgui){
		if(subgui instanceof SubGuiNpcTextArea){
			SubGuiNpcTextArea gui = (SubGuiNpcTextArea) subgui;
			dialog.text = gui.text;
		}
		if(subgui instanceof SubGuiNpcDialogOption){
        	setSubGui(new SubGuiNpcDialogOptions(dialog));
		}
		if(subgui instanceof SubGuiNpcCommand){
			dialog.command = ((SubGuiNpcCommand) subgui).command;
		}
	}

	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		getButton(0).enabled = true;
		String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);
		
		if(name != null)
			scroll.setSelected(name);
		initGui();
	}
    
	@Override
	public void setSelected(String selected) {
		
	}

	@Override
	public void selected(int ob) {
		dialog.quest = ob;
		NoppesUtil.sendData(EnumPacketType.DialogSave, category.id, dialog.writeToNBT(new NBTTagCompound()));
		NoppesUtil.sendData(EnumPacketType.DialogGet, dialog.id);
	}
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			save();
			String selected = scroll.getSelected();
			if(categorySelection){
				category = new DialogCategory();
				NoppesUtil.sendData(EnumPacketType.DialogCategoryGet, data.get(selected));
			}
			else{
				dialog = new Dialog();
				NoppesUtil.sendData(EnumPacketType.DialogGet, data.get(selected));
			}
			
		}
	}
	
	public void save() {
    	GuiNpcTextField.unfocus();
		if(!categorySelection && dialog.id >= 0)
			NoppesUtil.sendData(EnumPacketType.DialogSave, category.id, dialog.writeToNBT(new NBTTagCompound()));
		else if(categorySelection && category.id >= 0)
			NoppesUtil.sendData(EnumPacketType.DialogCategorySave, category.writeNBT(new NBTTagCompound()));
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiMailmanSendSetup;
import noppes.npcs.client.gui.SubGuiNpcFactionOptions;
import noppes.npcs.client.gui.SubGuiNpcTextArea;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeDialog;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeKill;
import noppes.npcs.client.gui.questtypes.GuiNpcQuestTypeLocation;
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
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumQuestCompletion;
import noppes.npcs.constants.EnumQuestRepeat;
import noppes.npcs.constants.EnumQuestType;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.Quest;
import noppes.npcs.controllers.QuestCategory;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCManageQuest extends GuiNPCInterface2 implements IScrollData, ISubGuiListener, GuiSelectionListener,GuiCustomScrollActionListener,ITextfieldListener, IGuiData
{
	private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	public static Quest quest = new Quest();
	private QuestCategory category = new QuestCategory();
	private boolean categorySelection = true;
	private boolean questlogTA = false;
	
	public static GuiScreen Instance;
	
    public GuiNPCManageQuest(EntityNPCInterface npc)
    {
    	super(npc);
    	Instance = this;
		NoppesUtil.sendData(EnumPacketType.QuestCategoriesGet);
    }

    public void initGui()
    {
        super.initGui();
       	this.addButton(new GuiNpcButton(0,guiLeft + 358, guiTop + 8, 58, 20,categorySelection?"quest.quests":"gui.categories"));
        
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
        if(!categorySelection && quest.id >= 0)
        	dialogGuiInit();
        
    }

	private void dialogGuiInit() {
		addLabel(new GuiNpcLabel(1,"gui.title", guiLeft + 4, guiTop + 8, 0x404040));
		addTextField(new GuiNpcTextField(1, this, this.fontRenderer, guiLeft + 60, guiTop + 3, 140, 20, quest.title));
		
		addLabel(new GuiNpcLabel(3, "quest.completedtext", guiLeft + 4, guiTop + 30, 0x404040));
    	addButton(new GuiNpcButton(3, guiLeft + 120, guiTop + 25, 50, 20, "selectServer.edit"));
    	
		addLabel(new GuiNpcLabel(4, "quest.questlogtext", guiLeft + 4, guiTop + 51, 0x404040));
		addButton(new GuiNpcButton(4, guiLeft + 120, guiTop + 46, 50, 20, "selectServer.edit"));

		addLabel(new GuiNpcLabel(5, "quest.reward", guiLeft + 4, guiTop + 72, 0x404040));
    	addButton(new GuiNpcButton(5, guiLeft + 120, guiTop + 67, 50, 20, "selectServer.edit"));

		addLabel(new GuiNpcLabel(6, "gui.type", guiLeft + 4, guiTop + 93, 0x404040));
    	addButton(new GuiNpcButton(6, guiLeft + 90, guiTop + 88, 70, 20, new String[]{"quest.item","quest.dialog","quest.kill","quest.location"},quest.type.ordinal()));
    	addButton(new GuiNpcButton(7, guiLeft + 162, guiTop + 88,50, 20, "selectServer.edit"));


		addLabel(new GuiNpcLabel(8, "quest.repeatable", guiLeft + 4, guiTop + 114, 0x404040));
    	this.addButton(new GuiNpcButton(8, guiLeft + 110, guiTop + 109,70, 20, new String[]{"gui.no","gui.yes","quest.daily","quest.weekly"},quest.repeat.ordinal()));

    	this.addButton(new GuiNpcButton(9, guiLeft + 4, guiTop + 131,90, 20, new String[]{"quest.npc","quest.instant"},quest.completion.ordinal()));

    	if(quest.completerNpc.isEmpty())
    		quest.completerNpc = npc.display.name;
    	
		this.addTextField(new GuiNpcTextField(2,this, this.fontRenderer, guiLeft + 96, guiTop + 131, 114, 20, quest.completerNpc));
		this.getTextField(2).enabled = quest.completion == EnumQuestCompletion.Npc;

		addLabel(new GuiNpcLabel(10, "faction.options", guiLeft + 4, guiTop + 157, 0x404040));
		addButton(new GuiNpcButton(10, guiLeft + 120, guiTop + 152, 50, 20, "selectServer.edit"));

		addButton(new GuiNpcButton(13, guiLeft + 4, guiTop + 173, 164, 20, "mailbox.setup"));
		addButton(new GuiNpcButton(14, guiLeft + 170, guiTop + 173, 20, 20, "X"));
		if(!quest.mail.subject.isEmpty())
			getButton(13).displayString = quest.mail.subject;
		
		addButton(new GuiNpcButton(11, guiLeft + 4, guiTop + 194, 164, 20, "quest.next"));
		addButton(new GuiNpcButton(12, guiLeft + 170, guiTop + 194, 20, 20, "X"));
		if(!quest.nextQuestTitle.isEmpty())
			getButton(11).displayString = quest.nextQuestTitle;
		
	}

	private void categoryGuiInit() {
        addTextField(new GuiNpcTextField(0,this, this.fontRenderer, guiLeft+8, guiTop + 8, 160, 16, category.title));
        getTextField(0).setMaxStringLength(20);
	}

	public void buttonEvent(GuiButton guibutton)
    {
		GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0)
        {
        	save();
        	if(categorySelection){
        		if(category.id < 0)
        			return;
            	quest = new Quest();
        		NoppesUtil.sendData(EnumPacketType.QuestsGet, category.id);
        	}
        	else if(!categorySelection){
            	quest = new Quest();
            	category = new QuestCategory();
        		NoppesUtil.sendData(EnumPacketType.QuestCategoriesGet);
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
        		QuestCategory category = new QuestCategory();
        		category.title = name;
        		NoppesUtil.sendData(EnumPacketType.QuestCategorySave, category.writeNBT(new NBTTagCompound()));
        	}
        	else{
        		Quest quest = new Quest();
        		quest.title = name;
        		NoppesUtil.sendData(EnumPacketType.QuestSave, category.id, quest.writeToNBT(new NBTTagCompound()));
        	}
        }
        if(guibutton.id == 2)
        {
        	if(data.containsKey(scroll.getSelected())) {
				if(categorySelection){
					NoppesUtil.sendData(EnumPacketType.QuestCategoryRemove, category.id);
					category = new QuestCategory();
				}
				else{
					NoppesUtil.sendData(EnumPacketType.QuestRemove, quest.id);
					quest = new Quest();
				}
        		scroll.clear();
        	}
        }
        if(guibutton.id == 3 && quest.id >= 0){
        	questlogTA = false;
        	setSubGui(new SubGuiNpcTextArea(quest.completeText));
        }
        if(guibutton.id == 4 && quest.id >= 0){
        	questlogTA = true;
        	setSubGui(new SubGuiNpcTextArea(quest.logText));
        }
        if(guibutton.id == 5 && quest.id >= 0){
        	NoppesUtil.sendData(EnumPacketType.QuestOpenGui, EnumGuiType.QuestReward, quest.writeToNBT(new NBTTagCompound()));
        }
        if(guibutton.id == 6 && quest.id >= 0){
        	quest.setType(EnumQuestType.values()[button.getValue()]);
        }
        if(guibutton.id == 7)
        {
        	if(quest.type == EnumQuestType.Item)
            	NoppesUtil.sendData(EnumPacketType.QuestOpenGui, EnumGuiType.QuestItem, quest.writeToNBT(new NBTTagCompound()));
        	
        	if(quest.type == EnumQuestType.Dialog)
        		NoppesUtil.openGUI(player,  new GuiNpcQuestTypeDialog(npc, quest,this)); 
        	
        	if(quest.type == EnumQuestType.Kill)
        		NoppesUtil.openGUI(player,  new GuiNpcQuestTypeKill(npc, quest,this)); 
        	
        	if(quest.type == EnumQuestType.Location)
        		NoppesUtil.openGUI(player,  new GuiNpcQuestTypeLocation(npc, quest,this)); 
        }
        if(guibutton.id == 8)
        {
        	quest.repeat = EnumQuestRepeat.values()[button.getValue()];
        }
        if(guibutton.id == 9)
        {	
        	quest.completion = EnumQuestCompletion.values()[button.getValue()];
    		this.getTextField(2).enabled = quest.completion == EnumQuestCompletion.Npc;
        }
        if(guibutton.id == 10)
        {	
        	setSubGui(new SubGuiNpcFactionOptions(quest.factionOptions));
        }
        
        if(guibutton.id == 11 && quest.id >= 0)
        {
			NoppesUtil.openGUI(player, new GuiNPCQuestSelection(npc, this, quest.nextQuestid));
        }
        
        if(guibutton.id == 12 && quest.id >= 0)
        {
        	quest.nextQuestid = -1;
        	initGui();
        }
    	if(guibutton.id == 13){
			setSubGui(new SubGuiMailmanSendSetup(quest.mail, this));
    	}
        if(guibutton.id == 14)
        {
        	quest.mail = new PlayerMail();
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
			if(quest.id < 0)
				guiNpcTextField.setText("");
			else{
				String name = guiNpcTextField.getText();
				if(name.isEmpty() || data.containsKey(name)){
					guiNpcTextField.setText(quest.title);
				}
				else if(!categorySelection && quest.id >= 0){
					String old = quest.title;
					data.remove(quest.title);
					quest.title = name;
					data.put(quest.title, quest.id);
					scroll.replace(old,quest.title);
				}
			}
		}
		if(guiNpcTextField.id == 2){
	    	quest.completerNpc = guiNpcTextField.getText();
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
			quest.readNBT(compound);
			setSelected(quest.title);
			initGui();
		}
	}

	@Override
	public void subGuiClosed(SubGuiInterface subgui){
		if(subgui instanceof SubGuiNpcTextArea){
			SubGuiNpcTextArea gui = (SubGuiNpcTextArea) subgui;
			if(questlogTA)
				quest.logText = gui.text;
			else
				quest.completeText = gui.text;
		}
		else
			initGui();
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
		quest.nextQuestid = ob;
	}
	
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			save();
			String selected = scroll.getSelected();
			if(categorySelection){
				category = new QuestCategory();
				NoppesUtil.sendData(EnumPacketType.QuestCategoryGet, data.get(selected));
			}
			else{
				quest = new Quest();
				NoppesUtil.sendData(EnumPacketType.QuestGet, data.get(selected));
			}
			
		}
	}
	
	@Override
	public void close(){
		super.close();
		quest = new Quest();
	}

	@Override
	public void save() {
    	GuiNpcTextField.unfocus();
		if(!categorySelection && quest.id >= 0)
			NoppesUtil.sendData(EnumPacketType.QuestSave, category.id, quest.writeToNBT(new NBTTagCompound()));
		else if(categorySelection && category.id >= 0)
			NoppesUtil.sendData(EnumPacketType.QuestCategorySave, category.writeNBT(new NBTTagCompound()));
	}

}

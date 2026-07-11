package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCQuestSelection;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumAvailabilityDialog;
import noppes.npcs.constants.EnumAvailabilityFactionType;
import noppes.npcs.constants.EnumAvailabilityQuest;
import noppes.npcs.constants.EnumDayTime;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.Availability;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.Quest;

public class SubGuiNpcAvailability extends SubGuiInterface implements ITextfieldListener, GuiSelectionListener, IGuiData 
{
	private Availability availabitily;
	private boolean selectDialog = false;
	private boolean selectFaction = false;
	private int slot = 0;
	public GuiScreen parent2;
	
    public SubGuiNpcAvailability(Availability availabitily)
    {
    	this.availabitily = availabitily;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
        addLabel(new GuiNpcLabel(1,"availability.available", guiLeft, guiTop + 4, 0x404040));
        getLabel(1).center(xSize);

        this.addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 12, 50, 20, new String[]{"availability.always","availability.after","availability.before"},availabitily.dialogAvailable.ordinal()));
    	this.addButton(new GuiNpcButton(1, guiLeft + 56, guiTop + 12, 172, 20, "availability.selectdialog"));
    	getButton(1).enabled = availabitily.dialogAvailable != EnumAvailabilityDialog.Always;
    	this.addButton(new GuiNpcButton(4, guiLeft + 230, guiTop + 12,20, 20, "X"));
        
    	this.addButton(new GuiNpcButton(10, guiLeft + 4, guiTop + 34, 50, 20, new String[]{"availability.always","availability.after","availability.before"},availabitily.dialog2Available.ordinal()));
    	this.addButton(new GuiNpcButton(11, guiLeft + 56, guiTop + 34, 172, 20, "availability.selectdialog"));
    	getButton(11).enabled = availabitily.dialog2Available != EnumAvailabilityDialog.Always;
    	this.addButton(new GuiNpcButton(14, guiLeft + 230, guiTop + 34, 20, 20, "X"));
        
    	this.addButton(new GuiNpcButton(2, guiLeft + 4, guiTop + 58, 50, 20, new String[]{"availability.always","availability.after","availability.before","availability.whenactive", "availability.whennotactive"},availabitily.questAvailable.ordinal()));
    	this.addButton(new GuiNpcButton(3, guiLeft + 56, guiTop + 58, 172, 20, "availability.selectquest"));
    	getButton(3).enabled = availabitily.questAvailable != EnumAvailabilityQuest.Always;
    	this.addButton(new GuiNpcButton(5, guiLeft + 230, guiTop + 58, 20, 20, "X"));
        
    	this.addButton(new GuiNpcButton(12, guiLeft + 4, guiTop + 80,50, 20, new String[]{"availability.always","availability.after","availability.before","availability.whenactive", "availability.whennotactive"},availabitily.quest2Available.ordinal()));
    	this.addButton(new GuiNpcButton(13, guiLeft + 56, guiTop + 80,172, 20, "availability.selectquest"));
    	getButton(13).enabled = availabitily.quest2Available != EnumAvailabilityQuest.Always;
    	this.addButton(new GuiNpcButton(15, guiLeft + 230, guiTop + 80,20, 20, "X"));

        this.addButton(new GuiNpcButton(20, guiLeft + 4, guiTop + 104, 50, 20, new String[]{"availability.always","availability.is","availability.isnot"},availabitily.factionAvailable.ordinal()));
        this.addButton(new GuiNpcButton(22, guiLeft + 56, guiTop + 104, 60, 20, new String[]{"faction.friendly","faction.neutral","faction.unfriendly"},availabitily.factionStance.ordinal()));
    	this.addButton(new GuiNpcButton(21, guiLeft + 118, guiTop + 104, 110, 20, "availability.selectfaction"));
    	getButton(21).enabled = availabitily.factionAvailable != EnumAvailabilityFactionType.Always;
    	getButton(22).enabled = availabitily.factionAvailable != EnumAvailabilityFactionType.Always;
    	this.addButton(new GuiNpcButton(23, guiLeft + 230, guiTop + 104,20, 20, "X"));
    	
        this.addButton(new GuiNpcButton(24, guiLeft + 4, guiTop + 126, 50, 20, new String[]{"availability.always","availability.is","availability.isnot"},availabitily.faction2Available.ordinal()));
        this.addButton(new GuiNpcButton(27, guiLeft + 56, guiTop + 126, 60, 20, new String[]{"faction.friendly","faction.neutral","faction.unfriendly"},availabitily.faction2Stance.ordinal()));
    	this.addButton(new GuiNpcButton(25, guiLeft + 118, guiTop + 126, 110, 20, "availability.selectfaction"));
    	getButton(25).enabled = availabitily.faction2Available != EnumAvailabilityFactionType.Always;
    	getButton(27).enabled = availabitily.faction2Available != EnumAvailabilityFactionType.Always;
    	this.addButton(new GuiNpcButton(26, guiLeft + 230, guiTop + 126,20, 20, "X"));

        addLabel(new GuiNpcLabel(50,"availability.daytime", guiLeft + 4 , guiTop + 153, 0x404040));
    	this.addButton(new GuiNpcButton(50, guiLeft + 50, guiTop + 148,150, 20, new String[]{"availability.wholeday","availability.night","availability.day"},availabitily.daytime.ordinal()));

        addLabel(new GuiNpcLabel(51,"availability.minlevel", guiLeft + 4 , guiTop + 175, 0x404040));
    	this.addTextField(new GuiNpcTextField(51, this, fontRenderer, guiLeft + 50, guiTop + 170,90, 20, availabitily.minPlayerLevel + ""));
    	this.getTextField(51).numbersOnly = true;
    	this.getTextField(51).setMinMaxDefault(0, 400, 0);
    	
    	this.addButton(new GuiNpcButton(66, guiLeft + 82, guiTop + 192,98, 20, "gui.done"));
    	
    	updateGuiButtons();
    }


    private void updateGuiButtons() {
		getButton(1).setDisplayText("availability.selectdialog");
		getButton(11).setDisplayText("availability.selectdialog");
		getButton(3).setDisplayText("availability.selectquest");
		getButton(13).setDisplayText("availability.selectquest");
		if(availabitily.dialogId >= 0){
			NoppesUtil.sendData(EnumPacketType.DialogGet, availabitily.dialogId);
		}
		if(availabitily.dialog2Id >= 0){
			NoppesUtil.sendData(EnumPacketType.DialogGet, availabitily.dialog2Id);
		}
		if(availabitily.questId >= 0){
			NoppesUtil.sendData(EnumPacketType.QuestGet, availabitily.questId);
		}
		if(availabitily.quest2Id >= 0){
			NoppesUtil.sendData(EnumPacketType.QuestGet, availabitily.quest2Id);
		}

		if(availabitily.factionId >= 0){
			NoppesUtil.sendData(EnumPacketType.FactionGet, availabitily.factionId);
		}
		if(availabitily.faction2Id >= 0){
			NoppesUtil.sendData(EnumPacketType.FactionGet, availabitily.faction2Id);
		}
	}

	protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;

        if(guibutton.id == 0)
        {
        	availabitily.setDialogAvailability(button.getValue());
        	if(availabitily.dialogAvailable == EnumAvailabilityDialog.Always)
        		availabitily.dialogId = -1;
        	initGui();
        }
        if(guibutton.id == 1)
        {
        	slot = 1;
        	selectDialog = true;
        	GuiNPCDialogSelection gui = new GuiNPCDialogSelection(npc, parent, availabitily.dialogId);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 2)
        {
        	availabitily.setQuestAvailability(button.getValue());
        	if(availabitily.questAvailable == EnumAvailabilityQuest.Always)
        		availabitily.questId = -1;
        	initGui();
        }
        if(guibutton.id == 3)
        {
        	slot = 1;
        	GuiNPCQuestSelection gui = new GuiNPCQuestSelection(npc, parent, availabitily.questId);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 4)
        {
        	availabitily.dialogId = -1;
    		getButton(1).setDisplayText("availability.selectdialog");
        }
        if(guibutton.id == 5)
        {
        	availabitily.questId = -1;
    		getButton(3).setDisplayText("availability.selectquest");
        }
        if(guibutton.id == 10)
        {
        	availabitily.setDialog2Availability(button.getValue());
        	if(availabitily.dialog2Available == EnumAvailabilityDialog.Always)
        		availabitily.dialog2Id = -1;
        	initGui();
        }
        if(guibutton.id == 11)
        {
        	slot = 2;
        	selectDialog = true;
        	GuiNPCDialogSelection gui = new GuiNPCDialogSelection(npc, parent, availabitily.dialog2Id);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 12)
        {
        	availabitily.setQuest2Availability(button.getValue());
        	if(availabitily.quest2Available == EnumAvailabilityQuest.Always)
        		availabitily.quest2Id = -1;
        	initGui();
        }
        if(guibutton.id == 13)
        {
        	slot = 2;
        	GuiNPCQuestSelection gui = new GuiNPCQuestSelection(npc, parent, availabitily.quest2Id);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 14)
        {
        	availabitily.dialog2Id = -1;
    		getButton(11).setDisplayText("availability.selectdialog");
        }
        if(guibutton.id == 15)
        {
        	availabitily.quest2Id = -1;
    		getButton(13).setDisplayText("availability.selectquest");
        }
        if(guibutton.id == 20)
        {
        	availabitily.setFactionAvailability(button.getValue());
        	if(availabitily.factionAvailable == EnumAvailabilityFactionType.Always)
        		availabitily.factionId = -1;
        	initGui();
        }
        if(guibutton.id == 24)
        {
        	availabitily.setFaction2Availability(button.getValue());
        	if(availabitily.faction2Available == EnumAvailabilityFactionType.Always)
        		availabitily.faction2Id = -1;
        	initGui();
        }
        if(guibutton.id == 21)
        {
        	selectFaction = true;
        	slot = 1;
        	GuiNPCFactionSelection gui = new GuiNPCFactionSelection(npc, parent, availabitily.factionId);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 25)
        {
        	selectFaction = true;
        	slot = 2;
        	GuiNPCFactionSelection gui = new GuiNPCFactionSelection(npc, parent, availabitily.faction2Id);
        	gui.listener = this;
        	NoppesUtil.openGUI(player, gui);
        }
        if(guibutton.id == 22)
        {
        	availabitily.setFactionAvailabilityStance(button.getValue());
        }

        if(guibutton.id == 27)
        {
        	availabitily.setFaction2AvailabilityStance(button.getValue());
        }
        if(guibutton.id == 23)
        {
        	availabitily.factionId = -1;
    		getButton(21).setDisplayText("availability.selectfaction");
        }
        if(guibutton.id == 26)
        {
        	availabitily.faction2Id = -1;
    		getButton(25).setDisplayText("availability.selectfaction");
        }
        if(guibutton.id == 50)
        {
        	availabitily.daytime = EnumDayTime.values()[button.getValue()];
        }
        if(guibutton.id == 66)
        {
    		close();
        	if(parent2 != null)
        		NoppesUtil.openGUI(player, parent2);
        }
    }

	@Override
	public void selected(int id) {
		if(selectDialog){
			if(slot == 1)
				availabitily.dialogId = id;
			if(slot == 2)
				availabitily.dialog2Id = id;
		}
		else if(selectFaction){
			if(slot == 1)
				availabitily.factionId = id;
			if(slot == 2)
				availabitily.faction2Id = id;
		}
		else{
			if(slot == 1)
				availabitily.questId = id;
			if(slot == 2)
				availabitily.quest2Id = id;
		}
		selectDialog = false;
		selectFaction = false;
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		if(compound.hasKey("DialogId")){
			Dialog dialog = new Dialog();
			dialog.readNBT(compound);
			if(availabitily.dialogId == dialog.id)
				getButton(1).displayString = dialog.title;
			if(availabitily.dialog2Id == dialog.id)
				getButton(11).displayString = dialog.title;
		}
		else if(compound.hasKey("Slot")){
			Faction faction = new Faction();
			faction.readNBT(compound);
			if(availabitily.factionId == faction.id)
				getButton(21).displayString = faction.name;
			if(availabitily.faction2Id == faction.id)
				getButton(25).displayString = faction.name;
		}
		else{
			Quest quest = new Quest();
			quest.readNBT(compound);
			if(availabitily.questId == quest.id)
				getButton(3).displayString = quest.title;
			if(availabitily.quest2Id == quest.id)
				getButton(13).displayString = quest.title;
		}
	}

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 51)
			availabitily.minPlayerLevel = textfield.getInteger();
	}

}

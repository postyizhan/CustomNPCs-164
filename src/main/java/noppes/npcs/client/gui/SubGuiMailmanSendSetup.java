package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCQuestSelection;
import noppes.npcs.client.gui.player.GuiMailmanWrite;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.GuiSelectionListener;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.PlayerMail;

public class SubGuiMailmanSendSetup extends SubGuiInterface implements ITextfieldListener, GuiSelectionListener{
	
	private PlayerMail mail;
	private GuiNPCQuestSelection questSelection;
	
	public SubGuiMailmanSendSetup(PlayerMail mail, GuiScreen parent){
		this.parent = parent;
        xSize = 256;
        setBackground("menubg.png");
		this.mail = mail;
	}

	@Override
	public void initGui(){
		super.initGui();
		addLabel(new GuiNpcLabel(1, "mailbox.subject", guiLeft + 4, guiTop + 19, 0x404040));
		addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft + 60, guiTop + 14, 180, 20, mail.subject));
		addLabel(new GuiNpcLabel(0, "mailbox.sender", guiLeft + 4, guiTop + 41, 0x404040));
		addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 60, guiTop + 36, 180, 20, mail.sender));

		addButton(new GuiNpcButton(2, guiLeft + 29, guiTop + 100, "mailbox.write"));

		
		addLabel(new GuiNpcLabel(3, "quest.quest", guiLeft + 13, guiTop + 135, 0x404040));
		String title = mail.questTitle;
		if(title.isEmpty())
			title = "gui.select";
		addButton(new GuiNpcButton(3, guiLeft + 70, guiTop + 130, 100, 20, title));
		addButton(new GuiNpcButton(4, guiLeft + 171, guiTop + 130, 20, 20, "X"));
		
		addButton(new GuiNpcButton(0, guiLeft + 26, guiTop + 190, 100, 20, "gui.done"));
		addButton(new GuiNpcButton(1, guiLeft + 130, guiTop + 190, 100, 20, "gui.cancel"));
	}

	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 0){
			close();
		}
		if(guibutton.id == 1){
			mail.questId = -1;
			mail.questTitle = "";
			mail.message = new NBTTagCompound();
			close();
		}
		if(guibutton.id == 2){
			this.mc.displayGuiScreen(new GuiMailmanWrite(parent, mail.message, true));
		}
    	if(guibutton.id == 3){
			NoppesUtil.openGUI(player, questSelection = new GuiNPCQuestSelection(npc, parent, mail.questId));
			questSelection.listener = this;
    	}
    	if(guibutton.id == 4){
    		mail.questId = -1;
    		mail.questTitle = "";
    		initGui();
    	}
	}

	@Override
	public void selected(int ob) {
		mail.questId = ob;
		mail.questTitle = questSelection.getSelected();
		initGui();
	}
	@Override
	public void save() {
	}

	@Override
	public void unFocused(GuiNpcTextField textField) {
		if(textField.id == 0)
			mail.sender = textField.getText();
		if(textField.id == 1)
			mail.subject = textField.getText();
	}
}

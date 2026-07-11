package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiClose;
import noppes.npcs.client.gui.util.IGuiError;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.PlayerMail;

public class GuiMailmanSend extends GuiNPCInterface implements ITextfieldListener, IGuiError, IGuiClose{
	
	private GuiNpcLabel error;
	private PlayerMail mail;
	private String username = "";
	private NBTTagCompound compound = new NBTTagCompound();
	
	public GuiMailmanSend(){
        xSize = 256;
        setBackground("menubg.png");
		mail = new PlayerMail();
	}

	@Override
	public void initGui(){
		super.initGui();

		addLabel(new GuiNpcLabel(0, "mailbox.username", guiLeft + 4, guiTop + 19, 0x404040));
		addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 60, guiTop + 14, 180, 20, username));

		addLabel(new GuiNpcLabel(1, "mailbox.subject", guiLeft + 4, guiTop + 49, 0x404040));
		addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft + 60, guiTop + 44, 180, 20, mail.subject));

		addButton(new GuiNpcButton(2, guiLeft + 29, guiTop + 100, "mailbox.write"));
		//addButton(new GuiNpcButton(3, guiLeft + 29, guiTop + 130, "Include items"));

		addLabel(error = new GuiNpcLabel(2, "", guiLeft + 4, guiTop + 160, 0xFF0000));
		
		addButton(new GuiNpcButton(0, guiLeft + 26, guiTop + 190, 100, 20, "mailbox.send"));
		addButton(new GuiNpcButton(1, guiLeft + 130, guiTop + 190, 100, 20, "gui.cancel"));
	}

	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 0){
			mail.sender = player.username;
			mail.message = compound;
			
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailSend, this.username, mail.writeNBT());
		}
		if(guibutton.id == 1){
			this.mc.displayGuiScreen(null);
		}
		if(guibutton.id == 2){
			this.mc.displayGuiScreen(new GuiMailmanWrite(this, compound, true));
		}
	}
	@Override
	public void save() {
	}

	@Override
	public void unFocused(GuiNpcTextField textField) {
		if(textField.id == 0)
			username = textField.getText();
		if(textField.id == 1)
			mail.subject = textField.getText();
	}

	@Override
	public void setError(int i, NBTTagCompound data) {
		if(i == 0)
			error.label = StatCollector.translateToLocal("mailbox.errorUsername");
		if(i == 1)
			error.label = StatCollector.translateToLocal("mailbox.errorSubject");
	}

	@Override
	public void setClose(int i, NBTTagCompound data) {
		player.addChatMessage(StatCollector.translateToLocalFormatted("mailbox.succes", data.getString("username")));
	}
}

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.controllers.CloneFolder;

public class SubGuiCloneFolderName extends SubGuiInterface {
	private String initialName;
	public String folderName = null;

	public SubGuiCloneFolderName(String initialName){
		this.initialName = initialName;
		setBackground("menubg.png");
		xSize = 200;
		ySize = 90;
		closeOnEsc = true;
	}

	@Override
	public void initGui(){
		super.initGui();
		addLabel(new GuiNpcLabel(0, initialName.isEmpty() ? "cloner.newfolder" : "cloner.renamefolder", guiLeft + 5, guiTop + 6));
		addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 5, guiTop + 20, 190, 20, initialName));
		getTextField(0).setMaxStringLength(32);
		addButton(new GuiNpcButton(66, guiLeft + 5, guiTop + 55, 90, 20, "gui.done"));
		addButton(new GuiNpcButton(67, guiLeft + 105, guiTop + 55, 90, 20, "gui.cancel"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton){
		if(guibutton.id == 66){
			String name = getTextField(0).getText();
			if(CloneFolder.isValidName(name)){
				folderName = name;
				close();
			}
			else
				getTextField(0).setTextColor(0xFF0000);
		}
		if(guibutton.id == 67){
			close();
		}
	}
}

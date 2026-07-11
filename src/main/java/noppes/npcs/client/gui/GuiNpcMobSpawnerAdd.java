package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import noppes.npcs.client.controllers.CloneController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;

public class GuiNpcMobSpawnerAdd extends GuiNPCInterface{
	
	private Entity toClone;

	public GuiNpcMobSpawnerAdd(){
		this.toClone = CloneController.toClone;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		CloneController.toClone = null;
		
	}
	@Override
	public void initGui(){
		super.initGui();
		String name = toClone.getEntityName();
		addLabel(new GuiNpcLabel(0, "Save as", guiLeft + 4, guiTop + 6, 0x404040));
		addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 4, guiTop + 18, 200, 20, name));
		

		addLabel(new GuiNpcLabel(1, "Tab", guiLeft + 10, guiTop + 50, 0x404040));
		addButton(new GuiNpcButton(2, guiLeft + 40, guiTop + 45, 20, 20, new String[]{"1","2","3","4","5","6","7","8","9"}, 0));

		addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 70, 80, 20, "gui.save"));
		addButton(new GuiNpcButton(1, guiLeft + 86, guiTop + 70, 80, 20, "gui.cancel"));
	}
	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 0){
			String name = getTextField(0).getText();
			if(name.isEmpty())
				return;
			CloneController.addClone(toClone,name,getButton(2).getValue() + 1);
			close();
		}
		if(guibutton.id == 1){
			close();
		}
	}
	@Override
	public void save() {
		
	}

}

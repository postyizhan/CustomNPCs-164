package noppes.npcs.client.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import noppes.npcs.client.controllers.CloneController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class GuiNpcMobSpawnerAdd extends GuiNPCInterface implements ISubGuiListener{
	
	private Entity toClone;
	private String cloneName;
	private String activeFolder = "Default";
	private String[] folders;

	public GuiNpcMobSpawnerAdd(){
		this.toClone = CloneController.toClone;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		CloneController.toClone = null;
		cloneName = toClone.getEntityName();
		
	}
	@Override
	public void initGui(){
		super.initGui();
		addLabel(new GuiNpcLabel(0, "Save as", guiLeft + 4, guiTop + 6, 0x404040));
		addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 4, guiTop + 18, 200, 20, cloneName));
		
		ArrayList<String> folderList = CloneController.getFolders();
		if(!folderList.contains(activeFolder))
			folderList.add(activeFolder);
		folders = folderList.toArray(new String[folderList.size()]);
		addLabel(new GuiNpcLabel(1, "cloner.folder", guiLeft + 4, guiTop + 50, 0x404040));
		addButton(new GuiNpcButton(2, guiLeft + 45, guiTop + 45, 120, 20, folders, folderList.indexOf(activeFolder)));
		addButton(new GuiNpcButton(3, guiLeft + 168, guiTop + 45, 84, 20, "cloner.newfolder"));

		addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 70, 80, 20, "gui.save"));
		addButton(new GuiNpcButton(1, guiLeft + 86, guiTop + 70, 80, 20, "gui.cancel"));
	}
	@Override
	public void keyTyped(char c, int i){
		if(hasSubGui()){
			String currentName = getTextField(0).getText();
			super.keyTyped(c, i);
			getTextField(0).setText(currentName);
			return;
		}
		super.keyTyped(c, i);
	}
	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 0){
			cloneName = getTextField(0).getText();
			if(cloneName.isEmpty())
				return;
			CloneController.addClone(toClone,cloneName,activeFolder);
			close();
		}
		if(guibutton.id == 1){
			close();
		}
		if(guibutton.id == 2){
			activeFolder = folders[getButton(2).getValue()];
		}
		if(guibutton.id == 3){
			cloneName = getTextField(0).getText();
			activeFolder = folders[getButton(2).getValue()];
			setSubGui(new SubGuiCloneFolderName(""));
		}
	}
	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		if(subgui instanceof SubGuiCloneFolderName){
			SubGuiCloneFolderName folderGui = (SubGuiCloneFolderName) subgui;
			if(folderGui.folderName != null){
				activeFolder = folderGui.folderName;
				initGui(); // 重建界面，folders 数组会包含新文件夹
			}
		}
	}
	@Override
	public void save() {
		
	}

}

package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.DataDisplay;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCTextures;
import noppes.npcs.client.gui.GuiNpcModelSelection;
import noppes.npcs.client.gui.GuiNpcTextureCloaks;
import noppes.npcs.client.gui.GuiNpcTextureOverlays;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcDisplay extends GuiNPCInterface2 implements ITextfieldListener, IGuiData{

	private DataDisplay display;
	
	public GuiNpcDisplay(EntityNPCInterface npc) {
		super(npc,1);
		display = npc.display;
    	NoppesUtil.sendData(EnumPacketType.MainmenuDisplayGet);
	}

    public void initGui()
    {
        super.initGui();
        int y = guiTop + 4;
        addLabel(new GuiNpcLabel(0,"gui.name", guiLeft + 5, y + 5));
        addTextField(new GuiNpcTextField(0,this, fontRenderer, guiLeft + 50, y, 200, 20, display.name));
    	this.addButton(new GuiNpcButton(0, guiLeft + 253, y , 110, 20, new String[]{"display.show","display.hide","display.showAttacking"} ,display.showName));

    	y+=23;
        addLabel(new GuiNpcLabel(11,"gui.title", guiLeft + 5, y + 5));
        addTextField(new GuiNpcTextField(11,this, fontRenderer, guiLeft + 50, y, 200, 20, display.title));
        
    	y+=23;
        addLabel(new GuiNpcLabel(1,"display.model", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(1, guiLeft + 50, y,110,20, "selectServer.edit"));
    	addLabel(new GuiNpcLabel(2,"display.size", guiLeft + 175, y + 5));
        addTextField(new GuiNpcTextField(2, this, fontRenderer, guiLeft + 203, y, 40, 20, display.modelSize + ""));
        getTextField(2).numbersOnly = true;
        getTextField(2).setMinMaxDefault(1, 30, 5);
        addLabel(new GuiNpcLabel(3,"(1-30)", guiLeft + 246 , y + 5));

    	y+=23;
    	addLabel(new GuiNpcLabel(4,"display.texture", guiLeft + 5, y + 5));
        addTextField(new GuiNpcTextField(3,this, fontRenderer, guiLeft + 80, y, 200, 20, display.skinType == 0?display.texture:display.url));
    	this.addButton(new GuiNpcButton(3, guiLeft + 325, y, 38, 20, "mco.template.button.select"));
    	this.addButton(new GuiNpcButton(2, guiLeft + 283, y, 40, 20, new String[]{"display.texture","display.player", "display.url"},display.skinType));
    	getButton(3).enabled = display.skinType == 0;

    	y+=23;
    	addLabel(new GuiNpcLabel(8,"display.cape", guiLeft + 5, y + 5));
        addTextField(new GuiNpcTextField(8,this, fontRenderer, guiLeft + 80, y, 200, 20, display.cloakTexture));
    	this.addButton(new GuiNpcButton(8, guiLeft + 283, y, 80, 20, "display.selectTexture"));

    	y+=23;
    	addLabel(new GuiNpcLabel(9,"display.overlay", guiLeft + 5, y + 5));
        addTextField(new GuiNpcTextField(9,this, fontRenderer, guiLeft + 80, y, 200, 20, display.glowTexture));
    	this.addButton(new GuiNpcButton(9, guiLeft + 283, y, 80, 20, "display.selectTexture"));

    	y+=23;
    	addLabel(new GuiNpcLabel(5,"display.livingAnimation", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(5, guiLeft + 120, y, 50, 20, new String[]{"gui.yes","gui.no"},display.NoLivingAnimation?1:0));

    	y+=23;
    	addLabel(new GuiNpcLabel(6,"display.tint", guiLeft + 5, y + 5));
    	String color = Integer.toHexString(display.skinColor);
    	while(color.length() < 6)
    		color = "0" + color;
    	this.addTextField(new GuiNpcTextField(6, this, fontRenderer, guiLeft + 120, y, 40, 20, color));
    	getTextField(6).setTextColor(display.skinColor);

    	y+=23;
    	addLabel(new GuiNpcLabel(7,"display.visible", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(7, guiLeft + 120, y, 50, 20, new String[]{"gui.yes","gui.no","gui.partly"}, display.visible));
    
    }

	@Override
	public void unFocused(GuiNpcTextField textfield){
		if(textfield.id == 0){
			if(!textfield.isEmpty())
				display.name = textfield.getText();
			else
				textfield.setText(display.name);
		}
		else if(textfield.id == 2){
			display.modelSize = textfield.getInteger();
		}
		else if(textfield.id == 3){
			if(display.skinType == 2)
				display.url = textfield.getText();
			else if(display.skinType == 1)
				display.skinUsername = textfield.getText();
			else
				display.texture = textfield.getText();
		}
		else if(textfield.id == 6){
			int color = 0;
			try{
				color = Integer.parseInt(textfield.getText(),16);
			}
			catch(NumberFormatException e){
				color = 0xFFFFFF;
			}
	    	display.skinColor = color;
	    	textfield.setTextColor(display.skinColor);
		}
		else if(textfield.id == 8){
			display.cloakTexture = textfield.getText();
		}
		else if(textfield.id == 9){
			display.glowTexture = textfield.getText();
		}
		else if(textfield.id == 11){
			display.title = textfield.getText();
		}
	}
	protected void actionPerformed(GuiButton guibutton){
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if(button.id == 0){
			display.showName = button.getValue();
		}
		if(button.id == 1){
			NoppesUtil.openGUI(player, new GuiNpcModelSelection(npc, this));
		}
		if(button.id == 2){
			display.skinType = (byte) button.getValue();
			display.url = "";
			initGui();
		}
		else if(button.id == 3){
			NoppesUtil.openGUI(player, new GuiNPCTextures(npc, this));
		}
		else if(button.id == 5){
			display.NoLivingAnimation = button.getValue() == 1;
		}
		else if(button.id == 7){
			display.visible = button.getValue();
		}
		else if(button.id == 8){
			NoppesUtil.openGUI(player, new GuiNpcTextureCloaks(npc, this));
		}
		else if(button.id == 9){
			NoppesUtil.openGUI(player, new GuiNpcTextureOverlays(npc, this));
		}
    }

	@Override
	public void save() {
		mc.renderGlobal.onEntityDestroy(npc);
		mc.renderGlobal.onEntityCreate(npc);
		NoppesUtil.sendData(EnumPacketType.MainmenuDisplaySave, display.writeToNBT(new NBTTagCompound()));
		
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		display.readToNBT(compound);
		initGui();
	}

}

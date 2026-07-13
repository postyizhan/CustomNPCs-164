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

    	y+=23;
    	addLabel(new GuiNpcLabel(10,"display.hitbox", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(10, guiLeft + 120, y, 50, 20, new String[]{"gui.disabled","gui.enabled"}, display.hitboxData.isHitboxEnabled()?1:0));

    	y+=23;
    	addLabel(new GuiNpcLabel(11,"display.hitboxWidth", guiLeft + 5, y + 5));
    	this.addTextField(new GuiNpcTextField(10, this, fontRenderer, guiLeft + 120, y, 50, 20, String.format("%.2f", display.hitboxData.getWidthScale())));
    	getTextField(10).setEnabled(display.hitboxData.isHitboxEnabled());

    	y+=23;
    	addLabel(new GuiNpcLabel(12,"display.hitboxHeight", guiLeft + 5, y + 5));
    	this.addTextField(new GuiNpcTextField(11, this, fontRenderer, guiLeft + 120, y, 50, 20, String.format("%.2f", display.hitboxData.getHeightScale())));
    	getTextField(11).setEnabled(display.hitboxData.isHitboxEnabled());

    	y+=23;
    	addLabel(new GuiNpcLabel(13,"display.tintSystem", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(11, guiLeft + 120, y, 50, 20, new String[]{"gui.disabled","gui.enabled"}, display.tintData.isTintEnabled()?1:0));

    	y+=23;
    	addLabel(new GuiNpcLabel(14,"display.hurtTint", guiLeft + 5, y + 5));
    	this.addButton(new GuiNpcButton(12, guiLeft + 120, y, 50, 20, new String[]{"gui.disabled","gui.enabled"}, display.tintData.isHurtTintEnabled()?1:0));
    	String hurtColor = Integer.toHexString(display.tintData.getHurtTint());
    	while(hurtColor.length() < 6)
    		hurtColor = "0" + hurtColor;
    	this.addTextField(new GuiNpcTextField(13, this, fontRenderer, guiLeft + 175, y, 70, 20, hurtColor));
    	getTextField(13).setTextColor(display.tintData.getHurtTint());
    	getTextField(13).setEnabled(display.tintData.isTintEnabled());

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
		else if(textfield.id == 10){
			try {
				float scale = Float.parseFloat(textfield.getText());
				display.hitboxData.setWidthScale(scale);
			} catch(NumberFormatException e) {
				textfield.setText(String.format("%.2f", display.hitboxData.getWidthScale()));
			}
		}
		else if(textfield.id == 11){
			display.title = textfield.getText();
		}
		else if(textfield.id == 12){
			try {
				float scale = Float.parseFloat(textfield.getText());
				display.hitboxData.setHeightScale(scale);
			} catch(NumberFormatException e) {
				textfield.setText(String.format("%.2f", display.hitboxData.getHeightScale()));
			}
		}
		else if(textfield.id == 13){
			int color = 0;
			try{
				color = Integer.parseInt(textfield.getText(),16);
			}
			catch(NumberFormatException e){
				color = 0xff0000;
			}
			display.tintData.setHurtTint(color);
			textfield.setTextColor(display.tintData.getHurtTint());
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
		else if(button.id == 10){
			display.hitboxData.setHitboxEnabled(button.getValue() == 1);
			initGui();
		}
		else if(button.id == 11){
			display.tintData.setTintEnabled(button.getValue() == 1);
			initGui();
		}
		else if(button.id == 12){
			display.tintData.setHurtTintEnabled(button.getValue() == 1);
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

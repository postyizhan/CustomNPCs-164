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
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcDisplay extends GuiNPCInterface2 implements ITextfieldListener, IGuiData, ISubGuiListener{

	private DataDisplay display;
	private boolean advancedPage = false;
	
	public GuiNpcDisplay(EntityNPCInterface npc) {
		super(npc,1);
		display = npc.display;
        NoppesUtil.sendData(EnumPacketType.MainmenuDisplayGet);
	}

    public void initGui()
    {
        super.initGui();
        if(advancedPage)
            initAdvancedPage();
        else
            initDisplayPage();
    }

    private void initDisplayPage()
    {
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
        this.addButton(new GuiNpcButton(15, guiLeft + 185, y, 178, 20, "gui.settings"));

        y+=23;
        addLabel(new GuiNpcLabel(6,"display.tint", guiLeft + 5, y + 5));
        this.addTextField(new GuiNpcTextField(6, this, fontRenderer, guiLeft + 120, y, 40, 20, formatColor(display.skinColor)));
        getTextField(6).setTextColor(display.skinColor);

        y+=23;
        addLabel(new GuiNpcLabel(7,"display.visible", guiLeft + 5, y + 5));
        this.addButton(new GuiNpcButton(7, guiLeft + 120, y, 50, 20, new String[]{"gui.yes","gui.no","gui.partly"}, display.visible));
    }

    private void initAdvancedPage()
    {
        int y = guiTop + 4;
        int controlX = guiLeft + 120;

        addLabel(new GuiNpcLabel(20,"display.hitbox", guiLeft + 5, y + 5));
        this.addButton(new GuiNpcButton(10, controlX, y, 80, 20, new String[]{"gui.disabled","gui.enabled"}, display.hitboxData.isHitboxEnabled()?1:0));
        this.addButton(new GuiNpcButton(16, guiLeft + 303, y, 60, 20, "gui.done"));

        y += 23;
        addLabel(new GuiNpcLabel(21,"display.hitboxWidth", guiLeft + 5, y + 5));
        this.addTextField(new GuiNpcTextField(10, this, fontRenderer, controlX, y, 80, 20, String.format("%.2f", display.hitboxData.getWidthScale())));
        getTextField(10).setEnabled(display.hitboxData.isHitboxEnabled());

        y += 23;
        addLabel(new GuiNpcLabel(22,"display.hitboxHeight", guiLeft + 5, y + 5));
        this.addTextField(new GuiNpcTextField(12, this, fontRenderer, controlX, y, 80, 20, String.format("%.2f", display.hitboxData.getHeightScale())));
        getTextField(12).setEnabled(display.hitboxData.isHitboxEnabled());

        y += 23;
        addLabel(new GuiNpcLabel(23,"display.tintSystem", guiLeft + 5, y + 5));
        this.addButton(new GuiNpcButton(11, controlX, y, 80, 20, new String[]{"gui.disabled","gui.enabled"}, display.tintData.isTintEnabled()?1:0));

        y += 23;
        addLabel(new GuiNpcLabel(24,"display.hurtTint", guiLeft + 5, y + 5));
        this.addButton(new GuiNpcButton(12, controlX, y, 50, 20, new String[]{"gui.no","gui.yes"}, display.tintData.isHurtTintEnabled()?1:0));
        this.addTextField(new GuiNpcTextField(13, this, fontRenderer, controlX + 53, y, 120, 20, formatColor(display.tintData.getHurtTint())));
        getTextField(13).setTextColor(display.tintData.getHurtTint());
        getTextField(13).setEnabled(display.tintData.isTintEnabled());
        this.addButton(new GuiNpcButton(14, controlX + 176, y, 67, 20, "selectServer.edit"));
        getButton(14).enabled = display.tintData.isTintEnabled();

        y += 23;
        addLabel(new GuiNpcLabel(25,"display.skinOverlay", guiLeft + 5, y + 5));
        this.addButton(new GuiNpcButton(13, controlX, y, 80, 20, new String[]{"gui.disabled","gui.enabled"}, display.skinOverlays.isEnabled()?1:0));

        y += 23;
        addLabel(new GuiNpcLabel(26,"display.overlayPath", guiLeft + 5, y + 5));
        this.addTextField(new GuiNpcTextField(14, this, fontRenderer, controlX, y, 243, 20, display.skinOverlays.getOverlay().getTexture()));
        getTextField(14).setEnabled(display.skinOverlays.isEnabled());
    }

    private String formatColor(int color)
    {
        String value = Integer.toHexString(color & 0xffffff);
        while(value.length() < 6)
            value = "0" + value;
        return value;
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
				if(!Float.isNaN(scale) && !Float.isInfinite(scale))
					display.hitboxData.setWidthScale(scale);
			} catch(NumberFormatException e) {
			}
			textfield.setText(String.format("%.2f", display.hitboxData.getWidthScale()));
		}
		else if(textfield.id == 11){
			display.title = textfield.getText();
		}
		else if(textfield.id == 12){
			try {
				float scale = Float.parseFloat(textfield.getText());
				if(!Float.isNaN(scale) && !Float.isInfinite(scale))
					display.hitboxData.setHeightScale(scale);
			} catch(NumberFormatException e) {
			}
			textfield.setText(String.format("%.2f", display.hitboxData.getHeightScale()));
		}
		else if(textfield.id == 13){
			int color = 0;
			try{
				color = Integer.parseInt(textfield.getText(),16);
			}
			catch(NumberFormatException e){
				color = 0xff0000;
			}
			display.tintData.setHurtTint(color & 0xffffff);
			textfield.setText(formatColor(display.tintData.getHurtTint()));
			textfield.setTextColor(display.tintData.getHurtTint());
		}
		else if(textfield.id == 14){
			display.skinOverlays.getOverlay().setTexture(textfield.getText());
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
		else if(button.id == 13){
			display.skinOverlays.setEnabled(button.getValue() == 1);
			initGui();
		}
		else if(button.id == 14){
			setSubGui(new SubGuiColorSelector(display.tintData.getHurtTint()));
		}
		else if(button.id == 15){
			advancedPage = true;
			initGui();
		}
		else if(button.id == 16){
			advancedPage = false;
			initGui();
		}
    }

	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		if(subgui instanceof SubGuiColorSelector){
			SubGuiColorSelector colorSelector = (SubGuiColorSelector) subgui;
			display.tintData.setHurtTint(colorSelector.color);
			getTextField(13).setText(formatColor(colorSelector.color));
			getTextField(13).setTextColor(colorSelector.color);
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

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.DataAI;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumAnimation;
import noppes.npcs.constants.EnumMovingType;
import noppes.npcs.constants.EnumStandingType;

public class SubGuiNpcMovement extends SubGuiInterface implements ITextfieldListener 
{
	private DataAI ai;
	
    public SubGuiNpcMovement(DataAI ai){
    	this.ai = ai;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui(){
        super.initGui();
        this.addLabel(new GuiNpcLabel(0, "Moving type", guiLeft + 4, guiTop + 9, 0x404040));
    	this.addButton(new GuiNpcButton(0, guiLeft + 80, guiTop + 4, 100, 20, EnumMovingType.names(), ai.movingType.ordinal()));
    	
		if(ai.movingType == EnumMovingType.Wandering){    
	        addLabel(new GuiNpcLabel(4,"Walking range", guiLeft + 4, guiTop + 31, 0x404040));	
			addTextField(new GuiNpcTextField(4,this, fontRenderer, guiLeft + 100, guiTop + 26, 40, 20, ai.walkingRange + ""));
	    	getTextField(4).numbersOnly = true;
	        getTextField(4).setMinMaxDefault(0, 1000, 5);
		} 
		else if(ai.movingType == EnumMovingType.Standing){         
	        addLabel(new GuiNpcLabel(7,"Position Offset X:", guiLeft + 4, guiTop + 31, 0x404040));
        	addTextField(new GuiNpcTextField(7,this, fontRenderer, guiLeft + 99, guiTop + 26, 24, 20, (int)ai.bodyOffsetX + ""));
        	getTextField(7).numbersOnly = true;
	        getTextField(7).setMinMaxDefault(0, 10, 5);
	        addLabel(new GuiNpcLabel(8,"Y:", guiLeft + 125, guiTop + 31, 0x404040));
        	addTextField(new GuiNpcTextField(8,this, fontRenderer, guiLeft + 135, guiTop + 26, 24, 20, (int)ai.bodyOffsetY + ""));
        	getTextField(8).numbersOnly = true;
	        getTextField(8).setMinMaxDefault(0, 10, 5);
	        addLabel(new GuiNpcLabel(9,"Z:", guiLeft + 161, guiTop + 31, 0x404040));
        	addTextField(new GuiNpcTextField(9,this, fontRenderer, guiLeft + 171, guiTop + 26, 24, 20, (int)ai.bodyOffsetZ + ""));
        	getTextField(9).numbersOnly = true;
	        getTextField(9).setMinMaxDefault(0, 10, 5);  

			this.addLabel(new GuiNpcLabel(3, "Animation", guiLeft + 4, guiTop + 53, 0x404040));
	    	this.addButton(new GuiNpcButton(3, guiLeft + 80, guiTop + 48, 100, 20, new String[]{"Normal","Sitting","Lying","Sneaking","Dancing","Aiming"},ai.animationType.ordinal()));

			if(ai.standingType == EnumStandingType.NoRotation || ai.standingType == EnumStandingType.HeadRotation){
				addTextField(new GuiNpcTextField(5,this, fontRenderer, guiLeft + 165, guiTop + 70, 40, 20, ai.orientation + ""));
				getTextField(5).numbersOnly = true;
				getTextField(5).setMinMaxDefault(0, 359, 0);
				addLabel(new GuiNpcLabel(5,"(0-359)", guiLeft + 207, guiTop + 75, 0x404040));
			}
			if(ai.animationType != EnumAnimation.LYING){
				this.addLabel(new GuiNpcLabel(1, "Rotation", guiLeft + 4, guiTop + 75, 0x404040));
				this.addButton(new GuiNpcButton(4, guiLeft + 80, guiTop + 70, 80, 20, new String[]{"Body","Manual","Stalking","Head"},ai.standingType.ordinal()));
	        }
			else{
				this.addLabel(new GuiNpcLabel(6, "Lying Rotation", guiLeft + 4, guiTop + 75, 0x404040));
				addTextField(new GuiNpcTextField(5,this, fontRenderer, guiLeft + 99, guiTop + 70, 40, 20, ai.orientation + ""));
				getTextField(5).numbersOnly = true;
				getTextField(5).setMinMaxDefault(0, 359, 0);
				addLabel(new GuiNpcLabel(5,"(0-359)", guiLeft + 142, guiTop + 75, 0x404040));
			}
		}
		if(ai.movingType != EnumMovingType.Standing){
			this.addLabel(new GuiNpcLabel(12, "Animation", guiLeft + 4, guiTop + 53, 0x404040));
	    	this.addButton(new GuiNpcButton(12, guiLeft + 80, guiTop + 48, 100, 20, new String[]{"Normal","Sneaking","Aiming", "Dancing"},ai.animationType.getWalkingAnimation()));
		}
		
		if(ai.movingType == EnumMovingType.MovingPath){  
			this.addLabel(new GuiNpcLabel(8, "Movement", guiLeft + 4, guiTop + 31, 0x404040));
	    	this.addButton(new GuiNpcButton(8, guiLeft + 80, guiTop + 26, 80, 20, new String[]{"ai.looping","ai.backtracking"}, ai.movingPattern));
			this.addLabel(new GuiNpcLabel(9, "Pauses", guiLeft + 4, guiTop + 75, 0x404040));
	    	this.addButton(new GuiNpcButton(9, guiLeft + 80, guiTop + 70, 80, 20, new String[]{"gui.no","gui.yes"}, ai.movingPause?1:0));
		}
    	addButton(new GuiNpcButton(66, guiLeft + 190, guiTop + 190, 60, 20, "gui.done"));
    }

	protected void actionPerformed(GuiButton guibutton){
    	GuiNpcButton button = (GuiNpcButton) guibutton;
		if(button.id == 0){
			ai.movingType = EnumMovingType.values()[button.getValue()];
			if(ai.movingType != EnumMovingType.Standing){
				ai.animationType = EnumAnimation.NONE;
				ai.standingType = EnumStandingType.RotateBody;
				ai.bodyOffsetX = ai.bodyOffsetY = ai.bodyOffsetZ = 5;
			}
			initGui();
		}
		else if(button.id == 3){
			ai.animationType = EnumAnimation.values()[button.getValue()];				
			initGui();
		}
		else if(button.id == 4){
			ai.standingType = EnumStandingType.values()[button.getValue()];
			initGui();
		}
		else if (button.id == 8) {
			ai.movingPattern = button.getValue();
		}
		else if (button.id == 9) {
			ai.movingPause = button.getValue() == 1;
		}
		else if (button.id == 12) {
			if(button.getValue() == 0)
				ai.animationType = EnumAnimation.NONE;
			if(button.getValue() == 1)
				ai.animationType = EnumAnimation.SNEAKING;
			if(button.getValue() == 2)
				ai.animationType = EnumAnimation.Aiming;
			if(button.getValue() == 3)
				ai.animationType = EnumAnimation.DANCING;
		}
		else if(guibutton.id == 66){
        	close();
        }
    }

	@Override
	public void unFocused(GuiNpcTextField textfield) {
		if(textfield.id == 7){
			ai.bodyOffsetX = textfield.getInteger();
		}
		else if(textfield.id == 8){
			ai.bodyOffsetY = textfield.getInteger();
		}
		else if(textfield.id == 9){
			ai.bodyOffsetZ = textfield.getInteger();
		}
		else if(textfield.id == 5){
			ai.orientation = textfield.getInteger();
		}
		else if(textfield.id == 4){
			ai.walkingRange = textfield.getInteger();
		}
		else if(textfield.id == 6){
			ai.distanceToMelee = textfield.getInteger();
		}
	}

}

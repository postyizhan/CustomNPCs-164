// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.player;

import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.constants.EnumOptionType;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.Dialog;
import noppes.npcs.controllers.DialogOption;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiDialogInteract extends GuiNPCInterface
{
	private Dialog dialog;
	private ResourceLocation wheel;
	private ResourceLocation[] wheelparts;
	private ResourceLocation indicator;
	
    public GuiDialogInteract(EntityNPCInterface npc, Dialog dialog)
    {
    	super(npc);
    	this.dialog = dialog;
    	title = "";
    	wheel = this.getResource("wheel.png");
    	indicator = this.getResource("indicator.png");
    	wheelparts = new ResourceLocation[]{getResource("wheel1.png"),getResource("wheel2.png"),getResource("wheel3.png"),
    			getResource("wheel4.png"),getResource("wheel5.png"),getResource("wheel6.png")};
    	
    	if(dialog.sound != null && !dialog.sound.isEmpty())
    		MusicController.Instance.playStreaming(dialog.sound, (float)npc.posX, (float)npc.posY, (float)npc.posZ);
    }

    public void initGui()
    {
        super.initGui();
        this.mc.mouseHelper.grabMouseCursor();
    }

    private int selectedX = 0;
    private int selectedY = 0;
    private int selected = 0;
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        int yoffset = (this.height - 240)/2 ;
        drawString(this.fontRenderer, npc.getEntityName(), width/2 - fontRenderer.getStringWidth(npc.getEntityName())/2,yoffset+  1 , 0xe0e0e0);
        int x = 0;
        String text = NoppesStringUtils.formatText(dialog.text, player, npc);
        String line = "";
        for(char c : text.toCharArray()){
        	if(c == '\r' || c == '\n'){
                drawString(this.fontRenderer, line, width/2 + 4 - 95,yoffset+  11 + (x * fontRenderer.FONT_HEIGHT), 0xe0e0e0);
                line = "";
        		x++;
        		continue;
        	}
        	if(fontRenderer.getStringWidth(line + c) > (190 - 8)){
                drawString(fontRenderer, line, width/2 + 4 - 95,yoffset+  11 + (x * fontRenderer.FONT_HEIGHT), 0xe0e0e0);
                line = "";
        		x++;
        	}
    		line += c;
        }
        drawString(fontRenderer, line, width/2 + 4 - 95,yoffset+  11 + (x * fontRenderer.FONT_HEIGHT), 0xe0e0e0);
        if(dialog == null || !dialog.hasOtherOptions())
        	return;
        GL11.glColor4f(1, 1, 1, 1);
        mc.renderEngine.bindTexture(wheel);
        drawTexturedModalRect((width/2) - 31, yoffset+198, 0, 0, 63, 40);
        
        selectedX += Mouse.getDX();        
        selectedY += Mouse.getDY();
        int limit = 80;
        if(selectedX > limit)
        	selectedX = limit;
        if(selectedX < -limit)
        	selectedX = -limit;
        
        if(selectedY > limit)
        	selectedY = limit;
        if(selectedY < -limit)
        	selectedY = -limit;

        selected = 2;
    	if(selectedY < -20)
    		selected++;
    	if(selectedY > 54)
    		selected--;
    	
    	if(selectedX < 0)
    		selected += 3;
        mc.renderEngine.bindTexture(wheelparts[selected - 1]);
        drawTexturedModalRect((width/2) - 31,yoffset+ 198, 0, 0, 85, 55);
        for(int slot:dialog.options.keySet()){
        	DialogOption option = dialog.options.get(slot);
        	if(option == null || option.optionType == EnumOptionType.Disabled)
        		continue;
            int color = option.optionColor;            	
        	if(slot == (selected-1))
        		color = 0x838FD8;
    		//drawString(fontRenderer, option.title, width/2 -50 ,yoffset+ 162 + slot * 13 , color);
        	if(slot == 0)
        		drawString(fontRenderer, option.title, width/2 + 13,yoffset+ 192 , color);
        	if(slot == 1)
        		drawString(fontRenderer, option.title, width/2 + 33,yoffset+ 210  , color);
        	if(slot == 2)
        		drawString(fontRenderer, option.title, width/2 + 27,yoffset+ 230  , color);
        	if(slot == 3)
        		drawString(fontRenderer, option.title, width/2 - 13 - fontRenderer.getStringWidth(option.title),yoffset+ 192  , color);
        	if(slot == 4)
        		drawString(fontRenderer, option.title, width/2 - 33 - fontRenderer.getStringWidth(option.title),yoffset+ 210  , color);
        	if(slot == 5)
        		drawString(fontRenderer, option.title, width/2 - 27 - fontRenderer.getStringWidth(option.title),yoffset+ 230  , color);
        	
        }
        mc.renderEngine.bindTexture(indicator);
        drawTexturedModalRect(width/2 + selectedX/4  - 2,yoffset+ 214 - selectedY/6, 0, 0, 8, 8);
        //System.out.println(selectedX + ":" + selectedY);
        //fontRenderer.drawString(".", width/2 + selectedX/4 ,yoffset+ 210 - selectedY/6 , 0xFF0000);
    }
    
    private void setSelectedXY(){
    	if(selected == 1){
    		selectedX = 33;
    		selectedY = 72;
    	}
    	else if(selected == 2){
    		selectedX = 80;
    		selectedY = 31;
    	}
    	else if(selected == 3){
    		selectedX = 54;
    		selectedY = -45;
    	}
    	else if(selected == 4){
    		selectedX = -33;
    		selectedY = 72;
    	}
    	else if(selected == 5){
    		selectedX = -80;
    		selectedY = 31;
    	}
    	else if(selected == 6){
    		selectedX = -54;
    		selectedY = -45;
    	}
    }
    @Override
    public void keyTyped(char c, int i)
    {
    	if(i == mc.gameSettings.keyBindForward.keyCode || i == Keyboard.KEY_UP){
    		if(selected - 1 != 0 && selected -1 !=3){
    			selected--;
    			setSelectedXY();
    		}
    	}
    	if(i == mc.gameSettings.keyBindBack.keyCode || i == Keyboard.KEY_DOWN){
    		if(selected + 1 != 4 && selected +1 !=7){
        		selected++;
        		setSelectedXY();
    		}
    	}
    	if(i == mc.gameSettings.keyBindRight.keyCode || i == Keyboard.KEY_RIGHT){
    		if(selected - 3 > 0){
    			selected -= 3;
    			setSelectedXY();
    		}
    	}
    	if(i == mc.gameSettings.keyBindLeft.keyCode || i == Keyboard.KEY_LEFT){
    		if(selected + 3 < 7){
    			selected += 3;
    			setSelectedXY();
    		}
    	}
    	if(i == 28){
        	handleDialogSelection();
    	}
        if (i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
        	closed();
            close();
        }
        super.keyTyped(c, i);
    }
    public void mouseClicked(int i,int  j,int  k){
    	handleDialogSelection();
    }
    private void handleDialogSelection(){
		close();
    	if(dialog == null || !dialog.hasOtherOptions()){
    		closed();
        	return;
    	}
    	DialogOption option = dialog.options.get(selected-1);
    	if(option == null || option.optionType == EnumOptionType.QuitOption || option.optionType == EnumOptionType.Disabled){
    		closed();
    		return;
    	}
    	
    	NoppesUtilPlayer.sendData(EnumPlayerPacket.Dialog, dialog.id,selected-1);
        mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
    	
    }
    private void closed(){
    	NoppesUtilPlayer.sendData(EnumPlayerPacket.CheckQuestCompletion);
    }
    
	public void save() {
//    	this.mc.mouseHelper.ungrabMouseCursor();
//    	this.mc.setIngameFocus();
	}

}

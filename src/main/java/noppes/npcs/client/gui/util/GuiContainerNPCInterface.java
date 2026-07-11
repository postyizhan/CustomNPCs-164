// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.util;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;

import org.lwjgl.input.Keyboard;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public abstract class GuiContainerNPCInterface extends GuiContainer
{
	public EntityClientPlayerMP player;
	public EntityNPCInterface npc;
	private HashMap<Integer,GuiNpcButton> buttons = new HashMap<Integer,GuiNpcButton>();
	private HashMap<Integer,GuiNpcTextField> textfields = new HashMap<Integer,GuiNpcTextField>();
	private HashMap<Integer,GuiNpcLabel> labels = new HashMap<Integer,GuiNpcLabel>();
	private HashMap<Integer,GuiNpcSlider> sliders = new HashMap<Integer,GuiNpcSlider>();
	public String title;
	public boolean closeOnEsc = false;
    public GuiContainerNPCInterface(EntityNPCInterface npc,Container cont)
    {
    	super(cont);
    	this.player = Minecraft.getMinecraft().thePlayer;
    	this.npc = npc;
    	title = "Npc Mainmenu";
    }
    public void initGui()
    {
    	super.initGui();
        buttonList.clear();
        buttons.clear();
        labels.clear();
        sliders.clear();
        textfields.clear();
        Keyboard.enableRepeatEvents(true);
    }
    public ResourceLocation getResource(String texture){
    	return new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    public void updateScreen(){
    	for(GuiNpcTextField tf : textfields.values())
    		if(tf.enabled)
    			tf.updateCursorCounter();
        super.updateScreen();
    }

    protected void mouseClicked(int i, int j, int k)
    {
    	for(GuiNpcTextField tf : textfields.values())
    		if(tf.enabled)
    			tf.mouseClicked(i, j, k);
    	super.mouseClicked(i, j, k);
    }
    @Override
    protected void keyTyped(char c, int i)
    {
    	for(GuiNpcTextField tf : textfields.values())
    		tf.textboxKeyTyped(c, i);


        if (closeOnEsc && (i == 1 || i == mc.gameSettings.keyBindInventory.keyCode))
        {
            close();
        }
    }
    public void close(){
    	GuiNpcTextField.unfocus();
    	save();
    	player.closeScreen();
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }
    public void addButton(GuiNpcButton button){
    	buttons.put(button.id,button);
    	buttonList.add(button);
    }
	public GuiNpcButton getButton(int i) {
		return buttons.get(i);
	}
    public void addTextField(GuiNpcTextField tf){
    	textfields.put(tf.id,tf);
    }
    public GuiNpcTextField getTextField(int i){
    	return textfields.get(i);
    }
    public void addLabel(GuiNpcLabel label) {
		labels.put(label.id, label);
	}
    public GuiNpcLabel getLabel(int i){
    	return labels.get(i);
    }

    public void addSlider(GuiNpcSlider slider){
		sliders.put(slider.id,slider);
    	buttonList.add(slider);
    }
	public GuiNpcSlider getSlider(int i) {
		return sliders.get(i);
	}
	
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
    	
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        drawCenteredString(fontRenderer, title, width / 2, 10, 0xffffff);
        for(GuiNpcLabel label : labels.values())
        	label.drawLabel(this,fontRenderer);
    	for(GuiNpcTextField tf : textfields.values())
    		tf.drawTextBox();
    }
    public abstract void save();
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
    }
	public FontRenderer getFontRenderer() {
		// TODO Auto-generated method stub
		return this.fontRenderer;
	}

}

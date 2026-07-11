// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public abstract class GuiNPCInterface extends GuiScreen
{
	public EntityPlayer player;
	public boolean drawDefaultBackground = true;
	public EntityNPCInterface npc;
	private HashMap<Integer,GuiNpcButton> buttons = new HashMap<Integer,GuiNpcButton>();
	private HashMap<Integer,GuiMenuTopButton> topbuttons = new HashMap<Integer,GuiMenuTopButton>();
	private HashMap<Integer,GuiMenuSideButton> sidebuttons = new HashMap<Integer,GuiMenuSideButton>();
	private HashMap<Integer,GuiNpcTextField> textfields = new HashMap<Integer,GuiNpcTextField>();
	private HashMap<Integer,GuiNpcLabel> labels = new HashMap<Integer,GuiNpcLabel>();
	private HashMap<Integer,GuiCustomScroll> scrolls = new HashMap<Integer,GuiCustomScroll>();
	private HashMap<Integer,GuiNpcSlider> sliders = new HashMap<Integer,GuiNpcSlider>();
	public String title;
	private ResourceLocation background = null;
	public boolean closeOnEsc = false;
	public int guiLeft,guiTop,xSize,ySize;
	private SubGuiInterface subgui;
	
    public GuiNPCInterface(EntityNPCInterface npc)
    {
    	this.player = Minecraft.getMinecraft().thePlayer;
    	this.npc = npc;
    	title = "";
    	xSize = 200;
    	ySize = 222;
    }
    public GuiNPCInterface()
    {
    	this(null);
    }
    public void setBackground(String texture){
    	background = new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    public ResourceLocation getResource(String texture){
    	return new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    public void initGui()
    {
    	if(subgui != null){
    		subgui.setWorldAndResolution(mc, width, height);
    		subgui.initGui();
    	}
    	guiLeft = (width- xSize)/2;
        guiTop = (height - ySize) / 2;
        buttonList.clear();
        labels.clear();
        textfields.clear();
        buttons.clear();
        sidebuttons.clear();
        topbuttons.clear();
        scrolls.clear();
        sliders.clear();
        Keyboard.enableRepeatEvents(true);
    }
    public void updateScreen(){
    	if(subgui != null)
    		subgui.updateScreen();
    	else{
	    	for(GuiNpcTextField tf : textfields.values()){
	    		if(tf.enabled)
	    			tf.updateCursorCounter();
	    	}
	        super.updateScreen();
    	}
    }

    public void mouseClicked(int i, int j, int k)
    {
    	if(subgui != null)
    		subgui.mouseClicked(i,j,k);
    	else{
	    	for(GuiNpcTextField tf : new ArrayList<GuiNpcTextField>(textfields.values()))
	    		if(tf.enabled)
	    			tf.mouseClicked(i, j, k);
	        if (k == 0){
		        for(GuiCustomScroll scroll : new ArrayList<GuiCustomScroll>(scrolls.values())){
		        	scroll.mouseClicked(i, j, k);
		        }
	        }
	    	mouseEvent(i,j,k);
	    	super.mouseClicked(i, j, k);
    	}
    }
    public void mouseEvent(int i, int j, int k){};
    
    protected void actionPerformed(GuiButton par1GuiButton) {
		if(subgui != null)
			subgui.buttonEvent(par1GuiButton);
		else{
			buttonEvent(par1GuiButton);
		}
    }
	public void buttonEvent(GuiButton guibutton) {
		
	}
    @Override
    public void keyTyped(char c, int i)
    {
    	if(subgui != null)
    		subgui.keyTyped(c,i);
    	
    	for(GuiNpcTextField tf : textfields.values())
    			tf.textboxKeyTyped(c, i);

        if(closeOnEsc && i == 1)
        {
        	close();
        }
    }
    public void close(){
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
        save();
    }
    public void addButton(int i, GuiNpcButton button){
    	buttons.put(i,button);
    	buttonList.add(button);
    }
    public void addButton(GuiNpcButton button){
    	buttons.put(button.id,button);
    	buttonList.add(button);
    }
    public void addTopButton(GuiMenuTopButton button){
    	topbuttons.put(button.id,button);
    	buttonList.add(button);
    }
    public void addSideButton(GuiMenuSideButton button){
    	sidebuttons.put(button.id,button);
    	buttonList.add(button);
    }
	public GuiNpcButton getButton(int i) {
		return buttons.get(i);
	}
	public GuiMenuSideButton getSideButton(int i) {
		return sidebuttons.get(i);
	}
    public void addTextField(int i, GuiNpcTextField tf){
    	textfields.put(i,tf);
    }
    public void addTextField( GuiNpcTextField tf){
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
	public void addScroll(GuiCustomScroll scroll) {
        scroll.setWorldAndResolution(Minecraft.getMinecraft(), 350, 250);
        scrolls.put(scroll.id, scroll);
	}

    public void addSlider(GuiNpcSlider slider){
		sliders.put(slider.id,slider);
    	buttonList.add(slider);
    }
	public GuiNpcSlider getSlider(int i) {
		return sliders.get(i);
	}
	public GuiCustomScroll getScroll(int id){
		return scrolls.get(id); 
	}
	
    public abstract void save();
    public void drawScreen(int i, int j, float f)
    {
    	if(drawDefaultBackground)
    		drawDefaultBackground();
    	
    	if(background != null && mc.renderEngine != null){
    		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    		mc.renderEngine.bindTexture(background);
    		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    	}
    	
        drawCenteredString(fontRenderer, title, width / 2, 10, 0xffffff);
        for(GuiNpcLabel label : labels.values())
        	label.drawLabel(this,fontRenderer);
    	for(GuiNpcTextField tf : textfields.values())
    		tf.drawTextBox();
        for(GuiCustomScroll scroll : scrolls.values())
            scroll.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
        if(subgui != null)
    		subgui.drawScreen(i,j,f);
    }
	public FontRenderer getFontRenderer() {
		// TODO Auto-generated method stub
		return this.fontRenderer;
	}
	public boolean drawSlot(int i, int j, int k, int l, Tessellator tessellator, String drawString) {
		return false;
	}
	public void elementClicked() {
		// TODO Auto-generated method stub
		
	}
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	public void doubleClicked() {
	}


    public void setSubGui(SubGuiInterface gui){
    	subgui = gui;
		subgui.setWorldAndResolution(mc, width, height);
		subgui.parent = this;
    	initGui();
    }
	public void closeSubGui(SubGuiInterface gui) {
		subgui = null;
	}
	public boolean hasSubGui() {
		return subgui != null;
	}
	public SubGuiInterface getSubGui() {
		if(hasSubGui() && subgui.hasSubGui())
			return subgui.getSubGui();
		return subgui;
	}
	
	public void setSubGuiData(NBTTagCompound compound) {
		((IGuiData)subgui).setGuiData(compound);
	}
}

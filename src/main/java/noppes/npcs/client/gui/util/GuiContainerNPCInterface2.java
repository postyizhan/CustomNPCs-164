// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.constants.EnumPacketType;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public abstract class GuiContainerNPCInterface2 extends GuiContainerNPCInterface
{
	public int guiLeft,guiTop;
	public EntityPlayer player;
	public boolean drawDefaultBackground = false;
	public EntityNPCInterface npc;
	private ResourceLocation background = new ResourceLocation("customnpcs","textures/gui/menubg.png");
	private final ResourceLocation defaultBackground = new ResourceLocation("customnpcs","textures/gui/menubg.png");
	private GuiNpcMenu menu;
	private SubGuiInterface subgui;

    public GuiContainerNPCInterface2(EntityNPCInterface npc,Container cont)
    {
    	this(npc, cont, -1);
    }
    public GuiContainerNPCInterface2(EntityNPCInterface npc,Container cont,int activeMenu)
    {
    	super(npc, cont);
    	this.player = Minecraft.getMinecraft().thePlayer;
    	this.npc = npc;
    	this.xSize = 420;
    	this.menu = new GuiNpcMenu(this,activeMenu,npc);
    }
    public void setBackground(String texture){
    	background = new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    public ResourceLocation getResource(String texture){
    	return new ResourceLocation("customnpcs","textures/gui/" + texture);
    }
    
	@Override
    public void initGui()
    {
    	super.initGui();
    	if(subgui != null){
    		subgui.setWorldAndResolution(mc, width, height);
    		subgui.initGui();
    	}
    	
        buttonList.clear();
        
        guiLeft = (width-xSize)/2;
        guiTop = (height - 200) / 2;
        
        menu.initGui(guiLeft,guiTop, xSize);
    }   
    
    public void setSubGui(SubGuiInterface gui){
    	subgui = gui;
		subgui.setWorldAndResolution(mc, width, height);
		subgui.parent = this;
    	initGui();
    }

    @Override
    protected void mouseClicked(int i, int j, int k)
    {
    	if(subgui != null)
    		subgui.mouseClicked(i,j,k);
    	else{
	    	menu.mouseClicked(i, j, k);
	    	mouseEvent(i,j,k);
	    	super.mouseClicked(i, j, k);
    	}
    }
    public void mouseEvent(int i, int j, int k){};

    @Override
	protected void actionPerformed(GuiButton guibutton) {
		if(subgui != null)
			subgui.buttonEvent(guibutton);
		else
			buttonEvent(guibutton);
	}
    public void buttonEvent(GuiButton guibutton){};
    
    @Override
    public void confirmClicked(boolean flag, int i)
    {
		if(flag){
			NoppesUtil.sendData(EnumPacketType.Delete);
	        mc.displayGuiScreen(null);
	        mc.setIngameFocus();
		}
		else{
			NoppesUtil.openGUI(player, this);
		}
    }
	@Override
	public void keyTyped(char c, int i){
    	if(subgui != null)
    		subgui.keyTyped(c,i);
    }
    
    public void delete(){
    	npc.delete();
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }
    public void drawDefaultBackground()
    {
    	if(drawDefaultBackground && subgui == null)
    		this.drawWorldBackground(0);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 256);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(defaultBackground);
        drawTexturedModalRect(guiLeft + xSize-200, guiTop, 26, 0, 200, 220);
        
        menu.drawElements(fontRenderer, i, j, mc, f);
    }
    public abstract void save();

    @Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        GL11.glColor4f(1, 1, 1, 1);
        if(subgui != null)
        {
            RenderHelper.disableStandardItemLighting();
    		subgui.drawScreen(i,j,f);
        }
    }   

    @Override
    protected void drawSlotInventory(Slot par1Slot)
    {
        if(subgui == null)
        	super.drawSlotInventory(par1Slot);
    }
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	public void closeSubGui(SubGuiInterface gui) {
		subgui = null;
	}
	public boolean hasSubGui() {
		return subgui != null;
	}
	public void setSubGuiData(NBTTagCompound compound) {
		((IGuiData)subgui).setGuiData(compound);
	}
	public SubGuiInterface getSubGui() {
		return subgui;
	}

}

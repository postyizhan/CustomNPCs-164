// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
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

public abstract class GuiNPCInterface2 extends GuiNPCInterface
{
	private ResourceLocation background = new ResourceLocation("customnpcs:textures/gui/menubg.png");
	private GuiNpcMenu menu;

    public GuiNPCInterface2(EntityNPCInterface npc)
    {
    	this(npc, -1);
    }
    public GuiNPCInterface2(EntityNPCInterface npc, int activeMenu){
    	super(npc);
    	xSize = 420;
    	ySize = 200;
    	menu = new GuiNpcMenu(this, activeMenu, npc);
    	
    }
    public void initGui()
    {
    	super.initGui();       
        menu.initGui(guiLeft, guiTop, xSize);
        
    }

    @Override
    public void mouseClicked(int i, int j, int k){
    	super.mouseClicked(i, j, k);
    	if(!hasSubGui())
	    	menu.mouseClicked(i, j, k);
    }   
    
    public void delete(){
    	npc.delete();
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }
    public abstract void save();
    @Override
    public void drawScreen(int i, int j, float f)
    {
    	if(drawDefaultBackground)
    		drawDefaultBackground(); //drawDefaultBackground
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 200, 220);
        drawTexturedModalRect(guiLeft + xSize-230, guiTop, 26, 0, 230, 220);
        menu.drawElements(getFontRenderer(), i, j, mc, f);
        boolean bo = drawDefaultBackground;
        drawDefaultBackground = false;
        super.drawScreen(i, j, f);
        drawDefaultBackground = bo;
    }
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}
	public boolean drawSlot(int i, int j, int k, int l, Tessellator tessellator, String drawString) {
		return false;
	}

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    
    @Override
    public void confirmClicked(boolean flag, int i){
    	Minecraft mc = Minecraft.getMinecraft();
		if(flag){
			NoppesUtil.sendData(EnumPacketType.Delete);
			mc.displayGuiScreen(null);
	        mc.setIngameFocus();
		}
		else{
			NoppesUtil.openGUI(mc.thePlayer, this);
		}
    }
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSkinPreviewInterface;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCTextures extends GuiNPCInterface implements GuiNpcSkinPreviewInterface
{
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	private String root = "";
	private AssetsBrowser assets;
	private HashSet<String> dataFolder = new HashSet<String>();
	private HashSet<String> dataTextures = new HashSet<String>();
    public GuiNPCTextures(EntityNPCInterface npc,GuiScreen parent)
    {
    	super(npc);
    	root = AssetsBrowser.getRoot(npc.display.texture);
    	assets = new AssetsBrowser(root, new String[]{"png"});
    	drawDefaultBackground = false;
    	title = "Select Texture";
    	this.parent = parent;
    }

    public void initGui()
    {
        super.initGui();
        dataFolder.clear();
        String ss = "Current Folder: /assets" + root;
        addLabel(new GuiNpcLabel(0,ss, width / 2 - (this.fontRenderer.getStringWidth(ss)/2), 20, 0xffffff));
        Vector<String> list = new Vector<String>();
        if(!assets.isRoot)
        	list.add("..<UP>..");
        for(String folder : assets.folders){
        	list.add("/" + folder);
        	dataFolder.add("/" + folder);
        }
        for(String texture : assets.files){
        	list.add(texture);
        	dataTextures.add(texture);
        }
        Collections.sort(list,String.CASE_INSENSITIVE_ORDER);
        slot = new GuiNPCStringSlot(list,this,npc,false,18);
        int index = npc.display.texture.lastIndexOf("/");
        if(index > 0){
        	String asset = npc.display.texture.substring(index + 1);
        	if(npc.display.texture.equals(assets.getAsset(asset)))
        		slot.selected = asset;
        }
        slot.registerScrollButtons(4, 5);
        
    	this.addButton(2,new GuiNpcButton(2, width / 2 - 100, height - 44,98, 20, "gui.back"));
    }


    public void drawScreen(int i, int j, float f)
    {
    	int l = (width/2)-180;
    	int i1 =  (height/2) - 90;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        GL11.glTranslatef(l + 33, i1 + 131, 50F);
        float f1 = (50F * 5) / npc.display.modelSize;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = npc.renderYawOffset;
        float f3 = npc.rotationYaw;
        float f4 = npc.rotationPitch;
        float f5 = (float)(l + 33) - i;
        float f6 = (float)((i1 + 131) - 50) - j;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        npc.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        npc.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        npc.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        npc.rotationYawHead = npc.rotationYaw;
        npc.cloakUpdate();
        GL11.glTranslatef(0.0F, npc.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180F;
        RenderManager.instance.renderEntityWithPosYaw(npc, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        npc.renderYawOffset = f2;
        npc.rotationYaw = f3;
        npc.rotationPitch = f4;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    	slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }
    @Override
    public void elementClicked(){
    	if(dataTextures.contains(slot.selected) && slot.selected != null){
    		npc.display.texture = assets.getAsset(slot.selected);
    		npc.textureLocation = null;
    	}
    }
    @Override
    public void doubleClicked(){
    	String selected = slot.selected;
    	if(selected.equals("..<UP>..")){
    		root = root.substring(0,root.lastIndexOf("/"));
        	assets = new AssetsBrowser(root, new String[]{"png"});
    		initGui();
    	}
    	else if(dataFolder.contains(selected)){
    		root += selected;
        	assets = new AssetsBrowser(root, new String[]{"png"});
    		initGui();
    	}
    	else {
    		close();
    		NoppesUtil.openGUI(player, parent);
    	}
    }

	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 2)
        {
        	close();
        	NoppesUtil.openGUI(player, parent);
        }
    }
	public void save() {
	}


}

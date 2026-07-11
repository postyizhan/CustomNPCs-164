// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcSkinPreviewInterface;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.roles.RoleFollower;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcFollower extends GuiContainerNPCInterface  implements GuiNpcSkinPreviewInterface
{
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/follower.png");
	private EntityNPCInterface npc;
	private RoleFollower role;
    private float xSize_lo;
    private float ySize_lo;
    public GuiNpcFollower(EntityNPCInterface npc,ContainerNPCFollower container)
    {
        super(npc, container);
        this.npc = npc;
        role = (RoleFollower) npc.roleInterface;
        closeOnEsc = true;
    }
    public void initGui()
    {
    	super.initGui();
        buttonList.clear();
        addButton(new GuiNpcButton(4, guiLeft + 100, guiTop+ 110, 50, 20, new String[]{StatCollector.translateToLocal("follower.waiting"),StatCollector.translateToLocal("follower.following")},role.isFollowing?1:0));
        //controlList.add(new GuiButton(6, guiLeft + 102, guiTop+ ySize, 98, 20, "Close"));
        addButton(new GuiNpcButton(5, guiLeft + 8, guiTop+ 30, 50, 20, StatCollector.translateToLocal("follower.hire")));
    }
    public void actionPerformed(GuiButton guibutton)
    {
    	super.actionPerformed(guibutton);
        if(guibutton.id == 4)
        {
        	NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerState);
        	close();
        	
        }
        if(guibutton.id == 5)
        {
        	NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerExtend);
        	close();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
       fontRenderer.drawString(StatCollector.translateToLocal("follower.health")+": " + npc.getHealth()+"/"+ npc.getMaxHealth(), 62, 70, 0x404040);
       if(role.getDaysLeft() <= 1)
    	   fontRenderer.drawString(StatCollector.translateToLocal("follower.daysleft")+": " + StatCollector.translateToLocal("follower.lastday"), 62, 94, 0x404040);
       else
    	   fontRenderer.drawString(StatCollector.translateToLocal("follower.daysleft") + ": " + (role.getDaysLeft()-1), 62, 94, 0x404040);
    }


    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
		int l = guiLeft;
	    int i1 = guiTop;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		int index = 0;
		for(int id : role.inventory.items.keySet()){
			ItemStack itemstack = role.inventory.items.get(id);
			if(itemstack == null)
				continue;
            int days = 1;
            if(role.rates.containsKey(id))
            	days = role.rates.get(id);
            
			int yOffset = index * 20;
			
			int x = guiLeft +  68;
			int y = guiTop + yOffset + 4;
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            RenderHelper.enableGUIStandardItemLighting();
	        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, x + 11,y);
	        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, x+11,y);
            RenderHelper.disableStandardItemLighting(); 
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

            String daysS = days + " " + ((days == 1)?StatCollector.translateToLocal("follower.day"):StatCollector.translateToLocal("follower.days"));
	      	fontRenderer.drawString(" = "+daysS, x + 27, y + 4, 0x404040);
	        //fontRenderer.drawString(quantity, x + 0 + (12-fontRenderer.getStringWidth(quantity))/2, y + 4, 0x404040);
	        
	        index++;
    	}
        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
        GL11.glPushMatrix();
        GL11.glTranslatef(l + 33, i1 + 131, 50F);
        float f1 = 30F;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        float f2 = npc.renderYawOffset;
        float f3 = npc.rotationYaw;
        float f4 = npc.rotationPitch;
        float f5 = (float)(l + 33) - xSize_lo;
        float f6 = (float)((i1 + 131) - 50) - ySize_lo;
        GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float)Math.atan(f6 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
        npc.renderYawOffset = (float)Math.atan(f5 / 40F) * 20F;
        npc.rotationYaw = (float)Math.atan(f5 / 40F) * 40F;
        npc.rotationPitch = -(float)Math.atan(f6 / 40F) * 20F;
        npc.rotationYawHead = npc.rotationYaw;
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
    }
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        xSize_lo = i;
        ySize_lo = j;
    }
	@Override
	public void save() {
		return;
	}
}

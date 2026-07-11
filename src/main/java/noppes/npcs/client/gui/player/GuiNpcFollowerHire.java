// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCFollowerHire;
import noppes.npcs.roles.RoleFollower;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcFollowerHire extends GuiContainerNPCInterface
{
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/followerhire.png");
	private EntityNPCInterface npc;
	private ContainerNPCFollowerHire container;
	private RoleFollower role;
    public GuiNpcFollowerHire(EntityNPCInterface npc,ContainerNPCFollowerHire container)
    {
        super(npc, container);
        this.container = container;
        this.npc = npc;
        role = (RoleFollower) npc.roleInterface;
        closeOnEsc = true;
    }
    public void initGui()
    {
    	super.initGui();
        addButton(new GuiNpcButton(5, guiLeft + 26, guiTop+ 60, 50, 20, StatCollector.translateToLocal("follower.hire")));
    }

    public void actionPerformed(GuiButton guibutton)
    {
    	super.actionPerformed(guibutton);
        if(guibutton.id == 5)
        {
        	NoppesUtilPlayer.sendData(EnumPlayerPacket.FollowerHire);
        	close();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        //fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		int index = 0;
		for(int id : role.inventory.items.keySet()){
			ItemStack itemstack = role.inventory.items.get(id);
			if(itemstack == null)
				continue;
			int days = 1;
			if(role.rates.containsKey(id))
	            days = role.rates.get(id);
				
			
			int yOffset = index * 26;
			
			int x = guiLeft +  78;
			int y = guiTop + yOffset + 10;
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
    	
    }
	@Override
	public void save() {
		return;
	}
}

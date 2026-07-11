// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.player;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerNPCTrader;
import noppes.npcs.roles.RoleTrader;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNPCTrader extends GuiContainerNPCInterface
{
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/npctrader.png");
	private RoleTrader role;
    public GuiNPCTrader(EntityNPCInterface npc,ContainerNPCTrader container)
    {
        super(npc, container);
        role = (RoleTrader) npc.roleInterface;
        closeOnEsc = true;
        ySize = 232;
    }
    public void initGui()
    {
    	super.initGui();
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
		for(int slot : role.inventoryCurrency.items.keySet()){
			ItemStack item = role.inventoryCurrency.items.get(slot);
			if(item == null || role.inventorySold.items.get(slot) == null)
				continue;
			
			int x = guiLeft + slot%3 * 45 + 40;
			int y = guiTop + slot/3 * 22 + 8;
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            RenderHelper.enableGUIStandardItemLighting();
	        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, item, x ,y);
	        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, item, x,y);
            RenderHelper.disableStandardItemLighting(); 
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

	      	fontRenderer.drawString("=", x + 16, y + 4, 0x404040);
    	}
    }
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
		for(int slot : role.inventoryCurrency.items.keySet()){
			ItemStack item = role.inventoryCurrency.items.get(slot);
			if(item == null || role.inventorySold.items.get(slot) == null)
				continue;

			int x = guiLeft + slot%3 * 45 + 40;
			int y = guiTop + slot/3 * 22 + 8;

            if (this.isPointInRegion(x - guiLeft, y - guiTop, 16, 16, par1,par2))
            {
                this.drawItemStackTooltip(item, par1, par2);
            }
    	}
    }
	@Override
	public void save() {
	}
}

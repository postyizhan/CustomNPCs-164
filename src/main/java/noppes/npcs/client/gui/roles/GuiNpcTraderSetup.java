// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.roles;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.containers.ContainerNPCTraderSetup;
import noppes.npcs.roles.RoleTrader;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcTraderSetup extends GuiContainerNPCInterface2
{
	
	private static final ResourceLocation field_110422_t = new ResourceLocation("customnpcs","textures/gui/npctradersetup2.png");
	private RoleTrader role;

    public GuiNpcTraderSetup(EntityNPCInterface npc,ContainerNPCTraderSetup container)
    {
        super(npc, container);
        ySize = 180;
		role = (RoleTrader)npc.roleInterface;
    }
    public void initGui()
    {
    	super.initGui();
        buttonList.clear();
        setBackground("npctradersetup.png");
		addButton(new GuiNpcButton(2, guiLeft + 306, guiTop + 158, 88, 20, "stock.options"));

    }
	@Override
	public void buttonEvent(GuiButton guibutton) {
		if(guibutton.id == 2)
			setSubGui(new SubGuiNpcTraderStock(role));
	}
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        super.drawGuiContainerForegroundLayer(par1, par2);
        for(int ii = 0; ii < 18; ii++){
        	int x =  212;
        	x += ii%3 * 59;
        	int y = 16;
        	y += ii/3 * 22;
        	fontRenderer.drawString("=", x , y + 4, 0x404040);
        }
    }

    public void updateScreen(){
        super.updateScreen();
    }

    protected void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    }
    public void keyTyped(char c, int i)
    {
//        if(i == 1)
//        {
//        	close();
//        }
    }
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(field_110422_t);
        drawTexturedModalRect(guiLeft + 190, guiTop + 20, 0, 0, 167, 134);
    }
    public void drawScreen(int i, int j, float f)
    {
    	super.drawScreen(i, j, f);
    }
	@Override
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}

package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiMenuSideButton extends GuiNpcButton
{
	public static final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/menusidebutton.png");
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean active;
    public boolean drawButton;
    public boolean hover = false;

    public GuiMenuSideButton(int i, int j, int k, String s)
    {
        this(i, j, k, 200, 20, s);
    }

    public GuiMenuSideButton(int i, int j, int k, int l, int i1, String s)
    {
    	super(i, j, k, l, i1, s);
        width = 200;
        height = 20;
        active = false;
        drawButton = true;
        id = i;
        xPosition = j;
        yPosition = k;
        width = l;
        height = i1;
        displayString = s;
    }

    protected int getHoverState(boolean flag)
    {
        byte byte0 = 1;
        if (active)
        {
            byte0 = 0;
        }
        else if (flag)
        {
            byte0 = 1;
        }
        return byte0;
    }
    
    public void drawButton(Minecraft minecraft, int i, int j)
    {
        if (!drawButton)
        {
            return;
        }
        FontRenderer fontrenderer = minecraft.fontRenderer;
        minecraft.renderEngine.bindTexture(resource);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int width = this.width + (active?2:0);
        hover = i >= xPosition && j >= yPosition && i < xPosition + width && j < yPosition + height;
        int k = getHoverState(hover);
        drawTexturedModalRect(xPosition, yPosition, 0,  k * 22, width, height);
        mouseDragged(minecraft, i, j);
        if (active)
        {
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffffa0);
        }
        else if (hover)
        {
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xffffa0);
        }
        else
        {
            drawCenteredString(fontrenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, 0xe0e0e0);
        }
    }

    protected void mouseDragged(Minecraft minecraft, int i, int j)
    {
    }

    public void mouseReleased(int i, int j)
    {
    }

    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
        return !active && drawButton && hover;
    }
}

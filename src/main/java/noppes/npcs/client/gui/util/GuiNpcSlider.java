package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.settings.EnumOptions;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiNpcSlider extends GuiSlider {
	private ISliderListener listener;
	
	public GuiNpcSlider(GuiScreen parent, int id, int xPos, int yPos, EnumOptions idFloat, String displayString, float sliderValue) {
		super(id, xPos, yPos, idFloat, displayString, sliderValue);
		
		if(parent instanceof ISliderListener)
			listener = (ISliderListener) parent;
	}
	
	@Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);

                if (this.sliderValue < 0.0F)
                {
                    this.sliderValue = 0.0F;
                }

                if (this.sliderValue > 1.0F)
                {
                    this.sliderValue = 1.0F;
                }

        		if(listener != null)
        			listener.mouseDragged(this);
        		
            	if(!Mouse.isButtonDown(0)){
            		this.mouseReleased(0, 0);
            	}
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
	
	@Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.enabled && this.drawButton && par2 >= this.xPosition && par3 >= this.yPosition && 
        		par2 < this.xPosition + this.width && par3 < this.yPosition + this.height)
        {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);

            if (this.sliderValue < 0.0F)
            {
                this.sliderValue = 0.0F;
            }

            if (this.sliderValue > 1.0F)
            {
                this.sliderValue = 1.0F;
            }

    		if(listener != null)
    			listener.mousePressed(this);

            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
        
        if(listener != null)
			listener.mouseReleased(this);
    }
}

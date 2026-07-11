// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.Resistances;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcSlider;
import noppes.npcs.client.gui.util.ISliderListener;
import noppes.npcs.client.gui.util.SubGuiInterface;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcResistanceProperties extends SubGuiInterface implements ISliderListener
{
	private Resistances resistances;
    public SubGuiNpcResistanceProperties(Resistances resistances)
    {
    	this.resistances = resistances;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
    }

    public void initGui()
    {
        super.initGui();
        addLabel(new GuiNpcLabel(0,"enchantment.knockback", guiLeft + 4, guiTop + 15, 0x404040));
        addSlider(new GuiNpcSlider(this, 0, guiLeft + 94, guiTop + 10, null, (int)(resistances.knockback * 100 - 100)  + "%", resistances.knockback / 2));

        addLabel(new GuiNpcLabel(1,"item.arrow.name", guiLeft + 4, guiTop + 37, 0x404040));
        addSlider(new GuiNpcSlider(this, 1, guiLeft + 94, guiTop + 32, null, (int)(resistances.arrow * 100 - 100)  + "%", resistances.arrow / 2));

        addLabel(new GuiNpcLabel(2,"stats.melee", guiLeft + 4, guiTop + 59, 0x404040));
        addSlider(new GuiNpcSlider(this, 2, guiLeft + 94, guiTop + 54, null, (int)(resistances.playermelee * 100 - 100)  + "%", resistances.playermelee / 2));

    	addButton(new GuiNpcButton(66, guiLeft + 190, guiTop + 190, 60, 20, "gui.done"));
    }
    
	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 66)
        {
        	close();
        }
    }

	@Override
	public void mouseDragged(GuiNpcSlider slider) {
		slider.displayString = (int)(slider.sliderValue * 200 - 100) + "%";
	}

	@Override
	public void mousePressed(GuiNpcSlider slider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(GuiNpcSlider slider) {
		if(slider.id == 0){
			resistances.knockback = slider.sliderValue * 2;
		}
		if(slider.id == 1){
			resistances.arrow = slider.sliderValue * 2;
		}
		if(slider.id == 2){
			resistances.playermelee = slider.sliderValue * 2;
		}
			
	}

}

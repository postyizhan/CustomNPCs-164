package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class GuiNpcButton extends GuiButton{
	public boolean shown = true;
	
	private String[] display;
	private int displayValue = 0;
	public GuiNpcButton(int i, int j, int k,  String s) {
		super(i, j, k,  StatCollector.translateToLocal(s));
	}
	public GuiNpcButton(int i, int j, int k,  String[] display, int val) {
		super(i, j, k, display[val]);
		this.display = display;
		this.displayValue = val;
	}
	public GuiNpcButton(int i, int j, int k, int l, int m, String string) {
		super(i, j, k, l, m, StatCollector.translateToLocal(string));
	}
	public GuiNpcButton(int i, int j, int k, int l, int m, String[] display,
			int val) {
		this(i, j, k, l, m, display[val % display.length]);
		this.display = display;
		this.displayValue = val % display.length;
	}
	public void setDisplayText(String text){
		this.displayString = StatCollector.translateToLocal(text);
	}
	public int getValue(){
		return displayValue;
	}
    public void drawButton(Minecraft minecraft, int i, int j)
    {
    	if(!shown)
    		return;
    	super.drawButton(minecraft, i, j);
    }
    public boolean mousePressed(Minecraft minecraft, int i, int j)
    {
    	boolean bo = super.mousePressed(minecraft, i, j);
    	if(bo && display != null){
    		displayValue = (displayValue+1) % display.length;
    		this.displayString = StatCollector.translateToLocal(display[displayValue]);
    	}
    	return bo;
    }
    public void setDisplay(int value){
    	this.displayValue = value;
    	this.displayString = StatCollector.translateToLocal(display[value]);
    }    
}

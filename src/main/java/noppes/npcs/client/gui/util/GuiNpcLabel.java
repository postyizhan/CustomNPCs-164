package noppes.npcs.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class GuiNpcLabel {
	public String label;
	private int x,y,color = 0x404040;
	public boolean enabled = true;
	public int id;
	
	public GuiNpcLabel(int id, Object label, int x, int y, int color){
		this.id = id;
		this.label = StatCollector.translateToLocal(label.toString());
		this.x = x;
		this.y = y;
		this.color = color;
	}
	public GuiNpcLabel(int id, Object label, int x, int y){
		this(id, label, x, y, 0x404040);
	}
	public void drawLabel(GuiScreen gui, FontRenderer fontRenderer) {
		if(enabled)
			fontRenderer.drawString(label, x, y, color);
	}
	public void center(int width) {
		int size = Minecraft.getMinecraft().fontRenderer.getStringWidth(label);
		x += (width - size) / 2;
	}
}

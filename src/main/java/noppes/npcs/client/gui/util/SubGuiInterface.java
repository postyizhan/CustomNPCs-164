package noppes.npcs.client.gui.util;

import net.minecraft.client.gui.GuiScreen;

public class SubGuiInterface extends GuiNPCInterface{
	public GuiScreen parent;
	
	@Override
	public void save() {
		
	}
	@Override
	public void close() {
		if(parent instanceof ISubGuiListener)
			((ISubGuiListener)parent).subGuiClosed(this);
		
		if(parent instanceof GuiNPCInterface2)
			((GuiNPCInterface2)parent).closeSubGui(this);
		else if(parent instanceof GuiContainerNPCInterface2)
			((GuiContainerNPCInterface2)parent).closeSubGui(this);
		else
			super.close();
	}
	
	protected void changeSubGui(SubGuiInterface gui){
		if(parent instanceof GuiNPCInterface2)
			((GuiNPCInterface2)parent).setSubGui(gui);
	}
}

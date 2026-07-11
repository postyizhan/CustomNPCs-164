package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.containers.ContainerCarpentryBench;

import org.lwjgl.opengl.GL11;

public class GuiNpcCarpentryBench extends GuiContainerNPCInterface
{
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/anvil.png");
    private ContainerCarpentryBench container;
    public GuiNpcCarpentryBench(ContainerCarpentryBench container)
    {
        super(null,container);
        this.container = container;
        this.title = "";
        allowUserInput = false;
        closeOnEsc = true;
        
    }
    @Override
    public void initGui(){
    	super.initGui();
    }

    public void actionPerformed(GuiButton guibutton)
    {
    	super.actionPerformed(guibutton);
    	

    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }

	@Override
	public void save() {
		return;
	}
}

package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.containers.ContainerNPCBankInterface;

import org.lwjgl.opengl.GL11;

public class GuiNPCBankChest extends GuiContainerNPCInterface implements IGuiData
{
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/bankchest.png");
    private ContainerNPCBankInterface container;
    private int availableSlots = 0;
    private int maxSlots = 1;
    private int unlockedSlots = 1;
    private ItemStack currency;
    
    public GuiNPCBankChest(EntityNPCInterface npc,ContainerNPCBankInterface container)
    {
        super(npc,container);
        this.container = container;
        this.title = "";
        allowUserInput = false;
        ySize = 235;
        closeOnEsc = true;
        
    }
    @Override
    public void initGui(){
    	super.initGui();
    	availableSlots = 0;
    	if(maxSlots > 1){
	        for(int i = 0; i < maxSlots ; i++){
	        	GuiNpcButton button = new GuiNpcButton(i, guiLeft - 50, guiTop + 10 + i * 24, 50, 20, "Tab " + (i+1));
	        	if( i > unlockedSlots)
	        		button.enabled = false;
	        	addButton(button);
	        	availableSlots++;
	        }
	        if(availableSlots == 1)
	        	buttonList.clear();
    	}
        if(!container.isAvailable()){
        	addButton(new GuiNpcButton(8, guiLeft + 48, guiTop + 48,80,20, StatCollector.translateToLocal("bank.unlock")));
        }
        else if(container.canBeUpgraded()){
        	addButton(new GuiNpcButton(9, guiLeft + 48, guiTop + 48,80,20, StatCollector.translateToLocal("bank.upgrade")));
        }
    	if(maxSlots > 1){
    		getButton(container.slot).shown = false;
    		getButton(container.slot).enabled = false;
    	}
    }

    public void actionPerformed(GuiButton guibutton)
    {
    	super.actionPerformed(guibutton);
    	if(guibutton.id < 6){
    		close();
    		NoppesUtilPlayer.sendData(EnumPlayerPacket.BankSlotOpen, guibutton.id, container.bankid);
    	}
    	if(guibutton.id == 8){
    		NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUnlock);
    	}
    	if(guibutton.id == 9){
    		NoppesUtilPlayer.sendData(EnumPlayerPacket.BankUpgrade);
    	}
    	

    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, 6);
        if(!container.isAvailable()){
	        drawTexturedModalRect(l, i1 + 6, 0, 6, xSize, 64); 
	        drawTexturedModalRect(l, i1 + 70, 0, 124, xSize, 222-124);    
	        int x = guiLeft + 30;
	        int y = guiTop + 8;
	      	fontRenderer.drawString(StatCollector.translateToLocal("bank.unlockCosts")+":", x , y + 4 , 0x404040);
	        drawItem(x + 90,y,currency,i,j);
        }
        else if(container.isUpgraded()){
	        drawTexturedModalRect(l, i1 + 60, 0, 60, xSize, 162);
	        drawTexturedModalRect(l, i1 + 6, 0, 60, xSize, 64);        	
        }
        else if(container.canBeUpgraded()){
	        drawTexturedModalRect(l, i1 + 6, 0, 6, xSize, 216);
	        int x = guiLeft + 30;
	        int y = guiTop + 8;
	      	fontRenderer.drawString(StatCollector.translateToLocal("bank.upgradeCosts") + ":", x , y + 4 , 0x404040);
	        drawItem(x + 90,y,currency,i,j);
        }
        else{
	        drawTexturedModalRect(l, i1 + 6, 0, 60, xSize, 162);
        }
        if(maxSlots > 1){
	        for(int ii = 0; ii < maxSlots ; ii++){
	        	if(availableSlots == ii)
	        		break;
		      	fontRenderer.drawString("Tab " + (ii+1), guiLeft - 40, guiTop + 16 + ii * 24 , 0xFFFFFF);
	        }
        }
        super.drawGuiContainerBackgroundLayer(f, i, j);
    }
    private void drawItem(int x, int y, ItemStack item, int mouseX, int mouseY){
		if(item == null)
			return;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, item, x,y);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, item, x,y);
        RenderHelper.disableStandardItemLighting(); 
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);

        if (this.isPointInRegion(x - guiLeft, y - guiTop, 16, 16, mouseX, mouseY))
        {
            this.drawItemStackTooltip(item, mouseX, mouseY);
        }
    }
	@Override
	public void save() {
		return;
	}
	@Override
	public void setGuiData(NBTTagCompound compound) {
		maxSlots = compound.getInteger("MaxSlots");
		unlockedSlots = compound.getInteger("UnlockedSlots");
		if(compound.hasKey("Currency"))
			currency = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Currency"));
		else
			currency = null;
		if(container.currency != null)
			container.currency.item = currency;
		initGui();
	}
}

package noppes.npcs.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcRedstoneBlock extends GuiNPCInterface{

	private TileRedstoneBlock tile;
	
    public GuiNpcRedstoneBlock(int x, int y, int z) {
		super();
		tile = (TileRedstoneBlock) player.worldObj.getBlockTileEntity(x, y, z);
	}
	public void initGui()
    {
    	super.initGui();
    	
		this.addButton(new GuiNpcButton(4, guiLeft + 40, guiTop + 40, 120, 20, "Availability Options"));
    	
    	addLabel(new GuiNpcLabel(0,"On Range X:", guiLeft+ 1, guiTop + 76, 0xffffff));    	
    	addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft+ 60, guiTop + 71,20,20, tile.onRangeX + ""));
    	getTextField(0).numbersOnly = true;
    	getTextField(0).setMinMaxDefault(0, 50, 6); 	
    	addLabel(new GuiNpcLabel(1,"Y:", guiLeft+ 83, guiTop + 76, 0xffffff)); 
    	addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft+ 92, guiTop + 71,20,20, tile.onRangeY + ""));
    	getTextField(1).numbersOnly = true;
    	getTextField(1).setMinMaxDefault(0, 50, 6); 	
    	addLabel(new GuiNpcLabel(2,"Z:", guiLeft+ 115, guiTop + 76, 0xffffff)); 
    	addTextField(new GuiNpcTextField(2, this, fontRenderer, guiLeft+ 124, guiTop + 71,20,20, tile.onRangeZ + ""));
    	getTextField(2).numbersOnly = true;
    	getTextField(2).setMinMaxDefault(0, 50, 6);
    	
    	addLabel(new GuiNpcLabel(3,"Off Range X:", guiLeft - 3, guiTop + 99, 0xffffff));    	
    	addTextField(new GuiNpcTextField(3, this, fontRenderer, guiLeft+ 60, guiTop + 94,20,20, tile.offRangeX + ""));
    	getTextField(3).numbersOnly = true;
    	getTextField(3).setMinMaxDefault(0, 50, 10);  	
    	addLabel(new GuiNpcLabel(4,"Y:", guiLeft+ 83, guiTop + 99, 0xffffff)); 
    	addTextField(new GuiNpcTextField(4, this, fontRenderer, guiLeft+ 92, guiTop + 94,20,20, tile.offRangeY + ""));
    	getTextField(4).numbersOnly = true;
    	getTextField(4).setMinMaxDefault(0, 50, 10);  	
    	addLabel(new GuiNpcLabel(5,"Z:", guiLeft+ 115, guiTop + 99, 0xffffff)); 
    	addTextField(new GuiNpcTextField(5, this, fontRenderer, guiLeft+ 124, guiTop + 94,20,20, tile.offRangeZ + ""));
    	getTextField(5).numbersOnly = true;
    	getTextField(5).setMinMaxDefault(0, 50, 10);

        addButton(new GuiNpcButton(0, guiLeft + 40, guiTop + 190,120,20, "Done"));
    }
	protected void actionPerformed(GuiButton guibutton) {
		if(guibutton.id == 0)
			close();
		if (guibutton.id == 4) {
			save();
			SubGuiNpcAvailability gui = new SubGuiNpcAvailability(tile.availability);
			gui.parent2 = this;
			gui.parent = gui;
			NoppesUtil.openGUI(player, gui);
		}
	}
	@Override
	public void save() {
		if(tile == null)
			return;
		tile.onRangeX = getTextField(0).getInteger();
		tile.onRangeY = getTextField(1).getInteger();
		tile.onRangeZ = getTextField(2).getInteger();

		tile.offRangeX = getTextField(3).getInteger();
		tile.offRangeY = getTextField(4).getInteger();
		tile.offRangeZ = getTextField(5).getInteger();
		
		if(tile.onRangeX > tile.offRangeX)
			tile.offRangeX = tile.onRangeX;
		if(tile.onRangeY > tile.offRangeY)
			tile.offRangeY = tile.onRangeY;
		if(tile.onRangeZ > tile.offRangeZ)
			tile.offRangeZ = tile.onRangeZ;
		tile.isActivated = false;
		
		NBTTagCompound compound = new NBTTagCompound();
		tile.writeToNBT(compound);
		NoppesUtil.sendData(EnumPacketType.SaveTileEntity, compound);
	}

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.roles;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.mainmenu.GuiNpcAdvanced;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.controllers.TransportLocation;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcTransporter extends GuiNPCInterface implements IScrollData, IGuiData
{
	public TransportLocation location = new TransportLocation();
	private GuiNPCStringSlot slot;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
    public GuiNpcTransporter(EntityNPCInterface npc)
    {
    	super(npc);
    	title = "Npc Transporter";  	
    	
		NoppesUtil.sendData(EnumPacketType.TransportCategoriesGet);
		NoppesUtil.sendData(EnumPacketType.TransportGetLocation);
    }
    public void initGui()
    {
        super.initGui();
        Vector<String> list = new Vector<String>();
        list.addAll(data.keySet());
        slot = new GuiNPCStringSlot(list,this,npc,false,18);
        slot.registerScrollButtons(4, 5);
        this.addLabel(new GuiNpcLabel(0,"Name:", guiLeft- 30, height - 49, 0xFFFFFF));
        this.addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft+1, height - 54, 198, 20, location.name));
        this.addButton(new GuiNpcButton(0, guiLeft, height - 31, new String[]{"Available when discovered","Available from the start","Available after interaction"}, location.type));
    	this.addButton(new GuiNpcButton(4,guiLeft + 210 , height - 42, 98, 20, "Back"));
    }


    public void drawScreen(int i, int j, float f)
    {
    	slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }

	protected void actionPerformed(GuiButton guibutton)
    {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
    	if(button.id == 0){
    		location.type = button.getValue();
    	}
    	if(button.id == 4){
        	close();
			NoppesUtil.openGUI(player, new GuiNpcAdvanced(npc));
    	}
    	
    }
	@Override
	public void save() {
		if(slot.selected == null || slot.selected.isEmpty())
			return;
		String name = getTextField(0).getText();
		if(!name.isEmpty())
			location.name = name;
		
		location.npcX = npc.startPos[0];
		location.npcY = npc.startPos[1];
		location.npcZ = npc.startPos[2];

		location.posX = player.posX;
		location.posY = player.posY;
		location.posZ = player.posZ;
		location.dimension = player.dimension;
		
		int cat = data.get(slot.selected);
		NoppesUtil.sendData(EnumPacketType.TransportSave,cat, location.writeNBT());
	}
	@Override
    public void drawDefaultBackground()
    {
		//drawBackground(0);
    }
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		this.data = data;
		this.slot.setList(list);
	}
	@Override
	public void setSelected(String selected) {
		slot.selected = selected;
	}
	@Override
	public void setGuiData(NBTTagCompound compound) {
		TransportLocation loc = new TransportLocation();
		loc.readNBT(compound);
		location = loc;
		System.out.println(location.name);
		initGui();
	}


}

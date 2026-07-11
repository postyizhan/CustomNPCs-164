// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.roles;

import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.containers.ContainerNPCFollowerSetup;
import noppes.npcs.roles.RoleFollower;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcFollowerSetup extends GuiContainerNPCInterface2
{
	private RoleFollower role;
	private static final ResourceLocation field_110422_t = new ResourceLocation("textures/gui/followersetup.png");
    public GuiNpcFollowerSetup(EntityNPCInterface npc,ContainerNPCFollowerSetup container)
    {
        super(npc, container);
        role = (RoleFollower) npc.roleInterface;
        ySize = 180;
        setBackground("followersetup.png");
    }
    public void initGui()
    {
    	super.initGui();
        for(int i = 0; i < 3; i++){
        	int x = guiLeft + 66;
        	int y = guiTop + 37;
        	y += i * 25;
        	GuiNpcTextField tf = new GuiNpcTextField(i,this, fontRenderer,x, y, 24, 20, "1");
        	tf.numbersOnly = true;
        	tf.setMinMaxDefault(1, Integer.MAX_VALUE, 1);
        	addTextField(tf);
        }
        int i = 0;
        for(int day : role.rates.values()){
        	getTextField(i).setText(day + "");
        	i++;
        }
        addTextField(new GuiNpcTextField(3,this, fontRenderer,guiLeft + 100, guiTop + 6, 286, 20, role.dialogHire));
        addTextField(new GuiNpcTextField(4,this, fontRenderer,guiLeft + 100, guiTop + 30, 286, 20, role.dialogFarewell));
    }

    protected void actionPerformed(GuiButton guibutton)
    {

    }

    public void onGuiClosed()
    {
    }
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        //fontRenderer.drawString("Currency", 30, 1, 0x404040);
        //fontRenderer.drawString("Days", 90, 1, 0x404040);
        //fontRenderer.drawString("Hired Line",  -75,ySize + 3, 0xFFFFFF);
        //fontRenderer.drawString("Farewell Line",  -75,ySize + 22, 0xFFFFFF);
    }
	@Override
	public void save() {
    	HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
    	for(int i= 0;i < role.inventory.getSizeInventory();i++){
    		ItemStack item = role.inventory.getStackInSlot(i);
    		if(item != null){

    			int days = 1;
    			if(!getTextField(i).isEmpty() && getTextField(i).isInteger())
    				days = getTextField(i).getInteger();
    			if(days <= 0)
    				days = 1;
    			
    			map.put(i,days);
    		}
        }
    	role.rates = map;
    	role.dialogHire = getTextField(3).getText();
    	role.dialogFarewell = getTextField(4).getText();
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
	}
}

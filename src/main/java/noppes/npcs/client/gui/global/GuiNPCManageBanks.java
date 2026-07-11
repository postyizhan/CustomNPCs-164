// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.containers.ContainerManageBanks;
import noppes.npcs.controllers.Bank;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNPCManageBanks extends GuiContainerNPCInterface2 implements IScrollData,GuiCustomScrollActionListener,ITextfieldListener, IGuiData
{
	private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private ContainerManageBanks container;
	private Bank bank = new Bank();
	private String selected = null;
	
    public GuiNPCManageBanks(EntityNPCInterface npc,ContainerManageBanks container)
    {
    	super(npc,container);
    	this.container = container;
    	drawDefaultBackground = false;
    	NoppesUtil.sendData(EnumPacketType.BanksGet);
    	setBackground("npcbanksetup.png");
    }

    public void initGui()
    {
        super.initGui();
        
       	this.addButton(new GuiNpcButton(6,guiLeft + 340, guiTop + 10, 45, 20, "gui.add"));
    	this.addButton(new GuiNpcButton(7,guiLeft + 340, guiTop + 32, 45, 20, "gui.remove"));
    	
    	scroll = new GuiCustomScroll(this,0);
    	scroll.setWorldAndResolution(mc, 350, 250);
    	scroll.setSize(160, 180);
    	scroll.guiLeft = guiLeft + 174;
    	scroll.guiTop = guiTop + 8;
    	
        for(int i = 0; i < 6; i++)
        {
        	int x = guiLeft + 6;
        	int y = (guiTop + 36) + (i * 22);
            addButton(new GuiNpcButton(i, x + 50, y,80, 20, new String[]{"Can Upgrade","Can't Upgrade","Upgraded"},0));
            getButton(i).enabled = false;
        }
        
        addTextField(new GuiNpcTextField(0,this, this.fontRenderer, guiLeft+8, guiTop + 8, 160, 16, ""));
        getTextField(0).setMaxStringLength(20);
        
        addTextField(new GuiNpcTextField(1,this, this.fontRenderer, guiLeft+10, guiTop + 80, 16, 16, ""));
        getTextField(1).numbersOnly = true;
        getTextField(1).setMaxStringLength(1);
        
        addTextField(new GuiNpcTextField(2,this, this.fontRenderer, guiLeft+10, guiTop + 110, 16, 16, ""));
        getTextField(2).numbersOnly = true;
        getTextField(2).setMaxStringLength(1);
    }

    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(x, y, f);
    }
    @Override
	protected void actionPerformed(GuiButton guibutton)
    {
		GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 6)
        {
        	save();
        	scroll.clear();
        	String name = "New";
        	while(data.containsKey(name))
        		name += "_";
        	Bank bank = new Bank();
        	bank.name = name;
        	
			NBTTagCompound compound = new NBTTagCompound();
			bank.writeEntityToNBT(compound);
        	NoppesUtil.sendData(EnumPacketType.BankSave, compound);
        }
        else if(guibutton.id == 7)
        {
        	if(data.containsKey(scroll.getSelected()))
        		NoppesUtil.sendData(EnumPacketType.BankRemove, data.get(selected));
        }
        else if(guibutton.id >= 0 && guibutton.id < 6)
        {
        	bank.slotTypes.put(guibutton.id, button.getValue());
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    { 
        fontRenderer.drawString("Tab Cost", 23, 10, 0x404040);
        fontRenderer.drawString("Upg. Cost", 123, 10, 0x404040);
        fontRenderer.drawString("Start", 6, 52, 0x404040);
        fontRenderer.drawString("Max", 9, 82, 0x404040);
    }
	
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	super.drawGuiContainerBackgroundLayer(f, i, j);
      	scroll.drawScreen(i, j, f);
    }

	@Override
	public void setGuiData(NBTTagCompound compound) {
		Bank bank = new Bank();
		bank.readEntityFromNBT(compound);
		this.bank = bank;
		
		if (bank.id == -1) {
			getTextField(0).setText("");
			getTextField(1).setText("");
			getTextField(2).setText("");		
			
	        for(int i = 0; i < 6; i++)
	        {
	        	getButton(i).setDisplay(0); 
	        	getButton(i).enabled = false;
	        }
		} else {			
			getTextField(0).setText(bank.name);
			getTextField(1).setText(Integer.toString(bank.startSlots));
			getTextField(2).setText(Integer.toString(bank.maxSlots));		
			
	        for(int i = 0; i < 6; i++)
	        {
	        	int type = 0;
	        	if(bank.slotTypes.containsKey(i))
	        		type = bank.slotTypes.get(i);
	        	getButton(i).setDisplay(type); 
	        	getButton(i).enabled = true;
	        }
		}
		setSelected(bank.name);
	}
	
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);
		
		if(name != null)
			scroll.setSelected(name);
	}
	
	public void mouseClicked(int i, int j, int k)
	{
		super.mouseClicked(i, j, k);
	    if(k == 0 && scroll != null)
	    	scroll.mouseClicked(i, j, k);
	}
	
	@Override
	public void setSelected(String selected) {
		this.selected = selected;
		scroll.setSelected(selected);
	}
	
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll.id == 0)
		{
			save();
			selected = scroll.getSelected();
			NoppesUtil.sendData(EnumPacketType.BankGet, data.get(selected));
		}
	}
	
	@Override
	public void save() {
		if(selected != null && data.containsKey(selected) && bank != null){
			NBTTagCompound compound = new NBTTagCompound();
			bank.currencyInventory = container.bank.currencyInventory;
			bank.upgradeInventory = container.bank.upgradeInventory;
			bank.writeEntityToNBT(compound);
			NoppesUtil.sendData(EnumPacketType.BankSave, compound);
		}
	}

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		if(bank.id != -1) {
			if(guiNpcTextField.id == 0) {
				String name = guiNpcTextField.getText();
				if(!name.isEmpty() && !data.containsKey(name)){
					String old = bank.name;
					data.remove(bank.name);
					bank.name = name;
					data.put(bank.name, bank.id);
					selected = name;
					scroll.replace(old,bank.name);
				}
			} else if(guiNpcTextField.id == 1 || guiNpcTextField.id == 2) {
				int num = 1;
				
				if(!guiNpcTextField.isEmpty())
					num = guiNpcTextField.getInteger();
				
		        if(num > 6)
		        	num = 6;
		        
		        if(num < 0)
		        	num = 0;
				
		        if(guiNpcTextField.id == 1) {
		        	bank.startSlots = num;
		        } else if(guiNpcTextField.id == 2) {
			        bank.maxSlots = num;
		        }
		        
		        if(bank.startSlots > bank.maxSlots)
		        	bank.maxSlots = bank.startSlots;
		        
		        guiNpcTextField.setText(Integer.toString(num));
			} 
		}
	}

}

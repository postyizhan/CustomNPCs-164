// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCDialogNpcOptions;
import noppes.npcs.client.gui.GuiNPCFactionSetup;
import noppes.npcs.client.gui.GuiNPCLinesMenu;
import noppes.npcs.client.gui.GuiNPCSoundsMenu;
import noppes.npcs.client.gui.roles.GuiNpcBard;
import noppes.npcs.client.gui.roles.GuiNpcBoss;
import noppes.npcs.client.gui.roles.GuiNpcConversation;
import noppes.npcs.client.gui.roles.GuiNpcGuard;
import noppes.npcs.client.gui.roles.GuiNpcHealer;
import noppes.npcs.client.gui.roles.GuiNpcSpawner;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumJobType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.constants.EnumRoleType;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcAdvanced extends GuiNPCInterface2 implements IGuiData
{
	private boolean hasChanges = false;
    public GuiNpcAdvanced(EntityNPCInterface npc)
    {
    	super(npc, 4);
		NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedGet);
    }

    public void initGui()
    {
    	super.initGui();
    	this.addButton(new GuiNpcButton(3, guiLeft + 85 + 160, guiTop + 20, 52, 20, "selectServer.edit"));
    	this.addButton(new GuiNpcButton(8, guiLeft + 85, guiTop + 20,155,20, new String[]{"role.none","role.trader","role.follower","role.bank","role.transporter", "role.mailman", "role.mount"},npc.advanced.role.ordinal()));
    	getButton(3).enabled = npc.advanced.role != EnumRoleType.None && npc.advanced.role != EnumRoleType.Postman;

    	this.addButton(new GuiNpcButton(4, guiLeft + 85 + 160, guiTop + 43, 52, 20, "selectServer.edit"));
    	this.addButton(new GuiNpcButton(5, guiLeft + 85, guiTop + 43,155,20, new String[]{"job.none","job.bard","job.healer","job.guard","job.itemgiver","Boss(WIP)", "job.spawner", "job.conversation"},npc.advanced.job.ordinal()));

   		getButton(4).enabled = npc.advanced.job != EnumJobType.None;

    	this.addButton(new GuiNpcButton(7, guiLeft + 85, guiTop + 66, 214, 20, "advanced.lines"));
    	this.addButton(new GuiNpcButton(9, guiLeft + 85, guiTop + 89, 214, 20, "menu.factions"));
    	this.addButton(new GuiNpcButton(10, guiLeft + 85, guiTop + 112, 214, 20, "dialog.dialogs"));
    	this.addButton(new GuiNpcButton(11, guiLeft + 85, guiTop + 135, 214, 20, "advanced.sounds"));
    	
    }
	protected void actionPerformed(GuiButton guibutton) {
    	GuiNpcButton button = (GuiNpcButton) guibutton;
		if (guibutton.id == 3) {
			switch(npc.advanced.role){
			case Trader:
				save();
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupTrader);
				break;
			case Follower:
				save();
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupFollower);
				break;
			case Bank:
				save();
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupBank);
				break;
			case Transporter:
				save();
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupTransporter);
				break;
			}
		}
        if(guibutton.id == 8)
        {
			hasChanges = true;
        	npc.advanced.setRole(button.getValue());

        	getButton(3).enabled = npc.advanced.role != EnumRoleType.None && npc.advanced.role != EnumRoleType.Postman;
        }
        if(guibutton.id == 4)
        {
			switch(npc.advanced.job){
			case Bard:
				NoppesUtil.openGUI(player, new GuiNpcBard(npc));
				break;
			case Healer:
				save();
				NoppesUtil.openGUI(player, new GuiNpcHealer(npc));
				break;
			case Guard:
				save();
				NoppesUtil.openGUI(player, new GuiNpcGuard(npc));
				break;
			case ItemGiver:
	        	close();
				NoppesUtil.requestOpenGUI(EnumGuiType.SetupItemGiver);
				break;
			case Boss:
	        	close();
				NoppesUtil.openGUI(player, new GuiNpcBoss(npc));
				break;
			case Spawner:
	        	close();
				NoppesUtil.openGUI(player, new GuiNpcSpawner(npc));
				break;
			case Conversation:
	        	close();
				NoppesUtil.openGUI(player, new GuiNpcConversation(npc));
				break;
			
			}
        }
        if(guibutton.id == 5)
        {
			hasChanges = true;
        	npc.advanced.setJob(button.getValue());
        	
       		getButton(4).enabled = npc.advanced.job != EnumJobType.None;
        }
        if(guibutton.id == 9)
        {
        	NoppesUtil.openGUI(player, new GuiNPCFactionSetup(npc));
        }
        if(guibutton.id == 10)
        {
        	NoppesUtil.openGUI(player, new GuiNPCDialogNpcOptions(npc,this));
        }
        if(guibutton.id == 11)
        {
        	NoppesUtil.openGUI(player, new GuiNPCSoundsMenu(npc));
        }
		if (guibutton.id == 7) {
			NoppesUtil.openGUI(player, new GuiNPCLinesMenu(npc));
		}
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		npc.advanced.readToNBT(compound);
		initGui();
	}
	
	@Override
	public void save() {
		if(hasChanges){
			NoppesUtil.sendData(EnumPacketType.MainmenuAdvancedSave, npc.advanced.writeToNBT(new NBTTagCompound()));
			hasChanges = false;
		}
	}
    

}

package noppes.npcs.client.gui.mainmenu;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.DataStats;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiNpcMeleeProperties;
import noppes.npcs.client.gui.SubGuiNpcProjectiles;
import noppes.npcs.client.gui.SubGuiNpcRangeProperties;
import noppes.npcs.client.gui.SubGuiNpcResistanceProperties;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcStats extends GuiNPCInterface2 implements ITextfieldListener, IGuiData{
	DataStats stats;
	public GuiNpcStats(EntityNPCInterface npc) {
		super(npc,2);
		stats = npc.stats;
    	NoppesUtil.sendData(EnumPacketType.MainmenuStatsGet);
	}

    public void initGui()
    {
        super.initGui();
        addLabel(new GuiNpcLabel(0,"stats.health", guiLeft + 5, guiTop + 15, 0x404040));
        addTextField(new GuiNpcTextField(0,this, fontRenderer, guiLeft + 85, guiTop + 10, 50, 18, stats.maxHealth+""));
        getTextField(0).numbersOnly = true;
        getTextField(0).setMinMaxDefault(1, Integer.MAX_VALUE, 20);
        addLabel(new GuiNpcLabel(1,"stats.aggro", guiLeft + 140, guiTop + 15, 0x404040));
        addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft + 220, guiTop + 10, 50, 18, stats.aggroRange+""));
        getTextField(1).numbersOnly = true;
        getTextField(1).setMinMaxDefault(1, 64, 2);
        addLabel(new GuiNpcLabel(34,"stats.creaturetype", guiLeft + 275, guiTop + 15, 0x404040));
    	addButton(new GuiNpcButton(8,guiLeft + 355, guiTop + 10, 56, 20, new String[]{"stats.normal","stats.undead","stats.arthropod"} ,stats.creatureType.ordinal()));

    	addLabel(new GuiNpcLabel(2,"stats.respawn", guiLeft + 5, guiTop + 35, 0x404040));
    	addButton(new GuiNpcButton(0,guiLeft + 82, guiTop + 30, 56, 20, new String[]{"gui.yes","gui.day","gui.night","gui.no"} ,stats.spawnCycle));
    	if(stats.respawnTime > 0){
    		addLabel(new GuiNpcLabel(3,"gui.time", guiLeft + 140, guiTop + 35, 0x404040));
    		addTextField(new GuiNpcTextField(2,this, fontRenderer, guiLeft + 220, guiTop + 30, 50, 18, stats.respawnTime + ""));
    		getTextField(2).numbersOnly = true;
    		getTextField(2).setMinMaxDefault(1, 99999, 20);
    		addLabel(new GuiNpcLabel(4,"stats.deadbody", guiLeft + 275, guiTop + 35, 0x404040));
    		addButton(new GuiNpcButton(1,guiLeft + 355, guiTop + 30, 51, 20, new String[]{"gui.no","gui.yes"} ,stats.hideKilledBody?1:0));
    	}
    	
    	addLabel(new GuiNpcLabel(5,"stats.meleeproperties", guiLeft + 5, guiTop + 65, 0x404040));
    	this.addButton(new GuiNpcButton(2, guiLeft + 82, guiTop + 60, 56, 20, "selectServer.edit"));
    	addLabel(new GuiNpcLabel(6,"stats.rangedproperties", guiLeft + 5, guiTop + 89, 0x404040));
    	this.addButton(new GuiNpcButton(3, guiLeft + 82, guiTop + 84, 56, 20, "selectServer.edit"));
    	addLabel(new GuiNpcLabel(7,"stats.projectileproperties", guiLeft + 140, guiTop + 89, 0x404040));
    	this.addButton(new GuiNpcButton(9, guiLeft + 217, guiTop + 84, 56, 20, "selectServer.edit"));
    	
    	addLabel(new GuiNpcLabel(15,"potion.resistance", guiLeft + 5, guiTop + 113, 0x404040));
    	this.addButton(new GuiNpcButton(15, guiLeft + 82, guiTop + 108, 56, 20, "selectServer.edit"));

        addLabel(new GuiNpcLabel(8,"stats.walkspeed", guiLeft + 5, guiTop + 141, 0x404040));
        addTextField(new GuiNpcTextField(3,this, fontRenderer, guiLeft + 85, guiTop + 136, 50, 18, stats.moveSpeed+""));
        getTextField(3).numbersOnly = true;
        getTextField(3).setMinMaxDefault(0, 10, 4);
    	    	
    	addLabel(new GuiNpcLabel(10,"stats.fireimmune", guiLeft + 5, guiTop + 163, 0x404040));
    	addButton(new GuiNpcButton(4,guiLeft + 82, guiTop + 158, 56, 20, new String[]{"gui.no","gui.yes"} ,npc.isImmuneToFire()? 1:0));
    	addLabel(new GuiNpcLabel(11,"stats.candrown", guiLeft + 140, guiTop + 163, 0x404040));
    	addButton(new GuiNpcButton(5,guiLeft + 217, guiTop + 158, 56, 20, new String[]{"gui.no","gui.yes"} ,stats.canDrown? 1:0));
    	addLabel(new GuiNpcLabel(12,"stats.burninsun", guiLeft + 5, guiTop + 185, 0x404040));
    	addButton(new GuiNpcButton(14,guiLeft + 355, guiTop + 158, 56, 20, new String[]{"gui.no","gui.yes"} ,stats.healthRegen? 1:0));
    	addLabel(new GuiNpcLabel(14,"stats.regenhealth", guiLeft + 275, guiTop + 163, 0x404040));
    	
    	addButton(new GuiNpcButton(6,guiLeft + 82, guiTop + 180, 56, 20, new String[]{"gui.no","gui.yes"} ,stats.burnInSun? 1:0));
    	addLabel(new GuiNpcLabel(13,"stats.nofalldamage", guiLeft + 140, guiTop + 185, 0x404040));
    	addButton(new GuiNpcButton(7,guiLeft + 217, guiTop + 180, 56, 20, new String[]{"gui.no","gui.yes"} ,stats.noFallDamage? 1:0));
    }	
    
	@Override
	public void unFocused(GuiNpcTextField textfield){
		if(textfield.id == 0){
			stats.maxHealth = textfield.getInteger();
			npc.heal(stats.maxHealth);
		}
		else if(textfield.id == 1){
			stats.aggroRange = textfield.getInteger();
		}
		else if(textfield.id == 2){
			stats.respawnTime = textfield.getInteger();
		}
		else if(textfield.id == 3){
			stats.moveSpeed = textfield.getInteger();
		}
	}
    @Override
	protected void actionPerformed(GuiButton guibutton)
    {
		GuiNpcButton button = (GuiNpcButton) guibutton;
		if(button.id == 0){
			stats.spawnCycle = button.getValue();
			if(stats.spawnCycle == 3)
				stats.respawnTime = 0;
			else
				stats.respawnTime = 20;
			initGui();
		}
		else if(button.id == 1){
			stats.hideKilledBody = button.getValue() == 1;
		}
		else if (button.id == 2){
			setSubGui(new SubGuiNpcMeleeProperties(this.stats));
		}
		else if (button.id == 3){
			setSubGui(new SubGuiNpcRangeProperties(this.stats));
		}
		else if(button.id == 4){
			npc.setImmuneToFire(button.getValue() == 1);
		}
		else if(button.id == 5){
			stats.canDrown = button.getValue() == 1;
		}
		else if(button.id == 6){
			stats.burnInSun = button.getValue() == 1;
		}
		else if(button.id == 7){
			stats.noFallDamage = button.getValue() == 1;
		}
		else if (button.id == 8) {
			stats.creatureType = EnumCreatureAttribute.values()[button.getValue()];
		}
		else if (button.id == 9) {
			setSubGui(new SubGuiNpcProjectiles(this.stats));
		}
		else if(button.id == 14){
			stats.healthRegen = button.getValue() == 1;
		}
		else if (button.id == 15) {
			setSubGui(new SubGuiNpcResistanceProperties(this.stats.resistances));
		}
    }
	@Override
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuStatsSave, stats.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		stats.readToNBT(compound);
		initGui();
	}
}

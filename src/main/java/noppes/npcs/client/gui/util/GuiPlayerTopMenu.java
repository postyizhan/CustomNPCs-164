package noppes.npcs.client.gui.util;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.player.GuiFaction;
import noppes.npcs.client.gui.player.GuiQuestLog;

public class GuiPlayerTopMenu extends GuiScreen implements ITopButtonListener{

    private int x;
    private int y;
    private int active;
    
    private EntityPlayer player;
	private HashMap<Integer,GuiMenuTopButton> topButtons = new HashMap<Integer,GuiMenuTopButton>();
	
	public GuiPlayerTopMenu(int x, int y, int active, EntityPlayer player) {
		super();
		this.player = player;
		this.x = x;
		this.y = y;
		this.active = active;
		initGui();
		this.mc = Minecraft.getMinecraft();
	}
    public void initGui()
    {
        super.initGui();  
        topButtons.clear();
        
        GuiMenuTopButton inv = new GuiMenuTopButton(1, this.x, this.y, "menu.inventory");
        GuiMenuTopButton quests = new GuiMenuTopButton(2, inv, "quest.quests");
        GuiMenuTopButton factions = new GuiMenuTopButton(3, quests, "menu.factions");
        topButtons.put(1, inv);
        topButtons.put(2, quests);
        topButtons.put(3, factions);

        topButtons.get(active).active = true;
    }
    public void drawScreen(int i, int j, float f)
    {
        for(GuiMenuTopButton button: topButtons.values()){
        	button.drawButton(mc, i, j);
        }
    }
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    }
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
        if (k == 0)
        {
            for (GuiMenuTopButton button : topButtons.values())
            {
                if (button.mousePressed(mc, i, j))
                {
                    topButtonPressed(button);
                }
            }
        }
    }
    private void topButtonPressed(GuiMenuTopButton button) {
    	if(button.active)
    		return;
        mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        if(button.id == 1){
        	NoppesUtil.openGUI(player, new GuiInventory(player));
        }
        if(button.id == 2){
        	NoppesUtil.openGUI(player, new GuiQuestLog(mc.thePlayer));
        }
        if(button.id == 3){
        	NoppesUtil.openGUI(player, new GuiFaction());
        }
    }
}

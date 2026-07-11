package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.DataAI;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPacketType;

import org.lwjgl.opengl.GL11;

public class GuiNpcPather extends GuiNPCInterface implements IGuiData{
    
    private GuiCustomScroll scroll;
    private HashMap<String, Integer> data = new HashMap<String, Integer>();
    private DataAI ai;
    
	public GuiNpcPather(EntityNPCInterface npc) {
		super();
		drawDefaultBackground = false;
        xSize = 176;
        title = "Npc Pather";
        setBackground("smallbg.png");
        ai = npc.ai;
    	NoppesUtil.sendData(EnumPacketType.MainmenuAIGet);
	}
	@Override
    public void initGui()
    {
        super.initGui();
        scroll = new GuiCustomScroll(this,0);
        scroll.setSize(160, 164);
        List<String> list = new ArrayList<String>();
        for(int[] arr: ai.getMovingPath()){
        	list.add("x:" + arr[0] + " y:" + arr[1] + " z:" + arr[2]);
        }
        scroll.setUnsortedList(list);
        scroll.guiLeft = guiLeft + 7;
        scroll.guiTop = guiTop + 12;
        
        addScroll(scroll);
        this.addButton(new GuiNpcButton(0, guiLeft + 6, guiTop + 178,52,20, "gui.down"));
        this.addButton(new GuiNpcButton(1, guiLeft + 62, guiTop + 178,52,20, "gui.up"));
        this.addButton(new GuiNpcButton(2, guiLeft + 118, guiTop + 178,52,20, "selectWorld.deleteButton"));
    }

	protected void actionPerformed(GuiButton guibutton)
    {
    	
    	if(scroll.selected < 0)
    		return;
    	if(guibutton.id == 0){
    		List<int[]> list = ai.getMovingPath();
    		int selected = scroll.selected;
    		if(list.size() <= selected + 1)
    			return;
    		int[] a = list.get(selected);
    		int[] b = list.get(selected + 1);
    		list.set(selected, b);
    		list.set(selected + 1, a);
    		ai.setMovingPath(list);
    		initGui();
    		scroll.selected = selected + 1;
    	}
    	if(guibutton.id == 1){
        	if(scroll.selected - 1 < 0)
        		return;
    		List<int[]> list = ai.getMovingPath();
    		int selected = scroll.selected;
    		int[] a = list.get(selected);
    		int[] b = list.get(selected - 1);
    		list.set(selected, b);
    		list.set(selected - 1, a);
    		ai.setMovingPath(list);
    		initGui();
    		scroll.selected = selected - 1;
    	}
    	if(guibutton.id == 2){
    		List<int[]> list = ai.getMovingPath();
    		if(list.size() <= 1)
    			return;
    		list.remove(scroll.selected);
    		ai.setMovingPath(list);
    		initGui();
    	}
    }
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    }
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	scroll.mouseClicked(i, j, k);
    }
    @Override
    public void keyTyped(char c, int i)
    {
        if (i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
            close();
        }
    }
    
	@Override
	public void save() {
		NoppesUtil.sendData(EnumPacketType.MainmenuAISave, ai.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void setGuiData(NBTTagCompound compound) {
		ai.readToNBT(compound);
		initGui();
	}

}

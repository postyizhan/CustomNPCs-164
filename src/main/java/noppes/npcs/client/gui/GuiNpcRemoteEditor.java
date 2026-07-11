package noppes.npcs.client.gui;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.entity.Entity;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcRemoteEditor extends GuiNPCInterface implements IScrollData{
    
    private GuiCustomScroll scroll;
    private HashMap<String, Integer> data = new HashMap<String, Integer>();
	public GuiNpcRemoteEditor() {
		super();
        xSize = 256;
        setBackground("menubg.png");
        NoppesUtil.sendData(EnumPacketType.RemoteNpcsGet);
	}
    public void initGui()
    {
        super.initGui();
        if(scroll == null){
	        scroll = new GuiCustomScroll(this,0);
	        scroll.setSize(165, 208);
	        scroll.guiLeft = guiLeft + 4;
	        scroll.guiTop = guiTop + 4;
        }
        addScroll(scroll);
        
        String title = StatCollector.translateToLocal("remote.title");
        int x = (xSize - this.fontRenderer.getStringWidth(title)) / 2;
        
        this.addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8, 0xffffff));

        this.addButton(new GuiNpcButton(0, guiLeft + 170, guiTop + 6,82,20, "selectServer.edit"));
        this.addButton(new GuiNpcButton(1, guiLeft + 170, guiTop + 28,82,20, "selectWorld.deleteButton"));
        this.addButton(new GuiNpcButton(2, guiLeft + 170, guiTop + 50,82,20, "remote.reset"));
        this.addButton(new GuiNpcButton(4, guiLeft + 170, guiTop + 72,82,20, "remote.tp"));
        this.addButton(new GuiNpcButton(5, guiLeft + 170, guiTop + 110,82,20, "remote.resetall"));
        this.addButton(new GuiNpcButton(3, guiLeft + 170, guiTop + 132,82,20, "remote.freeze"));
    }

    @Override
    public void confirmClicked(boolean flag, int i)
    {
		if(flag){
    		NoppesUtil.sendData(EnumPacketType.RemoteDelete,data.get(scroll.getSelected()));
		}
		NoppesUtil.openGUI(player, this);
    }
	protected void actionPerformed(GuiButton guibutton)
    {
    	if(guibutton.id == 3){
    		NoppesUtil.sendData(EnumPacketType.RemoteFreeze);
    	}
    	if(guibutton.id == 5){
    		for(int id : data.values()){
        		NoppesUtil.sendData(EnumPacketType.RemoteReset, id);
	    		Entity entity  = player.worldObj.getEntityByID(id);
	    		if(entity != null && entity instanceof EntityNPCInterface)
	    			((EntityNPCInterface)entity).reset();
    		}
    	}
    	
    	if(!data.containsKey(scroll.getSelected()))
    		return;
    	
    	if(guibutton.id == 0){
    		NoppesUtil.sendData(EnumPacketType.RemoteMainMenu,data.get(scroll.getSelected()));
    	}
    	if(guibutton.id == 1){
            GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
            mc.displayGuiScreen(guiyesno);
    	}
    	if(guibutton.id == 2){
    		NoppesUtil.sendData(EnumPacketType.RemoteReset,data.get(scroll.getSelected()));
    		Entity entity  = player.worldObj.getEntityByID(data.get(scroll.getSelected()));
    		if(entity != null && entity instanceof EntityNPCInterface)
    			((EntityNPCInterface)entity).reset();
    	}
    	if(guibutton.id == 4){
    		NoppesUtil.sendData(EnumPacketType.RemoteTpToNpc,data.get(scroll.getSelected()));
    		close();
    	}
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		scroll.setList(list);
		this.data = data;
	}
	@Override
	public void setSelected(String selected) {
		getButton(3).displayString = selected;
	}

}

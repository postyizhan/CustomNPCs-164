package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.client.controllers.CloneController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.SubGuiInterface;

public class GuiNpcMobSpawnerSelector extends SubGuiInterface{
    
    private GuiCustomScroll scroll;
        
    private HashMap<String,NBTTagCompound> cloneData = new HashMap<String,NBTTagCompound>();
    
    private ArrayList<String> list;

	private static String search = "";
	
	private int activeTab =  1;
	
    
	public GuiNpcMobSpawnerSelector() {
		super();
        xSize = 256;
        
        this.closeOnEsc = true;
        
        setBackground("menubg.png");
	}
    public void initGui()
    {
        super.initGui();
        
        if(scroll == null){
	        scroll = new GuiCustomScroll(this,0);
	        scroll.setSize(165, 188);
	        scroll.guiLeft = guiLeft + 4;
	        scroll.guiTop = guiTop + 26;
        }
        else
        	scroll.clear();
        addScroll(scroll);
        
    	addTextField(new GuiNpcTextField(1, this, fontRenderer, guiLeft + 4, guiTop + 4, 165, 20, search));

    	addButton(new GuiNpcButton(0, guiLeft + 171, guiTop + 80, 80, 20, "gui.done"));
    	addButton(new GuiNpcButton(1, guiLeft + 171, guiTop + 103, 80, 20, "gui.cancel"));
                
    	addSideButton(new GuiMenuSideButton(21,guiLeft - 69, this.guiTop + 2, 70,22, "Tab 1"));
    	addSideButton(new GuiMenuSideButton(22,guiLeft - 69, this.guiTop + 23, 70,22, "Tab 2"));
    	addSideButton(new GuiMenuSideButton(23,guiLeft - 69, this.guiTop + 44, 70,22, "Tab 3"));
    	addSideButton(new GuiMenuSideButton(24,guiLeft - 69, this.guiTop + 65, 70,22, "Tab 4"));
    	addSideButton(new GuiMenuSideButton(25,guiLeft - 69, this.guiTop + 86, 70,22, "Tab 5"));
    	addSideButton(new GuiMenuSideButton(26,guiLeft - 69, this.guiTop + 107, 70,22, "Tab 6"));
    	addSideButton(new GuiMenuSideButton(27,guiLeft - 69, this.guiTop + 128, 70,22, "Tab 7"));
    	addSideButton(new GuiMenuSideButton(28,guiLeft - 69, this.guiTop + 149, 70,22, "Tab 8"));
    	addSideButton(new GuiMenuSideButton(29,guiLeft - 69, this.guiTop + 170, 70,22, "Tab 9"));
    	
    	getSideButton(20 + activeTab).active = true;
    	showClones();
    }
	private void showClones() {
		cloneData.clear();
        ArrayList<String> list = new ArrayList<String>();
        for(NBTTagCompound comp : CloneController.getClones()){
        	String name = comp.getString("ClonedName");
        	int i = 1;
        	while(list.contains(name)){
        		i++;
            	name = String.format("%s%s", comp.getString("ClonedName"), i);
        	}
        	int tab = 1;
        	if(comp.hasKey("ClonedTab"))
        		tab = comp.getInteger("ClonedTab");
        	if(activeTab == tab){
            	list.add(name);
        		cloneData.put(name, comp);
        	}
        }
        this.list = list;
        scroll.setList(getSearchList());
	}
    public void keyTyped(char c, int i)
    {
    	super.keyTyped(c, i);
    	
    	if(search.equals(getTextField(1).getText()))
    		return;
    	search = getTextField(1).getText().toLowerCase();
    	scroll.setList(getSearchList());
    }
    private List<String> getSearchList(){
    	if(search.isEmpty())
    		return new ArrayList<String>(list);
    	List<String> list = new ArrayList<String>();
    	for(String name : this.list){
    		if(name.toLowerCase().contains(search))
    			list.add(name);
    	}
    	return list;
    }
	public NBTTagCompound getCompound(){
    	String sel = scroll.getSelected();
    	if(sel == null)
    		return null;

		NBTTagCompound compound = cloneData.get(sel);
		compound.removeTag("StartPos");
		return compound;
		
	}
	
	protected void actionPerformed(GuiButton guibutton)
    {
    	if(guibutton.id == 0){
    		close();
    	}
    	if(guibutton.id == 1){
    		scroll.clear();
    		close();
    	}
    	if(guibutton.id > 20){
    		activeTab = guibutton.id - 20;
    		initGui();
    	}
    }
    protected NBTTagList newDoubleNBTList(double ... par1ArrayOfDouble)
    {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble = par1ArrayOfDouble;
        int i = par1ArrayOfDouble.length;

        for (int j = 0; j < i; ++j)
        {
            double d1 = adouble[j];
            nbttaglist.appendTag(new NBTTagDouble((String)null, d1));
        }

        return nbttaglist;
    }
	
	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

}

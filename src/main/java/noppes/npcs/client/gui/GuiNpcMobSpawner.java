package noppes.npcs.client.gui;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.controllers.CloneController;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcMobSpawner extends  GuiNPCInterface{
    
    private GuiCustomScroll scroll;
    
    private int posX,posY,posZ;
    
    private HashMap<String,NBTTagCompound> cloneData = new HashMap<String,NBTTagCompound>();    
    private List<NBTTagCompound> data = new ArrayList<NBTTagCompound>();
    
    private ArrayList<String> list;
    
    private static boolean showingClones = true;

	private static String search = "";
	
	private int activeTab =  1;
    
	public GuiNpcMobSpawner(int i, int j, int k) {
		super();
        xSize = 256;

        posX = i;
        posY = j;
        posZ = k;
        
        this.closeOnEsc = true;
        
        setBackground("menubg.png");
	}
    public void initGui()
    {
        super.initGui();
        guiTop += 10;
        
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

        GuiMenuTopButton button;
        addTopButton(button = new GuiMenuTopButton(3,guiLeft + 4, guiTop - 17, "spawner.clones"));
        button.active = showingClones;
        addTopButton(button = new GuiMenuTopButton(4, button, "spawner.entities"));
        button.active = !showingClones;
        
        addButton(new GuiNpcButton(1, guiLeft + 170, guiTop + 6, 82, 20, "item.monsterPlacer.name"));
        
        addButton(new GuiNpcButton(2, guiLeft + 170, guiTop + 100, 82, 20, "spawner.mobspawner"));
        
        if(showingClones){
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
        else
        	showEntities();
    }

	private void showEntities() {
        Map<?,?> data = EntityList.stringToClassMapping;
        ArrayList<String> list = new ArrayList<String>();
        for(Object name : data.keySet()){
        	Class<?> c = (Class<?>) data.get(name);
        	try {
        		if(EntityLiving.class.isAssignableFrom(c) && c.getConstructor(new Class[] {World.class}) != null && !Modifier.isAbstract(c.getModifiers()))
        				list.add(name.toString());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
			}
        }  
        this.list = list;
        scroll.setList(getSearchList());
	}
	private void showClones() {
        addButton(new GuiNpcButton(5, guiLeft + 170, guiTop + 30, 82, 20, "gui.remove"));
        
		cloneData.clear();
        ArrayList<String> list = new ArrayList<String>();
        data = CloneController.getClones();
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
	private NBTTagCompound getCompound(){
    	String sel = scroll.getSelected();
    	if(sel == null)
    		return null;
    	
    	if(showingClones){
    		NBTTagCompound compound = cloneData.get(sel);
    		compound.removeTag("StartPos");
    		compound.removeTag("Pos");
    		if(!compound.hasKey("ModRev"))
    			compound.setInteger("ModRev", 1);
    		return compound;
    	}
    	else{
    		Entity entity = EntityList.createEntityByName(sel, Minecraft.getMinecraft().theWorld);
    		NBTTagCompound compound = new NBTTagCompound();
    		entity.writeToNBTOptional(compound);
    		return compound;
    	}
		
	}
	
	protected void actionPerformed(GuiButton guibutton)
    {
    	if(guibutton.id == 0){
    		close();
    	}
    	if(guibutton.id == 1){
    		NBTTagCompound compound = getCompound();
    		if(compound != null){
    			compound.setTag("Pos", this.newDoubleNBTList(new double[] {this.posX + 0.5, this.posY + 1, this.posZ + 0.5}));
	    		compound.setInteger("ItemGiverId", 0);
	    		compound.setInteger("TransporterId", -1);
    			NoppesUtil.sendData(EnumPacketType.SpawnMob, compound);
	    		close();
    		}
    	}
    	if(guibutton.id == 2){
    		NBTTagCompound compound = getCompound();
    		if(compound != null){
	    		NoppesUtil.sendData(EnumPacketType.MobSpawner,posX,posY,posZ,compound);
	    		close();
    		}
    	}
    	if(guibutton.id == 3){
    		showingClones = true;
    		initGui();
    	}
    	if(guibutton.id == 4){
    		showingClones = false;
    		initGui();
    	}
    	if(guibutton.id == 5){
    		if(scroll.getSelected() != null){
    			NBTTagCompound toCheck = cloneData.get(scroll.getSelected());
    			data.remove(toCheck);
    			CloneController.saveClones(data);
    			scroll.selected = -1;
        		initGui();
    		}
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

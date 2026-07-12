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
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiMenuTopButton;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ISubGuiListener;
import noppes.npcs.client.gui.util.SubGuiInterface;
import noppes.npcs.constants.EnumPacketType;

public class GuiNpcMobSpawner extends GuiNPCInterface implements GuiCustomScrollActionListener, ISubGuiListener{
    
    private GuiCustomScroll scroll;
    private GuiCustomScroll folderScroll;
    
    private int posX,posY,posZ;
    
    private HashMap<String,NBTTagCompound> cloneData = new HashMap<String,NBTTagCompound>();    
    private List<NBTTagCompound> data = new ArrayList<NBTTagCompound>();
    
    private ArrayList<String> list;
    
    private static boolean showingClones = true;

	private static String search = "";
	
	private String activeFolder = "Default";
	private boolean renamingFolder = false;
    
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
			if(folderScroll == null){
				folderScroll = new GuiCustomScroll(this,1);
				folderScroll.setSize(70, 188);
				folderScroll.guiLeft = guiLeft - 73;
				folderScroll.guiTop = guiTop + 26;
			}
			else
				folderScroll.clear();
			folderScroll.setList(CloneController.getFolders());
			folderScroll.setSelected(activeFolder);
			addScroll(folderScroll);

			addButton(new GuiNpcButton(5, guiLeft + 170, guiTop + 30, 82, 20, "gui.remove"));
			addButton(new GuiNpcButton(6, guiLeft + 170, guiTop + 54, 82, 20, "cloner.newfolder"));
			addButton(new GuiNpcButton(7, guiLeft + 170, guiTop + 78, 82, 20, "cloner.renamefolder"));
			addButton(new GuiNpcButton(8, guiLeft + 170, guiTop + 124, 82, 20, "cloner.deletefolder"));
			getButton(7).enabled = !activeFolder.equals("Default");
			getButton(8).enabled = !activeFolder.equals("Default");
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
		cloneData.clear();
        ArrayList<String> list = new ArrayList<String>();
        data = CloneController.getClones();
		for(NBTTagCompound comp : data){
			String name = comp.getString("ClonedName");
			int i = 1;
			while(list.contains(name)){
				i++;
            	name = String.format("%s%s", comp.getString("ClonedName"), i);
			}
			if(CloneController.getFolderOf(comp).equals(activeFolder)){
            	list.add(name);
        		cloneData.put(name, comp);
        	}
        }
        this.list = list;
		scroll.selected = -1;
        scroll.setList(getSearchList());
	}
    public void keyTyped(char c, int i)
    {
		if(hasSubGui()){
			String currentSearch = getTextField(1).getText();
			super.keyTyped(c, i);
			getTextField(1).setText(currentSearch);
			return;
		}
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
		if(guibutton.id == 6){
			renamingFolder = false;
			setSubGui(new SubGuiCloneFolderName(""));
		}
		if(guibutton.id == 7 && !activeFolder.equals("Default")){
			renamingFolder = true;
			setSubGui(new SubGuiCloneFolderName(activeFolder));
		}
		if(guibutton.id == 8 && !activeFolder.equals("Default")){
			CloneController.deleteFolder(activeFolder);
			activeFolder = "Default";
			initGui();
		}
    }
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		if(guiCustomScroll == folderScroll && folderScroll.getSelected() != null){
			activeFolder = folderScroll.getSelected();
			showClones();
			getButton(7).enabled = !activeFolder.equals("Default");
			getButton(8).enabled = !activeFolder.equals("Default");
		}
		else if(guiCustomScroll == scroll){
			return;
		}
	}
	@Override
	public void subGuiClosed(SubGuiInterface subgui) {
		if(subgui instanceof SubGuiCloneFolderName){
			SubGuiCloneFolderName folderGui = (SubGuiCloneFolderName) subgui;
			if(folderGui.folderName == null)
				return;
			if(renamingFolder)
				CloneController.renameFolder(activeFolder, folderGui.folderName);
			activeFolder = folderGui.folderName;
			ArrayList<String> folders = CloneController.getFolders();
			if(!folders.contains(activeFolder))
				folders.add(activeFolder);
			folderScroll.setList(folders);
			folderScroll.setSelected(activeFolder);
			showClones();
			getButton(7).enabled = !activeFolder.equals("Default");
			getButton(8).enabled = !activeFolder.equals("Default");
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

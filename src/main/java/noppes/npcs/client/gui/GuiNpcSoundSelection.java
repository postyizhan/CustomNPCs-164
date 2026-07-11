// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcSoundSelection extends GuiNPCInterface
{
	private GuiNPCStringSlot slot;
	private Object parent;
	int depth = 0;
	private String location = "";

	private String selectedLocation = "";
	private String selectedFile = "";

	private List<String> folders = new ArrayList<String>();
	private List<String> files = new ArrayList<String>();
	
	private String up = "..<" + StatCollector.translateToLocal("gui.up") + ">..";
	
    public GuiNpcSoundSelection(EntityNPCInterface npc,Object parent, String sound)
    {
    	super(npc);
    	sound = sound.replaceAll("\\.", "/");
    	int index = sound.lastIndexOf("/");
    	if(index > 0){
    		selectedFile = sound.substring(index + 1);
	    	sound = sound.substring(0, index);
	    	if(sound.startsWith("customnpcs:")){
	    		location = "customnpcs/" + sound.substring(11);
	    	}
	    	else{
	    		location = "minecraft/" + sound;
	    	}	    	
	    	depth = location.split("/").length;
	    	
	    	selectedLocation = location;
    	}
    	
    	drawDefaultBackground = false;
    	title = "";
    	this.parent = parent;
    }

    public void initGui()
    {
        super.initGui();

        String ss = StatCollector.translateToLocal("gui.currentFolder") + ": " + location;
        addLabel(new GuiNpcLabel(0,ss, width / 2 - (this.fontRenderer.getStringWidth(ss)/2), 20, 0xffffff));
        
        slot = new GuiNPCStringSlot(getList(),this,npc,false,18);
        slot.registerScrollButtons(4, 5);
        
        if(depth > 0 && selectedLocation.equals(location))
        	slot.selected = selectedFile;

    	this.addButton(new GuiNpcButton(2, width / 2 - 100, height - 44,98, 20, "gui.back"));
    	this.addButton(new GuiNpcButton(1, width / 2 + 2, height - 44,98, 20, "gui.play"));
    }
    
    private List<String> getList(){
    	folders.clear();
    	files.clear();
    	List<String> list = new ArrayList<String>();
    	
    	if(depth == 0){
    		folders.add("minecraft");
    		folders.add("customnpcs");
    	}
    	else{
        	list.add(up);
        	
        	String loc;
        	if(location.startsWith("customnpcs"))
        		loc = "/customnpcs/sound" + location.substring(10);
        	else
        		loc = "/sound" + location.substring(9);
        	
        	AssetsBrowser browser = new AssetsBrowser(loc, new String[]{"ogg","wav"});
        	for(String folder : browser.folders){
        		folders.add("/" + folder);
        	}
        	for(String file : browser.files){
        		file = file.substring(0,file.lastIndexOf("."));

                while (Character.isDigit(file.charAt(file.length() - 1)))
                {
                	file = file.substring(0, file.length() - 1);
                }
                if(!files.contains(file))
                	files.add(file);
        	}
    	}

    	list.addAll(folders);
    	list.addAll(files);
    	return list;
    }


    public void drawScreen(int i, int j, float f)
    {        
    	slot.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }
    @Override
    public void elementClicked(){
    	if(slot.selected != null && this.files.contains(slot.selected)){
    		selectedLocation = location;
    		selectedFile = slot.selected;
    		if(parent instanceof GuiNPCInterface){
    			((GuiNPCInterface)parent).elementClicked();
    		}
    		else if(parent instanceof GuiNPCInterface2){
    			((GuiNPCInterface2)parent).elementClicked();
    		}
    	}
    }
    public String getSelected(){
    	String file;
    	if(location.startsWith("customnpcs/"))
    		file = "customnpcs:" + location.substring(11);
    	else
    		file = location.substring(10);
    	file = file.replaceAll("/", ".");
    	return file + "." + slot.selected;
    }
    @Override
    public void doubleClicked(){
    	if(slot.selected.equals(up)){
    		depth--;
    		if(depth == 0){
    			location = "";
    		}
    		else{
    			location = location.substring(0, location.lastIndexOf("/"));
    		}
    		initGui();
    	}
    	else if(folders.contains(slot.selected)){
    		depth++;
    		if(location.endsWith("/") && slot.selected.startsWith("/"))
    			location += slot.selected.substring(1);
    		else
    			location += slot.selected;
    		initGui();
    	} 
    	else{
    		close();
    		NoppesUtil.openGUI(player, parent);
    	}
    }

	protected void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 1)
        {
        	if(files.contains(slot.selected))
        		mc.sndManager.playSoundFX(getSelected(), 1, 1);
        }
        if(guibutton.id == 2)
        {
        	close();
        	NoppesUtil.openGUI(player, parent);
        }
    }
	public void save() {
	}


}

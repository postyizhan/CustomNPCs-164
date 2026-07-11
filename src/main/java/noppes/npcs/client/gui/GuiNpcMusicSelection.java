// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.AssetsBrowser;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.controllers.MusicController;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.client.gui.util.GuiNPCStringSlot;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcMusicSelection extends GuiNPCInterface
{
	private GuiNPCStringSlot slot;
	private GuiScreen parent;
	public GuiScreen listener;
	int depth = 0;
	private String location = "";

	private String selectedLocation = "";
	private String selectedFile = "";

	private List<String> folders = new ArrayList<String>();
	private List<String> files = new ArrayList<String>();
	
	private String up = "..<" + StatCollector.translateToLocal("gui.up") + ">..";
	
    public GuiNpcMusicSelection(EntityNPCInterface npc,GuiScreen parent, String sound)
    {
    	super(npc);
    	sound = sound.replaceAll("\\.", "/");
    	if(sound.startsWith("customnpcs:")){
        	int index = sound.lastIndexOf("/");
        	if(index < 0)
        		index = 11;
    		selectedFile = sound.substring(index + 1);
	    	sound = sound.substring(0, index);
    		location = "/" + sound.substring(11);
	    	depth = location.split("/").length;
	    	selectedLocation = location;
    	}
    	
    	drawDefaultBackground = false;
    	title = "";
    	this.listener = this.parent = parent;
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
    	
    	if(depth > 0){
        	list.add(up);
    	}
    	
    	AssetsBrowser browser = new AssetsBrowser("/customnpcs/music" + location, new String[]{"ogg","wav"});
    	for(String folder : browser.folders){
    		folders.add("/" + folder);
    	}
    	for(String file : browser.files){
    		file = file.substring(0,file.lastIndexOf("."));
    		
            if(!files.contains(file))
            	files.add(file);
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
    	}
    }
    public String getSelected(){
    	if(selectedFile == null || selectedFile.isEmpty())
    		return "";
    	String file = selectedFile;
    	if(!selectedLocation.isEmpty())
    		file = selectedLocation.replaceAll("/", ".").substring(1) + "." + file;
    	
    	return "customnpcs:" + file;
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
        	MusicController.Instance.playMusic(getSelected());
        }
        if(guibutton.id == 2)
        {
        	close();
        	NoppesUtil.openGUI(player, parent);
        }
    }
	public void save() {
		if(listener instanceof GuiNPCInterface){
			((GuiNPCInterface)listener).elementClicked();
		}
		else if(listener instanceof GuiNPCInterface2){
			((GuiNPCInterface2)listener).elementClicked();
		}
	}


}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui.global;

import java.util.HashMap;
import java.util.Vector;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface2;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.IScrollData;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import noppes.npcs.containers.ContainerManageRecipes;
import noppes.npcs.controllers.RecipeCarpentry;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class GuiNpcManageRecipes extends GuiContainerNPCInterface2 implements IScrollData, IGuiData, GuiCustomScrollActionListener,ITextfieldListener
{
    private GuiCustomScroll scroll;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private ContainerManageRecipes container;
	private RecipeCarpentry recipe = new RecipeCarpentry();
	private String selected = null;
	private ResourceLocation slot;
	
    public GuiNpcManageRecipes(EntityNPCInterface npc,ContainerManageRecipes container)
    {
    	super(npc,container);
    	this.container = container;
    	drawDefaultBackground = false;
    	NoppesUtil.sendData(EnumPacketType.RecipesGet,container.width);
        setBackground("npctradersetup.png");
        slot = getResource("slot.png");
    }

	@Override
    public void initGui()
    {
        super.initGui();
        
        scroll = new GuiCustomScroll(this,0);
        scroll.setWorldAndResolution(mc, 350, 250);
        scroll.setSize(130, 180);
        scroll.guiLeft = guiLeft + 172;
        scroll.guiTop = guiTop + 8;
        
    	this.addButton(new GuiNpcButton(0,guiLeft + 306, guiTop + 10, 84, 20, "menu.global"));
    	this.addButton(new GuiNpcButton(1,guiLeft + 306, guiTop + 32, 84, 20, "tile.npcCarpentyBench.name"));
    	this.getButton(0).enabled = container.width == 4;
    	this.getButton(1).enabled = container.width == 3;

    	this.addButton(new GuiNpcButton(3,guiLeft + 306, guiTop + 60, 84, 20, "gui.add"));
    	this.addButton(new GuiNpcButton(4,guiLeft + 306, guiTop + 82, 84, 20, "gui.remove"));
    	
    	
    	this.addLabel(new GuiNpcLabel(0, "gui.ignoreDamage", guiLeft + 86, guiTop + 32, 0x404040));
    	this.addButton(new GuiNpcButton(5,guiLeft + 114, guiTop + 40, 30, 20, new String[]{"gui.no","gui.yes"},0));
    	
    	this.addTextField(new GuiNpcTextField(0, this, fontRenderer, guiLeft + 8, guiTop + 8, 160, 20, recipe.name));
    	this.getTextField(0).enabled = false;
    	this.getButton(5).enabled = false;
    }

	@Override
    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(x, y, f);
    }

	@Override
	protected void actionPerformed(GuiButton guibutton)
    {
		GuiNpcButton button = (GuiNpcButton) guibutton;
        if(guibutton.id == 0){
        	save();
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes,3,0,0);
        }
        if(guibutton.id == 1){
        	save();
        	NoppesUtil.requestOpenGUI(EnumGuiType.ManageRecipes,4,0,0);
        }
        if(guibutton.id == 3)
        {
        	save();
        	scroll.clear();
        	String name = "New";
        	while(data.containsKey(name))
        		name += "_";
        	RecipeCarpentry recipe = new RecipeCarpentry(-1, name);
        	recipe.isGlobal = container.width == 3;
        	NoppesUtil.sendData(EnumPacketType.RecipeSave,recipe.writeNBT());
        }
        if(guibutton.id == 4)
        {
        	if(data.containsKey(scroll.getSelected())){
        		NoppesUtil.sendData(EnumPacketType.RecipeRemove, data.get(scroll.getSelected()));
        		scroll.clear();
        	}
        }
        if(guibutton.id == 5)
        {
        	recipe.ignoreDamage = button.getValue() == 1;
        }
    }
	public void doubleClicked() {
	}
	@Override
	public void setGuiData(NBTTagCompound compound) {
		RecipeCarpentry recipe = new RecipeCarpentry();
		recipe.readNBT(compound);
		this.recipe = recipe;
		getTextField(0).setText(recipe.name);
		container.setRecipe(recipe);
		this.getTextField(0).enabled = true;
    	this.getButton(5).enabled = true;
    	this.getButton(5).setDisplay(recipe.ignoreDamage?1:0);
		setSelected(recipe.name);
	}

	@Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
    	super.drawGuiContainerBackgroundLayer(f, x, y);
    	scroll.drawScreen(x, y, f);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(slot);
    	
    	for(int i = 0; i < container.width;i++){
        	for(int j = 0; j < container.width;j++){
        		drawTexturedModalRect(guiLeft + i*18 + 7, guiTop + j*18 + 34, 0, 0, 20, 20);
        	}
    	}
		drawTexturedModalRect(guiLeft + 86, guiTop + 60, 0, 0, 20, 20);
    }
	@Override
	public void setData(Vector<String> list, HashMap<String, Integer> data) {
		String name = scroll.getSelected();
		this.data = data;
		scroll.setList(list);

		this.getTextField(0).enabled = name != null;
		this.getButton(5).enabled = name != null;
		
		if(name != null)
			scroll.setSelected(name);
	}
	@Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	if(k == 0 && scroll != null)
    		scroll.mouseClicked(i, j, k);
    }
	@Override
	public void setSelected(String selected) {
		this.selected = selected;
		scroll.setSelected(selected);
	}

	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
		save();
		selected = scroll.getSelected();
		NoppesUtil.sendData(EnumPacketType.RecipeGet, data.get(selected));
	}

	@Override
	public void save() {
		GuiNpcTextField.unfocus();
		if(selected != null && data.containsKey(selected)){
			container.saveRecipe();
			NoppesUtil.sendData(EnumPacketType.RecipeSave, recipe.writeNBT());
		}
	}

	@Override
	public void unFocused(GuiNpcTextField guiNpcTextField) {
		String name = guiNpcTextField.getText();
		if(!name.isEmpty() && !data.containsKey(name)){
			String old = recipe.name;
			data.remove(recipe.name);
			recipe.name = name;
			data.put(recipe.name, recipe.id);
			selected = name;
			scroll.replace(old,recipe.name);
		}
	}
}

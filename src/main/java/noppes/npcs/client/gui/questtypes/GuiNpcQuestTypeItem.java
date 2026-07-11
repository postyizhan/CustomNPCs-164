// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.questtypes;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.global.GuiNPCManageQuest;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.containers.ContainerNpcQuestTypeItem;
import noppes.npcs.controllers.Quest;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcQuestTypeItem extends GuiContainerNPCInterface implements ITextfieldListener
{
	private Quest quest;
	private static final ResourceLocation field_110422_t = new ResourceLocation("customnpcs","textures/gui/followersetup.png");
    public GuiNpcQuestTypeItem(EntityNPCInterface npc,ContainerNpcQuestTypeItem container)
    {
        super(npc,container);
        this.quest = GuiNPCManageQuest.quest;
        title = "Quest Item Setup";
        ySize = 202;
    }
    public void initGui()
    {
        super.initGui();
        addButton(new GuiNpcButton(5, guiLeft, guiTop+ ySize, 98, 20, "gui.back"));
    }

    public void actionPerformed(GuiButton guibutton)
    {
        if(guibutton.id == 5)
        {
        	NoppesUtil.openGUI(player,GuiNPCManageQuest.Instance);
        }
    }

    public void onGuiClosed()
    {
    }
    protected void keyTyped(char c, int i)
    {
    	
    }
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(field_110422_t);
        int l = (width - xSize) / 2;
        int i1 = (height - ySize) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
    	super.drawGuiContainerBackgroundLayer(f, i, j);
    }
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
    }
	@Override
	public void save() {
//    	HashMap<Integer,ItemStack> map = new HashMap<Integer,ItemStack>();
//    	for(int i= 0;i < container.invMatrix.getSizeInventory();i++){
//    		ItemStack item = container.invMatrix.getStackInSlot(i);
//    		if(item != null)
//    			map.put(i, item.copy());
//        }
//    	((QuestItem)quest.questInterface).items = map;
//    	QuestController.saveQuest(quest);
	}
	@Override
	public void unFocused(GuiNpcTextField textfield) {
		quest.rewardExp = textfield.getInteger();
	}
}

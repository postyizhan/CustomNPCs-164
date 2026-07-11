// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.gui.global;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiContainerNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.GuiNpcTextField;
import noppes.npcs.client.gui.util.ITextfieldListener;
import noppes.npcs.containers.ContainerNpcQuestReward;
import noppes.npcs.controllers.Quest;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            GuiContainer, ContainerWorkbench, FontRenderer, RenderEngine, 
//            InventoryPlayer, World

public class GuiNpcQuestReward extends GuiContainerNPCInterface implements ITextfieldListener
{
	private Quest quest;
	private ResourceLocation resource;
    public GuiNpcQuestReward(EntityNPCInterface npc,ContainerNpcQuestReward container)
    {
        super(npc,container);
        this.quest = GuiNPCManageQuest.quest;
        resource = getResource("questreward.png");
    }
    public void initGui()
    {
        super.initGui();
        addButton(new GuiNpcButton(5, guiLeft, guiTop+ ySize, 98, 20, "gui.back"));
        addLabel(new GuiNpcLabel(1,"Exp:", guiLeft+5, guiTop+45, 0x404040));
        addTextField(new GuiNpcTextField(0, this, this.fontRenderer, guiLeft+30, guiTop+40, 60, 20, quest.rewardExp + ""));
        getTextField(0).numbersOnly = true;
        getTextField(0).setMinMaxDefault(0, 99999, 0);
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
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
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
		
	}
	@Override
	public void unFocused(GuiNpcTextField textfield) {
		quest.rewardExp = textfield.getInteger();
	}
}

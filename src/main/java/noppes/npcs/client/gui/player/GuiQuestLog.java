package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.QuestLogData;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiMenuSideButton;
import noppes.npcs.client.gui.util.GuiPlayerTopMenu;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;

import org.lwjgl.opengl.GL11;

public class GuiQuestLog extends GuiScreen implements ITopButtonListener,GuiCustomScrollActionListener, IGuiData{

	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/standardbg.png");
    protected int xSize;
    protected int guiLeft;
    protected int guiTop;
    
    private EntityPlayer player;
    private GuiCustomScroll scroll;
	private HashMap<Integer,GuiMenuSideButton> sideButtons = new HashMap<Integer,GuiMenuSideButton>();
	private QuestLogData data = new QuestLogData();
	private boolean noQuests = false;
	
	private GuiPlayerTopMenu topMenu;
	
	public GuiQuestLog(EntityPlayer player) {
		super();
		this.player = player;
        xSize = 240;
        NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestLog);
	}
    public void initGui()
    {
        super.initGui();
    	sideButtons.clear();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - 176) / 2;
        
        topMenu = new GuiPlayerTopMenu(this.guiLeft+35, this.guiTop - 17, 2, player);
        
        noQuests = false;

        if(data.categories.isEmpty()){
        	noQuests = true;
        	return;
        }
        List<String> categories = new ArrayList<String>();
        categories.addAll(data.categories.keySet());
        Collections.sort(categories,String.CASE_INSENSITIVE_ORDER);
        int i = 0;
        for(String category : categories){
        	if(data.selectedCategory.isEmpty())
        		data.selectedCategory = category;
        	sideButtons.put(i, new GuiMenuSideButton(i,guiLeft - 89, this.guiTop +2 + i*21, 90,22, category));
        	i++;
        }
        sideButtons.get(categories.indexOf(data.selectedCategory)).active = true;
                
        scroll = new GuiCustomScroll(this,0);
        scroll.setList(data.categories.get(data.selectedCategory));
        scroll.setWorldAndResolution(mc, 350, 250);
        scroll.setSize(144, 183);
        scroll.guiLeft = guiLeft + 6;
        scroll.guiTop = guiTop + 6;
    }
    public void drawScreen(int i, int j, float f)
    {
    	drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 252, 195);
        drawTexturedModalRect(guiLeft + 252, guiTop, 188, 0, 67, 195);
        super.drawScreen(i, j, f);

        topMenu.drawScreen(i, j, f);
        
        if(noQuests){
            fontRenderer.drawString("You have no active quests",guiLeft + 84,guiTop + 80, 0x404040);
        	return;
        }
        for(GuiMenuSideButton button: sideButtons.values().toArray(new GuiMenuSideButton[sideButtons.size()])){
        	button.drawButton(mc, i, j);
        }
        scroll.drawScreen(i, j, f);
        if(!data.hasSelectedQuest())
        	return;
        drawQuestText();
        //GL11.glScalef(1.5f, 1.5f, 1);
        //fontRenderer.drawString(selectedQuest.title, (int)((guiLeft + 140) / 1.5), (int)((guiTop + 5) / 1.5), 0x404040);
    }
    private int yoffset;
    private void drawQuestText(){
        yoffset = guiTop + 5; 
        int x = 0;
        drawProgress();
        yoffset += 4;
        int xoffset = guiLeft + 150;
        String text = NoppesStringUtils.formatText(data.getQuestText(), player);
        String line = "";
        for(char c : text.toCharArray()){
        	if(c == '\r' || c == '\n'){
        		fontRenderer.drawString(line, xoffset,yoffset + (x * fontRenderer.FONT_HEIGHT), 0x404040);
                line = "";
        		x++;
        		continue;
        	}
        	if(fontRenderer.getStringWidth(line + c) > (176)){
        		fontRenderer.drawString(line, xoffset,yoffset + (x * fontRenderer.FONT_HEIGHT), 0x404040);
                line = "";
        		x++;
        	}
    		line += c;
        }
        fontRenderer.drawString(line, xoffset,yoffset + (x * fontRenderer.FONT_HEIGHT), 0x404040);
    }
    private void drawProgress() {
        int xoffset = guiLeft + 152;
        for(String process : data.getQuestStatus()){
	        fontRenderer.drawString(process, xoffset, yoffset, 0x404040);
	        yoffset += 10;
        }
	}
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    }
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	topMenu.mouseClicked(i, j, k);
        if (k == 0)
        {
        	if(scroll != null)
        		scroll.mouseClicked(i, j, k);
            for (GuiMenuSideButton button : sideButtons.values().toArray(new GuiMenuSideButton[sideButtons.size()]))
            {
                if (button.mousePressed(mc, i, j))
                {
                	sideButtonPressed(button);
                }
            }
        }
    }
    private void sideButtonPressed(GuiMenuSideButton button) {
    	if(button.active)
    		return;
        mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
        data.selectedCategory = button.displayString;
        data.selectedQuest = "";
        this.initGui();
    }
	@Override
	public void customScrollClicked(int i, int j, int k, GuiCustomScroll scroll) {
		if(!scroll.hasSelected())
			return;
		data.selectedQuest = scroll.getSelected();
	}

    @Override
    protected void keyTyped(char c, int i)
    {
        if (i == 1 || i == mc.gameSettings.keyBindInventory.keyCode)
        {
            mc.displayGuiScreen(null);
            mc.setIngameFocus();
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
	@Override
	public void setGuiData(NBTTagCompound compound) {
		QuestLogData data = new QuestLogData();
		data.readNBT(compound);
		this.data = data;
		initGui();
	}
}

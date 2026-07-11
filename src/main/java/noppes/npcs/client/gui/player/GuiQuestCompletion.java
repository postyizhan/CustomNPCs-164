package noppes.npcs.client.gui.player;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.ITopButtonListener;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.Quest;

import org.lwjgl.opengl.GL11;

public class GuiQuestCompletion extends GuiNPCInterface implements ITopButtonListener{

	private int xSize;
	private int ySize;
    private int guiLeft;
    private int guiTop;
    private Quest quest;
	private final ResourceLocation resource = new ResourceLocation("customnpcs","textures/gui/smallbg.png");

	public GuiQuestCompletion(Quest quest) {
		super();
        xSize = 176;
        ySize = 222;
        this.quest = quest;
        this.drawDefaultBackground = false;
        title = "";
	}
    public void initGui()
    {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        
        this.addButton(new GuiNpcButton(0, guiLeft + 38, guiTop + ySize - 24, 100, 20, StatCollector.translateToLocal("quest.complete")));
    }
    public void drawScreen(int i, int j, float f)
    {
    	drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resource);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        
        String questTitle = quest.title;
        int left = (xSize - this.fontRenderer.getStringWidth(questTitle)) / 2;
        this.addLabel(new GuiNpcLabel(0,questTitle, guiLeft + left, guiTop + 5, 0x404040));
        drawQuestText();
        super.drawScreen(i, j, f);
    }
    private void drawQuestText(){
        int x = 0;
        int yoffset = guiTop + 20; 
        int xoffset = guiLeft + 4;
        String text = NoppesStringUtils.formatText(quest.completeText, player);
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
    @Override
	protected void actionPerformed(GuiButton guibutton)
    {
		if(guibutton.id == 0){
			NoppesUtilPlayer.sendData(EnumPlayerPacket.QuestCompletion, quest.id);
			close();
		}
    }
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	
    }
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
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
	}

}

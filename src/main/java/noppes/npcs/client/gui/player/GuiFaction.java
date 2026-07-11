package noppes.npcs.client.gui.player;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.gui.util.GuiButtonNextPage;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiPlayerTopMenu;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.Faction;
import noppes.npcs.controllers.PlayerFactionData;

import org.lwjgl.opengl.GL11;

public class GuiFaction extends GuiNPCInterface implements IGuiData{

	private int xSize;
	private int ySize;
    private int guiLeft;
    private int guiTop;
    
    private ArrayList<Faction> playerFactions = new ArrayList<Faction>();
	
	private GuiPlayerTopMenu topMenu;    

	private int page = 0;
	private int pages = 1;

	private GuiButtonNextPage buttonNextPage;
	private GuiButtonNextPage buttonPreviousPage;
	private ResourceLocation indicator;

	public GuiFaction() {
		super();
        xSize = 200;
        ySize = 216;
        this.drawDefaultBackground = false;
        title = "";
        NoppesUtilPlayer.sendData(EnumPlayerPacket.FactionsGet);
        indicator = getResource("menubg.png");
	}
    public void initGui()
    {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;

        topMenu = new GuiPlayerTopMenu(this.guiLeft+4, this.guiTop - 9, 3, player);
        
        this.buttonList.add(buttonNextPage = new GuiButtonNextPage(1, guiLeft + xSize - 43, guiTop + 198, true));
        this.buttonList.add(buttonPreviousPage = new GuiButtonNextPage(2, guiLeft + 20, guiTop + 198, false));
    }
    public void drawScreen(int i, int j, float f)
    {
    	drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(indicator);
        drawTexturedModalRect(guiLeft, guiTop + 8, 0, 0, xSize, ySize);
        drawTexturedModalRect(guiLeft + 4, guiTop + 8, 56, 0, 200, ySize);
        
        if(playerFactions.isEmpty()){
        	String noFaction = StatCollector.translateToLocal("faction.nostanding");
            fontRenderer.drawString(noFaction,guiLeft + (xSize - fontRenderer.getStringWidth(noFaction)) / 2,guiTop + 80, 0x404040);
        }
        else
        	renderScreen();
        
        super.drawScreen(i, j, f);

        topMenu.drawScreen(i, j, f);
        
    }
    private void renderScreen(){
        int size = 6;
        if(pages == 1)
        	size = playerFactions.size();
        if(page == pages){
        	size = playerFactions.size() % 6;
        }
        for(int id = 0 ; id < size; id++){
        	drawHorizontalLine(guiLeft + 2, guiLeft + xSize, guiTop + 14 + id * 30, 0xFF404040);
        	
        	Faction faction = playerFactions.get((page - 1) * 6 + id);
        	String name = faction.name;
        	String points = " : " + faction.defaultPoints;
            
            String standing = StatCollector.translateToLocal("faction.friendly");
            int color = 0x00FF00;
            if(faction.defaultPoints < faction.neutralPoints){
            	standing = StatCollector.translateToLocal("faction.unfriendly");
            	color = 0xFF0000;
            	points += "/" + faction.neutralPoints;
            }
            else if(faction.defaultPoints < faction.friendlyPoints){
            	standing = StatCollector.translateToLocal("faction.neutral");
            	color = 0xF2FF00;
            	points += "/" + faction.friendlyPoints;
            }
            else{
            	points += "/-";
            }

            fontRenderer.drawString(name, guiLeft + (xSize - fontRenderer.getStringWidth(name)) / 2, guiTop + 19 + id * 30, faction.color);

            fontRenderer.drawString(standing, width / 2 - fontRenderer.getStringWidth(standing) - 1, guiTop + 31 + id * 30, color);
            fontRenderer.drawString(points, width / 2, guiTop + 35 + id * 30, 0x404040);
        }    
    	drawHorizontalLine(guiLeft + 2, guiLeft + xSize, guiTop + 14 + size * 30, 0xFF404040); 
        
        if(pages > 1){
        	String s = page +"/" + pages;
        	fontRenderer.drawString(s, guiLeft + (xSize - fontRenderer.getStringWidth(s)) / 2, guiTop + 203, 0x404040);
        }
    }
    
    @Override
	protected void actionPerformed(GuiButton guibutton)
    {
		if(guibutton.id == 1){
			page++;
		}
		if(guibutton.id == 2){
			page--;
		}
		updateButtons();
    }
    private void updateButtons(){
		buttonNextPage.drawButton = page < pages;
		buttonPreviousPage.drawButton = page > 1;
    }
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
    	
    }
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	topMenu.mouseClicked(i, j, k);
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
	@Override
	public void setGuiData(NBTTagCompound compound) {
		playerFactions = new ArrayList<Faction>();

		NBTTagList list = compound.getTagList("FactionList");
		for(int i = 0; i < list.tagCount(); i++){
			Faction faction = new Faction();
			faction.readNBT((NBTTagCompound) list.tagAt(i));
			playerFactions.add(faction);
		}
		PlayerFactionData data = new PlayerFactionData();
		data.readNBT(compound);
		for(int id : data.factionData.keySet()){
			int points = data.factionData.get(id);
			for(Faction faction : playerFactions){
				if(faction.id == id)
					faction.defaultPoints = points;
			}
		}
		
		pages = (playerFactions.size() - 1) / 6 ;
		pages++;
		
		page = 1;

		updateButtons();
	}

}
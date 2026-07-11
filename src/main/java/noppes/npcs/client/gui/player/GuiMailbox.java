package noppes.npcs.client.gui.player;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.NoppesUtilPlayer;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.util.GuiCustomScroll;
import noppes.npcs.client.gui.util.GuiCustomScrollActionListener;
import noppes.npcs.client.gui.util.GuiNPCInterface;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcLabel;
import noppes.npcs.client.gui.util.IGuiData;
import noppes.npcs.constants.EnumPlayerPacket;
import noppes.npcs.controllers.PlayerMail;
import noppes.npcs.controllers.PlayerMailData;

public class GuiMailbox extends GuiNPCInterface implements IGuiData, GuiCustomScrollActionListener{
    
    private GuiCustomScroll scroll;
    private PlayerMailData data;
    private PlayerMail selected;
    
	public GuiMailbox() {
		super();
        xSize = 256;
        setBackground("menubg.png");
        NoppesUtilPlayer.sendData(EnumPlayerPacket.MailGet);
	}
    public void initGui()
    {
        super.initGui();
        if(scroll == null){
	        scroll = new GuiCustomScroll(this,0);
	        scroll.setSize(165, 186);
	        scroll.guiLeft = guiLeft + 4;
	        scroll.guiTop = guiTop + 4;
        }
        addScroll(scroll);
        
        String title = StatCollector.translateToLocal("mailbox.name");
        int x = (xSize - this.fontRenderer.getStringWidth(title)) / 2;

        this.addLabel(new GuiNpcLabel(0, title, guiLeft + x, guiTop - 8, 0xffffff));

        if(selected != null){
        	this.addLabel(new GuiNpcLabel(3, StatCollector.translateToLocal("mailbox.sender") + ":", guiLeft + 170, guiTop + 6, 0x404040));
        	this.addLabel(new GuiNpcLabel(1, selected.sender, guiLeft + 174, guiTop + 18, 0x404040));
        	this.addLabel(new GuiNpcLabel(2, StatCollector.translateToLocalFormatted("mailbox.timesend",getTimePast()), guiLeft + 174, guiTop + 30, 0x404040));
        }

        this.addButton(new GuiNpcButton(0, guiLeft + 4, guiTop + 192,82,20, "mailbox.read"));
        this.addButton(new GuiNpcButton(1, guiLeft + 88, guiTop + 192,82,20, "selectWorld.deleteButton"));
        getButton(1).enabled = selected != null;
    }

    private String getTimePast() {
		if(selected.timePast > 86400000){
			int days = (int) (selected.timePast / 86400000);
			if(days == 1)
				return days + " " + StatCollector.translateToLocal("mailbox.day");
			else
				return days + " " + StatCollector.translateToLocal("mailbox.days");
		}
		if(selected.timePast > 3600000){
			int hours = (int) (selected.timePast / 3600000);
			if(hours == 1)
				return hours + " " + StatCollector.translateToLocal("mailbox.hour");
			else
				return hours + " " + StatCollector.translateToLocal("mailbox.hours");
		}
		int minutes = (int) (selected.timePast / 60000);
		if(minutes == 1)
			return minutes + " " + StatCollector.translateToLocal("mailbox.minutes");
		else
			return minutes + " " + StatCollector.translateToLocal("mailbox.minutes");
	}
	@Override
    public void confirmClicked(boolean flag, int i)
    {
		if(flag && selected != null){
	        NoppesUtilPlayer.sendData(EnumPlayerPacket.MailDelete, selected.time, selected.sender);
	        selected = null;
		}
		NoppesUtil.openGUI(player, this);
    }
	protected void actionPerformed(GuiButton guibutton)
    {
    	if(scroll.selected < 0)
    		return;
    	if(guibutton.id == 0){
			this.mc.displayGuiScreen(new GuiMailmanWrite(this, selected.message, false));
    	}
    	if(guibutton.id == 1){
            GuiYesNo guiyesno = new GuiYesNo(this, "Confirm", StatCollector.translateToLocal("gui.delete"), 0);
            mc.displayGuiScreen(guiyesno);
    	}
    }
	
    @Override
    public void mouseClicked(int i, int j, int k)
    {
    	super.mouseClicked(i, j, k);
    	scroll.mouseClicked(i, j, k);
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGuiData(NBTTagCompound compound) {
		PlayerMailData data = new PlayerMailData();
		data.readNBT(compound);
		
		List<String> list = new ArrayList<String>();
		for(PlayerMail mail : data.playermail){
			list.add(mail.subject);
		}
		this.data = data;
		
		scroll.clear();
		selected = null;
		scroll.setUnsortedList(list);
	}
	@Override
	public void customScrollClicked(int i, int j, int k,
			GuiCustomScroll guiCustomScroll) {
		selected = data.playermail.get(guiCustomScroll.selected);
		initGui();
		
		if(selected != null && !selected.beenRead){
			selected.beenRead = true;
			NoppesUtilPlayer.sendData(EnumPlayerPacket.MailRead, selected.time, selected.sender);
		}
	}

}

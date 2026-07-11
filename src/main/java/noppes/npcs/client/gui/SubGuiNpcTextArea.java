// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import noppes.npcs.client.gui.util.GuiNpcButton;
import noppes.npcs.client.gui.util.GuiNpcTextArea;
import noppes.npcs.client.gui.util.SubGuiInterface;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton, StatCollector, GuiOptions, 
//            StatList, StatFileWriter, World, GuiMainMenu, 
//            GuiAchievements, GuiStats, MathHelper

public class SubGuiNpcTextArea extends SubGuiInterface{
	public String text;

	public SubGuiNpcTextArea(String text){
		this.text = text;
		setBackground("menubg.png");
		xSize = 256;
		ySize = 216;
		closeOnEsc = true;
	}
	
	@Override
	public void initGui(){
		super.initGui();
		this.addTextField(new GuiNpcTextArea(2, this, this.fontRenderer, guiLeft + 4, guiTop + 4, 176, 182, text));

		this.buttonList.add(new GuiNpcButton(0, guiLeft + 196, guiTop + 160, 56, 20, "Close"));
		
		this.buttonList.add(new GuiNpcButton(102, guiLeft + 196, guiTop + 20, 56, 20, "Clear"));
		this.buttonList.add(new GuiNpcButton(101, guiLeft + 196, guiTop + 43, 56, 20, "Paste"));
		this.buttonList.add(new GuiNpcButton(100, guiLeft + 196, guiTop + 66, 56, 20, "Copy"));

	}

	public void setClipboardContents(String aString) {
		StringSelection stringSelection = new StringSelection(aString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, new ClipboardOwner() {

			@Override
			public void lostOwnership(Clipboard arg0, Transferable arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				System.err.println(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				System.err.println(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
    public void close(){
		text = getTextField(2).getText();
		super.close();
	}

	@Override
	public void buttonEvent(GuiButton guibutton) {
		if (guibutton.id == 100) {
			setClipboardContents(getTextField(2).getText());
		}
		if (guibutton.id == 101) {
			getTextField(2).setText(getClipboardContents());
		}
		if (guibutton.id == 102) {
			getTextField(2).setText("");
		}
		if(guibutton.id == 0){
			close();
		}
	}
}

package noppes.npcs.client.gui.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

public class GuiNpcTextArea extends GuiNpcTextField{
	public boolean inMenu = true;
	public boolean numbersOnly = false;
	private int posX,posY,width,height;
    private int cursorCounter;
	private FontRenderer fontrenderer;
	private int cursorPosition = 0;
	
	public GuiNpcTextArea(int id,GuiScreen guiscreen, FontRenderer fontrenderer,
			int i, int j, int k, int l, String s) {
		super(id,guiscreen, fontrenderer, i, j, k, l, s);
		posX = i;
		posY = j;
		width = k;
		height = l;
		this.fontrenderer = fontrenderer;
		setMaxStringLength(1500);
		this.setText(s);
	}
    public void updateCursorCounter()
    {
        cursorCounter++;
    }
	@Override
    public boolean textboxKeyTyped(char c, int i)
    {
        if (isFocused())
        {
        	String originalText = getText();       
        	this.setText(originalText);	
        	if(c == '\r' || c == '\n'){
        		this.setText(originalText + c);
        	}
        	boolean bo = super.textboxKeyTyped(c, i);
        	String newText = getText();  
    		if(originalText.length() > newText.length())
    			cursorPosition--;
    		if(originalText.length() < newText.length())
    			cursorPosition++;
        	return bo;
    		
        }
        return false;
    }
	@Override
    public void mouseClicked(int i, int j, int k)
    {
    	boolean wasFocused = isFocused();
        super.mouseClicked(i, j, k);
        if(!wasFocused && isFocused())
        	cursorPosition = getText().length();
    }
    public void drawTextBox()
    {
        drawRect(posX - 1, posY - 1, posX + width + 1, posY + height + 1, 0xffa0a0a0);
        drawRect(posX, posY, posX + width, posY + height, 0xff000000);

        int x = 0;
        String line = "";
        //int color = isEnabled?0xe0e0e0:0x707070;
        int color = 0xe0e0e0;
        for(char c : getText().toCharArray()){
        	if(c == '\r' || c == '\n'){
                drawString(fontrenderer, line, posX + 4, posY + 4 + (x * fontrenderer.FONT_HEIGHT), color);
                line = "";
        		x++;
        		continue;
        	}
        	else if(fontrenderer.getStringWidth(line + c) > (width - 8)){
                drawString(fontrenderer, line, posX + 4, posY + 4 + (x * fontrenderer.FONT_HEIGHT), color);
                line = "";
        		x++;
        	}
    		line += c;
        }
        drawString(fontrenderer, line, posX + 4, posY + 4 + (x * fontrenderer.FONT_HEIGHT), color);
        
        boolean flag = isFocused() && (cursorCounter / 6) % 2 == 0;
        int i = 0;
        x = 0;
        line = "";
        if(flag && 0 == cursorPosition){
        	fontrenderer.drawString("_", posX + 3 + fontrenderer.getStringWidth(line), posY + 4 + (x * fontrenderer.FONT_HEIGHT), color);            	
        }
        for(char c : getText().toCharArray()){
        	i++;
        	if(c == '\r' || c == '\n'){
                line = "";
        		x++;
        	}
        	else if(fontrenderer.getStringWidth(line + c) > (width - 8)){
                line = "";
        		x++;
        		line += c;
        	}
        	else
        		line += c;
            if(flag && i == cursorPosition){
            	fontrenderer.drawString("_", posX + 3 + fontrenderer.getStringWidth(line), posY + 4 + (x * fontrenderer.FONT_HEIGHT), color);            	
            }
        }
        

    }
	
}

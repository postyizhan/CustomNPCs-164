package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;
import noppes.npcs.NoppesStringUtils;
import noppes.npcs.TextBlock;

public class TextBlockClient extends TextBlock{
	public int color = 0xe0e0e0;
	private String name;
	private ICommandSender sender;

	public TextBlockClient(ICommandSender sender, String text, int lineWidth, int color, Object... obs) {
		this(text, lineWidth, obs);
		this.color = color;
		this.sender = sender;
	}
	public TextBlockClient(String name, String text, int lineWidth, int color, Object... obs) {
		this(text, lineWidth, obs);
		this.color = color;
		this.name = name;
	}
	
	public String getName(){
		if(sender != null)
			return sender.getCommandSenderName();
		return name;
	}
	
	public TextBlockClient(String text, int lineWidth, Object... obs){
		text = NoppesStringUtils.formatText(text, obs);
		
		String line = "";
		text = text.replace("\n", " \n ");
		text = text.replace("\r", " \r ");
		String[] words = text.split(" ");
		
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		for(String word : words){
			if(word.isEmpty())
				continue;
			if(word.length() == 1){
				char c = word.charAt(0);
				if(c == '\r' || c == '\n'){
	        		addLine(line);
					line = "";
					continue;
				}
			}
			String newLine;
			if(line.isEmpty())
				newLine = word;
			else
				newLine = line + " " + word;
			
			if(font.getStringWidth(newLine) > lineWidth){
				addLine(line);
				line = word.trim();
			}
			else{
				line = newLine;
			}			
		}
		if(!line.isEmpty())
			addLine(line);
	}

	private void addLine(String text){
		ChatMessageComponent line = ChatMessageComponent.createFromText(text);
		lines.add(line);
	}
}

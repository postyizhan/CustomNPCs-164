package noppes.npcs.client;

import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import noppes.npcs.CustomItems;

public class QuestAchievement extends Achievement{

	private String description;
	public QuestAchievement(String par2Str, String description) {
		super(-1, par2Str, 0, 0, CustomItems.letter==null?Item.paper:CustomItems.letter, null);
		this.description = description;
	}

    public String getName()
    {
        return this.statName.substring(12);
    }
    public String getDescription()
    {
    	return description;
    }
}

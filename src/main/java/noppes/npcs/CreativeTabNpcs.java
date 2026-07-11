package noppes.npcs;

import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabNpcs extends CreativeTabs{
	public int icon;

	public CreativeTabNpcs(String label) {
		super(label);
	}

	@Override
    public int getTabIconItemIndex()
    {
        return icon;
    }
}

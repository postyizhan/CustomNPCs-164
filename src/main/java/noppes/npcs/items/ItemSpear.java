package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

public class ItemSpear extends ItemNpcWeaponInterface{

	public ItemSpear(int par1, EnumToolMaterial tool) {
		super(par1, tool);
	}
	
	public void renderSpecial(){}
	
	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}

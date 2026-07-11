package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

public class ItemBattleAxe extends ItemNpcWeaponInterface{

	public ItemBattleAxe(int par1, EnumToolMaterial tool) {
		super(par1, tool);
	}

	public void renderSpecial(){}
	
	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}

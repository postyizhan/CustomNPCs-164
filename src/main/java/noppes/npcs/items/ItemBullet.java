package noppes.npcs.items;

import noppes.npcs.CustomItems;


public class ItemBullet extends ItemNpcInterface{
	private EnumNpcToolMaterial material;
	public ItemBullet(int par1, EnumNpcToolMaterial material) {
		super(par1);
		this.material = material;
		setCreativeTab(CustomItems.tabWeapon);
	}
	public int getBulletDamage(){
		return material.getDamageVsEntity();
	}

}

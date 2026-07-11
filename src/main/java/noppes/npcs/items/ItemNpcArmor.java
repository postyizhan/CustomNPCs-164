package noppes.npcs.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemNpcArmor extends ItemArmor{

	private String texture;
	public ItemNpcArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial,int par4, String texture) {
		super(par1 - 26700 + CustomNpcs.ItemStartId, par2EnumArmorMaterial, 0, par4);
		this.texture = texture;
		setCreativeTab(CustomItems.tabArmor);
	}
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, int layer)
    {
		if(armorType == 2)
			return "customnpcs:textures/armor/" + texture + "_2.png";
		return "customnpcs:textures/armor/" + texture + "_1.png";
    }

    @Override
    public Item setUnlocalizedName(String name){
		GameRegistry.registerItem(this, name);
    	return super.setUnlocalizedName(name);
    }
}

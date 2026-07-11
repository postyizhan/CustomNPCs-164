package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemNpcWeaponInterface extends ItemSword implements ItemRenderInterface{

    
	public ItemNpcWeaponInterface(int par1, EnumToolMaterial material) {
		super(par1 - 26700 + CustomNpcs.ItemStartId,material);
		setCreativeTab(CustomItems.tab);
		CustomNpcs.proxy.registerItem(itemID);
		setCreativeTab(CustomItems.tabWeapon);
	}
	public void renderSpecial(){
        GL11.glScalef(0.66f, 0.66f,0.66f);
        GL11.glTranslatef(0, 0.3f, 0);
    }

    @Override
    public Item setUnlocalizedName(String name){
		GameRegistry.registerItem(this, name);
    	return super.setUnlocalizedName(name);
    }
}

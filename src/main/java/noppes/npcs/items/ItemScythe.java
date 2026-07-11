package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

import org.lwjgl.opengl.GL11;

public class ItemScythe extends ItemNpcWeaponInterface{

	public ItemScythe(int par1 ,EnumToolMaterial tool) {
		super(par1, tool);
	}

	@Override
	public void renderSpecial(){
        GL11.glTranslatef(-0.1F, 0, 0.1f);
	}
	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}

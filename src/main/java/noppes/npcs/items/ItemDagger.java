package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

import org.lwjgl.opengl.GL11;

public class ItemDagger extends ItemNpcWeaponInterface{

	public ItemDagger(int par1, EnumToolMaterial tool) {
		super(par1, tool);
	}

	@Override
	public void renderSpecial(){
        GL11.glScalef(0.6f, 0.6f, 0.6f);
        GL11.glTranslatef(-0.05F, 0.32f, 0.05f);
	}
	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
}

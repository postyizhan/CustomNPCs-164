package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

import org.lwjgl.opengl.GL11;

public class ItemClaw extends ItemNpcWeaponInterface{

	public ItemClaw(int par1, EnumToolMaterial material) {
		super(par1,material);
	}

	@Override
	public void renderSpecial(){
        GL11.glScalef(0.6f, 0.6f,0.6f);
    	GL11.glTranslatef(-0.6f, 0.2f, 0.3f);
        GL11.glRotatef(90, -1, 0, -1);
        GL11.glRotatef(-6, 0, 1, 0);
	}	
}

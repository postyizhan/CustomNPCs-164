package noppes.npcs.items;

import org.lwjgl.opengl.GL11;

public class ItemRotatedShield extends ItemShield{

	public ItemRotatedShield(int par1, EnumNpcToolMaterial material) {
		super(par1,material);
	}

	@Override
	public void renderSpecial(){
        GL11.glScalef(0.6f, 0.6f,0.6f);
    	GL11.glTranslatef(-0.1f, 1f, -0.18f);
        GL11.glRotatef(120, 1, 0, 1);
        GL11.glRotatef(-6, 0, 1, 0);
	}
}

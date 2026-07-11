package noppes.npcs.items;

import org.lwjgl.opengl.GL11;

public class ItemBanjo extends ItemMusic{

	public ItemBanjo(int par1) {
		super(par1);
	}
	@Override
	public void renderSpecial(){
        GL11.glScalef(0.85f, 0.85f,0.85f);
        GL11.glTranslatef(0f, 0.4f, 0f);
        GL11.glRotatef(-90, -1F, 0, 1F);
	}
}

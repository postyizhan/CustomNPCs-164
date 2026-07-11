package noppes.npcs.items;

import net.minecraft.item.EnumToolMaterial;

import org.lwjgl.opengl.GL11;


public class ItemBroadSword extends ItemNpcWeaponInterface{

	public ItemBroadSword(int id, EnumToolMaterial tool) {
		super(id, tool);
	}
	
	public void renderSpecial(){
        GL11.glScalef(1f, 1.2f, 1f);
        GL11.glTranslatef(-0.12f, 0.14f, -0.16f);
        GL11.glRotatef(180, 0, 1, 0);
	}
	
}

package noppes.npcs.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;

import org.lwjgl.opengl.GL11;

public class ItemDaggerReversed extends ItemDagger{

	private ItemDagger dagger;
	public ItemDaggerReversed(int par1, ItemDagger dagger, EnumToolMaterial tool) {
		super(par1,tool);
		this.dagger = dagger;
	}

	@Override
	public void renderSpecial(){
        GL11.glScalef(0.6f, 0.6f, 0.6f);
        GL11.glTranslatef(-0.26f, 0.5f, 0.26f);
        GL11.glRotatef(180F, 1F, 0F, 1F);
	}
	@Override
	public boolean shouldRotateAroundWhenRendering(){
		return true;
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = dagger.getIconFromDamage(0);
    }
}

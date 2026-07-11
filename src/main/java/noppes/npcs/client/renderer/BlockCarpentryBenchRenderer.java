package noppes.npcs.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.client.model.ModelCarpentryBench;

import org.lwjgl.opengl.GL11;

public class BlockCarpentryBenchRenderer extends TileEntitySpecialRenderer{

	private ModelCarpentryBench model = new ModelCarpentryBench();
    private static final ResourceLocation field_110631_g = new ResourceLocation("customnpcs","textures/misc/CarpentryBench.png");
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		int meta = var1.worldObj.getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2 + 0.5f, (float)var4 +1.4f, (float)var6 + 0.5f);
        GL11.glScalef(0.95f, 0.95f, 0.95f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90 * meta, 0, 1, 0);
        this.bindTexture(field_110631_g);
        model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}
}

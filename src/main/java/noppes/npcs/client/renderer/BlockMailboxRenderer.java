package noppes.npcs.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import noppes.npcs.CustomItems;
import noppes.npcs.client.model.ModelMailboxUS;
import noppes.npcs.client.model.ModelMailboxWow;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockMailboxRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{

	private ModelMailboxUS model = new ModelMailboxUS();
	private ModelMailboxWow model2 = new ModelMailboxWow();

    private static final ResourceLocation text1 = new ResourceLocation("customnpcs","textures/misc/mailbox1.png");
    private static final ResourceLocation text2 = new ResourceLocation("customnpcs","textures/misc/mailbox2.png");
	
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
		int meta = var1.worldObj.getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord) | 4;
		int type = var1.worldObj.getBlockMetadata(var1.xCoord, var1.yCoord, var1.zCoord) >> 2;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)var2 + 0.5f, (float)var4 + 1.5f, (float)var6 + 0.5f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(90 * meta, 0, 1, 0);
        if(type == 0){
	        this.bindTexture(text1);
	        model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        }
        if(type == 1){
	        this.bindTexture(text2);
	        model2.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        }
		GL11.glPopMatrix();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0.8f, 0);
        GL11.glScalef(0.9f, 0.9f, 0.9f);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(180, 0, 1, 0);
        if(metadata == 0){
	        this.bindTexture(text1);
	        model.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        }
        if(metadata == 1){
	        this.bindTexture(text2);
	        model2.render(null, 0, 0, 0, 0, 0.0F, 0.0625F);
        }
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getRenderId() {
		return CustomItems.mailbox.getRenderType();
	}
}

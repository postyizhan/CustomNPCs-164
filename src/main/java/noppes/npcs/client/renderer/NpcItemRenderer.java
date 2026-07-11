package noppes.npcs.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import noppes.npcs.items.ItemRenderInterface;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class NpcItemRenderer implements IItemRenderer{
    private static final ResourceLocation field_110930_b = new ResourceLocation("textures/misc/enchanted_item_glint.png");

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.EQUIPPED){
			return true;
		}
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack par2ItemStack, Object... data) {
		if(!(par2ItemStack.getItem() instanceof ItemRenderInterface))
			return;
		
        GL11.glTranslatef(0.9375F, 0.0625F, 0.0F);
        GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
		
        ((ItemRenderInterface)par2ItemStack.getItem()).renderSpecial();
        renderItem3d((EntityLivingBase) data[1],par2ItemStack);
	}
	public void renderItem3d(EntityLivingBase par1EntityLiving, ItemStack par2ItemStack){
		Minecraft mc = Minecraft.getMinecraft();
        TextureManager texturemanager = mc.getTextureManager();
		int par3 = 0;
        texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
		 Tessellator tessellator = Tessellator.instance;
		 Icon icon = par1EntityLiving.getItemIcon(par2ItemStack, par3);

         if (icon == null)
         {
             return;
         } 
         float f = icon.getMinU();
         float f1 = icon.getMaxU();
         float f2 = icon.getMinV();
         float f3 = icon.getMaxV();
         float f4 = 0.0F;
         float f5 = 0.3F;
         GL11.glEnable(GL12.GL_RESCALE_NORMAL);
         GL11.glTranslatef(-f4, -f5, 0.0F);
         float f6 = 1.5F;
         GL11.glScalef(f6, f6, f6);
         GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
         ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);

         if (par2ItemStack.hasEffect(par3))
         {
             GL11.glDepthFunc(GL11.GL_EQUAL);
             GL11.glDisable(GL11.GL_LIGHTING);
             texturemanager.bindTexture(field_110930_b);
             GL11.glEnable(GL11.GL_BLEND);
             GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
             float f7 = 0.76F;
             GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
             GL11.glMatrixMode(GL11.GL_TEXTURE);
             GL11.glPushMatrix();
             float f8 = 0.125F;
             GL11.glScalef(f8, f8, f8);
             float f9 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
             GL11.glTranslatef(f9, 0.0F, 0.0F);
             GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
             ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
             GL11.glPopMatrix();
             GL11.glPushMatrix();
             GL11.glScalef(f8, f8, f8);
             f9 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
             GL11.glTranslatef(-f9, 0.0F, 0.0F);
             GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
             ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
             GL11.glPopMatrix();
             GL11.glMatrixMode(GL11.GL_MODELVIEW);
             GL11.glDisable(GL11.GL_BLEND);
             GL11.glEnable(GL11.GL_LIGHTING);
             GL11.glDepthFunc(GL11.GL_LEQUAL);
         }

         GL11.glDisable(GL12.GL_RESCALE_NORMAL);
     }

//	private void renderItemIn2D(Tessellator var5, float var8, float var9, float var7, float var10) {
//		try {
//			for(Method method : ItemRenderer.class.getDeclaredMethods()){
//				Type[] arguments = method.getGenericParameterTypes();
//				if(arguments.length < 5 || arguments[0] != Tessellator.class)
//					continue;
//				method.setAccessible(true);
//				method.invoke(RenderManager.instance.itemRenderer, var5,var8,var9,var7,var10);
//			}
//			
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
}

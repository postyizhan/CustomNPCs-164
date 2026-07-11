package noppes.npcs.client.model.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import noppes.npcs.EntityCustomNpc;
import noppes.npcs.ModelPartData;
import noppes.npcs.client.model.ModelMPM;

import org.lwjgl.opengl.GL11;

public abstract class ModelPartInterface extends ModelRenderer {
	private EntityCustomNpc entity;
	public float scale = 1;
	protected ResourceLocation location;
	public int color = 0xFFFFFF;
	
	public ModelMPM base;

	public ModelPartInterface(ModelMPM par1ModelBase) {
		super(par1ModelBase);
		this.base = par1ModelBase;
		this.setTextureSize(0, 0);
	}

	public void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float par1, float par2, float par3,
			float par4, float par5, float par6, Entity entity) {
	}

	public void setLivingAnimations(ModelPartData data,
			EntityLivingBase entityliving, float f, float f1, float f2) {
	}

	public void setData(EntityCustomNpc entity) {
		this.entity = entity;
		initData(entity);
	}

    public void render(float par1)
    {
    	if(!base.isArmor){
	    	if(location != null){
	            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
	            texturemanager.bindTexture(location);
	            base.currentlyPlayerTexture = false;
	    	}
	    	else if(!base.currentlyPlayerTexture){
	            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
	            texturemanager.bindTexture((ResourceLocation) entity.textureLocation);
	            base.currentlyPlayerTexture = true;
			}
    	}
    	boolean bo = entity.hurtTime <= 0 && entity.deathTime <= 0 && !base.isArmor;
    	if(bo){
	    	float red = (color >> 16 & 255) / 255f;
	    	float green = (color >> 8  & 255) / 255f;
	    	float blue = (color & 255) / 255f;
	    	GL11.glColor4f(red, green, blue, 1);
    	}
    	super.render(par1);
    	if(bo){
	    	GL11.glColor4f(1, 1, 1, 1);
    	}
    }
	public abstract void initData(EntityCustomNpc data);


}

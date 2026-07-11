// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import noppes.npcs.client.model.util.ModelPlaneRenderer;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer, MathHelper, Entity

public class ModelNagaFemale extends ModelNPCFemale {

	ModelRenderer leg;
	ModelRenderer leg2;
	ModelRenderer leg3;
	ModelRenderer leg4;
	ModelRenderer leg5;
    public ModelNagaFemale(int width, int height,float f)
    {
        super(width,height,f);
    }	
    public void init(float f, float f1) {
		super.init(f, f1);

		bipedRightLeg = new ModelRenderer(this,0,0);
		bipedLeftLeg = new ModelRenderer(this,0,0);
		
		leg = new ModelRenderer(this,0,0);
		
		ModelRenderer legPart = new ModelRenderer(this,0,16);
		legPart.addBox(0, -2, -2, 4, 4, 4);
		legPart.setRotationPoint(-4, 0, 0);
		leg.addChild(legPart);
		legPart = new ModelRenderer(this,0,16);
		legPart.mirror = true;
		legPart.addBox(0, -2, -2, 4, 4, 4);
		leg.addChild(legPart);

		leg2 = new ModelRenderer(this,0,0);
		leg2.childModels = leg.childModels;

		leg3 = new ModelRenderer(this,0,0);
		ModelPlaneRenderer plane = new ModelPlaneRenderer(this, 4, 24);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.addBackPlane(0,-2, 0, 4, 4);
		plane.setRotationPoint(-4, 0, 0);
		leg3.addChild(plane);
		plane = new ModelPlaneRenderer(this, 4, 24);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.mirror = true;
		plane.addBackPlane(0,-2, 0, 4, 4);
		leg3.addChild(plane);
		

		plane = new ModelPlaneRenderer(this, 8, 24);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.addBackPlane(0,-2, 6, 4, 4);
		plane.setRotationPoint(-4, 0, 0);
		leg3.addChild(plane);
		plane = new ModelPlaneRenderer(this, 8, 24);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.mirror = true;
		plane.addBackPlane(0,-2, 6, 4, 4);
		leg3.addChild(plane);

		plane = new ModelPlaneRenderer(this, 4, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.addTopPlane(0,-2, -6, 4, 6);
		plane.setRotationPoint(-4, 0, 0);
		plane.rotateAngleX = (float) (Math.PI);
		leg3.addChild(plane);
		plane = new ModelPlaneRenderer(this, 4, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.mirror = true;
		plane.addTopPlane(0,-2, -6, 4, 6);
		plane.rotateAngleX = (float) (Math.PI);
		leg3.addChild(plane);

		plane = new ModelPlaneRenderer(this, 8, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.addTopPlane(0,-2, 0, 4, 6);
		plane.setRotationPoint(-4, 0, 0);
		leg3.addChild(plane);
		plane = new ModelPlaneRenderer(this, 8, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.mirror = true;
		plane.addTopPlane(0,-2, 0, 4, 6);
		leg3.addChild(plane);;

		plane = new ModelPlaneRenderer(this, 0, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.rotateAngleX = (float) (Math.PI / 2);
		plane.addSidePlane(0,0, -2, 6, 4);
		plane.setRotationPoint(-4, 0, 0);
		leg3.addChild(plane);
		plane = new ModelPlaneRenderer(this, 0, 26);
		plane.setTextureSize(textureWidth, textureHeight);
		plane.rotateAngleX = (float) (Math.PI / 2);
		plane.addSidePlane(4,0, -2, 6, 4);
		leg3.addChild(plane);

		leg4 = new ModelRenderer(this,0,0);
		leg4.setTextureSize(textureWidth, textureHeight);
		leg4.childModels = leg3.childModels;
		
		leg5 = new ModelRenderer(this,0,0);
		
		legPart = new ModelRenderer(this,56,20);
		legPart.addBox(0, 0, -2, 2, 5, 2);
		legPart.setRotationPoint(-2, 0, 0);
		legPart.rotateAngleX = (float) (Math.PI/2);
		leg5.addChild(legPart);
		legPart = new ModelRenderer(this,56,20);
		legPart.mirror = true;
		legPart.addBox(0, 0, -2, 2, 5, 2);
		legPart.rotateAngleX = (float) (Math.PI/2);
		leg5.addChild(legPart);
		
		defaultRotation();
		
		if(textureHeight == 32)
			return;
       
        ModelRenderer Snout = new ModelRenderer(this, 0, 32);
        Snout.addBox(0F, 0F, 0F, 4, 3, 3);
        Snout.setRotationPoint(-2F, -3F, -7F);
        bipedHead.addChild(Snout);
        
        ModelRenderer Snout2 = new ModelRenderer(this, 0, 38);
        Snout2.addBox(0F, 0F, 0F, 4, 3, 1);
        Snout2.setRotationPoint(-2F, -3F, -5F);
        bipedHead.addChild(Snout2);

        ModelPlaneRenderer headFin = new ModelPlaneRenderer(this, 14, 32);
        headFin.setTextureSize(64, 64);
        headFin.addSidePlane(0, -12, -1, 9, 9);
        bipedHead.addChild(headFin);
        
        ModelPlaneRenderer backFin = new ModelPlaneRenderer(this, 23, 32);
        backFin.setTextureSize(64, 64);
        backFin.addSidePlane(0, 0, 2, 12, 7);
        bipedBody.addChild(backFin);
	}
	private void defaultRotation()
	{
        leg.setRotationPoint(0F, 14.0F, 0.0F);
        leg2.setRotationPoint(0, 18.0F, 0.6F);
		leg3.setRotationPoint(0F, 22.0F, -0.3F);
		leg4.setRotationPoint(0F, 22.0F, 5F);
		leg5.setRotationPoint(0F, 22.0F, 10F);

		leg.rotateAngleX = 0;
		leg2.rotateAngleX = 0;
		leg3.rotateAngleX = 0;
		leg4.rotateAngleX = 0;
		leg5.rotateAngleX = 0;
	}
	
    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    	leg.render(par7);
		leg3.render(par7);

		if(!this.isRiding)
	    	leg2.render(par7);

        GL11.glPushMatrix();
        GL11.glScalef(0.64f, 0.7f,0.85f);
        GL11.glTranslatef(leg3.rotateAngleY, 0.66f, 0.06F);
    	leg4.render(par7);
    	GL11.glPopMatrix();
    	
        GL11.glPushMatrix();
        GL11.glTranslatef(leg3.rotateAngleY + leg4.rotateAngleY, 0, 0);
    	leg5.render(par7);
    	GL11.glPopMatrix();
    }

	@Override
	public void setLivingAnimations(EntityLivingBase entityliving, float f,
			float f1, float f2) {
		super.setLivingAnimations(entityliving, f, f1, f2);
	}
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6)
    {
    	super.setRotationAngles(par1, par2, par3, par4, par5, par6);
        this.leg.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.26F * par2;
        this.leg2.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.5F * par2;
        this.leg3.rotateAngleY = MathHelper.cos(par1 * 0.6662F) * 0.26F * par2;
        this.leg4.rotateAngleY = -MathHelper.cos(par1 * 0.6662F) * 0.16F * par2;
        this.leg5.rotateAngleY = -MathHelper.cos(par1 * 0.6662F) * 0.3F * par2;
        

		defaultRotation();
		
		if(this.isSleeping){
			leg3.rotateAngleX = (float) -(Math.PI/2);
			leg4.rotateAngleX = (float) -(Math.PI/2);
			leg5.rotateAngleX = (float) -(Math.PI/2);

        	leg3.rotationPointY -= 2;
        	leg3.rotationPointZ = 0.9f;
        	

        	leg4.rotationPointY += 4;
        	leg4.rotationPointZ = 0.9f;
        	
        	leg5.rotationPointY += 7;
        	leg5.rotationPointZ = 2.9f;
		}
		if(this.isRiding){
			leg.rotationPointY-= 1;
			leg.rotateAngleX = (float) -(Math.PI/16f);
        	leg.rotationPointZ = -1;
        	
			leg2.rotationPointY-= 4;
			leg2.rotationPointZ = -1;
			
        	leg3.rotationPointY-= 9;
        	leg3.rotationPointZ -= 1;
        	leg4.rotationPointY-= 13;
        	leg4.rotationPointZ -= 1;
        	leg5.rotationPointY-= 9;
        	leg5.rotationPointZ -= 1;
        	if (this.isSneak){
            	leg.rotationPointZ += 5;
            	leg3.rotationPointZ += 5;
            	leg4.rotationPointZ += 5;
            	leg5.rotationPointZ += 4;
            	leg.rotationPointY--;
            	leg2.rotationPointY--;
            	leg3.rotationPointY--;
            	leg4.rotationPointY--;
            	leg5.rotationPointY--;
        	}
		}else if (this.isSneak)
        {
        	leg.rotationPointY--;
        	leg2.rotationPointY--;
        	leg3.rotationPointY--;
        	leg4.rotationPointY--;
        	leg5.rotationPointY--;

        	leg.rotationPointZ = 5;
        	leg2.rotationPointZ = 3;
        }
    }

}

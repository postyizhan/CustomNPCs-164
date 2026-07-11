// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import noppes.npcs.client.model.util.ModelPlaneRenderer;

import org.lwjgl.opengl.GL11;


// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer, MathHelper, Entity

public class ModelNPCFemale extends ModelNPCMale
{
    public ModelRenderer Breasts;

    public ModelNPCFemale(float f)
    {
        super(f);
    }
    public ModelNPCFemale(int width,int height,float f)
    {
        super(width,height,f);
    }
    public void init(float f, float f1)
    {
    	super.init(f, f1);
        Breasts = new ModelRenderer(this, 24, 0);
        Breasts.addBox(0F, 0F, 0F, 7, 3, 1,0);
        Breasts.setRotationPoint(-3.5F, 1.8F, -2.85F);

        ModelPlaneRenderer BreastsTop = new ModelPlaneRenderer(this, 56, 0);
    	BreastsTop.addTopPlane(0F, 0F, 0F, 7, 1,f);
    	Breasts.addChild(BreastsTop);
    	
    	ModelPlaneRenderer BreastsFront = new ModelPlaneRenderer(this, 56, 1);
    	BreastsFront.addBackPlane(0F, 0F, 0F, 7, 3,f);
    	Breasts.addChild(BreastsFront);

    	ModelPlaneRenderer BreastsBottom = new ModelPlaneRenderer(this, 56, 4);
        BreastsBottom.addTopPlane(0, -3F, -1, 7, 1,f);
        BreastsBottom.rotateAngleX = (float) Math.PI;
        Breasts.addChild(BreastsBottom);
    	
        ModelPlaneRenderer BreastsLeft = new ModelPlaneRenderer(this, 63, 0);
        BreastsLeft.addSidePlane(0F, 0F, 0F, 3, 1,f);
        Breasts.addChild(BreastsLeft);
    	
        ModelPlaneRenderer BreastsRight = new ModelPlaneRenderer(this, 63, 3);
        BreastsRight.addSidePlane(-7F, 0F, -1F, 3, 1,f);
        BreastsRight.rotateAngleY = (float) Math.PI;
        Breasts.addChild(BreastsRight);
        
        bipedBody.addChild(Breasts);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);

        float f6 = 0.85F;
        
        GL11.glPushMatrix();
        GL11.glScalef(f6, f6, f6);
        GL11.glTranslatef(0, -0.015f, 0.0F);
        renderHead(entity, f5);
        GL11.glPopMatrix();

        f6 = 0.8F;
        GL11.glPushMatrix();
        GL11.glScalef(f6, 0.96f, f6);
        GL11.glTranslatef(0.07f, 0, 0.0F);
        renderLeftArm(entity, f5);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        GL11.glScalef(f6, 0.96f, f6);
        GL11.glTranslatef(-0.07f, 0, 0.0F);
        renderRightArm(entity, f5);
        GL11.glPopMatrix();

        renderLegs(entity, f5);
        renderBody(entity, f5);
    }

}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.model;

import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;


// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer, MathHelper, Entity

public class ModelElfFemale extends ModelNPCFemale
{

    public ModelElfFemale(float f)
    {
        super(f);
    }
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        float f6 = 0.85F;
        
        GL11.glPushMatrix();
        GL11.glScalef(f6, f6-0.1f, f6);
        GL11.glTranslatef(0, -0.015f, 0.0F);
        renderHead(entity, f5);
        GL11.glPopMatrix();

        f6 = 0.74F;
        GL11.glPushMatrix();
        GL11.glScalef(f6, 0.9f, f6);
        GL11.glTranslatef(0.09f, 0, 0.0F);
        renderLeftArm(entity, f5);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        GL11.glScalef(f6, 0.9f, f6);
        GL11.glTranslatef(-0.09f, 0, 0.0F);
        renderRightArm(entity, f5);
        GL11.glPopMatrix();

        renderBody(entity, f5);
        renderLegs(entity, f5);

    }

}

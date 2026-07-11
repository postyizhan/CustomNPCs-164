// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import noppes.npcs.entity.EntityNPCVillager;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, ModelVillager, EntityVillager, EntityLiving, 
//            Entity

public class RenderNpcCreeper extends RenderNPCInterface
{


    public RenderNpcCreeper()
    {
        super(new ModelVillager(0.0F), 0.5F);
    }

    //TODO maybe show the items in the hands of the villager
    public void renderVillager(EntityNPCVillager entityvillager, double d, double d1, double d2, 
            float f, float f1)
    {
        super.doRenderLiving(entityvillager, d, d1, d2, f, f1);
    }

    protected void func_40290_a(EntityNPCVillager entityvillager, double d, double d1, double d2)
    {
    }

    protected void func_40291_a(EntityNPCVillager entityvillager, float f)
    {
        super.renderEquippedItems(entityvillager, f);
    }


    protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
        func_40291_a((EntityNPCVillager)entityliving, f);
    }

    @Override
    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
            float f, float f1)
    {
        renderVillager((EntityNPCVillager)entityliving, d, d1, d2, f, f1);
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        renderVillager((EntityNPCVillager)entity, d, d1, d2, f, f1);
    }
}

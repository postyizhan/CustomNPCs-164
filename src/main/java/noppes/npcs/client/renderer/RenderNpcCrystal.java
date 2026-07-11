package noppes.npcs.client.renderer;

import net.minecraft.entity.Entity;
import noppes.npcs.client.model.ModelNpcCrystal;
import noppes.npcs.entity.EntityNpcCrystal;

public class RenderNpcCrystal extends RenderNPCInterface
{
	ModelNpcCrystal mainmodel;
    public RenderNpcCrystal(ModelNpcCrystal model)
    {
    	super(model,0);
    	mainmodel = model;
    }

    public void func_41035_a(EntityNpcCrystal par1EntityEnderCrystal, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(par1EntityEnderCrystal, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        func_41035_a((EntityNpcCrystal)par1Entity, par2, par4, par6, par8, par9);
    }
}

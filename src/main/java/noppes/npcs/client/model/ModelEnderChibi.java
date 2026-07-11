package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelEnderChibi extends ModelNPCMale {

    public ModelEnderChibi(float f)
    {
        super(f);
    }

    public void init(float f, float f1)
    {
    	super.init(f,f1);
        bipedRightArm = new ModelRenderer(this, 44, 18);
        bipedRightArm.addBox(-1F, 4F, -1F, 2, 12, 2);
        bipedRightArm.setRotationPoint(-5F, 2.0F, 0.0F);
        
        ModelRenderer extension = new ModelRenderer(this, 44, 18);
        extension.addBox(-1, -2, -1F, 2, 6, 2);
        bipedRightArm.addChild(extension);
        
        bipedLeftArm = new ModelRenderer(this, 44, 18);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1F, 4F, -1F, 2, 12, 2);
        bipedLeftArm.setRotationPoint(5F, 2.0F, 0.0F);
        
        ModelRenderer extension2 = new ModelRenderer(this, 44, 18);
        extension2.mirror = true;
        extension2.addBox(-1, -2, -1F, 2, 6, 2);
        bipedLeftArm.addChild(extension2);
        
        bipedRightLeg = new ModelRenderer(this, 4, 20);
        bipedRightLeg.addBox(-1F, 0.0F, -1F, 2, 10, 2);
        bipedRightLeg.setRotationPoint(-2F, 12F, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 4, 20);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1F, 0.0F, -1F, 2, 10, 2);
        bipedLeftLeg.setRotationPoint(2.0F, 12F, 0.0F);
    }
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        GL11.glPushMatrix();
    	GL11.glTranslatef(0, 0.14f, 0);
        super.render(entity, f, f1, f2, f3, f4, f5);
        GL11.glPopMatrix();

    }
}

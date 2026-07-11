// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.model;

import net.minecraft.client.model.ModelRenderer;


// Referenced classes of package net.minecraft.src:
//            ModelBase, ModelRenderer, MathHelper, Entity

public class ModelFurryFemale extends ModelNPCFemale
{
	public ModelRenderer Snout;
	public ModelRenderer Snout2;
	public ModelRenderer Tail;
	public ModelRenderer LeftEar;
	public ModelRenderer RightEar;
	public ModelRenderer LeftWing;
	public ModelRenderer RightWing;
	public ModelRenderer LeftHorn;
	public ModelRenderer RightHorn;
	
    public ModelFurryFemale(int width, int height,float f)
    {
        super(width,height,f);
    }


    public void init(float f, float f1)
    {
        super.init(f,f1);
        Snout = new ModelRenderer(this, 0, 32);
        Snout.addBox(0F, 0F, 0F, 4, 3, 3);
        Snout.setRotationPoint(-2F, -3F, -7F);
        bipedHead.addChild(Snout);
        
        Snout2 = new ModelRenderer(this, 0, 38);
        Snout2.addBox(0F, 0F, 0F, 4, 3, 1);
        Snout2.setRotationPoint(-2F, -3F, -5F);
        bipedHead.addChild(Snout2);
        
        LeftEar = new ModelRenderer(this, 14, 32);
        LeftEar.addBox(-1.5F, -3F, -0.1F, 3, 3, 2);
        LeftEar.setRotationPoint(3F, -7.5F, 2F);
        bipedHead.addChild(LeftEar);
        
        RightEar = new ModelRenderer(this, 14, 32);
        RightEar.mirror = true;
        RightEar.addBox(-1.5F, -3F, -0.1F, 3, 3, 2);
        RightEar.setRotationPoint(-3F, -7.5F, 2F);
        bipedHead.addChild(RightEar);

        Tail = new ModelRenderer(this, 24, 32);
        Tail.addBox(0F, 0F, 0F, 2, 9, 2);
        Tail.setRotationPoint(-1F, 11F, 1F);
        setRotation(Tail, 0.8714253F, 0F, 0F);
        bipedBody.addChild(Tail);

        LeftWing = new ModelRenderer(this, 32, 32);
        LeftWing.mirror = true;
        LeftWing.addBox(0F, 0F, 0F, 15, 20, 0);
        LeftWing.setRotationPoint(1F, -2F, 0F);
        setRotation(LeftWing, 0.3141593F, -0.5235988F, -0.3490659F);
        bipedBody.addChild(LeftWing);
        
        RightWing = new ModelRenderer(this, 32, 32);
        RightWing.addBox(-15F, 0F, 0F, 15, 20, 0);
        RightWing.setRotationPoint(-1F, -2F, 0F);
        setRotation(RightWing, 0.3141593F, 0.5235988F, 0.3490659F);
        bipedBody.addChild(RightWing);
        
        LeftHorn = new ModelRenderer(this, 0, 42);
        LeftHorn.mirror = true;
        LeftHorn.addBox(0F, 0F, 0F, 5, 2, 2);
        LeftHorn.setRotationPoint(4F, -7F, 0F);
        bipedHead.addChild(LeftHorn);
        
        RightHorn = new ModelRenderer(this, 0, 42);
        RightHorn.addBox(0F, 0F, 0F, 5, 2, 2);
        RightHorn.setRotationPoint(-9F, -7F, 0F);
        bipedHead.addChild(RightHorn);

		ModelRenderer earleft = new ModelRenderer(this, 56, 23);
		earleft.mirror = true;
		earleft.addBox(-1.466667F, -4F, 0F, 3, 8, 1);
		earleft.setRotationPoint(2.533333F, -12F, 0F);
		bipedHead.addChild(earleft);

		ModelRenderer earright = new ModelRenderer(this, 56, 23);
		earright.addBox(-1.5F, -4F, 0F, 3, 8, 1);
		earright.setRotationPoint(-2.466667F, -12F, 0F);
		bipedHead.addChild(earright);

		ModelRenderer tailbottom = new ModelRenderer(this, 56, 18);
		tailbottom.addBox(-1.5F, -2F, 0F, 3, 4, 1);
		tailbottom.setRotationPoint(0F, 8.5F, 2F);
		tailbottom.setTextureSize(64, 32);
		tailbottom.mirror = true;
		setRotation(tailbottom, 0F, 0F, -1.570796F);
		bipedBody.addChild(tailbottom);

		ModelRenderer tailtop = new ModelRenderer(this, 56, 16);
		tailtop.addBox(0F, 0F, 0F, 2, 1, 1);
		tailtop.setRotationPoint(-1F, 6F, 2F);
		tailtop.setTextureSize(64, 32);
		tailtop.mirror = true;
		bipedBody.addChild(tailtop);

		ModelRenderer tooth = new ModelRenderer(this, 14, 40);
		tooth.addBox(-1F, -2F, -5F, 2, 1, 1);
		tooth.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(tooth);
		
		ModelRenderer face = new ModelRenderer(this, 14, 37);
		face.addBox(1F, 0F, 0F, 4, 2, 1);
		face.setRotationPoint(-3F, -4F, -5F);
		bipedHead.addChild(face);
    }

}

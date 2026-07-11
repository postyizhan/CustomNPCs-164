// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.client.renderer;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLiving;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.model.ModelNPCFemale;

// Referenced classes of package net.minecraft.src:
//            RenderLiving, ModelBiped, EntityPlayer, InventoryPlayer, 
//            ItemStack, ItemArmor, ModelRenderer, EnumAction, 
//            EntityPlayerSP, RenderManager, Tessellator, FontRenderer, 
//            Item, Block, RenderBlocks, ItemRenderer, 
//            MathHelper, ItemPotion, EntityLiving, Entity

public class RenderNPCHumanFemale extends RenderNPCHumanMale
{

    public RenderNPCHumanFemale(ModelNPCFemale mainmodel, ModelNPCFemale armorChest, ModelNPCFemale armor)
    {
        super(mainmodel, armorChest,armor);
    }

    protected int setArmorModel(EntityNPCInterface entityplayer, int i, float f)
    {
        ((ModelNPCFemale)mainModel).Breasts.showModel = entityplayer.inventory.armorItemInSlot(1) == null;
        for(Object child : ((ModelNPCFemale)mainModel).Breasts.childModels)
        	((ModelRenderer)child).showModel = entityplayer.inventory.armorItemInSlot(1) == null;
        
        return super.func_130006_a(entityplayer, i, f);
    }



    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return setArmorModel((EntityNPCInterface)entityliving, i, f);
    }


}

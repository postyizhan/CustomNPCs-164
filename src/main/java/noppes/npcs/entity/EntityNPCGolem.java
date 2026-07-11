// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.constants.EnumAnimation;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, Item, EntityPlayer, InventoryPlayer, 
//            ItemStack, World, NBTTagCompound

public class EntityNPCGolem extends EntityNPCInterface
{

    public EntityNPCGolem(World world)
    {
        super(world);
        labelOffset = 0.5F;
        display.texture = "customnpcs:textures/entity/golem/Iron Golem.png";
    }
    
    public void updateHitbox() {
		
		if(currentAnimation == EnumAnimation.LYING){
			width = height = 0.5f;
		}
		else if (currentAnimation == EnumAnimation.SITTING){
			width = 1.4f;
			height = 2.3f;
		}
		else{
			width = 1.4f;
			height = 2.9f;
		}
		width = (width / 5f) * display.modelSize;
		height = (height / 5f) * display.modelSize;
	}
}
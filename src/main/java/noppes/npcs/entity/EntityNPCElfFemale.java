// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, Item, EntityPlayer, InventoryPlayer, 
//            ItemStack, World, NBTTagCompound

public class EntityNPCElfFemale extends EntityNPCInterface
{
    public EntityNPCElfFemale(World world)
    {
        super(world);
        display.texture = "customnpcs:textures/entity/elffemale/ElfFemale.png";
		scaleX = 0.8f;
		scaleY = 1f;
		scaleZ = 0.8f;
    }

}

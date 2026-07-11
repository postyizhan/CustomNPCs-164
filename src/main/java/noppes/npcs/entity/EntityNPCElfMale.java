// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, Item, EntityPlayer, InventoryPlayer, 
//            ItemStack, World, NBTTagCompound

public class EntityNPCElfMale extends EntityNPCInterface
{
    public EntityNPCElfMale(World world)
    {
        super(world);
		scaleX = 0.85f;
		scaleY = 1.07f;
		scaleZ = 0.85f;
		display.texture = "customnpcs:textures/entity/elfmale/ElfMale.png";
    }

}

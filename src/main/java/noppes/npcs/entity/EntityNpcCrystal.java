// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, Item, EntityPlayer, InventoryPlayer, 
//            ItemStack, World, NBTTagCompound

public class EntityNpcCrystal extends EntityNPCInterface
{
    public int innerRotation;
    public EntityNpcCrystal(World world)
    {
        super(world);
        innerRotation = rand.nextInt(0x186a0);
		scaleX = 0.7f;
		scaleY = 0.7f;
		scaleZ = 0.7f;
		labelOffset = 0.8f;
		display.texture = "customnpcs:textures/entity/crystal/EnderCrystal.png";
    }
    public void onUpdate()
    {
        innerRotation++;
        super.onUpdate();
    }

}

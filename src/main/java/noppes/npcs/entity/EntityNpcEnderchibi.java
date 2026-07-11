// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package noppes.npcs.entity;

import net.minecraft.world.World;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.client.NoppesUtil;

// Referenced classes of package net.minecraft.src:
//            EntityAnimal, Item, EntityPlayer, InventoryPlayer, 
//            ItemStack, World, NBTTagCompound

public class EntityNpcEnderchibi extends EntityNPCInterface
{
    public EntityNpcEnderchibi(World world)
    {
        super(world);
        display.texture = "customnpcs:textures/entity/enderchibi/MrEnderchibi.png";
    }
    public void onUpdate(){
    	super.onUpdate();
    	if(worldObj.isRemote){
    		NoppesUtil.spawnEnderchibi(this);
    	}
    }

}

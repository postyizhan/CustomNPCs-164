package noppes.npcs.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;

public class ItemGunChainsaw extends ItemNpcWeaponInterface{

	public ItemGunChainsaw(int par1, EnumToolMaterial tool) {
		super(par1, tool);
	}

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
    {
    	if(par2EntityLiving.getHealth() <= 0)
    		return false;
        double x = par2EntityLiving.posX;
        double y = par2EntityLiving.posY+ par2EntityLiving.height / 2;
        double z = par2EntityLiving.posZ;
        
        par3EntityLiving.worldObj.playSoundEffect(x, y, z, "random.explode", 0.8F, (1.0F + (par3EntityLiving.worldObj.rand.nextFloat() - par3EntityLiving.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        par3EntityLiving.worldObj.spawnParticle("largeexplode", x, y , z, 0.0D, 0.0D, 0.0D);
        return super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
    }
}

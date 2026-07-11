package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.entity.EntityProjectile;

import org.lwjgl.opengl.GL11;

public class ItemMachineGun extends ItemNpcInterface{

	public ItemMachineGun(int par1) {
		super(par1);
        this.setMaxDamage(80);
        setCreativeTab(CustomItems.tabWeapon);
	}
    public void onPlayerStoppedUsing(ItemStack stack, World par2World, EntityPlayer player, int count) {
    	if(player.capabilities.isCreativeMode)
    		return;
    	
    	int ticks = getMaxItemUseDuration(stack) - count;
    	
    	int shotsleft = stack.stackTagCompound.getInteger("ShotsLeft") - ticks/6;
    	if(stack.stackTagCompound.getInteger("Reloading") == 1){
    		shotsleft = ticks/3;
    		if(ticks > 24)
    			shotsleft = 8;
    		if(shotsleft > 1){
	    		stack.stackTagCompound.setInteger("ShotsLeft", shotsleft);
	    		stack.stackTagCompound.setInteger("Reloading", 0);
    		}
    	}
    	else if(shotsleft <= 0){
    		stack.stackTagCompound.setInteger("Reloading", 1);
    		stack.damageItem(1, player);
    	}
    	else
    		stack.stackTagCompound.setInteger("ShotsLeft", shotsleft);
    	
    }

    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) 
    {
    	if(player.worldObj.isRemote){
    		return;
    	}
    	
    	int ticks = getMaxItemUseDuration(stack) - count;
    	if(ticks % 6 != 0){
	    	return;
	    }
    	 
    	int shotsleft = stack.stackTagCompound.getInteger("ShotsLeft") - ticks/6;
    	if(!player.capabilities.isCreativeMode){
	    	if(stack.stackTagCompound.getInteger("Reloading") == 1 && player.inventory.hasItem(CustomItems.bulletBlack.itemID)){
	    		if(ticks > 0 && ticks <= 24){
	    			player.worldObj.playSoundAtEntity(player, "customnpcs:gun.ak47chamberround", 1.0F,1);
	    		}
	    		return;
	    	}
	    	else if((shotsleft <= 0 || !player.inventory.hasItem(CustomItems.bulletBlack.itemID)))
	        {
	    		player.worldObj.playSoundAtEntity(player, "customnpcs:gun.empty", 1.0F,1);
	        	return;
	        }
    	}
    	EntityProjectile projectile = new EntityProjectile(player.worldObj, player, new ItemStack(CustomItems.bulletBlack,1, 0), false);
    	projectile.damage = 4;
    	projectile.setSpeed(40);
    	projectile.shoot(2);
    			
		if(!player.capabilities.isCreativeMode)
			player.inventory.consumeInventoryItem(CustomItems.bulletBlack.itemID);
    	
		player.worldObj.playSoundAtEntity(player, "customnpcs:gun.pistolshot", 0.9F, itemRand.nextFloat() * 0.3F + 0.8F);
		player.worldObj.spawnEntityInWorld(projectile);
    	
    	
    }
	@Override
	public void renderSpecial(){
        GL11.glScalef(0.7f, 0.7f,0.7f);
    	GL11.glTranslatef(0f, 0.2f, 0);
	}
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		if(!player.capabilities.isCreativeMode && !player.inventory.hasItem(CustomItems.bulletBlack.itemID))
			stack.stackTagCompound.setInteger("Reloading", 1);
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }
	@Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

	public EnumAction getItemUseAction(ItemStack stack)
    {	
		if(stack.stackTagCompound == null || stack.stackTagCompound.getInteger("Reloading") == 0)
			return EnumAction.bow;
		
		return EnumAction.block;
    }
}

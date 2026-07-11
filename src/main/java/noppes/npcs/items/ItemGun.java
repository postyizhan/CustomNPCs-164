package noppes.npcs.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.entity.EntityProjectile;

import org.lwjgl.opengl.GL11;

public class ItemGun extends ItemNpcInterface{

	private EnumNpcToolMaterial material;
	public ItemGun(int par1, EnumNpcToolMaterial material) {
		super(par1);
        this.maxStackSize = 1;
		this.material = material;
        this.setMaxDamage(material.getMaxUses());
        setCreativeTab(CustomItems.tabWeapon);
	}
	@Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World worldObj, EntityPlayer player, int par4) {
    	if(worldObj.isRemote)
    		return;

        if (!hasBullet(player))
        {
    		worldObj.playSoundAtEntity(player, "customnpcs:gun.empty", 1.0F,1);
        	return;
        }
    	int ticks = getMaxItemUseDuration(par1ItemStack) - par4;
    	if(ticks < 10){
    		return;
    	}
        par1ItemStack.damageItem(1, player);
		ItemBullet bullet = (ItemBullet) getBullet(player);
		int damage = (bullet.getBulletDamage() + material.getDamageVsEntity() + 1) / 2 + 5;
    	EntityProjectile projectile = new EntityProjectile(worldObj, player, new ItemStack(getBullet(player)), false);
    	projectile.damage = damage;
    	projectile.setSpeed(40);
    	projectile.shoot(material.getDamageVsEntity() + 1);
		
		if(!player.capabilities.isCreativeMode)
			player.inventory.consumeInventoryItem(getBullet(player).itemID);
    	
		worldObj.playSoundAtEntity(player, "customnpcs:gun.pistolshot", 1.0F,itemRand.nextFloat() * 0.3F + 0.8F);
		worldObj.spawnEntityInWorld(projectile);
    }    
    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) 
    {
    	int ticks = getMaxItemUseDuration(stack) - count;
    	if(ticks == 8 && !player.worldObj.isRemote){
    		player.worldObj.playSoundAtEntity(player, "customnpcs:gun.pistoltrigger", 1.0F,
    				1.0F / (player.worldObj.rand.nextFloat() * 0.4F + 0.8F));
    	}
    }

	@Override
	public void renderSpecial(){
        GL11.glScalef(0.7f, 0.7f,0.7f);
    	GL11.glTranslatef(0f, 0.3f, 0);
	}
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
    	par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
	@Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }
	private boolean hasBullet(EntityPlayer par3EntityPlayer){
		Item bullet = getBullet(par3EntityPlayer);
		return bullet != null && bullet.itemID >= 0;
	}
	private Item getBullet(EntityPlayer par3EntityPlayer){
		switch(material){
		case EMERALD:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletEmerald.itemID))
				return CustomItems.bulletEmerald;
		case DIA:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletDiamond.itemID))
				return CustomItems.bulletDiamond;
		case IRON:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletIron.itemID))
				return CustomItems.bulletIron;
		case BRONZE:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletBronze.itemID))
				return CustomItems.bulletBronze;
		case GOLD:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletGold.itemID))
				return CustomItems.bulletGold;
		case STONE:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletStone.itemID))
				return CustomItems.bulletStone;
		case WOOD:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletWood.itemID))
				return CustomItems.bulletWood;
		default:
			if(par3EntityPlayer.inventory.hasItem(CustomItems.bulletBlack.itemID) || par3EntityPlayer.capabilities.isCreativeMode)
				return CustomItems.bulletBlack;
		
		}
		return null;
	}

	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }
}

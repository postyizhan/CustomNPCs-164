package noppes.npcs.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.entity.EntityMagicProjectile;
import noppes.npcs.entity.EntityProjectile;

public class ItemStaff extends ItemNpcInterface{

	private EnumNpcToolMaterial material;
	public ItemStaff(int par1, EnumNpcToolMaterial material) {
		super(par1);
		this.material = material;
		setCreativeTab(CustomItems.tabWeapon);
	}
	public void renderSpecial(){}
	@Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldObj, EntityPlayer player, int par4) {
    	if(worldObj.isRemote){
    		return;
    	}
    	if(stack.stackTagCompound == null)
    		return;
		Entity entity = ((WorldServer)player.worldObj).getEntityByID(stack.stackTagCompound.getInteger("MagicProjectile"));
		if(entity == null || !(entity instanceof EntityProjectile))
			return;
		EntityProjectile item = (EntityProjectile) entity;

		item.prevRotationYaw = item.rotationYaw = player.rotationYaw;
		item.prevRotationPitch = item.rotationPitch = player.rotationPitch;
		item.shoot(2);

		player.worldObj.playSoundAtEntity(player, "customnpcs:magic.shot", 1.0F,1);
    	
	}
    public void onUsingItemTick(ItemStack stack, EntityPlayer player, int count) 
    {
    	int tick = getMaxItemUseDuration(stack) - count;
    	if(player.worldObj.isRemote){
    		spawnParticle(stack, player);
	    	return;
    	}
    	int chargeTime = 20 + material.getHarvestLevel() * 8;
    	if(tick == chargeTime){
    		if(!player.capabilities.isCreativeMode){
    			if(!player.inventory.hasItem(CustomItems.mana.itemID))
    				return;
    			player.inventory.consumeInventoryItem(CustomItems.mana.itemID);
    		}
    		player.worldObj.playSoundAtEntity(player, "customnpcs:magic.charge", 1.0F,1);
    		if(stack.stackTagCompound == null){
    			stack.stackTagCompound = new NBTTagCompound();
    		}
    		int damage = 6 + material.getDamageVsEntity() + player.worldObj.rand.nextInt(4);
        	EntityProjectile projectile = new EntityMagicProjectile(player.worldObj, player, getProjectile(stack), false);
        	projectile.damage = damage;
        	projectile.setSpeed(25);
        	double dx = -MathHelper.sin((float) ((player.rotationYaw / 180F) * Math.PI)) * MathHelper.cos((float) ((player.rotationPitch / 180F) * Math.PI));
        	double dz = MathHelper.cos((float) ((player.rotationYaw / 180F) * Math.PI)) * MathHelper.cos((float) ((player.rotationPitch / 180F) * Math.PI));
        	projectile.setPosition(player.posX + dx * 0.8, player.posY + 1.5 - player.rotationPitch/40, player.posZ + dz * 0.8);
        	player.worldObj.spawnEntityInWorld(projectile);
        	stack.stackTagCompound.setInteger("MagicProjectile", projectile.entityId);
    	}
    	if(tick > chargeTime && stack.stackTagCompound != null){
    		Entity entity = ((WorldServer)player.worldObj).getEntityByID(stack.stackTagCompound.getInteger("MagicProjectile"));
    		if(entity == null || !(entity instanceof EntityProjectile))
    			return;
    		EntityProjectile item = (EntityProjectile) entity;
    		item.ticksInAir = 0;

        	double dx = -MathHelper.sin((float) ((player.rotationYaw / 180F) * Math.PI)) * MathHelper.cos((float) ((player.rotationPitch / 180F) * Math.PI));
        	double dz = MathHelper.cos((float) ((player.rotationYaw / 180F) * Math.PI)) * MathHelper.cos((float) ((player.rotationPitch / 180F) * Math.PI));
        	item.setPosition(player.posX + dx * 0.8, player.posY + 1.5 - player.rotationPitch/40, player.posZ + dz * 0.8);
    	}
    	
    }
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

	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }
	public ItemStack getProjectile(ItemStack stack){
		if(stack.getItem() == CustomItems.staffWood){
			return new ItemStack(CustomItems.spellNature);
		}
		if(stack.getItem() == CustomItems.staffStone){
			return new ItemStack(CustomItems.spellDark);
		}
		if(stack.getItem() == CustomItems.staffIron){
			return new ItemStack(CustomItems.spellHoly);
		}
		if(stack.getItem() == CustomItems.staffBronze){
			return new ItemStack(CustomItems.spellLightning);
		}
		if(stack.getItem() == CustomItems.staffGold){
			return new ItemStack(CustomItems.spellFire);
		}
		if(stack.getItem() == CustomItems.staffDiamond){
			return new ItemStack(CustomItems.spellIce);
		}
		if(stack.getItem() == CustomItems.staffEmerald){
			return new ItemStack(CustomItems.spellArcane);
		}
		return new ItemStack(CustomItems.orb,1,stack.getItemDamage());
	}
	public void spawnParticle(ItemStack stack, EntityPlayer player){
		if(stack.getItem() == CustomItems.staffWood){
			CustomNpcs.proxy.spawnParticle(player,"Spell",5,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",12,2);
		}
		if(stack.getItem() == CustomItems.staffStone){
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x563357,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x432744,2);
		}
		if(stack.getItem() == CustomItems.staffBronze){
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x83F7F6,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x5CF0FF,2);
		}
		if(stack.getItem() == CustomItems.staffIron){
			CustomNpcs.proxy.spawnParticle(player,"Spell",0xFCFFC9,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",0xEFFF97,2);
		}
		if(stack.getItem() == CustomItems.staffGold){
			CustomNpcs.proxy.spawnParticle(player,"Spell",1,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",14,2);
		}
		if(stack.getItem() == CustomItems.staffDiamond){
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x94DFED,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",0x44B6FF,2);
		}
		if(stack.getItem() == CustomItems.staffEmerald){
			CustomNpcs.proxy.spawnParticle(player,"Spell",0xFFC3E7,2);
			CustomNpcs.proxy.spawnParticle(player,"Spell",0xFB92FF,2);
		}
	}
}

// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCHumanMale;
import noppes.npcs.permissions.CustomNpcsPermissions;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


// Referenced classes of package net.minecraft.src:
//            Item, World, Block, BlockFire, 
//            ItemStack, EntityPlayer

public class ItemNpcWand extends Item
{
    public ItemNpcWand(int i)
    {
        super(i - 26700 + CustomNpcs.ItemStartId);
        maxStackSize = 1;
        setCreativeTab(CustomItems.tab);
    }
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		if(!par2World.isRemote)
			return par1ItemStack;
    	CustomNpcs.proxy.openGui((EntityNPCInterface)null,EnumGuiType.NpcRemote, 0, 0, 0);
        return par1ItemStack;
    }
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
		if(par3World.isRemote)
			return true;
		if(CustomNpcs.OpsOnly && !MinecraftServer.getServer().getConfigurationManager().getOps().contains(player.username.toLowerCase())){
			MinecraftServer.getServer().logWarning(player.username + ": tried to use custom npcs without being an op");
		}
		else if(CustomNpcsPermissions.Instance.hasPermission(player.username, "customnpcs.npc.create")){
			EntityNPCHumanMale npc = (EntityNPCHumanMale)EntityList.createEntityByName("npchumanmale", par3World);
	    	npc.startPos = new int[]{par4,par5,par6};
	    	
			npc.setLocationAndAngles((float)par4 + 0.5F, npc.getStartYPos(), (float)par6 + 0.5F, player.rotationYaw, player.rotationPitch);

			par3World.spawnEntityInWorld(npc);
			npc.setHealth(npc.getMaxHealth());
			
			NoppesUtilServer.sendOpenGui(player,EnumGuiType.MainMenuDisplay,npc);
		}
        return true;
    }
	
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
		return 0x8B4513;
    }
    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = Item.hoeIron.getIconFromDamage(0);
    }

    @Override
    public Item setUnlocalizedName(String name){
		GameRegistry.registerItem(this, name);
    	return super.setUnlocalizedName(name);
    }
}

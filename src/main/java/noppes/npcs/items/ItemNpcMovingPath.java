// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package noppes.npcs.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.EntityNPCInterface;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


// Referenced classes of package net.minecraft.src:
//            Item, World, Block, BlockFire, 
//            ItemStack, EntityPlayer

public class ItemNpcMovingPath extends Item
{
    public ItemNpcMovingPath(int i)
    {
        super(i - 26700 + CustomNpcs.ItemStartId);
        maxStackSize = 1;
        setCreativeTab(CustomItems.tab);
    }
    
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
		EntityNPCInterface npc = getNpc(par1ItemStack, par2World);
		if(npc != null)
			NoppesUtilServer.sendOpenGui(par3EntityPlayer, EnumGuiType.MovingPath, npc);
        return par1ItemStack;
    }
    
	@Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer player, World par3World, int x, int y, int z, int par7, float par8, float par9, float par10)
    {		
		EntityNPCInterface npc = getNpc(par1ItemStack, par3World);
		if(npc == null)
			return true;
		List<int[]> list = npc.ai.getMovingPath();
		list.add(new int[]{x,y,z});
		player.addChatMessage("Added point x:" + x + " y:"+ y + " z:" + z + " to npc " + npc.getEntityName());
        return true;
    }
	
	private EntityNPCInterface getNpc(ItemStack item, World world){
		if(world.isRemote || item.stackTagCompound == null)
			return null;
		
		Entity entity = world.getEntityByID(item.stackTagCompound.getInteger("NPCID"));
		if(entity == null || !(entity instanceof EntityNPCInterface))
			return null;
		
		return (EntityNPCInterface) entity;
	}
	
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
		return 0x8B4513;
    }
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = Item.swordIron.getIconFromDamage(0);
    }

    @Override
    public Item setUnlocalizedName(String name){
		GameRegistry.registerItem(this, name);
    	return super.setUnlocalizedName(name);
    }
}

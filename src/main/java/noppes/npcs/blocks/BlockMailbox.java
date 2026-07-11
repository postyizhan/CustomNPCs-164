package noppes.npcs.blocks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.constants.EnumPacketType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMailbox extends BlockContainer{
	
	public int renderId = -1;

	public BlockMailbox(int par1) {
		super(par1, Material.rock);
		setCreativeTab(CustomItems.tab);
	}

    @Override   
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
    }
    
    @Override    
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	if(!par1World.isRemote){
    		NoppesUtilServer.sendData(player, EnumPacketType.Gui, EnumGuiType.PlayerMailbox, i, j, k);
    	}
		return true;
    }
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileMailbox();
	}
	
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this,1,getDamageValue(world, x, y, z)));
        return ret;
    }

    public int damageDropped(int par1)
    {
        return par1 >> 2;
    }
    
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        l %= 4;
        
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l | par6ItemStack.getItemDamage() << 2, 2);
    }
    
	public boolean isOpaqueCube(){
		return false;
	}
	
	public boolean renderAsNormalBlock(){
		return false;
	}
	public int getRenderType(){
		return renderId; 	
	}
//
//    @SideOnly(Side.CLIENT)
//    public void registerIcons(IconRegister par1IconRegister)
//    {
//    	
//    }
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return Block.slowSand.getBlockTextureFromSide(par1);
    }
}

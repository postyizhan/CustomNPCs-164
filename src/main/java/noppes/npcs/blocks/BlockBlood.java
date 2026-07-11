package noppes.npcs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlood extends Block{
	
    @SideOnly(Side.CLIENT)
    private Icon field_94458_cO;
    @SideOnly(Side.CLIENT)
    private Icon field_94459_cP;
    
    public BlockBlood(int par1)
    {
        super(par1, Material.rock);
		this.setBlockUnbreakable();
		setCreativeTab(CustomItems.tabMisc);	
		setBlockBounds(0.01f, 0.01f, 0.01f, 0.99f, 0.99f, 0.99f);
		this.setLightValue(0.08f);
    }

//    @Override
//    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
//    {
//        int var6 = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
//        return ((var6 + par5) % 3) + 1;
//    }
    @Override
    @SideOnly(Side.CLIENT)

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
    	if(par2 % 3 == 1)
    		return field_94459_cP;
    	if(par2 % 3 == 2)
    		return field_94458_cO;
        return blockIcon;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
    	return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4 , par2, par3, par4 );
    }
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }    
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(this.getTextureName());
        this.field_94459_cP = par1IconRegister.registerIcon(getTextureName() + "2");
        this.field_94458_cO = par1IconRegister.registerIcon(getTextureName() + "3");
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int par2, int par3, int par4, int par5)
    {
        Block block = Block.blocksList[world.getBlockId(par2, par3, par4)];
        return block != null && block.renderAsNormalBlock();
    }
    public boolean isOpaqueCube()
    {
        return false;
    }
    public int getRenderBlockPass()
    {
        return 1;
    }
    
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack item)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw / 90.0F) + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
    }
}

package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.constants.EnumGuiType;

public class BlockCarpentryBench extends BlockContainer
{
    public BlockCarpentryBench(int par1)
    {
        super(par1, Material.iron);
        this.setCreativeTab(CustomItems.tab);
    }

    @Override    
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	if(!par1World.isRemote){
    		player.openGui(CustomNpcs.instance, EnumGuiType.PlayerAnvil.ordinal(), par1World, i, j, k);
    	}
		return true;
    }

	@Override
    public TileEntity createNewTileEntity(World var1){
		return new TileBlockAnvil();
	}

    @Override    
    public boolean isOpaqueCube()
    {
        return false;
    }   
    @Override    
    public int getRenderType()
    {
        return -1;
    }
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override    
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override    
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLiving, ItemStack item)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLiving.rotationYaw / 90.0F) + 0.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
    }
}

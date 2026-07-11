package noppes.npcs.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockLightable extends BlockRotated{

	protected BlockLightable(Block block, boolean lit) {
		super(block);
		
        if (lit)
            this.setLightValue(1.0F);
	}

	public abstract Block unlitBlock();
	public abstract Block litBlock();
	

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
        TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(litBlock() == this)
        	world.setBlock(x, y, z, unlitBlock().blockID, world.getBlockMetadata(x, y, z), 2);
        else
        	world.setBlock(x, y, z, litBlock().blockID, world.getBlockMetadata(x, y, z), 2);
    	tile.validate();
    	world.setBlockTileEntity(x, y, z, tile);
        return true;
    }

    @Override
    public int idDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_){
        return litBlock().blockID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_){
        return litBlock().blockID;
    }

    @Override
    protected ItemStack createStackedBlock(int p_149644_1_){
        return new ItemStack(litBlock());
    }

}

package noppes.npcs.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.blocks.tiles.TileBanner;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBanner extends BlockNpcContainerInterface{
	
	public int renderId = -1;

	public BlockBanner() {
        super(Material.rock);
        setBlockBounds(0, 0, 0, 1, 2, 1);
	}
    @Override    
    public boolean onBlockActivated(World par1World, int i, int j, int k, EntityPlayer player, int par6, float par7, float par8, float par9){
    	ItemStack item = player.inventory.getCurrentItem();
    	if(item == null)
    		return false;
    	int meta = par1World.getBlockMetadata(i, j, k);
    	if(meta >= 7)
    		j--;
    	TileBanner tile = (TileBanner) par1World.getBlockTileEntity(i, j, k);
    	if(tile.canEdit())
    		return true;
    	else if(item.getItem() != Item.dyePowder)
    		return false;
    	
    	int color = BlockColored.getDyeFromBlock(item.getItemDamage());
    	if(tile.color != color){
    		NoppesUtilServer.consumeItemStack(1, player);
			tile.color = color;
	    	par1World.markBlockForUpdate(i, j, k);
    	}
    	return true;
    }
    @Override   
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List){
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
        par3List.add(new ItemStack(par1, 1, 4));
    }

    @Override   
    public int damageDropped(int par1)
    {
        return par1 % 7;
    }

    @Override 
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        int meta = world.getBlockMetadata(x, y, z);
        if(meta >= 7)
            setBlockBounds(0, -1, 0, 1, 1, 1);
        else
            setBlockBounds(0, 0, 0, 1, 2, 1);
    }

    @Override   
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
    	if(!par1World.isAirBlock(par2, par3 + 1, par4)){
    		par1World.setBlockToAir(par2, par3, par4);
    	}
    	else{
    		
	        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	        l %= 4;
	
	    	TileBanner tile = (TileBanner) par1World.getBlockTileEntity(par2, par3, par4);
	    	tile.rotation = l;
	    	tile.color = 15 - par6ItemStack.getItemDamage();
	    	tile.time = System.currentTimeMillis();
	    	
	        par1World.setBlockMetadataWithNotify(par2, par3, par4, par6ItemStack.getItemDamage() , 2);
	        par1World.setBlock(par2, par3 + 1, par4, this.blockID, par6ItemStack.getItemDamage() + 7, 2);

	    	if(par5EntityLivingBase instanceof EntityPlayer && par1World.isRemote)
	    		((EntityPlayer)par5EntityLivingBase).addChatMessage("availability.editIcon");
    	}
    }

    @Override   
	public boolean isOpaqueCube(){
		return false;
	}

    @Override   
	public boolean renderAsNormalBlock(){
		return false;
	}
    @Override   
	public int getRenderType(){
		return renderId; 	
	}

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister){
    	
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int p_149691_1_, int meta){
    	meta %= 7;
    	if(meta == 1)
            return Block.stone.getIcon(p_149691_1_, 0);
    	else if(meta == 2)
            return Block.blockIron.getIcon(p_149691_1_, 0);
    	else if(meta == 3)
            return Block.blockGold.getIcon(p_149691_1_, 0);
    	else if(meta == 4)
            return Block.blockDiamond.getIcon(p_149691_1_, 0);
        return Block.planks.getIcon(p_149691_1_, 0);
    }

	@Override
	public TileEntity createTileEntity(World var1, int var2) {
		if(var2 < 7)
			return new TileBanner();
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileBanner();
	}

	@Override
    public void onBlockHarvested(World p_149681_1_, int p_149681_2_, int p_149681_3_, int p_149681_4_, int p_149681_5_, EntityPlayer p_149681_6_)
    {
        if (p_149681_5_ >= 7 && p_149681_1_.getBlockId(p_149681_2_, p_149681_3_ - 1, p_149681_4_) == this.blockID)
            p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ - 1, p_149681_4_);
        
        else if (p_149681_5_ < 7 && p_149681_1_.getBlockId(p_149681_2_, p_149681_3_ + 1, p_149681_4_) == this.blockID)
            p_149681_1_.setBlockToAir(p_149681_2_, p_149681_3_ + 1, p_149681_4_);
    }
}

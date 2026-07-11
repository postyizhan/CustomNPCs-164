package noppes.npcs.blocks;

import java.awt.Color;
import java.util.List;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import noppes.npcs.CustomNpcs;

public class BlockCrystal extends BlockBreakable{
    
	public BlockCrystal() {
		super(CustomNpcs.BlockStartId++, "customnpcs:npcCrystal",Material.glass, false);
		setLightValue(0.8f);
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
    public int getRenderBlockPass(){
        return 1;
    }

    @Override
    public int damageDropped(int meta){
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int item, CreativeTabs tab, List list){
        for (int i = 0; i < 16; ++i){
        	list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z){
        return getRenderColor(world.getBlockMetadata(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int par1){
    	float[] color = EntitySheep.fleeceColorTable[par1];
        return new Color(color[0],color[1],color[2]).getRGB();
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public int getRenderColor(int meta){
//        return MapColor.getMapColorForBlockColored(meta).colorValue;
//    }
    
    @Override   
    public String getUnlocalizedName(){
    	return "item.npcCrystal";
    }
}

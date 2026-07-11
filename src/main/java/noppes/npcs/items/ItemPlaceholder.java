package noppes.npcs.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPlaceholder extends ItemBlock{
	private Block p_i45328_1_;
	public ItemPlaceholder(int id, Block p_i45328_1_) {
		super(id);
		setHasSubtypes(true);
		this.p_i45328_1_ = p_i45328_1_;
	}
	
	@Override
    public String getUnlocalizedName(ItemStack par1ItemStack){
        return super.getUnlocalizedName(par1ItemStack) + "_" + par1ItemStack.getItemDamage();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamage(int par1)
    {
        return this.p_i45328_1_.getIcon(0, par1);
    }
}

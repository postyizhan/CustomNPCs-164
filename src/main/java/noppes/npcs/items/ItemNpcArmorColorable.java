package noppes.npcs.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNpcArmorColorable extends ItemArmor{

	private String texture;
	public ItemNpcArmorColorable(int par1, EnumArmorMaterial par2EnumArmorMaterial,int par4, String texture) {
		super(par1 - 26700 + CustomNpcs.ItemStartId, par2EnumArmorMaterial, 0, par4);
		this.texture = texture;
		setCreativeTab(CustomItems.tabArmor);
	}
	
	
	@Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type){
		if(type != null)
			return "customnpcs:textures/gui/invisible.png";
		if(armorType == 2)
			return "customnpcs:textures/armor/" + texture + "_2.png";
		return "customnpcs:textures/armor/" + texture + "_1.png";
    }
	
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2){
        int j = this.getColor(par1ItemStack);
        if (j < 0)
            return 16777215;
        return j;
    }
    
    public int getColor(ItemStack par1ItemStack){
        NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

        if (nbttagcompound == null)
            return 10511680;
        else{
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
            return nbttagcompound1 == null ? 10511680 : (nbttagcompound1.hasKey("color") ? nbttagcompound1.getInteger("color") : 10511680);
        }
    }

	@Override
	public void removeColor(ItemStack par1ItemStack) {
		NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

		if (nbttagcompound != null) {
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color")) {
				nbttagcompound1.removeTag("color");
			}
		}
	}
	
    public void func_82813_b(ItemStack par1ItemStack, int par2){
        NBTTagCompound nbttagcompound = par1ItemStack.getTagCompound();

        if (nbttagcompound == null){
            nbttagcompound = new NBTTagCompound();
            par1ItemStack.setTagCompound(nbttagcompound);
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (!nbttagcompound.hasKey("display"))
            nbttagcompound.setTag("display", nbttagcompound1);

        nbttagcompound1.setInteger("color", par2);
        
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIconFromDamageForRenderPass(int par1, int par2){
        return this.getIconFromDamage(par1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses(){
    	return true;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister){
        this.itemIcon = par1IconRegister.registerIcon(this.getIconString());
    }

    @Override
    public boolean hasColor(ItemStack par1ItemStack){
        return !par1ItemStack.hasTagCompound() ? false : (!par1ItemStack.getTagCompound().hasKey("display") ? false : par1ItemStack.getTagCompound().getCompoundTag("display").hasKey("color"));
    }

    @Override
    public Item setUnlocalizedName(String name){
		GameRegistry.registerItem(this, name);
    	return super.setUnlocalizedName(name);
    }
}

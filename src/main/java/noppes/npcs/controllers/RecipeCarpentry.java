package noppes.npcs.controllers;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import noppes.npcs.NBTTags;
import noppes.npcs.NoppesUtilPlayer;

public class RecipeCarpentry implements IRecipe{
	public int id = -1;
	
	public String name = "";
	
    public int recipeWidth = 4;

    public int recipeHeight = 4;
    
    private ItemStack[] recipeItems = new ItemStack[16];

    public ItemStack recipeOutput;
    
    public Availability availability = new Availability();
    
    public boolean isGlobal = false;
    
    public boolean ignoreDamage = false;
    
    public RecipeCarpentry(){
    	
    }
    public RecipeCarpentry(int id,String name){
    	this.id = id;
    	this.name = name;
    }

	public void readNBT(NBTTagCompound compound) {
		id = compound.getInteger("ID");
		recipeWidth = compound.getInteger("Width");
		recipeHeight = compound.getInteger("Height");
		recipeOutput = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("Item"));
		recipeItems = NBTTags.getItemStackArray(compound.getTagList("Materials"));
		availability.readFromNBT(compound.getCompoundTag("Availability"));
		ignoreDamage = compound.getBoolean("IgnoreDamage");
		name = compound.getString("Name");
		isGlobal = compound.getBoolean("Global");
	}

	public NBTTagCompound writeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger("ID", id);
		compound.setInteger("Width", recipeWidth);
		compound.setInteger("Height", recipeHeight);
		if(recipeOutput != null)
			compound.setCompoundTag("Item", recipeOutput.writeToNBT(new NBTTagCompound()));
		compound.setTag("Materials", NBTTags.nbtItemStackArray(recipeItems));
		compound.setCompoundTag("Availability", availability.writeToNBT(new NBTTagCompound()));
		compound.setString("Name", name);
		compound.setBoolean("Global", isGlobal);
		compound.setBoolean("IgnoreDamage", ignoreDamage);
		return compound;
	}

	@Override
	public boolean matches(InventoryCrafting par1InventoryCrafting, World world) {
        for (int var2 = 0; var2 <= 4 - this.recipeWidth; ++var2)
        {
            for (int var3 = 0; var3 <= 4 - this.recipeHeight; ++var3)
            {
                if (this.checkMatch(par1InventoryCrafting, var2, var3, true))
                {
                    return true;
                }

                if (this.checkMatch(par1InventoryCrafting, var2, var3, false))
                {
                    return true;
                }
            }
        }

        return false;
	}



    /**
     * Checks if the region of a crafting inventory is match for the recipe.
     */
    private boolean checkMatch(InventoryCrafting par1InventoryCrafting, int par2, int par3, boolean par4)
    {
        for (int var5 = 0; var5 < 4; ++var5)
        {
            for (int var6 = 0; var6 < 4; ++var6)
            {
                int var7 = var5 - par2;
                int var8 = var6 - par3;
                ItemStack var9 = null;

                if (var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight)
                {
                    if (par4)
                    {
                        var9 = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
                    }
                    else
                    {
                        var9 = this.recipeItems[var7 + var8 * this.recipeWidth];
                    }
                }

                ItemStack var10 = par1InventoryCrafting.getStackInRowAndColumn(var5, var6);

                if (var10 != null || var9 != null)
                {
                	if(!NoppesUtilPlayer.compareItems(var9, var10, ignoreDamage)){
                		return false;
                	}
                }
            }
        }

        return true;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		if(recipeOutput == null)
			return null;
		return recipeOutput.copy();
	}

	@Override
	public int getRecipeSize() {
		return 16;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}
    public void addRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj)
    {
        String var3 = "";
        int var4 = 0;
        int var5 = 0;
        int var6 = 0;
        int var9;

        if (par2ArrayOfObj[var4] instanceof String[])
        {
            String[] var7 = (String[])((String[])par2ArrayOfObj[var4++]);
            String[] var8 = var7;
            var9 = var7.length;

            for (int var10 = 0; var10 < var9; ++var10)
            {
                String var11 = var8[var10];
                ++var6;
                var5 = var11.length();
                var3 = var3 + var11;
            }
        }
        else
        {
            while (par2ArrayOfObj[var4] instanceof String)
            {
                String var13 = (String)par2ArrayOfObj[var4++];
                ++var6;
                var5 = var13.length();
                var3 = var3 + var13;
            }
        }

        HashMap var14;

        for (var14 = new HashMap(); var4 < par2ArrayOfObj.length; var4 += 2)
        {
            Character var16 = (Character)par2ArrayOfObj[var4];
            ItemStack var17 = null;

            if (par2ArrayOfObj[var4 + 1] instanceof Item)
            {
                var17 = new ItemStack((Item)par2ArrayOfObj[var4 + 1]);
            }
            else if (par2ArrayOfObj[var4 + 1] instanceof Block)
            {
                var17 = new ItemStack((Block)par2ArrayOfObj[var4 + 1], 1, -1);
            }
            else if (par2ArrayOfObj[var4 + 1] instanceof ItemStack)
            {
                var17 = (ItemStack)par2ArrayOfObj[var4 + 1];
            }

            var14.put(var16, var17);
        }

        ItemStack[] var15 = new ItemStack[var5 * var6];

        for (var9 = 0; var9 < var5 * var6; ++var9)
        {
            char var18 = var3.charAt(var9);

            if (var14.containsKey(Character.valueOf(var18)))
            {
                var15[var9] = ((ItemStack)var14.get(Character.valueOf(var18))).copy();
            }
            else
            {
                var15[var9] = null;
            }
        }
        
        this.recipeOutput = par1ItemStack;
        this.recipeItems = var15;
        this.recipeWidth = var5;
        this.recipeHeight = var6;
        
        if(var5 == 4 || var6 == 4)
        	isGlobal = false;
    }
    public ItemStack getCraftingItem(int i){
    	if(recipeItems == null || i >= recipeItems.length)
    		return null;
    	return recipeItems[i];
    }
    public void setCraftingItem(int i, ItemStack item){
    	if(i < recipeItems.length)
    		recipeItems[i] = item;
    }
	public void clear() {
		this.recipeOutput = null;
		this.recipeItems = new ItemStack[16];
	}
}

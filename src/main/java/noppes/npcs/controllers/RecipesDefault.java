package noppes.npcs.controllers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.CustomItems;
public class RecipesDefault {
	Object[][] recipes = new Object[][]{
			new Object[]{"XXXY"," ZM ","  M ",'Y', Block.lever, 'M', Item.stick, 'Z', Block.stoneButton,'X',null},
			new Object[]{
					"Gun Wooden",Block.planks, CustomItems.gunWood,1,0,0,
					"Gun Stone",Block.cobblestone, CustomItems.gunStone,1,0,0,
					"Gun Iron",Item.ingotIron, CustomItems.gunIron,1,0,0,
					"Gun Gold",Item.ingotGold, CustomItems.gunGold,1,0,0,
					"Gun Diamond",Item.diamond, CustomItems.gunDiamond,1,0,0,
					"Gun Emerald",Item.emerald, CustomItems.gunEmerald,1,0,0,
			},
			new Object[]{"X",'X',null},
			new Object[]{
					"Bullet Wooden",Block.planks, CustomItems.bulletWood,9,0,0,
					"Bullet Stone",Block.cobblestone, CustomItems.bulletStone,9,0,0,
					"Bullet Iron",Item.ingotIron, CustomItems.bulletIron,9,0,0,
					"Bullet Gold",Item.ingotGold, CustomItems.bulletGold,9,0,0,
					"Bullet Diamond",Item.diamond, CustomItems.bulletDiamond,9,0,0,
					"Bullet Emerald",Item.emerald, CustomItems.bulletEmerald,9,0,0,
			},
			new Object[]{"XX"," Y"," Y",'X',Item.bread,'Y',null},
			new Object[]{"Npc Wand",Item.stick, CustomItems.wand,1,0,1,},
			new Object[]{"XX","XY"," Y",'X',Item.bread,'Y',null},
			new Object[]{"Npc Cloner",Item.stick, CustomItems.cloner,1,0,1,},
			new Object[]{"XYX","Z Z","Z Z",'X',Block.planks,'Z',Item.stick,'Y',null},
			new Object[]{"Carpentry Bench",Block.workbench, CustomItems.carpentyBench,1,0,1,},
			new Object[]{"XY",'X',Item.redstone,'Y',null},
			new Object[]{"Mana",Item.glowstone, CustomItems.mana,1,0,1,},
	};
	public RecipesDefault(RecipeController controller){
		int id = 0;
		for(int i = 0; i < recipes.length; i+=2){
			Object[] recipe = recipes[i];
			Object[] materials = recipes[i+1];
			for(int j = 0; j < materials.length; j += 6,id++){
				String name = (String) materials[j];
				Object material = materials[j+1];
				Object item = materials[j+2];
				if(item == null)
					continue;
				int shifted = -1;
				if(item instanceof Block)
					shifted = ((Block)item).blockID;
				else
					shifted = ((Item)item).itemID;
				ItemStack output = new ItemStack(shifted, (Integer)materials[j+3],(Integer)materials[j+4]);
				int type = (Integer) materials[j+5];
				
				RecipeCarpentry recipeAnvil = new RecipeCarpentry(id,name);
				recipe[recipe.length-1] = material;
				recipeAnvil.isGlobal = type == 1;
				recipeAnvil.addRecipe(output, recipe);
				if(recipeAnvil.isGlobal)
					RecipeController.instance.anvilRecipes.put(recipeAnvil.id, recipeAnvil);
				else{
					RecipeController.instance.globalRecipes.put(recipeAnvil.id, recipeAnvil);
				}
			}
		}

	}
}

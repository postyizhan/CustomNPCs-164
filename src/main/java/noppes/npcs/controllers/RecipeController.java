package noppes.npcs.controllers;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.npcs.CustomNpcs;

public class RecipeController {
	private static Collection<RecipeCarpentry> prevRecipes;
	public HashMap<Integer,RecipeCarpentry> globalRecipes = new HashMap<Integer, RecipeCarpentry>();
	public HashMap<Integer,RecipeCarpentry> anvilRecipes = new HashMap<Integer, RecipeCarpentry>();
	public static RecipeController instance;
	
	public RecipeController(){
		instance = this;
		loadCategories();
		
		reloadGlobalRecipes(globalRecipes);
	}
	public static void reloadGlobalRecipes(HashMap<Integer,RecipeCarpentry> globalRecipes){
		
		List list = CraftingManager.getInstance().getRecipeList();
		if(prevRecipes != null){
			list.removeAll(prevRecipes);
		}
		
		prevRecipes = new HashSet<RecipeCarpentry>(globalRecipes.values());
		list.addAll(prevRecipes);
	}
	
	private void loadCategories(){
		File saveDir = CustomNpcs.getWorldSaveDirectory();
		try {
	        File file = new File(saveDir, "recipes.dat");
	        if(file.exists()){
		        loadCategories(file);
	        }
	        else
	    		loadDefaultRecipes();
		} catch (Exception e) {
			e.printStackTrace();
			try {
		        File file = new File(saveDir, "recipes.dat_old");
		        if(file.exists()){
		        	loadCategories(file);
		        }
		        
			} catch (Exception ee) {
				e.printStackTrace();
			}
		}
	}
	private void loadDefaultRecipes() {
		new RecipesDefault(this);		
		saveCategories();
	}

	private void loadCategories(File file) throws Exception{
        NBTTagCompound nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(file));
        NBTTagList list = nbttagcompound1.getTagList("Data");
        HashMap<Integer,RecipeCarpentry> globalRecipes = new HashMap<Integer, RecipeCarpentry>();
        HashMap<Integer,RecipeCarpentry> anvilRecipes = new HashMap<Integer, RecipeCarpentry>();
        if(list != null){
            for(int i = 0; i < list.tagCount(); i++)
            {
            	RecipeCarpentry recipe = new RecipeCarpentry();
            	recipe.readNBT((NBTTagCompound)list.tagAt(i));
            	if(recipe.isGlobal)
            		globalRecipes.put(recipe.id,recipe);
            	else
            		anvilRecipes.put(recipe.id,recipe);
            }
        }
        this.anvilRecipes = anvilRecipes;
        this.globalRecipes = globalRecipes;
	}
	private void saveCategories(){
		try {
			File saveDir = CustomNpcs.getWorldSaveDirectory();
	        NBTTagList list = new NBTTagList();
	        for(RecipeCarpentry recipe : globalRecipes.values()){
	        	list.appendTag(recipe.writeNBT());
	        }
	        for(RecipeCarpentry recipe : anvilRecipes.values()){
	        	list.appendTag(recipe.writeNBT());
	        }
	        NBTTagCompound nbttagcompound = new NBTTagCompound();
	        nbttagcompound.setTag("Data", list);
            File file = new File(saveDir, "recipes.dat_new");
            File file1 = new File(saveDir, "recipes.dat_old");
            File file2 = new File(saveDir, "recipes.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file));
            if(file1.exists())
            {
                file1.delete();
            }
            file2.renameTo(file1);
            if(file2.exists())
            {
                file2.delete();
            }
            file.renameTo(file2);
            if(file.exists())
            {
                file.delete();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public RecipeCarpentry findMatchingRecipe(InventoryCrafting par1InventoryCrafting)
    {
    	for(RecipeCarpentry recipe : anvilRecipes.values()){
    		if(recipe.matches(par1InventoryCrafting,null))
    			return recipe;
    	}
    	return null;
    }

	public RecipeCarpentry getRecipe(int id) {
		if(globalRecipes.containsKey(id))
			return globalRecipes.get(id);
		if(anvilRecipes.containsKey(id))
			return anvilRecipes.get(id);
		return null;
	}

	public RecipeCarpentry saveRecipe(DataInputStream dis) throws IOException {
		RecipeCarpentry recipe = new RecipeCarpentry();
		recipe.readNBT(CompressedStreamTools.read(dis));
		
		RecipeCarpentry current = getRecipe(recipe.id);
		if(current != null && !current.name.equals(recipe.name)){
			while(containsRecipeName(recipe.name))
				recipe.name += "_";
		}
		
		if(recipe.id == -1){
			recipe.id = getUniqueId();
			while(containsRecipeName(recipe.name))
				recipe.name += "_";
		}
		if(recipe.isGlobal){
			anvilRecipes.remove(recipe.id);
			globalRecipes.put(recipe.id, recipe);
		}
		else{
			globalRecipes.remove(recipe.id);
			anvilRecipes.put(recipe.id, recipe);
		}
		saveCategories();
		reloadGlobalRecipes(globalRecipes);
		return recipe;
	}

	private int getUniqueId() {
		int id = 0;
		for(int key : globalRecipes.keySet()){
			if(key > id){
				id = key;
			}
		}
		for(int key : anvilRecipes.keySet()){
			if(key > id){
				id = key;
			}
		}
		id++;
		return id;
	}
	private boolean containsRecipeName(String name) {
		name = name.toLowerCase();
		for(RecipeCarpentry recipe : globalRecipes.values()){
			if(recipe.name.toLowerCase().equals(name))
				return true;
		}
		for(RecipeCarpentry recipe : anvilRecipes.values()){
			if(recipe.name.toLowerCase().equals(name))
				return true;
		}
		return false;
	}

	public RecipeCarpentry removeRecipe(int id) {
		RecipeCarpentry recipe = getRecipe(id);
		globalRecipes.remove(recipe.id);
		anvilRecipes.remove(recipe.id);
		saveCategories();
		reloadGlobalRecipes(globalRecipes);
		return recipe;
	}
}

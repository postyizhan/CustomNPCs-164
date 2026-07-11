package noppes.npcs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

public class NBTTags {

	public static HashMap<Integer, ItemStack> getItemStackList(
			NBTTagList tagList) {
		HashMap<Integer, ItemStack> list = new HashMap<Integer, ItemStack>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            try{
            	list.put(nbttagcompound.getByte("Slot") & 0xff, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
            catch(ClassCastException e){
            	list.put(nbttagcompound.getInteger("Slot"), ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
		return list;
	}
	
	public static ItemStack[] getItemStackArray(
			NBTTagList tagList) {
		ItemStack[] list = new ItemStack[tagList.tagCount()];
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
        	list[nbttagcompound.getByte("Slot") & 0xff] = ItemStack.loadItemStackFromNBT(nbttagcompound);
        }
		return list;
	}
	
	public static ArrayList<int[]> getIntegerArraySet(NBTTagList tagList) {
		ArrayList<int[]> set = new ArrayList<int[]>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
        	NBTTagList list = (NBTTagList)tagList.tagAt(i);
        	int[] arr = new int[list.tagCount()];
            for(int j = 0; j < list.tagCount(); j++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)list.tagAt(j);
            	arr[j] = nbttagcompound.getInteger("Slot");
            }
        	set.add(arr);
        }
		return set;
	}

	public static HashMap<Integer, Boolean> getBooleanList(NBTTagList tagList) {
		HashMap<Integer, Boolean> list = new HashMap<Integer, Boolean>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getBoolean("Boolean"));
        }
		return list;
	}
	
	public static HashMap<Integer, Integer> getIntegerIntegerMap(
			NBTTagList tagList) {
		HashMap<Integer, Integer> list = new HashMap<Integer, Integer>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getInteger("Integer"));
        }
		return list;
	}
	
	public static HashMap<Integer, Long> getIntegerLongMap(
			NBTTagList tagList) {
		HashMap<Integer, Long> list = new HashMap<Integer, Long>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getLong("Long"));
        }
		return list;
	}
	
	public static HashSet<Integer> getIntegerSet(NBTTagList tagList) {
		HashSet<Integer> list = new HashSet<Integer>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
        	NBTBase base = tagList.tagAt(i);
        	if(base instanceof NBTTagInt)
        		list.add(((NBTTagInt)base).data);
        	else{
	            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
	            list.add(nbttagcompound.getInteger("Integer"));
        	}
        }
		return list;
	}

	public static HashMap<String, String> getStringStringMap(NBTTagList tagList) {
		HashMap<String, String> list = new HashMap<String, String>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getString("Slot"), nbttagcompound.getString("Value"));
        }
		return list;
	}

	public static HashMap<Integer, String> getIntegerStringMap(NBTTagList tagList) {
		HashMap<Integer, String> list = new HashMap<Integer, String>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getInteger("Slot"), nbttagcompound.getString("Value"));
        }
		return list;
	}
	
	public static HashMap<String, Integer> getStringIntegerMap(NBTTagList tagList) {
		HashMap<String, Integer> list = new HashMap<String, Integer>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            list.put(nbttagcompound.getString("Slot"), nbttagcompound.getInteger("Value"));
        }
		return list;
	}
	public static HashMap<String, Vector<String>> getVectorMap(NBTTagList tagList) {
		HashMap<String, Vector<String>> map = new HashMap<String, Vector<String>>();
        for(int i = 0; i < tagList.tagCount(); i++)
        {
        	Vector<String> values = new Vector<String>();
            NBTTagCompound nbttagcompound = (NBTTagCompound)tagList.tagAt(i);
            NBTTagList list = nbttagcompound.getTagList("Values");
            for(int j = 0; j < list.tagCount(); j++)
            {
                NBTTagCompound value = (NBTTagCompound)list.tagAt(j);
                values.add(value.getString("Value"));
            }
            
            map.put(nbttagcompound.getString("Key"), values);
        }
		return map;
	}

	public static int[] getIntArray(NBTTagList list) {
		int[] inta = new int[list.tagCount()];
		for (int i = 0; i < list.tagCount(); i++)
			inta[i] = ((NBTTagInt) list.tagAt(i)).data;
		return inta;
	}

	public static List<String> getStringList(NBTTagList tagList) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.tagAt(i);
			String line = nbttagcompound.getString("Line");
			list.add(line);
		}
		return list;
	}
	
	public static String[] getStringArray(NBTTagList tagList, int size) {
		String[] arr = new String[size];
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) tagList.tagAt(i);
			String line = nbttagcompound.getString("Value");
			int slot = nbttagcompound.getInteger("Slot");
			arr[slot] = line;
		}
		return arr;
	}
	
    public static NBTTagList nbtIntegerArraySet(List<int[]> set) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(set == null)
    		return nbttaglist;
        for(int[] arr : set)
        {
            NBTTagList list = new NBTTagList();
            for(int i : arr){
	            NBTTagCompound nbttagcompound = new NBTTagCompound();
	            nbttagcompound.setInteger("Slot", i);
	            
	            list.appendTag(nbttagcompound);
            }
            nbttaglist.appendTag(list);
        }
        return nbttaglist;
	}
	
    public static NBTTagList nbtItemStackList(HashMap<Integer,ItemStack>inventory) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(inventory == null)
    		return nbttaglist;
        for(int slot : inventory.keySet())
        {
        	ItemStack item = inventory.get(slot);
        	if(item == null)
        		continue;
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)slot);
            
            item.writeToNBT(nbttagcompound);
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}
	
    public static NBTTagList nbtItemStackArray(ItemStack[] inventory) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(inventory == null)
    		return nbttaglist;
        for(int slot = 0 ; slot < inventory.length; slot++)
        {
        	ItemStack item = inventory[slot];
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setByte("Slot", (byte)slot);

        	if(item != null)
        		item.writeToNBT(nbttagcompound);
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}

	public static NBTTagList nbtBooleanList(HashMap<Integer, Boolean> updatedSlots) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(updatedSlots == null)
    		return nbttaglist;
        HashMap<Integer,Boolean> inventory2 = updatedSlots;
        for(Integer slot : inventory2.keySet())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setBoolean("Boolean", inventory2.get(slot));
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}

	public static NBTTagList nbtIntegerIntegerMap(HashMap<Integer, Integer> lines) {
		NBTTagList nbttaglist = new NBTTagList();
		if(lines == null)
			return nbttaglist;
		for (int slot : lines.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setInteger("Integer", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerLongMap(HashMap<Integer, Long> lines) {
		NBTTagList nbttaglist = new NBTTagList();
		if(lines == null)
			return nbttaglist;
		for (int slot : lines.keySet()) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setLong("Long", lines.get(slot));
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtIntegerSet(HashSet<Integer> set) {
		NBTTagList nbttaglist = new NBTTagList();
		if(set == null)
			return nbttaglist;
		for (int slot : set) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Integer", slot);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}

	public static NBTTagList nbtVectorMap(HashMap<String, Vector<String>> map) {
        NBTTagList list = new NBTTagList();
        if(map == null)
        	return list;
        for(String key : map.keySet()){
        	NBTTagCompound compound = new NBTTagCompound();
        	compound.setString("Key", key);
            NBTTagList values = new NBTTagList();
        	for(String value : map.get(key)){
            	NBTTagCompound comp = new NBTTagCompound();
            	comp.setString("Value", value);
            	values.appendTag(comp);
        	}
            compound.setTag("Values", values);
        	list.appendTag(compound);
        }
		return list;
	}
	
	public static NBTTagList nbtStringStringMap(HashMap<String, String> map) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(map == null)
    		return nbttaglist;
        for(String slot : map.keySet())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Slot", slot);
            nbttagcompound.setString("Value", map.get(slot));
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}
	
	public static NBTTagList nbtStringIntegerMap(HashMap<String, Integer> map) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(map == null)
    		return nbttaglist;
        for(String slot : map.keySet())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setString("Slot", slot);
            nbttagcompound.setInteger("Value", map.get(slot));
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}

	public static NBTBase nbtIntegerStringMap(HashMap<Integer, String> map) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(map == null)
    		return nbttaglist;
        for(int slot : map.keySet())
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("Slot", slot);
            nbttagcompound.setString("Value", map.get(slot));
            
            nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}
	
	public static NBTTagList nbtStringArray(String[] list) {
        NBTTagList nbttaglist = new NBTTagList();
    	if(list == null)
    		return nbttaglist;
        for(int i = 0; i < list.length; i++){ 
        	if(list[i] == null)
        		continue;
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Value", list[i]);
			nbttagcompound.setInteger("Slot", i);
			nbttaglist.appendTag(nbttagcompound);
        }
        return nbttaglist;
	}
	
	public static NBTTagList nbtIntArray(int ad[]) {
		NBTTagList nbttaglist = new NBTTagList();
		int ad1[] = ad;
		for (int i : ad1) {
			nbttaglist.appendTag(new NBTTagInt(null, i));
		}
		return nbttaglist;
	}
	
	public static NBTTagList nbtStringList(List<String> list) {
		NBTTagList nbttaglist = new NBTTagList();
		for (String s : list) {
			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("Line", s);
			nbttaglist.appendTag(nbttagcompound);
		}
		return nbttaglist;
	}


}

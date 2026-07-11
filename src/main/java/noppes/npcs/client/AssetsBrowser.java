package noppes.npcs.client;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import noppes.npcs.CustomNpcs;

import org.apache.commons.lang3.StringUtils;

public class AssetsBrowser {
	public boolean isRoot;
	private int depth;
	private String folder;
	private static ArrayList<File> locations;
	public HashSet<String> folders = new HashSet<String>();
	public HashSet<String> files = new HashSet<String>();
	private String[] extensions;

	static{
		locations = new ArrayList<File>();
		URLClassLoader loader = (URLClassLoader) net.minecraft.client.Minecraft.class.getClassLoader();
		for(URL url : loader.getURLs()){
			try {
				File file = new File(url.toURI());
				if(isAssetFile(file)){
					locations.add(file);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isAssetFile(File file){
		try {
	        if (!file.isDirectory() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")))
	        {
	            ZipFile zip = new ZipFile(file);
	            Enumeration<? extends ZipEntry> entries = zip.entries();
	            while(entries.hasMoreElements()){
	                ZipEntry zipentry = entries.nextElement();
	                String entryName = zipentry.getName();
	        		if(entryName.startsWith("assets/")){
	    	            zip.close();
	        			return true;
	        		}
	                
	            }
	            zip.close();
	        }
	        else if(file.isDirectory()){
	        	for(File f : file.listFiles()){
	        		if(f.isDirectory() && f.getName().equals("assets"))
	        			return true;
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public AssetsBrowser(String folder, String[] extensions){
		this.extensions = extensions;
		this.setFolder(folder);
	}
	public void setFolder(String folder){
		if(!folder.endsWith("/"))
			folder += "/";
		isRoot = folder.length() <= 1;
		this.folder = "/assets" + folder;
		depth = StringUtils.countMatches(this.folder, "/");
		getFiles();
	}
	public AssetsBrowser(String[] extensions){
		this.extensions = extensions;
	}

	private void getFiles() {
		folders.clear();
		files.clear();
		for(File file : locations){
			progressFile(file);
		}
		ResourcePackRepository repos = Minecraft.getMinecraft().getResourcePackRepository();
		File file = new File(repos.getDirResourcepacks(),repos.getResourcePackName());
		if(file.exists()){
			progressFile(file);
		}
		checkFolder(new File(CustomNpcs.Dir,"assets"),CustomNpcs.Dir.getAbsolutePath().length());
	}
	private void progressFile(File file){
		try {
	        if (!file.isDirectory() && (file.getName().endsWith(".jar") || file.getName().endsWith(".zip")))
	        {
	            ZipFile zip = new ZipFile(file);
	            Enumeration<? extends ZipEntry> entries = zip.entries();
	            while(entries.hasMoreElements()){
	                ZipEntry zipentry = entries.nextElement();
	                String entryName = zipentry.getName();
	        		checkFile(entryName);
	                
	            }
	            zip.close();
	        }
	        else if(file.isDirectory()){
	        	int length = file.getAbsolutePath().length();
	        	checkFolder(file,length);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void checkFolder(File file, int length){
		File[] files = file.listFiles();
		if(files == null){
			return;
		}
    	for(File f : files){
    		String name = f.getAbsolutePath().substring(length);
    		name = name.replace("\\", "/");
            if(!name.startsWith("/"))
            	name = "/" + name;
    		if(f.isDirectory() && (folder.startsWith(name) || name.startsWith(folder))){
    			checkFile(name + "/");
    			checkFolder(f, length);
    		}
    		else
    			checkFile(name);
    	}
	}
	
	private void checkFile(String name){
        if(!name.startsWith("/"))
        	name = "/" + name;
        if(!name.startsWith(this.folder)){
        	return;
        }
        String[] split = name.split("/");
        int count = split.length;
        if(count == depth + 1){
        	if(validExtension(name))
        		files.add(split[depth]);
        }
        else if(depth + 1 < count){
        	folders.add(split[depth]);
        }
	}

	private boolean validExtension(String entryName) {
		int index = entryName.indexOf(".");
		if(index < 0)
			return false;
    	String extension = entryName.substring(index + 1);
    	for(String ex : extensions){
    		if(ex.equalsIgnoreCase(extension))
    			return true;
    	}
		return false;
	}

	public String getAsset(String asset) {
		String[] split = folder.split("/");
		if(split.length < 3)
			return null;
		String texture = split[2] + ":";
		texture += folder.substring(texture.length() + 8) + asset;
		return texture;
	}

	public static String getRoot(String asset) {
		String mod = "minecraft";
		int index = asset.indexOf(":");
		if(index > 0){
			mod = asset.substring(0,index);
			asset = asset.substring(index + 1);
		}
		if(asset.startsWith("/"))
			asset = asset.substring(1);
		String location = "/" + mod + "/" + asset;
		index = location.lastIndexOf("/");
		if(index > 0)
			location = location.substring(0,index);
		return location;
	}
}

package noppes.npcs.permissions;


public class CustomNpcsPermissions implements PermissionsInterface{
	public static PermissionsInterface Instance = new CustomNpcsPermissions();
	private final static String[] permissions = {"-customnpcs.*"};
	
	public boolean hasPermission(String username, String permission){
		return true;
	}
}

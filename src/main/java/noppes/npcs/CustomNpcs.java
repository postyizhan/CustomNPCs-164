package noppes.npcs;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.client.PacketHandlerClient;
import noppes.npcs.commands.CommandSlay;
import noppes.npcs.config.ConfigLoader;
import noppes.npcs.config.ConfigProp;
import noppes.npcs.constants.EnumModelType;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.entity.EntityChairMount;
import noppes.npcs.entity.EntityNPCDwarfFemale;
import noppes.npcs.entity.EntityNPCDwarfMale;
import noppes.npcs.entity.EntityNPCElfFemale;
import noppes.npcs.entity.EntityNPCElfMale;
import noppes.npcs.entity.EntityNPCEnderman;
import noppes.npcs.entity.EntityNPCFurryFemale;
import noppes.npcs.entity.EntityNPCFurryMale;
import noppes.npcs.entity.EntityNPCGolem;
import noppes.npcs.entity.EntityNPCHumanFemale;
import noppes.npcs.entity.EntityNPCHumanMale;
import noppes.npcs.entity.EntityNPCOrcFemale;
import noppes.npcs.entity.EntityNPCOrcMale;
import noppes.npcs.entity.EntityNPCPony;
import noppes.npcs.entity.EntityNPCVillager;
import noppes.npcs.entity.EntityNpcCrystal;
import noppes.npcs.entity.EntityNpcDragon;
import noppes.npcs.entity.EntityNpcEnderchibi;
import noppes.npcs.entity.EntityNpcMonsterFemale;
import noppes.npcs.entity.EntityNpcMonsterMale;
import noppes.npcs.entity.EntityNpcNagaFemale;
import noppes.npcs.entity.EntityNpcNagaMale;
import noppes.npcs.entity.EntityNpcSkeleton;
import noppes.npcs.entity.EntityNpcSlime;
import noppes.npcs.entity.EntityProjectile;
import noppes.npcs.events.CraftbenchEvent;
import noppes.npcs.events.EntityKilledEvent;
import noppes.npcs.events.ItemInteractEvent;
import noppes.npcs.events.PlayerGetHurtEvent;
import noppes.npcs.events.WorldLoadEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@NetworkMod(clientSideRequired = true, serverSideRequired = true, channels = { "CNPCs Player" }, packetHandler = PacketHandlerPlayer.class,
	clientPacketHandlerSpec = @SidedPacketHandler(channels = { "CNPCs Client", "CNPCs Player" }, packetHandler = PacketHandlerClient.class),
	serverPacketHandlerSpec = @SidedPacketHandler(channels = { "CNPCs Server", "CNPCs Player" }, packetHandler = PacketHandlerServer.class), 
	versionBounds = "[1.6.4b]"
)
@Mod(modid = "customnpcs", name = "CustomNpcs", version = "1.6.4b")
public class CustomNpcs {

	@ConfigProp(info = "Disable Chat Bubbles")
    public static boolean EnableChatBubbles = true;

	public static boolean TextureSelection = false;

	@ConfigProp
	public static boolean DisableExtraNpcItems = false;

	@ConfigProp(info = "Default Item ID range is from 26700")
	public static int ItemStartId = 26700;
	@ConfigProp(info = "Default Block ID range is from 1525")
	public static int BlockStartId = 1525;
	@ConfigProp(info = "Uses unique entities ids")
	public static boolean UseUniqueEntities = true;
	@ConfigProp(info = "To use this UseUniqueEntities has to be false")
	public static int EntityStartId = 120;
	@ConfigProp(info = "Navigation search range for NPCs. Not recommended to increase if you have a slow pc or on a server")
	public static int NpcNavRange = 32;

	@ConfigProp(info = "Set to true if you want the dialog command option to be able to use op commands like tp etc")
	public static boolean NpcUseOpCommands = false;
	
	@ConfigProp public static boolean InventoryGuiEnabled = true;
	
	public static long ticks;

	@SidedProxy(clientSide = "noppes.npcs.client.ClientProxy", serverSide = "noppes.npcs.CommonProxy")
	public static CommonProxy proxy;

	@ConfigProp(info="Enables CustomNpcs startup update message")
	public static boolean EnableUpdateChecker = true;

	public static CustomNpcs instance;
	
	public static boolean FreezeNPCs = false;
	
	@ConfigProp(info = "Only ops can create and edit npcs")
	public static boolean OpsOnly = false;

	public static File Dir;

	public CustomNpcs(){
		instance = this;
	}
	
	@EventHandler
	public void load(FMLInitializationEvent ev) {
//		if (ForgeVersion.getBuildVersion() < 251) {
//			Exception ex = new Exception(
//					"Install a newer version of Forge. Recommended version currently is 251. Visit http://jenkins.minecraftforge.net:7070/job/forge/251/");
//			Minecraft.getMinecraft().displayCrashReport(new CrashReport("Failed to start game", ex));
//		}
//		ModContainer mod = Loader.instance().activeModContainer();
//		ModMetadata meta = mod.getMetadata();
//		meta.modId = "customnpcs";
//		meta.url = "http://tinyurl.com/customnpcs";
//		meta.description = "The Custom Npcs mod lets you create your own npcs and lets you customize them how you want";
//		meta.authorList.add("Noppes");
//		meta.authorList.add("DarkSignal");
//		meta.credits = "A lot of people, see the list at the forum";
//		meta.logoFile = "customnpcs:textures/gui/logo.png";
//		meta.autogenerated = false;
		
		MinecraftServer server = MinecraftServer.getServer();
		String dir = "";
		if (server != null) {
			dir = new File(".").getAbsolutePath();
		} else
			dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
		Dir = new File(dir, "customnpcs");
		Dir.mkdir();

		ConfigLoader configLoader = new ConfigLoader(this.getClass(), new File(dir, "config"), "CustomNpcs");
		configLoader.loadConfig();
		
		if(NpcNavRange < 16)
			NpcNavRange = 16;

		CustomItems.load();
		
		proxy.load();
		NetworkRegistry.instance().registerGuiHandler(this, proxy);

		MinecraftForge.EVENT_BUS.register(new PlayerGetHurtEvent());
		MinecraftForge.EVENT_BUS.register(new EntityKilledEvent());
		MinecraftForge.EVENT_BUS.register(new CraftbenchEvent());
		MinecraftForge.EVENT_BUS.register(new ItemInteractEvent());
		MinecraftForge.EVENT_BUS.register(new WorldLoadEvent());
		MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
		GameRegistry.registerPlayerTracker(new ServerTickHandler());
		
		registerNpc(EntityNPCHumanMale.class, EnumModelType.HumanMale.entityName, EnumModelType.HumanMale.id);
		registerNpc(EntityNPCVillager.class, "npcvillager", getEntityId());
		registerNpc(EntityNPCPony.class, "npcpony", getEntityId());
		registerNpc(EntityNPCHumanFemale.class, "npchumanfemale", getEntityId());
		registerNpc(EntityNPCDwarfMale.class, "npcdwarfmale", getEntityId());
		registerNpc(EntityNPCFurryMale.class, "npcfurrymale", getEntityId());
		registerNpc(EntityNpcMonsterMale.class, "npczombiemale", getEntityId());
		registerNpc(EntityNpcMonsterFemale.class, "npczombiefemale", getEntityId());
		registerNpc(EntityNpcSkeleton.class, "npcskeleton", getEntityId());
		registerNpc(EntityNPCDwarfFemale.class, "npcdwarffemale", getEntityId());
		registerNpc(EntityNPCFurryFemale.class, "npcfurryfemale", getEntityId());
		registerNpc(EntityNPCOrcMale.class, "npcorcfmale", getEntityId());
		registerNpc(EntityNPCOrcFemale.class, "npcorcfemale", getEntityId());
		registerNpc(EntityNPCElfMale.class, "npcelfmale", getEntityId());
		registerNpc(EntityNPCElfFemale.class, "npcelffemale", getEntityId());
		registerNpc(EntityNpcCrystal.class, "npccrystal", getEntityId());
		registerNpc(EntityNpcEnderchibi.class, "npcenderchibi", getEntityId());
		registerNpc(EntityNpcNagaMale.class, "npcnagamale", getEntityId());
		registerNpc(EntityNpcNagaFemale.class, "npcnagafemale", getEntityId());
		registerNpc(EntityNpcSlime.class, "NpcSlime", getEntityId());
		registerNpc(EntityNpcDragon.class, "NpcDragon", getEntityId());
		registerNpc(EntityNPCEnderman.class, "npcEnderman", getEntityId());
		registerNpc(EntityNPCGolem.class, "npcGolem", getEntityId());
		registerNpc(EntityChairMount.class, "CustomNpcChairMount", getEntityId());
		
		int thowid = getEntityId();
		EntityRegistry.registerGlobalEntityID(EntityProjectile.class, "throwableitem", thowid);
		EntityRegistry.registerModEntity(EntityProjectile.class, "throwableitem", thowid, this, 64, 3, true);
	}

	@EventHandler
	public void serverstart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSlay());
	}
	
	public static int getEntityId(){
		if(UseUniqueEntities)
			return EntityRegistry.findGlobalUniqueEntityId();
		return EntityStartId++;
	}

	private void registerNpc(Class<? extends Entity> cl, String name, int id) {
		EntityRegistry.registerGlobalEntityID(cl, name, id);
		EntityRegistry.registerModEntity(cl, name, id, this, 80, 3, true);
	}

	public static void GivePlayerItem(Entity entity, EntityPlayer player, ItemStack item) {
		if (entity.worldObj.isRemote) {
			return;
		}
		item = item.copy();
		float f = 0.7F;
		double d = (double) (entity.worldObj.rand.nextFloat() * f) + (double) (1.0F - f);
		double d1 = (double) (entity.worldObj.rand.nextFloat() * f) + (double) (1.0F - f);
		double d2 = (double) (entity.worldObj.rand.nextFloat() * f) + (double) (1.0F - f);
		EntityItem entityitem = new EntityItem(entity.worldObj, entity.posX + d, entity.posY + d1, entity.posZ + d2,
				item);
		entityitem.delayBeforeCanPickup = 2;
		entity.worldObj.spawnEntityInWorld(entityitem);

		int i = item.stackSize;

		if (player.inventory.addItemStackToInventory(item)) {
			GameRegistry.onPickupNotification(player, entityitem);
			entity.worldObj.playSoundAtEntity(entityitem, "random.pop", 0.2F,
					((entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.onItemPickup(entityitem, i);

			if (item.stackSize <= 0) {
				entityitem.setDead();
			}
		}
	}

	public static File getWorldSaveDirectory() {
		MinecraftServer server = MinecraftServer.getServer();
		File saves = new File(".");
		if (server != null && !server.isDedicatedServer())
			saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
		if (server != null) {
			File savedir = new File(new File(saves, server.getFolderName()), "customnpcs");
			if (!savedir.exists())
				savedir.mkdir();
			return savedir;
		}
		return null;
	}

}

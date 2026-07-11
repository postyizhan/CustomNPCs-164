package noppes.npcs;

import net.minecraft.block.Block;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import noppes.npcs.blocks.BlockBanner;
import noppes.npcs.blocks.BlockBarrel;
import noppes.npcs.blocks.BlockBeam;
import noppes.npcs.blocks.BlockBigSign;
import noppes.npcs.blocks.BlockBlood;
import noppes.npcs.blocks.BlockBook;
import noppes.npcs.blocks.BlockCampfire;
import noppes.npcs.blocks.BlockCandle;
import noppes.npcs.blocks.BlockCarpentryBench;
import noppes.npcs.blocks.BlockChair;
import noppes.npcs.blocks.BlockCouchWood;
import noppes.npcs.blocks.BlockCouchWool;
import noppes.npcs.blocks.BlockCrate;
import noppes.npcs.blocks.BlockCrystal;
import noppes.npcs.blocks.BlockLamp;
import noppes.npcs.blocks.BlockMailbox;
import noppes.npcs.blocks.BlockNpcRedstone;
import noppes.npcs.blocks.BlockPedestal;
import noppes.npcs.blocks.BlockPlaceholder;
import noppes.npcs.blocks.BlockShelf;
import noppes.npcs.blocks.BlockSign;
import noppes.npcs.blocks.BlockStool;
import noppes.npcs.blocks.BlockTable;
import noppes.npcs.blocks.BlockTallLamp;
import noppes.npcs.blocks.BlockTombstone;
import noppes.npcs.blocks.BlockWallBanner;
import noppes.npcs.blocks.BlockWaypoint;
import noppes.npcs.blocks.BlockWeaponRack;
import noppes.npcs.blocks.tiles.TileBanner;
import noppes.npcs.blocks.tiles.TileBarrel;
import noppes.npcs.blocks.tiles.TileBeam;
import noppes.npcs.blocks.tiles.TileBigSign;
import noppes.npcs.blocks.tiles.TileBlockAnvil;
import noppes.npcs.blocks.tiles.TileBook;
import noppes.npcs.blocks.tiles.TileBorder;
import noppes.npcs.blocks.tiles.TileCampfire;
import noppes.npcs.blocks.tiles.TileCandle;
import noppes.npcs.blocks.tiles.TileChair;
import noppes.npcs.blocks.tiles.TileCouchWood;
import noppes.npcs.blocks.tiles.TileCouchWool;
import noppes.npcs.blocks.tiles.TileCrate;
import noppes.npcs.blocks.tiles.TileLamp;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.blocks.tiles.TilePedestal;
import noppes.npcs.blocks.tiles.TileRedstoneBlock;
import noppes.npcs.blocks.tiles.TileShelf;
import noppes.npcs.blocks.tiles.TileSign;
import noppes.npcs.blocks.tiles.TileStool;
import noppes.npcs.blocks.tiles.TileTable;
import noppes.npcs.blocks.tiles.TileTallLamp;
import noppes.npcs.blocks.tiles.TileTombstone;
import noppes.npcs.blocks.tiles.TileWallBanner;
import noppes.npcs.blocks.tiles.TileWaypoint;
import noppes.npcs.blocks.tiles.TileWeaponRack;
import noppes.npcs.items.EnumNpcToolMaterial;
import noppes.npcs.items.ItemBanjo;
import noppes.npcs.items.ItemBattleAxe;
import noppes.npcs.items.ItemBroadSword;
import noppes.npcs.items.ItemBullet;
import noppes.npcs.items.ItemClaw;
import noppes.npcs.items.ItemCrossbow;
import noppes.npcs.items.ItemDagger;
import noppes.npcs.items.ItemDaggerReversed;
import noppes.npcs.items.ItemElementalStaff;
import noppes.npcs.items.ItemExcalibur;
import noppes.npcs.items.ItemGlaive;
import noppes.npcs.items.ItemGun;
import noppes.npcs.items.ItemGunChainsaw;
import noppes.npcs.items.ItemKunai;
import noppes.npcs.items.ItemKunaiReversed;
import noppes.npcs.items.ItemMachineGun;
import noppes.npcs.items.ItemMusic;
import noppes.npcs.items.ItemMusicClarinet;
import noppes.npcs.items.ItemMusicOracina;
import noppes.npcs.items.ItemMusket;
import noppes.npcs.items.ItemNpcArmor;
import noppes.npcs.items.ItemNpcArmorColorable;
import noppes.npcs.items.ItemNpcCloner;
import noppes.npcs.items.ItemNpcColored;
import noppes.npcs.items.ItemNpcInterface;
import noppes.npcs.items.ItemNpcMovingPath;
import noppes.npcs.items.ItemNpcWand;
import noppes.npcs.items.ItemNpcWeaponInterface;
import noppes.npcs.items.ItemOrb;
import noppes.npcs.items.ItemPlaceholder;
import noppes.npcs.items.ItemRotatedShield;
import noppes.npcs.items.ItemScythe;
import noppes.npcs.items.ItemShield;
import noppes.npcs.items.ItemSkirt;
import noppes.npcs.items.ItemSlingshot;
import noppes.npcs.items.ItemSpear;
import noppes.npcs.items.ItemStaff;
import noppes.npcs.items.ItemThrowingShuriken;
import noppes.npcs.items.ItemThrowingWeapon;
import noppes.npcs.items.ItemViolin;
import noppes.npcs.items.ItemWand;
import noppes.npcs.items.ItemWarhammer;
import cpw.mods.fml.common.registry.GameRegistry;

public class CustomItems {

	public static Item wand;
	public static Item cloner;
	//public static Item spawner;
	public static Item moving;	
	
	public static Item banjo;
	public static Item violin;
	public static Item violinbow;
	public static Item harp;
	public static Item guitar;
	public static Item frenchHorn;
	public static Item clarinet;
	public static Item ocarina;

	public static Item coinWood;
	public static Item coinStone;
	public static Item coinIron;
	public static Item coinGold;
	public static Item coinDiamond;
	public static Item coinBronze;
	public static Item coinEmerald;
	public static Item moneyBag;

	public static Item bulletWood;
	public static Item bulletStone;
	public static Item bulletIron;
	public static Item bulletGold;
	public static Item bulletDiamond;
	public static Item bulletBronze;
	public static Item bulletEmerald;
	public static Item bulletBlack;

	public static Item gunWood;
	public static Item gunStone;
	public static Item gunIron;
	public static Item gunGold;
	public static Item gunDiamond;
	public static Item gunBronze;
	public static Item gunEmerald;
	public static Item gunMachine;

	public static Item tridentWood;
	public static Item tridentStone;
	public static Item tridentIron;
	public static Item tridentGold;
	public static Item tridentDiamond;
	public static Item tridentBronze;
	public static Item tridentEmerald;
	public static Item tridentCurse;

	public static Item spellNature;
	public static Item spellArcane;
	public static Item spellItem;
	public static Item spellLightning;
	public static Item spellIce;
	public static Item spellFire;
	public static Item spellDark;
	public static Item spellHoly;

	public static Item staffWood;
	public static Item staffStone;
	public static Item staffIron;
	public static Item staffGold;
	public static Item staffDiamond;
	public static Item staffBronze;
	public static Item staffEmerald;
	public static Item staffElemental;
	public static Item orb;
	public static Item mana;

	public static Item kunai;
	public static Item shuriken;

	public static Item excalibur;
	public static Item letter;
	
	public static Block innDoor;
	public static Block redstoneBlock;
	public static Block carpentyBench;
	public static Block mailbox;
	public static Block waypoint;
	public static Block border;
	
	public static Block banner;
	public static Block wallBanner;
	public static Block tallLamp;
	public static Block blood;
	public static Block book;
	public static Block chair;
	public static Block crate;
	public static Block weaponsRack;
	public static Block pedestal;
	public static Block couchWool;
	public static Block couchWood;
	public static Block table;
	public static Block stool;
	public static Block bigsign;
	public static Block barrel;
	public static Block tombstone;
	public static Block shelf;
	public static Block sign;
	public static Block beam;
	public static Block crystal;

	public static Block lamp;
	public static Block campfire;
	public static Block candle;

	public static Block lamp_unlit;
	public static Block campfire_unlit;
	public static Block candle_unlit;
	
	public static Item crossbowBolt;

	public static CreativeTabNpcs tab = new CreativeTabNpcs("cnpcs");
	public static CreativeTabNpcs tabArmor;
	public static CreativeTabNpcs tabWeapon;
	public static CreativeTabNpcs tabMisc;
	public static CreativeTabNpcs tabBlocks;
	
	public static void load(){		
		GameRegistry.registerTileEntity(TileRedstoneBlock.class, "TileRedstoneBlock");
		GameRegistry.registerTileEntity(TileBlockAnvil.class, "TileBlockAnvil");
		GameRegistry.registerTileEntity(TileMailbox.class, "TileMailbox");
		GameRegistry.registerTileEntity(TileWaypoint.class, "TileWaypoint");
		GameRegistry.registerTileEntity(TileBanner.class, "TileNPCBanner");

		if(!CustomNpcs.DisableExtraNpcItems){
			GameRegistry.registerTileEntity(TileWallBanner.class, "TileNPCWallBanner");
			GameRegistry.registerTileEntity(TileTallLamp.class, "TileNPCTallLamp");
			GameRegistry.registerTileEntity(TileChair.class, "TileNPCChair");
			GameRegistry.registerTileEntity(TileCrate.class, "TileNPCCrate");
			GameRegistry.registerTileEntity(TileWeaponRack.class, "TileNPCWeaponRack");
			GameRegistry.registerTileEntity(TileCouchWool.class, "TileNPCCouchWool");
			GameRegistry.registerTileEntity(TileCouchWood.class, "TileNPCCouchWood");
			GameRegistry.registerTileEntity(TileTable.class, "TileNPCTable");
			GameRegistry.registerTileEntity(TileLamp.class, "TileNPCLamp");
			GameRegistry.registerTileEntity(TileCandle.class, "TileNPCCandle");
			GameRegistry.registerTileEntity(TileBorder.class, "TileNPCBorder");
			GameRegistry.registerTileEntity(TileStool.class, "TileNPCStool");
			GameRegistry.registerTileEntity(TileBigSign.class, "TileNPCBigSign");
			GameRegistry.registerTileEntity(TileBarrel.class, "TileNPCBarrel");
			GameRegistry.registerTileEntity(TileCampfire.class, "TileNPCCampfire");
			GameRegistry.registerTileEntity(TileTombstone.class, "TileNPCTombstone");
			GameRegistry.registerTileEntity(TileShelf.class, "TileNPCShelf");
			GameRegistry.registerTileEntity(TileSign.class, "TileNPCSign");
			GameRegistry.registerTileEntity(TileBeam.class, "TileNPCBeam");
			GameRegistry.registerTileEntity(TileBook.class, "TileNPCBook");
			GameRegistry.registerTileEntity(TilePedestal.class, "TileNPCPedestal");
		}
		
		wand = new ItemNpcWand(26724).setUnlocalizedName("npcWand").setFull3D();
		cloner = new ItemNpcCloner(26725).setUnlocalizedName("npcMobCloner").setFull3D();
		//spawner = new ItemNpcSpawner(26726).setUnlocalizedName("npcSpawner").setFull3D();
		moving = new ItemNpcMovingPath(26716).setUnlocalizedName("npcMovingPath").setFull3D();
		
		redstoneBlock = new BlockNpcRedstone(CustomNpcs.BlockStartId).setBlockUnbreakable().setResistance(2000).setUnlocalizedName("npcRedstoneBlock").setTextureName("customnpcs:npcRedstoneBlock");
		GameRegistry.registerBlock(redstoneBlock, "npcRedstoneBlock");
		
		carpentyBench = new BlockCarpentryBench(CustomNpcs.BlockStartId+1).setUnlocalizedName("npcCarpentyBench").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setTextureName("customnpcs:npcCarpentryBench");
		GameRegistry.registerBlock(carpentyBench, "npcCarpentyBench");
		tab.icon = CustomItems.carpentyBench.blockID;

		mailbox = new BlockMailbox(CustomNpcs.BlockStartId + 3).setUnlocalizedName("npcMailbox").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep);
		GameRegistry.registerBlock(mailbox, "npcMailbox");
		
		waypoint = new BlockWaypoint(CustomNpcs.BlockStartId + 4).setUnlocalizedName("npcWaypoint").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setTextureName("customnpcs:npcWaypoint");
		GameRegistry.registerBlock(waypoint, "npcWaypoint");

		
		banjo = new ItemBanjo(26732).setUnlocalizedName("npcBanjo").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBanjo");
		violin = new ItemViolin(26733).setUnlocalizedName("npcViolin").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcViolin");	
		violinbow = new ItemNpcInterface(26734).setUnlocalizedName("npcViolinBow").setFull3D().setMaxStackSize(1).setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcViolinBow");
		harp = new ItemViolin(26735).setUnlocalizedName("npcHarp").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcHarp");
		guitar = new ItemBanjo(26731).setUnlocalizedName("npcGuitar").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGuitar");
		frenchHorn = new ItemMusic(26857).setRotated().setUnlocalizedName("npcFrenchHorn").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrenchHorn");
		ocarina = new ItemMusicOracina(26858).setUnlocalizedName("npcOcarina").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcOcarina");
		clarinet = new ItemMusicClarinet(26859).setUnlocalizedName("npcClarinet").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcClarinet");
		
		
		if(!CustomNpcs.DisableExtraNpcItems){
			tabArmor = new CreativeTabNpcs("cnpcsa");
			tabWeapon = new CreativeTabNpcs("cnpcsw");
			tabMisc = new CreativeTabNpcs("cnpcsm");
			tabBlocks = new CreativeTabNpcs("cnpcsb");
			blood = new BlockBlood(CustomNpcs.BlockStartId+2).setUnlocalizedName("npcBloodBlock").setTextureName("customnpcs:npcBloodBlock");
			CustomNpcs.BlockStartId += 5;
			banner = new BlockBanner().setUnlocalizedName("npcBanner").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setCreativeTab(tabBlocks);
			wallBanner = new BlockWallBanner().setUnlocalizedName("npcWallBanner").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setCreativeTab(tabBlocks);
			tallLamp = new BlockTallLamp().setUnlocalizedName("npcTallLamp").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundMetalFootstep).setCreativeTab(tabBlocks);
			chair = new BlockChair().setUnlocalizedName("npcChair").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			crate = new BlockCrate().setUnlocalizedName("npcCrate").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			weaponsRack = new BlockWeaponRack().setUnlocalizedName("npcWeaponRack").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			couchWool = new BlockCouchWool().setUnlocalizedName("npcCouchWool").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			table = new BlockTable().setUnlocalizedName("npcTable").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			couchWood = new BlockCouchWood().setUnlocalizedName("npcCouchWood").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			lamp = new BlockLamp(true).setUnlocalizedName("npcLamp").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			candle = new BlockCandle(true).setUnlocalizedName("npcCandle").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			stool = new BlockStool().setUnlocalizedName("npcStool").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			Block placeholder = new BlockPlaceholder().setUnlocalizedName("npcPlaceholder").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabMisc);
			bigsign = new BlockBigSign().setUnlocalizedName("npcBigSign").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			barrel = new BlockBarrel().setUnlocalizedName("npcBarrel").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			campfire = new BlockCampfire(true).setUnlocalizedName("npcCampfire").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setCreativeTab(tabBlocks);
			tombstone = new BlockTombstone().setUnlocalizedName("npcTombstone").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setCreativeTab(tabBlocks);
			shelf = new BlockShelf().setUnlocalizedName("npcShelf").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			sign = new BlockSign().setUnlocalizedName("npcSign").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			beam = new BlockBeam().setUnlocalizedName("npcBeam").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			book = new BlockBook().setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			pedestal = new BlockPedestal().setUnlocalizedName("npcPedestal").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep).setCreativeTab(tabBlocks);
			crystal = new BlockCrystal().setUnlocalizedName("npcCrystal").setTextureName("customnpcs:npcCrystal").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundGlassFootstep).setCreativeTab(tabBlocks);
	
			campfire_unlit = new BlockCampfire(false).setUnlocalizedName("npcCampfire").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep);
			lamp_unlit = new BlockLamp(false).setUnlocalizedName("npcLamp").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep);
			candle_unlit = new BlockCandle(false).setUnlocalizedName("npcCandle").setHardness(5.0F).setResistance(10.0F).setStepSound(Block.soundWoodFootstep);

			
			GameRegistry.registerBlock(blood, "npcBloodBlock");
			GameRegistry.registerBlock(banner, ItemBlock.class, "npcBanner");
			GameRegistry.registerBlock(wallBanner, ItemBlock.class, "npcWallBanner");
			GameRegistry.registerBlock(tallLamp, ItemBlock.class, "npcTallLamp");
			GameRegistry.registerBlock(chair, ItemBlock.class, "npcChair");
			GameRegistry.registerBlock(crate, ItemBlock.class, "npcCrate");	
			GameRegistry.registerBlock(weaponsRack, ItemBlock.class, "npcWeaponRack");	
			GameRegistry.registerBlock(couchWool, ItemBlock.class, "npcCouchWool");	
			GameRegistry.registerBlock(couchWood, ItemBlock.class, "npcCouchWood");	
			GameRegistry.registerBlock(table, ItemBlock.class, "npcTable");	
			GameRegistry.registerBlock(stool, ItemBlock.class, "npcStool");	
			GameRegistry.registerBlock(placeholder, ItemPlaceholder.class, "npcPlaceholder");	
			GameRegistry.registerBlock(bigsign, "npcBigSign");
			GameRegistry.registerBlock(barrel, ItemBlock.class, "npcBarrel");	
			GameRegistry.registerBlock(tombstone, ItemBlock.class, "npcTombstone");	
			GameRegistry.registerBlock(shelf, ItemBlock.class, "npcShelf");	
			GameRegistry.registerBlock(sign, ItemBlock.class, "npcSign");
			GameRegistry.registerBlock(beam, ItemBlock.class, "npcBeam");
			GameRegistry.registerBlock(book, "npcBook");
			GameRegistry.registerBlock(pedestal, ItemBlock.class, "npcPedestal");
			GameRegistry.registerBlock(crystal, ItemNpcColored.class, "npcCrystalBlock");
			GameRegistry.registerBlock(campfire, "npcCampfire");
			GameRegistry.registerBlock(candle, "npcLamp");
			GameRegistry.registerBlock(lamp, "npcCandle");
			GameRegistry.registerBlock(campfire_unlit, "npcCampfireUnlit");
			GameRegistry.registerBlock(candle_unlit, "npcLampUnlit");
			GameRegistry.registerBlock(lamp_unlit, "npcCandleUnlit");
			
			
			coinWood = new ItemNpcInterface(26717).setUnlocalizedName("npcCoinWooden").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinWooden");
			coinStone = new ItemNpcInterface(26718).setUnlocalizedName("npcCoinStone").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinStone");
			coinBronze = new ItemNpcInterface(26719).setUnlocalizedName("npcCoinBronze").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinBronze");
			coinIron = new ItemNpcInterface(26720).setUnlocalizedName("npcCoinIron").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinIron");
			coinGold = new ItemNpcInterface(26721).setUnlocalizedName("npcCoinGold").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinGold");
			coinDiamond = new ItemNpcInterface(26722).setUnlocalizedName("npcCoinDiamond").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinDiamond");
			coinEmerald = new ItemNpcInterface(26723).setUnlocalizedName("npcCoinEmerald").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCoinEmerald");
			new ItemNpcInterface(26759).setUnlocalizedName("npcAncientCoin").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcAncientCoin");
			
			gunWood = new ItemGun(26820,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcWoodenGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenGun");
			bulletWood = new ItemBullet(26821,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcWoodenBullet").setMaxStackSize(64).setTextureName("customnpcs:npcWoodenBullet");
	
			gunStone = new ItemGun(26822,EnumNpcToolMaterial.STONE).setUnlocalizedName("npcStoneGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneGun");
			bulletStone = new ItemBullet(26823,EnumNpcToolMaterial.STONE).setUnlocalizedName("npcStoneBullet").setMaxStackSize(64).setTextureName("customnpcs:npcStoneBullet");
	
			gunIron = new ItemGun(26824,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcIronGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronGun");
			bulletIron = new ItemBullet(26825,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcIronBullet").setMaxStackSize(64).setTextureName("customnpcs:npcIronBullet");
	
			gunGold = new ItemGun(26826,EnumNpcToolMaterial.GOLD).setUnlocalizedName("npcGoldGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldGun");
			bulletGold = new ItemBullet(26827,EnumNpcToolMaterial.GOLD).setUnlocalizedName("npcGoldenBullet").setMaxStackSize(64).setTextureName("customnpcs:npcGoldenBullet");
	
			gunDiamond = new ItemGun(26828,EnumNpcToolMaterial.DIA).setUnlocalizedName("npcDiamondGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondGun");
			bulletDiamond = new ItemBullet(26829,EnumNpcToolMaterial.DIA).setUnlocalizedName("npcDiamondBullet").setMaxStackSize(64).setTextureName("customnpcs:npcDiamondBullet");

			gunBronze = new ItemGun(26830,EnumNpcToolMaterial.BRONZE).setUnlocalizedName("npcBronzeGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeGun");
			bulletBronze = new ItemBullet(26831,EnumNpcToolMaterial.BRONZE).setUnlocalizedName("npcBronzeBullet").setMaxStackSize(64).setTextureName("customnpcs:npcBronzeBullet");

			gunEmerald = new ItemGun(26832,EnumNpcToolMaterial.EMERALD).setUnlocalizedName("npcEmeraldGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldGun");
			bulletEmerald = new ItemBullet(26833,EnumNpcToolMaterial.EMERALD).setUnlocalizedName("npcEmeraldBullet").setMaxStackSize(64).setTextureName("customnpcs:npcEmeraldBullet");
	
			gunMachine = new ItemMachineGun(26834).setUnlocalizedName("npcMachineGun").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMachineGun");
			bulletBlack = new ItemBullet(26835,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcBlackBullet").setMaxStackSize(64).setTextureName("customnpcs:npcBlackBullet");

			EnumToolMaterial bronze = EnumHelper.addToolMaterial("BRONZE", 2, 170, 5.0F, 2, 15);
			EnumToolMaterial emerald = EnumHelper.addToolMaterial("REALEMERALD", 3, 1000, 8.0F, 4, 10);
			EnumToolMaterial demonic = EnumHelper.addToolMaterial("DEMONIC", 3, 100, 8.0F, 6, 22);
			EnumToolMaterial frost = EnumHelper.addToolMaterial("FROST", 2, 59, 6.0F, 3, 5);
			EnumToolMaterial mithril = EnumHelper.addToolMaterial("MITHRIL", 3, 3000, 8.0F, 3.0F, 10);
			
			tridentWood = new ItemSpear(26840,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenTrident");
			tridentStone = new ItemSpear(26841,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneTrident");
			tridentIron = new ItemSpear(26842,EnumToolMaterial.IRON).setUnlocalizedName("npcIronTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronTrident");
			tridentGold = new ItemSpear(26843,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldTrident");
			tridentDiamond = new ItemSpear(26844,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondTrident");
			tridentBronze = new ItemSpear(26845,bronze).setUnlocalizedName("npcBronzeTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeTrident");
			tridentEmerald = new ItemSpear(26846,emerald).setUnlocalizedName("npcEmeraldTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldTrident");
			tridentCurse = new ItemSpear(26847,emerald).setUnlocalizedName("npcCursedTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCursedTrident");
			Item tridentFrost = new ItemSpear(26848,frost).setUnlocalizedName("npcFrostTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostTrident");
			tridentFrost = new ItemSpear(27002,mithril).setUnlocalizedName("npcMithrilTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilTrident");
			tridentFrost = new ItemSpear(27003,demonic).setUnlocalizedName("npcDemonicTrident").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicTrident");
			
			new ItemNpcWeaponInterface(27004, bronze).setUnlocalizedName("npcBronzeSword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeSword");
			new ItemNpcWeaponInterface(27005, emerald).setUnlocalizedName("npcEmeraldSword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldSword");
			new ItemNpcWeaponInterface(27006, demonic).setUnlocalizedName("npcDemonicSword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicSword");
			new ItemNpcWeaponInterface(27007, frost).setUnlocalizedName("npcFrostSword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostSword");
			new ItemNpcWeaponInterface(27008, mithril).setUnlocalizedName("npcMithrilSword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilSword");
			
			spellNature = new ItemNpcInterface(26930).setUnlocalizedName("npcNatureSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcNatureSpell");
			spellArcane = new ItemNpcInterface(26931).setUnlocalizedName("npcArcaneSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcArcaneSpell");
			spellLightning = new ItemNpcInterface(26932).setUnlocalizedName("npcLightningSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcLightningSpell");
			spellIce = new ItemNpcInterface(26933).setUnlocalizedName("npcIceSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcIceSpell");
			spellFire = new ItemNpcInterface(26934).setUnlocalizedName("npcFireSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcFireSpell");
			spellDark = new ItemNpcInterface(26935).setUnlocalizedName("npcDarkSpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcDarkSpell");
			spellHoly = new ItemNpcInterface(26936).setUnlocalizedName("npcHolySpell").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcHolySpell");

			orb = new ItemOrb(26937).setUnlocalizedName("npcOrb").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcOrb");
			mana = new ItemNpcInterface(26938).setUnlocalizedName("npcMana").setCreativeTab(CustomItems.tabWeapon).setTextureName("customnpcs:npcMana");
			Item brokenOrb = new ItemOrb(26939).setUnlocalizedName("npcBrokenOrb").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcBrokenOrb");
			
			kunai = new ItemKunai(26910,EnumToolMaterial.IRON).setUnlocalizedName("npcKunai").setMaxStackSize(1).setFull3D().setTextureName("customnpcs:npcKunai");
			Item kunai = new ItemKunaiReversed(26911,EnumToolMaterial.IRON).setUnlocalizedName("npcReverseKunai").setMaxStackSize(1).setFull3D().setTextureName("customnpcs:npcReverseKunai");
			kunai = new ItemThrowingShuriken(26912).setRotating().setDropItem().setUnlocalizedName("npcShuriken").setFull3D().setTextureName("customnpcs:npcShuriken");
			shuriken = new ItemThrowingWeapon(26913).setDamage(4).setRotating().setDropItem().setUnlocalizedName("npcGiantShuriken").setFull3D().setTextureName("customnpcs:npcGiantShuriken");
			Item katana = new ItemNpcWeaponInterface(26914,EnumToolMaterial.IRON).setUnlocalizedName("npcKatana").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcKatana");
			Item knive = new ItemDagger(26915,EnumToolMaterial.IRON).setUnlocalizedName("npcKukri").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcKukri");
	
			Item golf = new ItemSpear(26800,EnumToolMaterial.IRON).setUnlocalizedName("npcGolfClub").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGolfClub");
			Item magicwand = new ItemWand(26801).setUnlocalizedName("npcMagicWand").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMagicWand");
			Item chickenSword = new ItemNpcInterface(26802).setUnlocalizedName("npcChickenSword").setFull3D().setMaxStackSize(1).setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcChickenSword");
			Item handcuffs = new ItemNpcInterface(26803).setUnlocalizedName("npcHandCuffs").setFull3D().setMaxStackSize(1).setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcHandCuffs");
			Item hammer = new ItemNpcWeaponInterface(26804,EnumToolMaterial.STONE).setUnlocalizedName("npcHammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcHammer");
			Item baseballbat = new ItemNpcWeaponInterface(26805,EnumToolMaterial.IRON).setUnlocalizedName("npcBaseballBat").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBaseballBat");
			Item leadpipe = new ItemNpcWeaponInterface(26806,EnumToolMaterial.IRON).setUnlocalizedName("npcLeadPipe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcLeadPipe");
			Item cleaver = new ItemNpcWeaponInterface(26807,EnumToolMaterial.IRON).setUnlocalizedName("npcCleaver").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCleaver");
			Item saber = new ItemNpcWeaponInterface(26808,EnumToolMaterial.IRON).setUnlocalizedName("npcSaber").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcSaber");
			Item hockey = new ItemSpear(26809,EnumToolMaterial.IRON).setUnlocalizedName("npcHockeyStick").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcHockeyStick");
			hammer = new ItemSpear(26810,EnumToolMaterial.IRON).setUnlocalizedName("npcSledgeHammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcSledgeHammer");
			Item bottle = new ItemNpcWeaponInterface(26811,EnumToolMaterial.IRON).setUnlocalizedName("npcBrokenBottle").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBrokenBottle");
			knive = new ItemDagger(26812,EnumToolMaterial.STONE).setUnlocalizedName("npcCombatKnive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCombatKnive");
			Item lighter = new ItemFlintAndSteel(26813).setUnlocalizedName("npcLighter").setMaxStackSize(1).setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcLighter");
			Item macuahuitl = new ItemNpcWeaponInterface(26714,EnumToolMaterial.IRON).setUnlocalizedName("npcMacuahuitl").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMacuahuitl");
			Item bowstaff = new ItemGlaive(26715,EnumToolMaterial.IRON).setUnlocalizedName("npcBoStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBoStaff");
			Item sai = new ItemDagger(26757,EnumToolMaterial.IRON).setUnlocalizedName("npcSaiBlade").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcSaiBlade");
			Item slingshot = new ItemSlingshot(26767).setUnlocalizedName("npcSlingshot").setFull3D().setTextureName("customnpcs:npcSlingshot");
			Item musket = new ItemMusket(26758).setUnlocalizedName("npcMusket").setFull3D().setTextureName("customnpcs:npcMusket");
			
			Item crossbow = new ItemCrossbow(26768).setUnlocalizedName("npcCrossbow").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCrossbow");
			crossbowBolt = new ItemBullet(26769,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcCrossBolt").setMaxStackSize(64).setTextureName("customnpcs:npcCrossBolt");
			
			Item batton = new ItemNpcWeaponInterface(26814,EnumToolMaterial.STONE).setUnlocalizedName("npcBatton").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBatton");
			Item chainsaw = new ItemGunChainsaw(26815,EnumToolMaterial.EMERALD).setUnlocalizedName("npcGunChainsaw").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGunChainsaw");
			Item rapier = new ItemNpcWeaponInterface(26816,EnumToolMaterial.IRON).setUnlocalizedName("npcRapier").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcRapier");
			Item crowbar = new ItemNpcWeaponInterface(26817,EnumToolMaterial.IRON).setUnlocalizedName("npcCrowbar").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCrowbar");
			Item pipewrench = new ItemNpcWeaponInterface(26818,EnumToolMaterial.IRON).setUnlocalizedName("npcPipeWrench").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcPipeWrench");
			Item swiss = new ItemDagger(26819,EnumToolMaterial.IRON).setUnlocalizedName("npcSwissArmyKnife").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcSwissArmyKnife");
			Item wrench = new ItemNpcWeaponInterface(26836,EnumToolMaterial.IRON).setUnlocalizedName("npcWrench").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWrench");
			excalibur = new ItemExcalibur(26799,EnumToolMaterial.EMERALD).setUnlocalizedName("npcExcalibur").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcExcalibur");
			
			Item shield = new ItemShield(26729,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcWoodenShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenShield");
			shield = new ItemShield(26730,EnumNpcToolMaterial.STONE).setUnlocalizedName("npcStoneShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneShield");
			shield = new ItemShield(26736,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcIronShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronShield");
			shield = new ItemShield(26737,EnumNpcToolMaterial.GOLD).setUnlocalizedName("npcGoldenShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenShield");
			shield = new ItemShield(26738,EnumNpcToolMaterial.DIA).setUnlocalizedName("npcDiaShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiaShield");
			shield = new ItemShield(26739,EnumNpcToolMaterial.BRONZE).setUnlocalizedName("npcBronzeShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeShield");
			shield = new ItemRotatedShield(27001,EnumNpcToolMaterial.FROST).setUnlocalizedName("npcFrostShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostShield");
			shield = new ItemRotatedShield(27009,EnumNpcToolMaterial.MITHRIL).setUnlocalizedName("npcMithrilShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilShield");
			shield = new ItemRotatedShield(27010,EnumNpcToolMaterial.DEMONIC).setUnlocalizedName("npcDemonicShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicShield");
			
			shield = new ItemRotatedShield(26740,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcFullWoodenShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullWoodenShield");
			shield = new ItemRotatedShield(26741,EnumNpcToolMaterial.STONE).setUnlocalizedName("npcFullStoneShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullStoneShield");
			shield = new ItemRotatedShield(26742,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcFullIronShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullIronShield");
			shield = new ItemRotatedShield(26743,EnumNpcToolMaterial.GOLD).setUnlocalizedName("npcFullGoldenShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullGoldenShield");
			shield = new ItemRotatedShield(26744,EnumNpcToolMaterial.DIA).setUnlocalizedName("npcFullDiaShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullDiaShield");
			shield = new ItemRotatedShield(26745,EnumNpcToolMaterial.BRONZE).setUnlocalizedName("npcFullBronzeShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullBronzeShield");
			shield = new ItemRotatedShield(26727,EnumNpcToolMaterial.EMERALD).setUnlocalizedName("npcFullEmeraldShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFullEmeraldShield");
			shield = new ItemShield(26728,EnumNpcToolMaterial.EMERALD).setUnlocalizedName("npcEmeraldShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldShield");
			
			shield = new ItemShield(26746,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcHeaterShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcHeaterShield");
			shield = new ItemShield(26747,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcCryscentShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcCryscentShield");
			shield = new ItemShield(26748,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcScutumShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcScutumShield");
			shield = new ItemRotatedShield(26749,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcTowerShield").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcTowerShield");
	
			Item spear = new ItemSpear(26750,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenSpear");
			spear = new ItemSpear(26751,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneSpear");
			spear = new ItemSpear(26752,EnumToolMaterial.IRON).setUnlocalizedName("npcIronSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronSpear");
			spear = new ItemSpear(26753,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenSpear");
			spear = new ItemSpear(26754,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondSpear");
			spear = new ItemSpear(26755,bronze).setUnlocalizedName("npcBronzeSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeSpear");
			spear = new ItemSpear(26756,emerald).setUnlocalizedName("npcEmeraldSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldSpear");
			spear = new ItemSpear(27000,frost).setUnlocalizedName("npcFrostSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostSpear");
			spear = new ItemSpear(27011,mithril).setUnlocalizedName("npcMithrilSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilSpear");
			spear = new ItemSpear(27012,demonic).setUnlocalizedName("npcDemonicSpear").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicSpear");
			
			new ItemSpear(26992,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenHalberd");
			new ItemSpear(26993,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneHalberd");
			new ItemSpear(26994,EnumToolMaterial.IRON).setUnlocalizedName("npcIronHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronHalberd");
			new ItemSpear(26995,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenHalberd");
			new ItemSpear(26996,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondHalberd");
			new ItemSpear(26997,bronze).setUnlocalizedName("npcBronzeHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeHalberd");
			new ItemSpear(26998,emerald).setUnlocalizedName("npcEmeraldHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldHalberd");
			new ItemSpear(26999,frost).setUnlocalizedName("npcFrostHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostHalberd");
			new ItemSpear(27013,mithril).setUnlocalizedName("npcMithrilHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilHalberd");
			new ItemSpear(27014,demonic).setUnlocalizedName("npcDemonicHalberd").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicHalberd");
			
			Item axe = new ItemBattleAxe(26760,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenBattleAxe");
			axe = new ItemBattleAxe(26761,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneBattleAxe");
			axe = new ItemBattleAxe(26762,EnumToolMaterial.IRON).setUnlocalizedName("npcIronBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronBattleAxe");
			axe = new ItemBattleAxe(26763,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenBattleAxe");
			axe = new ItemBattleAxe(26764,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondBattleAxe");
			axe = new ItemBattleAxe(26765,bronze).setUnlocalizedName("npcBronzeBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeBattleAxe");
			axe = new ItemBattleAxe(26766,emerald).setUnlocalizedName("npcEmeraldBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldBattleAxe");
			axe = new ItemBattleAxe(26837,frost).setUnlocalizedName("npcFrostBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostBattleAxe");
			axe = new ItemBattleAxe(27015,mithril).setUnlocalizedName("npcMithrilBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilBattleAxe");
			axe = new ItemBattleAxe(27016,demonic).setUnlocalizedName("npcDemonicBattleAxe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicBattleAxe");
			
			Item warhammer = new ItemWarhammer(26942,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenWarhammer");
			warhammer = new ItemWarhammer(26943,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneWarhammer");
			warhammer = new ItemWarhammer(26944,EnumToolMaterial.IRON).setUnlocalizedName("npcIronWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronWarhammer");
			warhammer = new ItemWarhammer(26945,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenWarhammer");
			warhammer = new ItemWarhammer(26946,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondWarhammer");
			warhammer = new ItemWarhammer(26947,bronze).setUnlocalizedName("npcBronzeWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeWarhammer");
			warhammer = new ItemWarhammer(26948,emerald).setUnlocalizedName("npcEmeraldWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldWarhammer");
			warhammer = new ItemWarhammer(26949,frost).setUnlocalizedName("npcFrostWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostWarhammer");
			warhammer = new ItemWarhammer(27017,mithril).setUnlocalizedName("npcMithrilWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilWarhammer");
			warhammer = new ItemWarhammer(27018,demonic).setUnlocalizedName("npcDemonicWarhammer").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicWarhammer");
			
			Item mace = new ItemNpcWeaponInterface(26770,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenMace");
			mace = new ItemNpcWeaponInterface(26771,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneMace");
			mace = new ItemNpcWeaponInterface(26772,EnumToolMaterial.IRON).setUnlocalizedName("npcIronMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronMace");
			mace = new ItemNpcWeaponInterface(26773,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenMace");
			mace = new ItemNpcWeaponInterface(26774,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondMace");
			mace = new ItemNpcWeaponInterface(26775,bronze).setUnlocalizedName("npcBronzeMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeMace");
			mace = new ItemNpcWeaponInterface(26776,emerald).setUnlocalizedName("npcEmeraldMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldMace");
			mace = new ItemNpcWeaponInterface(26849,frost).setUnlocalizedName("npcFrostMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostMace");
			mace = new ItemNpcWeaponInterface(27019,mithril).setUnlocalizedName("npcMithrilMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilMace");
			mace = new ItemNpcWeaponInterface(27020,demonic).setUnlocalizedName("npcDemonicMace").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicMace");
			
			Item dagger = new ItemDagger(26700,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenDagger");
			dagger = new ItemDaggerReversed(26701, (ItemDagger) dagger, EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenDagger");
			dagger = new ItemDagger(26702,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneDagger");
			dagger = new ItemDaggerReversed(26703, (ItemDagger) dagger,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneDagger");
			dagger = new ItemDagger(26704,EnumToolMaterial.IRON).setUnlocalizedName("npcIronDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronDagger");
			dagger = new ItemDaggerReversed(26705, (ItemDagger) dagger,EnumToolMaterial.IRON).setUnlocalizedName("npcIronReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronDagger");
			dagger = new ItemDagger(26706,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenDagger");
			dagger = new ItemDaggerReversed(26707, (ItemDagger) dagger,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenDagger");
			dagger = new ItemDagger(26708,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondDagger");
			dagger = new ItemDaggerReversed(26709, (ItemDagger) dagger,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondDagger");
			dagger = new ItemDagger(26710,bronze).setUnlocalizedName("npcBronzeDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeDagger");
			dagger = new ItemDaggerReversed(26711, (ItemDagger) dagger,bronze).setUnlocalizedName("npcBronzeReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeDagger");
			dagger = new ItemDagger(26712,emerald).setUnlocalizedName("npcEmeraldDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldDagger");
			dagger = new ItemDaggerReversed(26713, (ItemDagger) dagger,emerald).setUnlocalizedName("npcEmeraldReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldDagger");
			dagger = new ItemDagger(26838,frost).setUnlocalizedName("npcFrostDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostDagger");
			dagger = new ItemDaggerReversed(26839, (ItemDagger) dagger,frost).setUnlocalizedName("npcFrostReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostDagger");
			dagger = new ItemDagger(27021,mithril).setUnlocalizedName("npcMithrilDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilDagger");
			dagger = new ItemDaggerReversed(27022, (ItemDagger) dagger,frost).setUnlocalizedName("npcMithrilReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilDagger");
			dagger = new ItemDagger(27023,demonic).setUnlocalizedName("npcDemonicDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicDagger");
			dagger = new ItemDaggerReversed(27024, (ItemDagger) dagger,demonic).setUnlocalizedName("npcDemonicReversedDagger").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicDagger");
			
			Item claw = new ItemClaw(26850,EnumToolMaterial.IRON).setUnlocalizedName("npcNinjaClaw").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcNinjaClaw");
			claw = new ItemClaw(26851,EnumToolMaterial.IRON).setUnlocalizedName("npcSteelClaw").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcSteelClaw");
			claw = new ItemClaw(26852,EnumToolMaterial.IRON).setUnlocalizedName("npcBearClaw").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBearClaw");
			claw = new ItemClaw(26853,EnumToolMaterial.IRON).setUnlocalizedName("npcKatar").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcKatar");
			
			staffWood = new ItemStaff(26777,EnumNpcToolMaterial.WOOD).setUnlocalizedName("npcWoodenStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenStaff");
			staffStone = new ItemStaff(26778,EnumNpcToolMaterial.STONE).setUnlocalizedName("npcStoneStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneStaff");
			staffIron = new ItemStaff(26779,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcIronStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronStaff");
			staffGold = new ItemStaff(26780,EnumNpcToolMaterial.GOLD).setUnlocalizedName("npcGoldenStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenStaff");
			staffDiamond = new ItemStaff(26781,EnumNpcToolMaterial.DIA).setUnlocalizedName("npcDiamondStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondStaff");
			staffBronze = new ItemStaff(26782,EnumNpcToolMaterial.BRONZE).setUnlocalizedName("npcBronzeStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeStaff");
			staffEmerald = new ItemStaff(26783,EnumNpcToolMaterial.EMERALD).setUnlocalizedName("npcEmeraldStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldStaff");
			staffElemental = new ItemElementalStaff(26784,EnumNpcToolMaterial.IRON).setUnlocalizedName("npcElementalStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcElementalStaff");
			new ItemStaff(26854,EnumNpcToolMaterial.FROST).setUnlocalizedName("npcFrostStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostStaff");
			new ItemStaff(27025,EnumNpcToolMaterial.MITHRIL).setUnlocalizedName("npcMithrilStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilStaff");
			new ItemStaff(27026,EnumNpcToolMaterial.DEMONIC).setUnlocalizedName("npcDemonicStaff").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicStaff");
			
			Item scythe = new ItemScythe(26785,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenScythe");
			scythe = new ItemScythe(26786,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneScythe");
			scythe = new ItemScythe(26787,EnumToolMaterial.IRON).setUnlocalizedName("npcIronScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronScythe");
			scythe = new ItemScythe(26788,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenScythe");
			scythe = new ItemScythe(26789,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondScythe");
			scythe = new ItemScythe(26798,emerald).setUnlocalizedName("npcEmeraldScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldScythe");
			scythe = new ItemScythe(26797,bronze).setUnlocalizedName("npcBronzeScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeScythe");
			scythe = new ItemScythe(26855,frost).setUnlocalizedName("npcFrostScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostScythe");
			scythe = new ItemScythe(27027,mithril).setUnlocalizedName("npcMithrilScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilScythe");
			scythe = new ItemScythe(27028,demonic).setUnlocalizedName("npcDemonicScythe").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicScythe");
			
			Item glaive = new ItemGlaive(26790,EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenGlaive");
			glaive = new ItemGlaive(26791,EnumToolMaterial.STONE).setUnlocalizedName("npcStoneGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneGlaive");
			glaive = new ItemGlaive(26792,EnumToolMaterial.IRON).setUnlocalizedName("npcIronGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronGlaive");
			glaive = new ItemGlaive(26793,EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenGlaive");
			glaive = new ItemGlaive(26794,EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondGlaive");
			glaive = new ItemGlaive(26795,bronze).setUnlocalizedName("npcBronzeGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeGlaive");
			glaive = new ItemGlaive(26796,emerald).setUnlocalizedName("npcEmeraldGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldGlaive");
			glaive = new ItemGlaive(26856,frost).setUnlocalizedName("npcFrostGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostGlaive");
			glaive = new ItemGlaive(27029,mithril).setUnlocalizedName("npcMithrilGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilGlaive");
			glaive = new ItemGlaive(27030,demonic).setUnlocalizedName("npcDemonicGlaive").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicGlaive");
			
			new ItemBroadSword(26974, EnumToolMaterial.WOOD).setUnlocalizedName("npcWoodenBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcWoodenBroadsword");
			new ItemBroadSword(26975, EnumToolMaterial.STONE).setUnlocalizedName("npcStoneBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcStoneBroadsword");
			new ItemBroadSword(26976, EnumToolMaterial.IRON).setUnlocalizedName("npcIronBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcIronBroadsword");
			new ItemBroadSword(26977, EnumToolMaterial.GOLD).setUnlocalizedName("npcGoldenBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcGoldenBroadsword");
			new ItemBroadSword(26978, EnumToolMaterial.EMERALD).setUnlocalizedName("npcDiamondBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDiamondBroadsword");
			new ItemBroadSword(26979, bronze).setUnlocalizedName("npcBronzeBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcBronzeBroadsword");
			new ItemBroadSword(26980, emerald).setUnlocalizedName("npcEmeraldBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcEmeraldBroadsword");
			new ItemBroadSword(26981, frost).setUnlocalizedName("npcFrostBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcFrostBroadsword");
			new ItemBroadSword(27031, demonic).setUnlocalizedName("npcDemonicBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcDemonicBroadsword");
			new ItemBroadSword(27032, mithril).setUnlocalizedName("npcMithrilBroadsword").setFull3D().setMaxStackSize(1).setTextureName("customnpcs:npcMithrilBroadsword");

			EnumArmorMaterial armorMithril = EnumHelper.addArmorMaterial("MITHRIL", 40, new int[]{3, 8, 6, 3}, 20);
			EnumArmorMaterial armorBronze = EnumHelper.addArmorMaterial("BRONZE", 7, new int[]{2, 6, 5, 2}, 20);
			EnumArmorMaterial armorEmerald = EnumHelper.addArmorMaterial("EMERALD", 35, new int[]{5, 7, 4, 5}, 5);
			
			Item cowarmor = new ItemNpcArmor(26860, EnumArmorMaterial.CHAIN,0,"cowleather").setUnlocalizedName("npcCowleatherHead").setMaxStackSize(1).setTextureName("customnpcs:npcCowleatherHead");
			Item armor = new ItemNpcArmor(26861,EnumArmorMaterial.CHAIN,1,"cowleather").setUnlocalizedName("npcCowleatherChest").setMaxStackSize(1).setTextureName("customnpcs:npcCowleatherChest");
			armor = new ItemNpcArmor(26862,EnumArmorMaterial.CHAIN,2,"cowleather").setUnlocalizedName("npcCowleatherLegs").setMaxStackSize(1).setTextureName("customnpcs:npcCowleatherLegs");
			armor = new ItemNpcArmor(26863,EnumArmorMaterial.CHAIN,3,"cowleather").setUnlocalizedName("npcCowleatherBoots").setMaxStackSize(1).setTextureName("customnpcs:npcCowleatherBoots");
	
			armor = new ItemNpcArmor(26864,EnumArmorMaterial.IRON,0,"nanorum").setUnlocalizedName("npcNanorumHead").setMaxStackSize(1).setTextureName("customnpcs:npcNanorumHead");
			armor = new ItemNpcArmor(26865,EnumArmorMaterial.IRON,1,"nanorum").setUnlocalizedName("npcNanorumChest").setMaxStackSize(1).setTextureName("customnpcs:npcNanorumChest");
			armor = new ItemNpcArmor(26866,EnumArmorMaterial.IRON,2,"nanorum").setUnlocalizedName("npcNanorumLegs").setMaxStackSize(1).setTextureName("customnpcs:npcNanorumLegs");
			armor = new ItemNpcArmor(26867,EnumArmorMaterial.IRON,3,"nanorum").setUnlocalizedName("npcNanorumBoots").setMaxStackSize(1).setTextureName("customnpcs:npcNanorumBoots");
			
			armor = new ItemNpcArmor(26868,EnumArmorMaterial.IRON,0,"tactical").setUnlocalizedName("npcTacticalHead").setMaxStackSize(1).setTextureName("customnpcs:npcTacticalHead");
			armor = new ItemNpcArmor(26869,EnumArmorMaterial.IRON,1,"tactical").setUnlocalizedName("npcTacticalChest").setMaxStackSize(1).setTextureName("customnpcs:npcTacticalChest");
			
			armor = new ItemNpcArmor(26919,EnumArmorMaterial.IRON,0,"fullcloth").setUnlocalizedName("npcFullLeatherHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullLeatherHead");
			armor = new ItemNpcArmor(26920,EnumArmorMaterial.IRON,1,"fullcloth").setUnlocalizedName("npcFullLeatherChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullLeatherChest");
			
			armor = new ItemNpcArmor(26870,EnumArmorMaterial.IRON,0,"fulliron").setUnlocalizedName("npcFullIronHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullIronHead");
			armor = new ItemNpcArmor(26871,EnumArmorMaterial.IRON,1,"fulliron").setUnlocalizedName("npcFullIronChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullIronChest");
			
			armor = new ItemNpcArmor(26872,EnumArmorMaterial.GOLD,0,"fullgold").setUnlocalizedName("npcFullGoldHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullGoldHead");
			armor = new ItemNpcArmor(26873,EnumArmorMaterial.GOLD,1,"fullgold").setUnlocalizedName("npcFullGoldChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullGoldChest");
			
			armor = new ItemNpcArmor(26874,EnumArmorMaterial.DIAMOND,0,"fulldiamond").setUnlocalizedName("npcFullDiamondHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullDiamondHead");
			armor = new ItemNpcArmor(26875,EnumArmorMaterial.DIAMOND,1,"fulldiamond").setUnlocalizedName("npcFullDiamondChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullDiamondChest");
	
			armor = new ItemNpcArmor(26876, armorBronze,0,"fullbronze").setUnlocalizedName("npcFullBronzeHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullBronzeHead");
			armor = new ItemNpcArmor(26877, armorBronze,1,"fullbronze").setUnlocalizedName("npcFullBronzeChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullBronzeChest");
			armor = new ItemNpcArmor(26878, armorBronze,2,"fullbronze").setUnlocalizedName("npcFullBronzeLeggings").setMaxStackSize(1).setTextureName("customnpcs:npcFullBronzeLeggings");
			armor = new ItemNpcArmor(26879, armorBronze,3,"fullbronze").setUnlocalizedName("npcFullBronzeBoots").setMaxStackSize(1).setTextureName("customnpcs:npcFullBronzeBoots");
	
			armor = new ItemNpcArmor(26921, armorEmerald,0,"fullemerald").setUnlocalizedName("npcFullEmeraldHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullEmeraldHead");
			armor = new ItemNpcArmor(26922, armorEmerald,1,"fullemerald").setUnlocalizedName("npcFullEmeraldChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullEmeraldChest");
			armor = new ItemNpcArmor(26923, armorEmerald,2,"fullemerald").setUnlocalizedName("npcFullEmeraldLeggings").setMaxStackSize(1).setTextureName("customnpcs:npcFullEmeraldLeggings");
			armor = new ItemNpcArmor(26924, armorEmerald,3,"fullemerald").setUnlocalizedName("npcFullEmeraldBoots").setMaxStackSize(1).setTextureName("customnpcs:npcFullEmeraldBoots");
	
			armor = new ItemNpcArmor(26880,EnumArmorMaterial.CLOTH,0,"fullwood").setUnlocalizedName("npcFullWoodenHead").setMaxStackSize(1).setTextureName("customnpcs:npcFullWoodenHead");
			armor = new ItemNpcArmor(26881,EnumArmorMaterial.CLOTH,1,"fullwood").setUnlocalizedName("npcFullWoodenChest").setMaxStackSize(1).setTextureName("customnpcs:npcFullWoodenChest");
			armor = new ItemNpcArmor(26882,EnumArmorMaterial.CLOTH,2,"fullwood").setUnlocalizedName("npcFullWoodenLeggings").setMaxStackSize(1).setTextureName("customnpcs:npcFullWoodenLeggings");
			armor = new ItemNpcArmor(26883,EnumArmorMaterial.CLOTH,3,"fullwood").setUnlocalizedName("npcFullWoodenBoots").setMaxStackSize(1).setTextureName("customnpcs:npcFullWoodenBoots");
	
			armor = new ItemNpcArmor(26884,EnumArmorMaterial.CHAIN,1,"tuxido").setUnlocalizedName("npcTuxedoChest").setMaxStackSize(1).setTextureName("customnpcs:npcTuxedoChest");
			armor = new ItemNpcArmor(26885,EnumArmorMaterial.CHAIN,2,"tuxido").setUnlocalizedName("npcTuxedoPants").setMaxStackSize(1).setTextureName("customnpcs:npcTuxedoPants");
			armor = new ItemNpcArmor(26886,EnumArmorMaterial.CHAIN,3,"tuxido").setUnlocalizedName("npcTuxedoBottom").setMaxStackSize(1).setTextureName("customnpcs:npcTuxedoBottom");
	
			armor = new ItemNpcArmor(26887,EnumArmorMaterial.CHAIN,0,"wizard").setUnlocalizedName("npcWizardHead").setMaxStackSize(1).setTextureName("customnpcs:npcWizardHead");
			armor = new ItemNpcArmor(26888,EnumArmorMaterial.CHAIN,1,"wizard").setUnlocalizedName("npcWizardChest").setMaxStackSize(1).setTextureName("customnpcs:npcWizardChest");
			armor = new ItemNpcArmor(26889,EnumArmorMaterial.CHAIN,2,"wizard").setUnlocalizedName("npcWizardPants").setMaxStackSize(1).setTextureName("customnpcs:npcWizardPants");
	
			armor = new ItemNpcArmor(26890,EnumArmorMaterial.IRON,0,"assassin").setUnlocalizedName("npcAssassinHead").setMaxStackSize(1).setTextureName("customnpcs:npcAssassinHead");
			armor = new ItemNpcArmor(26891,EnumArmorMaterial.IRON,1,"assassin").setUnlocalizedName("npcAssassinChest").setMaxStackSize(1).setTextureName("customnpcs:npcAssassinChest");
			armor = new ItemNpcArmor(26892,EnumArmorMaterial.IRON,2,"assassin").setUnlocalizedName("npcAssassinLeggings").setMaxStackSize(1).setTextureName("customnpcs:npcAssassinLeggings");
			armor = new ItemNpcArmor(26893,EnumArmorMaterial.IRON,3,"assassin").setUnlocalizedName("npcAssassinBoots").setMaxStackSize(1).setTextureName("customnpcs:npcAssassinBoots");
	
			armor = new ItemNpcArmor(26894,EnumArmorMaterial.IRON,0,"soldier").setUnlocalizedName("npcSoldierHead").setMaxStackSize(1).setTextureName("customnpcs:npcSoldierHead");
			armor = new ItemNpcArmor(26895,EnumArmorMaterial.IRON,1,"soldier").setUnlocalizedName("npcSoldierChest").setMaxStackSize(1).setTextureName("customnpcs:npcSoldierChest");
			armor = new ItemNpcArmor(26896,EnumArmorMaterial.IRON,2,"soldier").setUnlocalizedName("npcSoldierLegs").setMaxStackSize(1).setTextureName("customnpcs:npcSoldierLegs");
			armor = new ItemNpcArmor(26897,EnumArmorMaterial.IRON,3,"soldier").setUnlocalizedName("npcSoldierBottom").setMaxStackSize(1).setTextureName("customnpcs:npcSoldierBottom");

			armor = new ItemNpcArmor(26898,EnumArmorMaterial.DIAMOND,0,"x407").setUnlocalizedName("npcX407Head").setMaxStackSize(1).setTextureName("customnpcs:npcX407Head");
			armor = new ItemNpcArmor(26899,EnumArmorMaterial.DIAMOND,1,"x407").setUnlocalizedName("npcX407Chest").setMaxStackSize(1).setTextureName("customnpcs:npcX407Chest");
			armor = new ItemNpcArmor(26900,EnumArmorMaterial.DIAMOND,2,"x407").setUnlocalizedName("npcX407Legs").setMaxStackSize(1).setTextureName("customnpcs:npcX407Legs");
			armor = new ItemNpcArmor(26901,EnumArmorMaterial.DIAMOND,3,"x407").setUnlocalizedName("npcX407Boots").setMaxStackSize(1).setTextureName("customnpcs:npcX407Boots");

//			armor = new ItemNpcArmor(26898,armorMithril,0,"mithril").setUnlocalizedName("npcMithrilHead").setMaxStackSize(1).setTextureName("customnpcs:npcMithrilHead");
//			armor = new ItemNpcArmor(26899,armorMithril,1,"mithril").setUnlocalizedName("npcMithrilChest").setMaxStackSize(1).setTextureName("customnpcs:npcMithrilChest");
//			armor = new ItemNpcArmor(26900,armorMithril,2,"mithril").setUnlocalizedName("npcMithrilLegs").setMaxStackSize(1).setTextureName("customnpcs:npcMithrilLegs");
//			armor = new ItemNpcArmor(26901,armorMithril,3,"mithril").setUnlocalizedName("npcMithrilBoots").setMaxStackSize(1).setTextureName("customnpcs:npcMithrilBoots");

//			armor = new ItemNpcArmor(26898,EnumArmorMaterial.DIAMOND,0,"demonic").setUnlocalizedName("npcDemonicHead").setMaxStackSize(1).setTextureName("customnpcs:npcDemonicHead");
//			armor = new ItemNpcArmor(26899,EnumArmorMaterial.DIAMOND,1,"demonic").setUnlocalizedName("npcDemonicChest").setMaxStackSize(1).setTextureName("customnpcs:npcDemonicChest");
//			armor = new ItemNpcArmor(26900,EnumArmorMaterial.DIAMOND,2,"demonic").setUnlocalizedName("npcDemonicLegs").setMaxStackSize(1).setTextureName("customnpcs:npcDemonicLegs");
//			armor = new ItemNpcArmor(26901,EnumArmorMaterial.DIAMOND,3,"demonic").setUnlocalizedName("npcDemonicBoots").setMaxStackSize(1).setTextureName("customnpcs:npcDemonicBoots");
//	
			armor = new ItemNpcArmor(26902,EnumArmorMaterial.GOLD,0,"commissar").setUnlocalizedName("npcCommissarHead").setMaxStackSize(1).setTextureName("customnpcs:npcCommissarHead");
			armor = new ItemNpcArmor(26903,EnumArmorMaterial.GOLD,1,"commissar").setUnlocalizedName("npcCommissarChest").setMaxStackSize(1).setTextureName("customnpcs:npcCommissarChest");
			armor = new ItemNpcArmor(26904,EnumArmorMaterial.GOLD,2,"commissar").setUnlocalizedName("npcCommissarLegs").setMaxStackSize(1).setTextureName("customnpcs:npcCommissarLegs");
			armor = new ItemNpcArmor(26905,EnumArmorMaterial.GOLD,3,"commissar").setUnlocalizedName("npcCommissarBottom").setMaxStackSize(1).setTextureName("customnpcs:npcCommissarBottom");
	
			armor = new ItemNpcArmor(26906,EnumArmorMaterial.IRON,0,"infantry").setUnlocalizedName("npcInfantryHelmet").setMaxStackSize(1).setTextureName("customnpcs:npcInfantryHelmet");
			armor = new ItemNpcArmor(26907,EnumArmorMaterial.DIAMOND,1,"officer").setUnlocalizedName("npcOfficerChest").setMaxStackSize(1).setTextureName("customnpcs:npcOfficerChest");
	
			armor = new ItemNpcArmor(26908,EnumArmorMaterial.DIAMOND,0,"crown1").setUnlocalizedName("npcCrown2").setMaxStackSize(1).setTextureName("customnpcs:npcCrown2");
			armor = new ItemNpcArmor(26909,EnumArmorMaterial.DIAMOND,0,"crown2").setUnlocalizedName("npcCrown").setMaxStackSize(1).setTextureName("customnpcs:npcCrown");
			armor = new ItemNpcArmorColorable(26726,EnumArmorMaterial.CLOTH,0,"papercrown").setUnlocalizedName("npcPaperCrown").setMaxStackSize(1).setTextureName("customnpcs:npcPaperCrown");
	
			armor = new ItemNpcArmor(26916,EnumArmorMaterial.IRON,0,"ninja").setUnlocalizedName("npcNinjaHead").setMaxStackSize(1).setTextureName("customnpcs:npcNinjaHead");
			armor = new ItemNpcArmor(26917,EnumArmorMaterial.IRON,1,"ninja").setUnlocalizedName("npcNinjaChest").setMaxStackSize(1).setTextureName("customnpcs:npcNinjaChest");
			armor = new ItemNpcArmor(26918,EnumArmorMaterial.IRON,2,"ninja").setUnlocalizedName("npcNinjaPants").setMaxStackSize(1).setTextureName("customnpcs:npcNinjaPants");
	
			armor = new ItemSkirt(26925,EnumArmorMaterial.CHAIN,"textures/models/armor/chainmail_layer_2.png").setUnlocalizedName("npcChainSkirt").setTextureName("customnpcs:npcChainSkirt");
			armor = new ItemSkirt(26926,EnumArmorMaterial.CLOTH,"textures/models/armor/leather_layer_2.png").setUnlocalizedName("npcLeatherSkirt").setTextureName("customnpcs:npcLeatherSkirt");
			armor = new ItemSkirt(26927,EnumArmorMaterial.GOLD,"textures/models/armor/gold_layer_2.png").setUnlocalizedName("npcGoldSkirt").setTextureName("customnpcs:npcGoldSkirt");
			armor = new ItemSkirt(26928,EnumArmorMaterial.IRON,"textures/models/armor/iron_layer_2.png").setUnlocalizedName("npcIronSkirt").setTextureName("customnpcs:npcIronSkirt");
			armor = new ItemSkirt(26929,EnumArmorMaterial.DIAMOND,"textures/models/armor/diamond_layer_2.png").setUnlocalizedName("npcDiamondSkirt").setTextureName("customnpcs:npcDiamondSkirt");
			armor = new ItemSkirt(26940,armorEmerald,"customnpcs:textures/armor/fullemerald_2.png").setUnlocalizedName("npcEmeraldSkirt").setTextureName("customnpcs:npcEmeraldSkirt");
			armor = new ItemSkirt(26941,armorBronze,"customnpcs:textures/armor/fullbronze_2.png").setUnlocalizedName("npcBronzeSkirt").setTextureName("customnpcs:npcBronzeSkirt");
			armor = new ItemSkirt(27033, EnumArmorMaterial.DIAMOND,"customnpcs:textures/armor/demonic_2.png").setUnlocalizedName("npcDemonicSkirt").setTextureName("customnpcs:npcDemonicSkirt");
			armor = new ItemSkirt(27034, armorMithril,"customnpcs:textures/armor/mithril_2.png").setUnlocalizedName("npcMithrilSkirt").setTextureName("customnpcs:npcMithrilSkirt");
			
			letter = new ItemNpcInterface(26950).setUnlocalizedName("npcLetter").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcLetter");
			Item quest = new ItemNpcInterface(26951).setUnlocalizedName("npcPlans").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcPlans");
			quest = new ItemNpcInterface(26952).setUnlocalizedName("npcSatchel").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcSatchel");
			quest = new ItemNpcInterface(26953).setUnlocalizedName("npcBag").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcBag");
			
			quest = new ItemNpcInterface(26954).setUnlocalizedName("npcCrystal").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCrystal");
			quest = new ItemNpcInterface(26955).setUnlocalizedName("npcSeveredEar").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcSeveredEar");
			quest = new ItemNpcInterface(26956).setUnlocalizedName("npcPhone").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcPhone");

			quest = new ItemNpcArmor(26957,EnumArmorMaterial.IRON,0,"mask").setMaxStackSize(64).setUnlocalizedName("npcBanditMask").setCreativeTab(CustomItems.tabArmor).setTextureName("customnpcs:npcBanditMask");
			quest = new ItemNpcInterface(26958).setUnlocalizedName("npcBrokenArrow").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcBrokenArrow");
			quest = new ItemNpcInterface(26959).setUnlocalizedName("npcEarthElement").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcEarthElement");
			quest = new ItemNpcInterface(26960).setUnlocalizedName("npcWaterElement").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcWaterElement");
			quest = new ItemNpcInterface(26961).setUnlocalizedName("npcFireElement").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcFireElement");
			quest = new ItemNpcInterface(26962).setUnlocalizedName("npcAirElement").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcAirElement");
			
			quest = new ItemNpcInterface(26963).setUnlocalizedName("npcCarKey").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcCarKey");
			quest = new ItemNpcInterface(26964).setUnlocalizedName("npcKey").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcKey");
			quest = new ItemNpcInterface(26965).setUnlocalizedName("npcKey2").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcKey2");
			quest = new ItemNpcInterface(26966).setUnlocalizedName("npcPendant").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcPendant");
			quest = new ItemNpcInterface(26967).setUnlocalizedName("npcBlueprint").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcBlueprint");
			quest = new ItemNpcInterface(26968).setUnlocalizedName("npcRing").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcRing");
			quest = new ItemNpcInterface(26969).setUnlocalizedName("npcSkull").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcSkull");

			Item sapphire = new ItemNpcInterface(26970).setUnlocalizedName("npcSaphire").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcSaphire");
			Item ruby = new ItemNpcInterface(26971).setUnlocalizedName("npcRuby").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcRuby");
			Item amethyst = new ItemNpcInterface(26972).setUnlocalizedName("npcAmethyst").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcAmethyst");

			OreDictionary.registerOre("gemSaphire", sapphire);
			OreDictionary.registerOre("gemRuby", ruby);
			OreDictionary.registerOre("gemAmethyst", amethyst);
			
			Item bronze_ingot = new ItemNpcInterface(26973).setUnlocalizedName("npcBronzeIngot").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcBronzeIngot");
			OreDictionary.registerOre("ingotBronze", bronze_ingot);
			
			quest = new ItemNpcInterface(26989).setUnlocalizedName("npcAncientScroll").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcAncientScroll");
			quest = new ItemNpcInterface(26990).setUnlocalizedName("npcArtifact").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcArtifact");
			quest = new ItemNpcInterface(26991).setUnlocalizedName("npcLocket").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcLocket");
			quest = new ItemNpcInterface(26982).setUnlocalizedName("npcSilk").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcSilk");
			quest = new ItemNpcInterface(26983).setUnlocalizedName("npcStatuette").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcStatuette");
			quest = new ItemNpcInterface(26984).setUnlocalizedName("npcTablet").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcTablet");

			quest = new ItemNpcInterface(26985).setUnlocalizedName("npcHeart").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcHeart");
			quest = new ItemNpcInterface(26986).setUnlocalizedName("npcMoney").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcMoney");
			quest = new ItemNpcInterface(26987).setUnlocalizedName("npcNecklace").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcNecklace");
			quest = new ItemNpcInterface(26988).setUnlocalizedName("npcUsbStick").setCreativeTab(CustomItems.tabMisc).setTextureName("customnpcs:npcUsbStick");
			
			
			tabArmor.icon = cowarmor.itemID;
			tabWeapon.icon = CustomItems.shuriken.itemID;
			tabMisc.icon = CustomItems.coinGold.itemID;
			tabBlocks.icon = couchWool.blockID;
			//moneyBag = new ItemMoneyBag(26974).setItemName("npcMoneyBag");
			
			available();
		}
	}
	private static void available(){
		for(int i = 26700; i < 27200;i++){
			if(Item.itemsList[i+256] == null)
				System.out.println(i + "");
		}
	}
}

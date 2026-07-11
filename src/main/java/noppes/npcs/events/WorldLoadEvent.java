package noppes.npcs.events;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import noppes.npcs.controllers.BankController;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.GlobalDataController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.RecipeController;
import noppes.npcs.controllers.TransportController;

public class WorldLoadEvent {

	@ForgeSubscribe
	public void invoke(WorldEvent.Load event) {
		World world = event.world;
		if(world.isRemote || DimensionManager.getWorld(0) != world)
			return;
		new PlayerDataController();
		new QuestController();
		new DialogController();
		new BankController();
		new RecipeController();
		new FactionController();
		new TransportController();
		new GlobalDataController();
	}
}

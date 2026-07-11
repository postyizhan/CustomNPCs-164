package noppes.npcs.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import noppes.npcs.CustomNpcs;

public abstract class BlockNpcContainerInterface extends BlockContainer{

	protected BlockNpcContainerInterface(Material par2Material) {
		super(CustomNpcs.BlockStartId++, par2Material);
	}

}

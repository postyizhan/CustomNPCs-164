package noppes.npcs.constants;

import noppes.npcs.CustomNpcs;

public enum EnumModelType {
	HumanMale("Human Male", CustomNpcs.getEntityId(), "npchumanmale"),
	Villager("Villager", CustomNpcs.getEntityId(), "npcvillager"),
	Pony("Pony", CustomNpcs.getEntityId(), "npcpony"),
	HumanFemale("Human Female", CustomNpcs.getEntityId(), "npchumanfemale"),
	DwarfMale("Dwarf Male", CustomNpcs.getEntityId(), "npcdwarfmale"),
	FurryMale("Furry Male", CustomNpcs.getEntityId(), "npcfurrymale"),
	MonsterMale("Monster Male", CustomNpcs.getEntityId(), "npczombiemale"),
	MonsterFemale("Monster Female", CustomNpcs.getEntityId(), "npczombiefemale"),
	Skeleton("Skeleton", CustomNpcs.getEntityId(), "npcskeleton"),
	DwarfFemale("Dwarf Female", CustomNpcs.getEntityId(), "npcdwarffemale"),
	FurryFemale("Furry Female", CustomNpcs.getEntityId(), "npcfurryfemale"),
	OrcMale("Orc Male", CustomNpcs.getEntityId(), "npcorcfmale"),
	OrcFemale("Orc Female", CustomNpcs.getEntityId(), "npcorcfemale"),
	ElfMale("Elf Male", CustomNpcs.getEntityId(), "npcelfmale"),
	ElfFemale("Elf Female", CustomNpcs.getEntityId(), "npcelffemale"),
	Crystal("Crystal", CustomNpcs.getEntityId(), "npccrystal"),
	Golem("Golem", CustomNpcs.getEntityId(), "npcGolem"),
	EnderChibi("EnderChibi", CustomNpcs.getEntityId(), "npcenderchibi"),
	EnderMan("EnderMan", CustomNpcs.getEntityId(), "npcEnderman"),
	NagaMale("Naga Male", CustomNpcs.getEntityId(), "npcnagamale"),
	NagaFemale("Naga Female", CustomNpcs.getEntityId(), "npcnagafemale"),
	Slime("Slime", CustomNpcs.getEntityId(), "NpcSlime"),
	Dragon("Dragon", CustomNpcs.getEntityId(), "NpcDragon");

	public String name;
	public String entityName;
	public int id;
	EnumModelType(String name, int id, String entityName){
		this.name = name;
		this.entityName = entityName;
		this.id = id;
	}
}

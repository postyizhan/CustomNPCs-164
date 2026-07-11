package noppes.npcs.constants;

public enum EnumAnimation {
	NONE,SITTING,LYING,SNEAKING,DANCING,Aiming,CRAWLING,HUG,CRY, WAVING, BOW;

	public int getWalkingAnimation() {
		if(this == SNEAKING)
			return 1;
		if(this == Aiming)
			return 2;
		if(this == DANCING)
			return 3;
		return 0;
	}

}

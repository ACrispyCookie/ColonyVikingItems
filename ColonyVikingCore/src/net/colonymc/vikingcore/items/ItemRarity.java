package net.colonymc.vikingcore.items;

public enum ItemRarity {
	MYTHICAL,
	EPIC,
	RARE,
	COMMON;
	
	public static char getColor(ItemRarity r) {
		switch(r) {
		case COMMON:
			return 'f';
		case EPIC:
			return '5';
		case MYTHICAL:
			return 'c';
		case RARE:
			return 'b';
		default:
			return 'f';
		}
	}
}

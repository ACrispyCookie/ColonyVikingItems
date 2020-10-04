package net.colonymc.vikingcore.items;

public class Enchantment {

	int level;
	ItemEnchant type;
	
	public Enchantment(ItemEnchant type, int level) {
		this.type = type;
		this.level = level;
	}
	
	public Enchantment() {
		this.type = null;
		this.level = 0;
	}
	
	public int getLevel() {
		return level;
	}
	
	public ItemEnchant getEnchant() {
		return type;
	}
	
}

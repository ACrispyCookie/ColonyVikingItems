package net.colonymc.colonyvikingcore.items;

public class Enchantment {

	final int level;
	final ItemEnchant type;
	
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

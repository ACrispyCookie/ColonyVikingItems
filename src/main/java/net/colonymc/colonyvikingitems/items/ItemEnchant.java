package net.colonymc.colonyvikingitems.items;

public enum ItemEnchant {
	TELEKINESIS("Telekinesis", "TelekinesisBook", ItemRarity.COMMON, "&fThis enchantment when applied\n&fto a tool will take all the items\n&fthat it harvests and it will\n"
			+ "&finstantly move them to your inventory!", "For tools"),
	
	SHARPNESS("Sharpness", "SharpnessBook", ItemRarity.MYTHICAL, "&fThis enchantment when applied\n&fto a tool will take all the items\n&fthat it harvests and it will\n"
			+ "&finstantly move them to your inventory!", "For weapons"),
	
	EXPERIENCE("Experience", "ExperienceBook", ItemRarity.MYTHICAL, "&fThis enchantment when applied\n&fto a tool will take all the items\n&fthat it harvests and it will\n"
			+ "&finstantly move them to your inventory!", "For tools/weapons"),
	
	UNBREAKING("Unbreaking", "UnbreakingBook", ItemRarity.COMMON, "&fThis enchantment when applied\n&fto a tool/weapon has a chance to\n&ffix the damage done to a tool after a use!", "For tools/weapons");
	
	public final String name;
	public final ItemRarity rarity;
	public final String encodedName;
	public final String itemDescription;
	public final String forWhat;
	
	ItemEnchant(String name, String encodedName, ItemRarity rarity, String itemDescription, String forWhat) {
		this.name = name;
		this.encodedName = encodedName;
		this.rarity = rarity;
		this.itemDescription = itemDescription;
		this.forWhat = forWhat;
	}
}

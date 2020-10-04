package net.colonymc.vikingcore.items;

import org.bukkit.Material;

public enum ItemType {
	MJOLNIR("Mjolnir", "mjolnir", new ItemInfo("Mjolnir", "&fThis axe was created by the dwarves.\n&fWhen you hit an enemy they will\n&fget striked by a lightning!", "Weapon", 
			true, true, true, Material.IRON_AXE, ItemRarity.COMMON, new ItemEnchant[] {ItemEnchant.SHARPNESS, ItemEnchant.UNBREAKING, ItemEnchant.EXPERIENCE}, 9)),
	
	GUNGNIR("Gungnir", "gungnir", new ItemInfo("Gungnir", "&fThis spear was created by the dwarves.\n&fIt will never miss its target! The spear will also\n&fteleport all the items of anything it kills\n&fin your inventory!", "Weapon", 
			true, true,true,  Material.BLAZE_ROD, ItemRarity.MYTHICAL, new ItemEnchant[] {ItemEnchant.SHARPNESS, ItemEnchant.UNBREAKING}, 30)),
	
	HOFUND("Hofund", "hofund", new ItemInfo("Hofund", "&fThis mythical sword of Heimdall\n&fis able to retrieve health of other players\n&fand use it to damage one enemy!\n&fUse it when a lot of players are close\n\n"
			+ "&5» &fRight-Click to super charge it!", "Weapon", true, true, true, Material.GOLD_SWORD, ItemRarity.EPIC, new ItemEnchant[] {ItemEnchant.SHARPNESS, ItemEnchant.UNBREAKING}, 10)),
	
	ODINS_SHIELD("OdinsShield", "odinsshield", new ItemInfo("Odins Shield", "&fThis mythical shield has a &d30% &fchance\n&fto deflect a Gungnir. After deflecting one,\n&fit will break and it will not\n&fbe able to stop another one.", "Special Item", 
			false, false, false, Material.EYE_OF_ENDER, ItemRarity.RARE, new ItemEnchant[] {}, -1)),
	
	JOROS_HOE("JorosHoe", "joroshoe", new ItemInfo("Joros Hoe", "&fThis hoe was created by the dwarves.\n&fDepending on the selected mode,\n&fwill either instantly teleport the crops\n&fin your inventory or it will automatically,"
			+ "\n&fsell them to the market.", "Special Tool", 
			false, true, true, Material.DIAMOND_HOE, ItemRarity.RARE, new ItemEnchant[] {ItemEnchant.TELEKINESIS, ItemEnchant.UNBREAKING}, 3)),
	
	BRAGIS_PICKAXE("BragisPickaxe", "bragispickaxe", new ItemInfo("Bragis Pickaxe", "&fThis pickaxe was created by the dwarves.\n&fWhen you break a block using this,\n&fit will make a 3x3 hole on the ground.", "Special Tool", 
			false, true, true,  Material.DIAMOND_PICKAXE, ItemRarity.COMMON, new ItemEnchant[] {ItemEnchant.TELEKINESIS, ItemEnchant.UNBREAKING}, 4)),
	
	BRAGIS_SHOVEL("BragisShovel", "bragisshovel", new ItemInfo("Bragis Shovel", "&fThis shovel was created by the dwarves.\n&fWhen you break a block using this,\n&fit will make a 3x3 hole on the ground.", "Special Tool", 
			false, true, true, Material.DIAMOND_SPADE, ItemRarity.COMMON, new ItemEnchant[] {ItemEnchant.TELEKINESIS, ItemEnchant.UNBREAKING}, 1)),
	
	YMIRS_DRILL("YmirsDrill", "ymirsdrill", new ItemInfo("Ymirs Drill", "&fThis item was created by the dwarves.\n&fWhen placed down it will dig out,\n&fevery block in the chunk after &d10 seconds\n&fAfter it has been used one time,"
			+ "\n&fit will disappear and you won't be\n&fable to use it again.", "Special Tool", false, false, false, Material.ENDER_PORTAL_FRAME, ItemRarity.RARE, new ItemEnchant[] {}, -1)),
	
	HOENIRS_HOPPER("HoenirsHopper", "hoenirshopper", new ItemInfo("Hoenirs Hopper", "&fThis hopper was created by the dwarves.\n"
			+ "&fWhen placed on a chunk it will collect\n&fanything that falls on the ground.", "Special Item", false, false, false, Material.HOPPER, ItemRarity.RARE, new ItemEnchant[] {}, -1));
	
	public final String className;
	public final String encodedName;
	public final ItemInfo info;
	
	ItemType(String className, String encodedName, ItemInfo info) {
		this.className = className;
		this.encodedName = encodedName;
		this.info = info;
	}
	
	public static ItemType fromEncodedName(String name) {
		for(ItemType type : ItemType.values()) {
			if(type.encodedName.equals(name)) {
				return type;
			}
		}
		return null;
	}
	
	public static ItemType fromName(String name) {
		for(ItemType type : ItemType.values()) {
			if(type.info.getName().equals(name)) {
				return type;
			}
		}
		return null;
	}
	
	public static char getLevelColor(int level) {
		switch(level) {
		case 1:
			return 'a';
		case 2:
			return 'a';
		case 3:
			return 'e';
		case 4:
			return 'e';
		case 5:
			return '6';
		case 6:
			return '6';
		case 7:
			return 'c';
		case 8:
			return 'c';
		case 9:
			return '4';
		case 10:
			return '4';
		default:
			return '4';
		}
	}
}

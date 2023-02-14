package net.colonymc.colonyvikingitems.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotapi.api.itemstack.ItemStackNBT;

public class ItemChecker {
	
	public static boolean isSpecialItem(ItemStack item) {
		if(item != null && item.hasItemMeta()) {
			return ItemStackNBT.hasTag(item, "vikingItem");
		}
		return false;
	}
	
	public static boolean isEnchantmentBook(ItemStack item) {
		if(item != null && item.hasItemMeta() && item.getType() == Material.BOOK) {
			return ItemStackNBT.hasTag(item, "vikingBook");
		}
		return false;
	}
	
	public static ItemType whatType(ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getType() != Material.AIR && isSpecialItem(item)) {
				return typeFromEncodedName(ItemStackNBT.getString(item, "vikingItem"));
		}
		return null;
	}
	
	public static ItemEnchant whatEnchantment(ItemStack item) {
		if(item != null && item.getItemMeta() != null && item.getType() != Material.AIR && isEnchantmentBook(item)) {
				return enchantFromEncodedName(ItemStackNBT.getString(item, "vikingBook"));
		}
		return null;
	}
	
	public static ItemType typeFromEncodedName(String s) {
		for(ItemType t : ItemType.values()) {
			if(t.encodedName.equalsIgnoreCase(s)) {
				return t;
			}
		}
		return null;
	}
	
	public static ItemEnchant enchantFromEncodedName(String s) {
		for(ItemEnchant t : ItemEnchant.values()) {
			if(t.encodedName.equalsIgnoreCase(s)) {
				return t;
			}
		}
		return null;
	}

}

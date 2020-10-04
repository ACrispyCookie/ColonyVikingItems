package net.colonymc.vikingcore.items;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Material;

public class ItemInfo {
	
	private String name;
	private String itemDescription;
	private String itemType;
	private double damage;
	private boolean upgradeable;
	private boolean enchantable;
	private boolean hasDurability;
	private ItemRarity rarity;
	private ItemEnchant[] validEnchants;
	private Material material;
	private HashMap<String, Field> specialValues = new HashMap<String, Field>();
	
	public ItemInfo(String name, String description, String itemType, boolean upgradeable, boolean enchantable, boolean hasDurability, Material mat, ItemRarity rarity, ItemEnchant[] enchants, double damage) {
		this.name = name;
		this.damage = damage;
		this.itemDescription = description;
		this.itemType = itemType;
		this.upgradeable = upgradeable;
		this.enchantable = enchantable;
		this.hasDurability = hasDurability;
		this.material = mat;
		this.rarity = rarity;
		this.validEnchants = enchants;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return itemDescription;
	}
	
	public String getType() {
		return itemType;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public boolean isUpgradeable() {
		return upgradeable;
	}
	
	public boolean isEnchantable() {
		return enchantable;
	}
	
	public boolean hasDurability() {
		return hasDurability;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public ItemRarity getRarity() {
		return rarity;
	}
	
	public ItemEnchant[] getValidEnchants() {
		return validEnchants;
	}
	
	public HashMap<String, Field> getSpecialValues() {
		return specialValues;
	}

}

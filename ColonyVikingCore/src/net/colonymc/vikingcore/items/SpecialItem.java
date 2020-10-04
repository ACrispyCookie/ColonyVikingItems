package net.colonymc.vikingcore.items;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import net.colonymc.api.itemstacks.ItemStackBuilder;
import net.colonymc.api.itemstacks.NBTItems;
import net.colonymc.api.primitive.RomanNumber;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public abstract class SpecialItem implements Listener {
	
	private Player p;
	private ItemType type;
	private int durability;
	private int maxDurability;
	private double damage;
	private String name;
	private ItemStack item;
	private ItemInfo info;
	private ArrayList<Enchantment> enchantments = new ArrayList<Enchantment>();
	protected int level;
	protected final int attributeIndex = 4;
	
	protected abstract String getOtherValues();
	protected abstract void setOtherValues();
	protected abstract ArrayList<NBTBase> getOtherTags();
	protected abstract boolean hasOtherValues();
	protected abstract void applyChanges();
	public abstract String getUpgradeString(int level);
	
	public SpecialItem(ItemType type, int durability, int maxDurability, int level, Player p) {
		this.type = type;
		this.p = p;
		this.info = type.info;
		this.durability = durability;
		this.maxDurability = maxDurability;
		this.level = level;
		this.damage = info.getDamage();
		setOtherValues();
		if(level > 1) {
			applyChanges();
		}
		this.item = getNormalItemStack();
		if(info.isUpgradeable()) {
			this.name = item.getItemMeta().getDisplayName().substring(0, item.getItemMeta().getDisplayName().indexOf('(') - 3);
		}
		else {
			this.name = item.getItemMeta().getDisplayName();
		}
	}
	
	public SpecialItem(ItemStack i, Player p) {
		this.p = p;
		this.item = i;
		this.type = ItemType.fromEncodedName(((NBTTagString) NBTItems.getTag(i, "vikingItem")).a_());
		this.info = type.info;
		NBTTagList attributes = (NBTTagList) NBTItems.getTag(i, "vikingAttributes");
		this.durability = (int) NBTItems.getDouble(attributes, 0);
		this.maxDurability = (int) NBTItems.getDouble(attributes, 1);
		this.level = (int) NBTItems.getDouble(attributes, 2);
		this.damage = NBTItems.getDouble(attributes, 3);
		NBTTagList enchants = (NBTTagList) NBTItems.getTag(i, "vikingEnchants");
		NBTTagList enchantLevels = (NBTTagList) NBTItems.getTag(i, "vikingEnchantLevels");
		for(int c = 0; c < enchants.size(); c++) {
			enchantments.add(new Enchantment(ItemEnchant.valueOf(NBTItems.getString(enchants, c)), NBTItems.getInt(enchantLevels, c)));
		}
		if(info.isUpgradeable()) {
			this.name = item.getItemMeta().getDisplayName().substring(0, item.getItemMeta().getDisplayName().indexOf('(') - 3);
		}
		else {
			this.name = item.getItemMeta().getDisplayName();
		}
	}
	
	public void removeDurability(int amount) {
		durability = durability - amount;
		if(durability > 0) {
			updateItem();
		}
		else {
			p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 2, 1);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYour " + name + " &cjust broke!"));
			this.item = new ItemStack(Material.AIR);
		}
	}
	
	public void addEnchant(ItemEnchant ench, int level) {
		Enchantment e = new Enchantment(ench, level);
		enchantments.add(e);
		updateItem();
	}
	
	public void updateItem() {
		this.item = getNormalItemStack();
	}
	
	public void upgrade() {
		level++;
		applyChanges();
		updateItem();
	}
	
	private ItemStack getNormalItemStack() {
		ItemStackBuilder builder = new ItemStackBuilder(info.getMaterial());
		if(info.isUpgradeable()) {
			builder.name(ChatColor.translateAlternateColorCodes('&', "&" + getRarityColor() + info.getName() + " &7(" + ChatColor.getByChar(getLevelColor()) + RomanNumber.toRoman(level) + "&7)"));
		}
		else {
			builder.name(ChatColor.translateAlternateColorCodes('&', "&" + getRarityColor() + info.getName()));
		}
		builder.glint(true);
		String lore = "&8" + info.getType() + "\n \n" + info.getDescription() + "\n \n";
		lore = lore + (hasDurability() ? "&5» &fDurability: " + ChatColor.getByChar(getDurabilityColor()) + durability + "/" + maxDurability + "\n \n" : "");
		lore = lore + (isEnchantable() ? "&5» &fEnchantments: " + (enchantments.isEmpty() ? "&7(None)" : getEnchantString()) + "\n \n" : "");
		lore = lore + (hasOtherValues() ? getOtherValues() : "");
		lore = lore + (!isUpgradeable() ? "&8Not Upgradeable\n" : "");
		lore = lore + ChatColor.getByChar(getRarityColor()) + ChatColor.translateAlternateColorCodes('&', "&l") + info.getRarity().name();
		builder.lore(lore);
		builder.addFlag(ItemFlag.HIDE_ATTRIBUTES);
		builder.addTag("vikingItem", new NBTTagString(this.type.encodedName));
		NBTTagList integers = new NBTTagList();
		integers.add(new NBTTagDouble(durability));
		integers.add(new NBTTagDouble(maxDurability));
		integers.add(new NBTTagDouble(level));
		integers.add(new NBTTagDouble(damage));
		if(hasOtherValues()) {
			for(NBTBase toAdd : getOtherTags()) {
				integers.add(toAdd);
			}
		}
		builder.addTag("vikingAttributes", integers);
		NBTTagList enchants = new NBTTagList();
		NBTTagList enchantLevels = new NBTTagList();
		for(Enchantment ench : enchantments) {
			enchants.add(new NBTTagString(ench.type.name()));
			enchantLevels.add(new NBTTagInt(ench.level));
		}
		builder.addTag("vikingEnchants", enchants);
		builder.addTag("vikingEnchantLevels", enchantLevels);
		return builder.build();
	}
	
	public static SpecialItem getByType(ItemType i, int durability, int maxDurability, int level, Player p) {
		try {
			Class<?> cl = Class.forName("net.colonymc.vikingcore.items." + i.encodedName + "." + i.className);
			Constructor<?> con = cl.getDeclaredConstructor(new Class[] {ItemType.class, int.class, int.class, int.class, Player.class});
			SpecialItem item = (SpecialItem) con.newInstance(i, durability, maxDurability, level, p);
			return item;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static SpecialItem getByType(ItemStack i, Player p) {
		try {
			ItemType type = ItemType.fromEncodedName(NBTItems.getString(i, "vikingItem"));
			Class<?> cl = Class.forName("net.colonymc.vikingcore.items." + type.encodedName + "." + type.className);
			Constructor<?> con = cl.getDeclaredConstructor(new Class[] {ItemStack.class, Player.class});
			SpecialItem item = (SpecialItem) con.newInstance(i, p);
			return item;
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private char getDurabilityColor() {
			if((double) durability / (double) maxDurability >= .75) {
				return 'a';
			}
			else if((double) durability / (double) maxDurability >= .5) {
				return 'e';
			}
			else if((double) durability / (double) maxDurability >= .25) {
				return '6';
			}
			else {
				return 'c';
			}
	}
	
	private char getLevelColor() {
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
	
	private char getRarityColor() {
		switch(info.getRarity()) {
		case COMMON:
			return 'f';
		case RARE:
			return 'b';
		case EPIC:
			return '5';
		case MYTHICAL:
			return 'c';
		default:
			break;
		}
	    return '1';
	}
	
	private String getEnchantString() {
		String enchs = "";
		ArrayList<Enchantment> sorted = new ArrayList<Enchantment>();
		sorted.addAll(enchantments);
		Collections.sort(sorted, new EnchantComparator());
		int counter = 0;
		for(int i = 0; i < sorted.size(); i++) {
			if(i < sorted.size()) {
				Enchantment ench = sorted.get(i);
				if(counter == 3) {
					if(i + 1 == sorted.size()) {
						enchs = enchs + "\n" + ChatColor.getByChar(ItemRarity.getColor(ench.getEnchant().rarity)) + ench.type.name + " " + RomanNumber.toRoman(ench.getLevel()) + ", ";
					}
					else {
						enchs = enchs + "\n" + ChatColor.getByChar(ItemRarity.getColor(ench.getEnchant().rarity)) + ench.type.name + " " + RomanNumber.toRoman(ench.getLevel());
					}
					counter = 0;
				}
				else {
					if(i + 1 == sorted.size()) {
						enchs = enchs + ChatColor.getByChar(ItemRarity.getColor(ench.getEnchant().rarity)) + ench.type.name + " " + RomanNumber.toRoman(ench.getLevel());
					}
					else {
						enchs = enchs + ChatColor.getByChar(ItemRarity.getColor(ench.getEnchant().rarity)) + ench.type.name + " " + RomanNumber.toRoman(ench.getLevel()) + ", ";
					}
					counter++;
				}
			}
		}
		return ChatColor.translateAlternateColorCodes('&', enchs);
	}
	
	public void setDamage(double dam) {
		damage = dam;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public ItemEnchant[] getValidEnchants() {
		return info.getValidEnchants();
	}
	
	public ItemType getItemType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public ItemRarity getRarity() {
		return info.getRarity();
	}
	
	public double getDurability() {
		return durability;
	}
	
	public double getMaxDurability() {
		return maxDurability;
	}
	
	public int getLevel() {
		return level;
	}
	
	public ArrayList<Enchantment> getRawEnchants() {
		return enchantments;
	}
	
	public double getDamage() {
		return damage;
	}
	
	public boolean isUpgradeable() {
		return type.info.isUpgradeable();
	}
	
	public boolean hasDurability() {
		return type.info.hasDurability();
	}
	
	public boolean isEnchantable() {
		return type.info.isEnchantable();
	}
	
	public int containsEnchant(ItemEnchant ench, int level) {
		boolean contains = false;
		boolean hasLevel = false;
		for(Enchantment encha : enchantments) {
			if(encha.getEnchant() == ench) {
				contains = true;
				if(encha.getLevel() >= level) {
					hasLevel = true;
				}
				break;
			}
		}
		if(contains) {
			if(hasLevel) {
				return 1;
			}
			else {
				return 0;
			}
		}
		else {
			return -1;
		}
	}
	
	public HashMap<ItemEnchant, Integer> getEnchants() {
		HashMap<ItemEnchant, Integer> enchants = new HashMap<ItemEnchant, Integer>();
		for(Enchantment ench : enchantments) {
			enchants.put(ench.getEnchant(), ench.getLevel());
		}
		return enchants;
	}
	
	public double getFinalDamage() {
		double finalDamage = damage;
		if(getEnchants().containsKey(ItemEnchant.SHARPNESS)) {
			int level = getEnchants().get(ItemEnchant.SHARPNESS);
			finalDamage = finalDamage + 0.5 * (level - 1) + 1;
		}
		return finalDamage;
	}

}
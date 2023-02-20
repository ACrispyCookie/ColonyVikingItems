package net.colonymc.colonyvikingitems.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotlib.lib.itemstack.ItemStackBuilder;
import net.colonymc.colonyspigotlib.lib.itemstack.ItemStackNBT;
import net.colonymc.colonyspigotlib.lib.primitive.RomanNumber;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class EnchantmentBook {
	
	protected final ItemEnchant enchant;
	protected final int level;
	protected final ItemStack item;
	protected final OfflinePlayer player;
	
	public EnchantmentBook(ItemEnchant enchant, int level, Player p) {
		this.enchant = enchant;
		this.level = level;
		this.player = p;
		this.item = createItem(enchant.itemDescription, enchant.forWhat);
	}
	
	public EnchantmentBook(ItemStack item, Player p) {
		this.enchant = ItemEnchant.valueOf(ItemStackNBT.getString(item, "vikingBook"));
		this.level = ((NBTTagInt) ItemStackNBT.getTag(item, "vikingLevel")).d();
		this.player = p;
		this.item = item;
	}

	private ItemStack createItem(String description, String type) {
		ItemStackBuilder builder = new ItemStackBuilder(Material.BOOK);
		builder.name(ChatColor.translateAlternateColorCodes('&', "&" + getRarityColor(enchant.rarity) + enchant.name + " Book " + "&7(&d" + RomanNumber.toRoman(level) + "&7)"));
		builder.addTag("vikingBook", new NBTTagString(this.enchant.name()));
		builder.addTag("vikingLevel", new NBTTagInt(this.level));
		builder.lore("&8" + type + "\n \n" + description + "\n \n" + ChatColor.getByChar(getRarityColor(enchant.rarity)) + ChatColor.translateAlternateColorCodes('&', "&l") + enchant.rarity.name());
		builder.glint(true);
		return builder.build();
	}
	
	public static char getRarityColor(ItemRarity i) {
		switch(i) {
		case COMMON:
			return 'f';
		case RARE:
			return 'b';
		case EPIC:
			return '5';
		case MYTHICAL:
			return 'c';
		}
	    return '1';
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public int getLevel() {
		return level;
	}
	
	public ItemEnchant getEnchant() {
		return enchant;
	}
	
	public ItemRarity getRarity() {
		return enchant.rarity;
	}

}

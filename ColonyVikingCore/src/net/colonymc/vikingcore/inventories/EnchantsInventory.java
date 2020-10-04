package net.colonymc.vikingcore.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.colonymc.api.itemstacks.SkullItemBuilder;

public class EnchantsInventory implements InventoryHolder {
	
	int infoItemSlot = 13;
	public Player p = null;
	Inventory inv = null;
	ItemStack infoItem = null;
	
	public EnchantsInventory(Player p) {
		this.p = p;
	}
	
	@Override
	public Inventory getInventory() {
		inv = Bukkit.createInventory(this, 45, ChatColor.translateAlternateColorCodes('&', "&5&l&k:&5&lGalar&k:"));
		fillInventory();
		return inv;
	}
	
	public void fillInventory() {
		fillBlackGlass();
		fillPurpleGlass();
		fillMagentaGlass();
		infoItem = new SkullItemBuilder()
				.url("http://textures.minecraft.net/texture/badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a")
				.name("&d&lInfo")
				.lore("&8Enchanter Dwarf\n \n" +
				"&f&oUse this dwarf to buy enchantments for your\n" +
				"&f&ospecial weapons and tools! &f&oEnchantments\n" +
				"&f&oboost your weapon or tool and make\n" + "&f&othem stronger and faster! Depending on\n" +
				"&f&othe rarity you choose, you will receive\n" + "&f&othe corresponding enchantment for your tools!")
				.build();
		inv.setItem(infoItemSlot, infoItem);
	}
	
	public void fillBlackGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		glass.setDurability((short) 15);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		inv.setItem(0, glass);
		inv.setItem(1, glass);
		inv.setItem(2, glass);
		inv.setItem(3, glass);
		inv.setItem(4, glass);
		inv.setItem(5, glass);
		inv.setItem(6, glass);
		inv.setItem(7, glass);
		inv.setItem(8, glass);
		inv.setItem(9, glass);
		inv.setItem(17, glass);
		inv.setItem(18, glass);
		inv.setItem(26, glass);
		inv.setItem(27, glass);
		inv.setItem(35, glass);
		inv.setItem(36, glass);
		inv.setItem(37, glass);
		inv.setItem(38, glass);
		inv.setItem(39, glass);
		inv.setItem(40, glass);
		inv.setItem(41, glass);
		inv.setItem(42, glass);
		inv.setItem(43, glass);
		inv.setItem(44, glass);
	}
	
	public void fillPurpleGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		glass.setDurability((short) 10);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		inv.setItem(10, glass);
		inv.setItem(11, glass);
		inv.setItem(12, glass);
		inv.setItem(13, glass);
		inv.setItem(14, glass);
		inv.setItem(15, glass);
		inv.setItem(16, glass);
		inv.setItem(28, glass);
		inv.setItem(29, glass);
		inv.setItem(30, glass);
		inv.setItem(31, glass);
		inv.setItem(32, glass);
		inv.setItem(33, glass);
		inv.setItem(34, glass);
	}
	
	public void fillMagentaGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		glass.setDurability((short) 2);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		inv.setItem(20, glass);
		inv.setItem(22, glass);
		inv.setItem(24, glass);
	}
	
	public ItemStack createItem(Material material, int amount, String name, String... lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> finalLore = new ArrayList<String>();
		for(String s : lore) {
			if(s.contains("\n")) {
				String[] strings = s.split("\n");
				for(String ss : strings) {
					finalLore.add(ChatColor.translateAlternateColorCodes('&', ss));
				}
			}
			else {
				finalLore.add(ChatColor.translateAlternateColorCodes('&', s));
			}
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(finalLore);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack createGlassItem(int amount, String name, String... lore) {
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> finalLore = new ArrayList<String>();
		for(String s : lore) {
			finalLore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(finalLore);
		item.setItemMeta(meta);
		return item;
	}

}

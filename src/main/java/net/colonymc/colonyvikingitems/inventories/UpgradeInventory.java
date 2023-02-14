package net.colonymc.colonyvikingitems.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.colonymc.colonyspigotapi.api.itemstack.SkullItemBuilder;
import net.colonymc.colonyspigotapi.api.player.PlayerInventory;
import net.colonymc.colonyspigotapi.api.primitive.RomanNumber;
import net.colonymc.colonyskyblockcore.guilds.Guild;
import net.colonymc.colonyvikingitems.items.ItemRarity;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;

public class UpgradeInventory implements InventoryHolder {
	

	int infoItemSlot = 13;
	final int selectedItemSlot = 22;
	final int upgradeItemSlot = 31;
	int costInExp = 0;
	int costInDust = 0;
	public boolean hasSelectedItem = false;
	public boolean canUpgrade = false;
	public boolean hasUpgraded = false;
	public SpecialItem selectedItem = null;
	ItemStack repairItem = null;
	public Player p = null;
	Inventory inv = null;
	public static final HashMap<Player, UpgradeInventory> upgrade = new HashMap<>();
	
	public UpgradeInventory(Player p) {
		this.p = p;
		upgrade.put(p, this);
	}
	
	@Override
	public Inventory getInventory() {
		inv = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', "&5&l&k:&5&lDurnir&k:"));
		fillInventory();
		return inv;
	}
	
	public void fillInventory() {
		fillBlackGlass();
		fillPurpleGlass();
		fillMagentaGlass();
		repairItem = new SkullItemBuilder()
				.url("http://textures.minecraft.net/texture/badc048a7ce78f7dad72a07da27d85c0916881e5522eeed1e3daf217a38c1a")
				.name("&d&lInfo")
				.lore("&8Upgrader Dwarf\n \n" +
				"&f&oUse this dwarf to upgrade your\n" +
				"&f&ospecial weapons and tools!\n" + "&f&oUpgrading a weapon or tool will make it\n" +  
				"&f&osharper and stronger so it can\n" + "&f&odeal more damage and last for more uses!\n \n" + 
				"&cNo item selected\n" + "&dClick any special item to select it!")
				.build();
		inv.setItem(13, repairItem);
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cNo item selected"));
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&fClick an item on you inventory"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&fin order to select it and upgrade it!"));
		meta.setLore(lore);
		glass.setItemMeta(meta);
		inv.setItem(22, glass);
	}
	
	public void selectItem(ItemStack item, int slot) {
		if(!hasSelectedItem) {
			hasSelectedItem = true;
			p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1.5f);
			//set the item in the menu
			selectedItem = SpecialItem.getByType(item.clone(), p);
			selectedItem.getItemStack().setAmount(1);
			inv.setItem(22, selectedItem.getItemStack());
			//set the lore of the info item in the menu
			List<String> lore = repairItem.getItemMeta().getLore();
			lore.set(lore.size() - 1, ChatColor.translateAlternateColorCodes('&', "&dClick the anvil below to upgrade it!"));
			lore.set(lore.size() - 2, ChatColor.translateAlternateColorCodes('&', "&dSelected Item: " + item.getItemMeta().getDisplayName()));
			ItemMeta meta = repairItem.getItemMeta();
			meta.setLore(lore);
			repairItem.setItemMeta(meta);
			inv.setItem(13, repairItem);
			//set players inventory
			item.setAmount(item.getAmount() - 1);
			p.getInventory().setItem(slot, item);
			//set the anvil item
			int level = selectedItem.getLevel();
			ItemRarity rarity = selectedItem.getRarity();
			switch(rarity) {
			case COMMON:
				costInExp = level * 6;
				costInDust =  (int) ((level - 1) * 55.25);
				break;
			case EPIC:
				costInExp = level * 16;
				costInDust = level * 75;
				break;
			case MYTHICAL:
				costInExp = level * 30;
				costInDust = level * 120;
				break;
			case RARE:
				costInExp = level * 10;
				costInDust = level * 60;
				break;
			default:
				break;
			}
			if(Guild.getByPlayer(p).getGuildPlayer(p).getDust() >= costInDust && p.getLevel() >= costInExp) {
				canUpgrade = true;
			}
			ItemStack anvil = canUpgradeItem(level, costInExp, costInDust);
			inv.setItem(31, anvil);
		}
	}
	
	public void deselectItem() {
		//set the repair item
		List<String> lore = repairItem.getItemMeta().getLore();
		lore.set(lore.size() - 1, ChatColor.translateAlternateColorCodes('&', "&dClick any special item to select it!"));
		lore.set(lore.size() - 2, ChatColor.translateAlternateColorCodes('&', "&cNo item selected"));
		ItemMeta meta = repairItem.getItemMeta();
		meta.setLore(lore);
		repairItem.setItemMeta(meta);
		inv.setItem(13, repairItem);
		//give item to player
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
		PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);
		//set the selectedItem slot
		ItemStack selectedItemGlass = createGlassItem(1, "&cNo item selected", "", "&fClick an item on you inventory", "&fin order to select it and upgrade it!");
		inv.setItem(22, selectedItemGlass);
		//set the upgrade item
		ItemStack glass = createGlassItem(1, " ");
		glass.setDurability((short) 2);
		inv.setItem(31, glass);
		// variables
		hasSelectedItem = false;
		selectedItem = null;
		canUpgrade = false;
		costInExp = 0;
		costInDust = 0;
	}
	
	@SuppressWarnings("deprecation")
	public void upgradeSuccessful() {
		hasUpgraded = true;
		Guild.getByPlayer(p).getGuildPlayer(p).removeDust(costInDust);
		p.setLevel(p.getLevel() - costInExp);
		p.closeInventory();
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &aYou have successfully upgraded your " + selectedItem.getName() + " &ato level " 
		+ ChatColor.getByChar(ItemType.getLevelColor(selectedItem.getLevel() + 1)) + RomanNumber.toRoman(selectedItem.getLevel() + 1) + "&a!"));
		p.sendTitle(selectedItem.getName(), ChatColor.translateAlternateColorCodes('&', "&5&lSUCCESSFULLY UPGRADED!"));
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 1);
		selectedItem.upgrade();
		PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);
	}
	
	public void closeInventory() {
		if(hasSelectedItem) {
			PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cancelled your upgrade order!"));
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 2, 1);
		}
		hasSelectedItem = false;
		upgrade.remove(p);
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
		inv.setItem(6, glass);
		inv.setItem(7, glass);
		inv.setItem(8, glass);
		inv.setItem(9, glass);
		inv.setItem(10, glass);
		inv.setItem(16, glass);
		inv.setItem(17, glass);
		inv.setItem(18, glass);
		inv.setItem(26, glass);
		inv.setItem(27, glass);
		inv.setItem(35, glass);
		inv.setItem(36, glass);
		inv.setItem(37, glass);
		inv.setItem(43, glass);
		inv.setItem(44, glass);
		inv.setItem(45, glass);
		inv.setItem(46, glass);
		inv.setItem(47, glass);
		inv.setItem(51, glass);
		inv.setItem(52, glass);
		inv.setItem(53, glass);
	}
	
	public void fillPurpleGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		glass.setDurability((short) 10);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		inv.setItem(3, glass);
		inv.setItem(4, glass);
		inv.setItem(5, glass);
		inv.setItem(11, glass);
		inv.setItem(12, glass);
		inv.setItem(14, glass);
		inv.setItem(15, glass);
		inv.setItem(19, glass);
		inv.setItem(20, glass);
		inv.setItem(24, glass);
		inv.setItem(25, glass);
		inv.setItem(28, glass);
		inv.setItem(29, glass);
		inv.setItem(33, glass);
		inv.setItem(34, glass);
		inv.setItem(38, glass);
		inv.setItem(39, glass);
		inv.setItem(41, glass);
		inv.setItem(42, glass);
		inv.setItem(48, glass);
		inv.setItem(49, glass);
		inv.setItem(50, glass);
	}
	
	public void fillMagentaGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		glass.setDurability((short) 2);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		inv.setItem(21, glass);
		inv.setItem(23, glass);
		inv.setItem(30, glass);
		inv.setItem(31, glass);
		inv.setItem(32, glass);
		inv.setItem(40, glass);
	}
	
	public ItemStack createItem(Material material, int amount, String name, String... lore) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		List<String> finalLore = new ArrayList<>();
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
		List<String> finalLore = new ArrayList<>();
		for(String s : lore) {
			finalLore.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(finalLore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack canUpgradeItem(int level, int costInExp, int costInDust) {
		ItemStack anvil;
		if(canUpgrade) {
			if(costInDust == 0) {
				anvil = createItem(Material.ANVIL, 1, "&5&lUPGRADE ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fLevel: " + ChatColor.getByChar(ItemType.getLevelColor(level)) + RomanNumber.toRoman(level) + " &d➜ " + ChatColor.getByChar(ItemType.getLevelColor(level + 1)) + RomanNumber.toRoman(level + 1), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " Levels",
						"", "&f&nImprovements:", selectedItem.getUpgradeString(level + 1), "", 
								"&dClick to upgrade your item!");
			}
			else {
				anvil = createItem(Material.ANVIL, 1, "&5&lUPGRADE ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fLevel: " + ChatColor.getByChar(ItemType.getLevelColor(level)) + RomanNumber.toRoman(level) + " &d➜ " + ChatColor.getByChar(ItemType.getLevelColor(level + 1)) + RomanNumber.toRoman(level + 1), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " Levels",
						" &5&l* &fDwarf Dust: &" + getAvailibilityDustColor(p, costInDust) + costInDust + "g", "", "&f&nImprovements:", selectedItem.getUpgradeString(level + 1), "", 
								"&dClick to upgrade your item!");
			}
		}
		else {
			if(costInDust == 0) {
				anvil = createItem(Material.ANVIL, 1, "&c&lUPGRADE ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fLevel: " + ChatColor.getByChar(ItemType.getLevelColor(level)) + RomanNumber.toRoman(level) + " &d➜ " + ChatColor.getByChar(ItemType.getLevelColor(level + 1)) + RomanNumber.toRoman(level + 1), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " Levels",
						"", "&f&nImprovements:", selectedItem.getUpgradeString(level + 1), "", 
								"&cCannot afford the upgrade!");
			}
			else {
				anvil = createItem(Material.ANVIL, 1, "&c&lUPGRADE ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fLevel: " + ChatColor.getByChar(ItemType.getLevelColor(level)) + RomanNumber.toRoman(level) + " &d➜ " + ChatColor.getByChar(ItemType.getLevelColor(level + 1)) + RomanNumber.toRoman(level + 1), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " Levels",
						" &5&l* &fDwarf Dust: &" + getAvailibilityDustColor(p, costInDust) + costInDust + "g", "", "&f&nImprovements:", selectedItem.getUpgradeString(level + 1), "", 
								"&cCannot afford the upgrade!");
			}
		}
		return anvil;
	}
	
	public char getAvailibilityDustColor(Player p, int amount) {
		if(Guild.getByPlayer(p).getGuildPlayer(p).getDust() >= amount) {
			return 'a';
		}
		else {
			return 'c';
		}
	}
	
	public char getAvailibilityExpColor(Player p, int amount) {
		if(p.getLevel() >= amount) {
			return 'a';
		}
		else {
			return 'c';
		}
	}

}

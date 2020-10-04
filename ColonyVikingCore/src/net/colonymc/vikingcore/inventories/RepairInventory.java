package net.colonymc.vikingcore.inventories;

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

import net.colonymc.api.itemstacks.SkullItemBuilder;
import net.colonymc.api.player.ExperienceManager;
import net.colonymc.api.player.PlayerInventory;
import net.colonymc.colonyskyblockcore.guilds.Guild;
import net.colonymc.vikingcore.items.SpecialItem;

public class RepairInventory implements InventoryHolder {
	
	int infoItemSlot = 13;
	int selectedItemSlot = 22;
	int repairItemSlot = 31;
	int[] durabilityGlasses = new int[] {41, 32, 23, 30, 21};
	int durability = 0;
	int costInExp = 0;
	int costInDust = 0;
	public boolean hasSelectedItem = false;
	public boolean hasRepaired = false;
	public boolean canRepair = false;
	public static HashMap<Player, RepairInventory> repair = new HashMap<Player, RepairInventory>();
	public Player p = null;
	Inventory inv = null;
	ItemStack repairItem = null;
	public SpecialItem selectedItem = null;
	
	public RepairInventory(Player p) {
		this.p = p;
		repair.put(p, this);
	}
	
	@Override
	public Inventory getInventory() {
		inv = Bukkit.createInventory(this, 54, ChatColor.translateAlternateColorCodes('&', "&5&l&k:&5&lBrokkr&k:"));
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
				.lore("&8Repairer Dwarf\n \n" + 
				"&f&oUse this dwarf to repair your damaged\n" +
				"&f&ospecial weapons and tools!\n" + "&f&oRepairing a weapon or tool will restore\n" + 
				"&f&oits lost durability so you can\n" + "&f&ouse it more times!\n \n" +
				"&cNo item selected\n" + "&dClick any special item to select it!\n")
				.build();
		inv.setItem(infoItemSlot, repairItem);
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cNo item selected"));
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.translateAlternateColorCodes('&', ""));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&fClick an item on you inventory"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&fin order to select it and repair it!"));
		meta.setLore(lore);
		glass.setItemMeta(meta);
		inv.setItem(selectedItemSlot, glass);
	}
	
	public void selectItem(ItemStack item, int slot) {
		if(hasSelectedItem == false) {
			hasSelectedItem = true;
			p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1.5f);
			//set the item in the menu
			selectedItem = SpecialItem.getByType(item.clone(), p);
			selectedItem.getItemStack().setAmount(1);
			inv.setItem(selectedItemSlot, selectedItem.getItemStack());
			//set the lore of the info item in the menu
			List<String> lore = repairItem.getItemMeta().getLore();
			lore.set(lore.size() - 1, ChatColor.translateAlternateColorCodes('&', "&dClick the buttons below to select an amount!"));
			lore.set(lore.size() - 2, ChatColor.translateAlternateColorCodes('&', "&dSelected Item: " + item.getItemMeta().getDisplayName()));
			ItemMeta meta = repairItem.getItemMeta();
			meta.setLore(lore);
			repairItem.setItemMeta(meta);
			inv.setItem(infoItemSlot, repairItem);
			//set players inventory
			item.setAmount(item.getAmount() - 1);
			p.getInventory().setItem(slot, item);
			//set the glass item
			ItemStack glass = createGlassItem(1, "&cNo durability selected", "", "&fClick the buttons on this menu", "&fto select an amount of durability!");
			inv.setItem(repairItemSlot, glass);
			//durability glasses
			setDurabilityGlasses();
		}
	}
	
	private void setDurabilityGlasses() {
		ItemStack plus1 = createItem(Material.STAINED_GLASS_PANE, 1, "&a+1 durability");
		plus1.setDurability((short) 5);
		ItemStack plus10 = createItem(Material.STAINED_GLASS_PANE, 1, "&a+10 durability");
		plus10.setDurability((short) 5);
		ItemStack max = createItem(Material.STAINED_GLASS_PANE, 1, "&aMax affordable durability");
		max.setDurability((short) 5);
		ItemStack minus1 = createItem(Material.STAINED_GLASS_PANE, 1, "&c-1 durability");
		minus1.setDurability((short) 14);
		ItemStack minus10 = createItem(Material.STAINED_GLASS_PANE, 1, "&c-10 durability");
		minus10.setDurability((short) 14);
		inv.setItem(durabilityGlasses[0], plus1);
		inv.setItem(durabilityGlasses[1], plus10);
		inv.setItem(durabilityGlasses[2], max);
		inv.setItem(durabilityGlasses[3], minus1);
		inv.setItem(durabilityGlasses[4], minus10);
	}
	
	private void unsetDurabilityGlasses() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		ItemMeta meta = glass.getItemMeta();
		meta.setDisplayName(" ");
		glass.setItemMeta(meta);
		glass.setDurability((short) 2);
		ItemStack glass1 = new ItemStack(Material.STAINED_GLASS_PANE, 1);
		glass1.setItemMeta(meta);
		glass1.setDurability((short) 10);
		inv.setItem(durabilityGlasses[0], glass);
		inv.setItem(durabilityGlasses[1], glass);
		inv.setItem(durabilityGlasses[3], glass);
		inv.setItem(durabilityGlasses[4], glass);
		inv.setItem(durabilityGlasses[2], glass1);
	}

	public void deselectItem() {
		//set the repair item
		List<String> lore = repairItem.getItemMeta().getLore();
		lore.set(lore.size() - 1, ChatColor.translateAlternateColorCodes('&', "&dClick any special item to select it!"));
		lore.set(lore.size() - 2, ChatColor.translateAlternateColorCodes('&', "&cNo item selected"));
		ItemMeta meta = repairItem.getItemMeta();
		meta.setLore(lore);
		repairItem.setItemMeta(meta);
		inv.setItem(infoItemSlot, repairItem);
		//give item to player
		p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
		PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);
		//set the selectedItem slot
		ItemStack selectedItemGlass = createGlassItem(1, "&cNo item selected", "", "&fClick an item on you inventory", "&fin order to select it and repair it!");
		inv.setItem(selectedItemSlot, selectedItemGlass);
		//set the upgrade item
		ItemStack glass = createGlassItem(1, " ");
		glass.setDurability((short) 2);
		inv.setItem(repairItemSlot, glass);
		//set durability glasses
		unsetDurabilityGlasses();
		// variables
		hasSelectedItem = false;
		selectedItem = null;
		canRepair = false;
		costInExp = 0;
		costInDust = 0;
	}

	@SuppressWarnings("deprecation")
	public void repair() {
		hasRepaired = true;
		Guild.getByPlayer(p).getGuildPlayer(p).removeDust(costInDust);
		ExperienceManager.setTotalExperience(p, ExperienceManager.getTotalExperience(p) - costInExp);
		selectedItem.removeDurability(-durability);
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &aYou have successfully repaired your " + selectedItem.getName() + " &aand added " 
		+ durability + "&a durability to it!"));
		p.sendTitle(selectedItem.getName(), ChatColor.translateAlternateColorCodes('&', "&5&lSUCCESSFULLY REPAIRED!"));
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 1);
		p.closeInventory();
		PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);

	}
	
	public void closeInventory() {
		if(hasSelectedItem) {
			PlayerInventory.addItem(selectedItem.getItemStack(), p, 1);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cancelled your repair order!"));
			p.playSound(p.getLocation(), Sound.VILLAGER_NO, 2, 1);
		}
		hasSelectedItem = false;
		repair.remove(p);
	}
	
	public void checkDurabilityItem() {
		ItemStack item = null;
		if(durability == 0) {
			item = createGlassItem(1, "&cNo durability selected", "", "&fClick the buttons on this menu", "&fto select an amount of durability!");
		}
		else {
			switch(selectedItem.getRarity()) {
			case COMMON:
				costInExp = durability * 3;
				costInDust = (int) (durability * 0.5);
				break;
			case EPIC:
				costInExp = durability * 5;
				costInDust = (int) (durability * 0.6);
				break;
			case MYTHICAL:
				costInExp = durability * 10;
				costInDust = (int) (durability * 0.9);
				break;
			case RARE:
				costInExp = durability * 20;
				costInDust =(int) (durability * 1.2);
			default:
				break;
			}
			if(ExperienceManager.getTotalExperience(p) >= costInExp && Guild.getByPlayer(p).getGuildPlayer(p).getDust() >= costInDust) {
				canRepair = true;
			}
			item = canUpgradeItem();
		}
		inv.setItem(repairItemSlot, item);
	}
	
	public int getMaxAffordableDurability() {
		double dust = Guild.getByPlayer(p).getGuildPlayer(p).getDust();
		double totalExp = ExperienceManager.getTotalExperience(p);
		int currentDurability = (int) selectedItem.getDurability();
		int maxDurability = (int) selectedItem.getMaxDurability();
		double dustMultiplier = 0;
		double expMultiplier = 0;
		switch(selectedItem.getRarity()) {
		case COMMON:
			expMultiplier = 3;
			dustMultiplier = 0.5;
			break;
		case EPIC:
			expMultiplier = 5;
			dustMultiplier = 0.6;
			break;
		case MYTHICAL:
			expMultiplier = 10;
			dustMultiplier = 0.9;
			break;
		case RARE:
			expMultiplier = 20;
			dustMultiplier = 1.2;
		default:
			break;
		}
		for(int i = 0; i < maxDurability - currentDurability; i++) {
			if(dust >= i * dustMultiplier && totalExp >= i * expMultiplier) {
				continue;
			}
			else {
				if(i == 0) {
					return i;
				}
				return (i-1);
			}
		}
		return maxDurability - currentDurability;
	}
	
	public ItemStack canUpgradeItem() {
		ItemStack anvil;
		if(canRepair) {
			if(costInDust == 0) {
				anvil = createItem(Material.ANVIL, 1, "&5&lREPAIR ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fDurability: " + selectedItem.getDurability() + " &d➜ " + (selectedItem.getDurability() + durability), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " EXP",
						"", "&dClick to repair your item!");
			}
			else {
				anvil = createItem(Material.ANVIL, 1, "&5&lREPAIR ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fDurability: " + selectedItem.getDurability() + " &d➜ " + (selectedItem.getDurability() + durability), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " EXP",
						" &5&l* &fDwarf Dust: &" + getAvailibilityDustColor(p, costInDust) + costInDust + "g", "", "&dClick to repair your item!");
			}
		}
		else {
			if(costInDust == 0) {
				anvil = createItem(Material.ANVIL, 1, "&c&lREPAIR ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fDurability: " + selectedItem.getDurability() + " &d➜ " + (selectedItem.getDurability() + durability), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " EXP", "", "&cCannot afford the repair!");
			}
			else {
				anvil = createItem(Material.ANVIL, 1, "&c&lREPAIR ITEM", "", "&fItem: " + selectedItem.getName(), 
						"&fDurability: " + selectedItem.getDurability() + " &d➜ " + (selectedItem.getDurability() + durability), "", "&f&nCost", " &5&l* &fEXP Levels: &" + getAvailibilityExpColor(p, costInExp) 
						+ costInExp + " EXP",
						" &5&l* &fDwarf Dust: &" + getAvailibilityDustColor(p, costInDust) + costInDust + "g", "", "&cCannot afford the repair!");
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
		if(ExperienceManager.getTotalExperience(p) >= amount) {
			return 'a';
		}
		else {
			return 'c';
		}
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

package net.colonymc.colonyvikingcore.inventories;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.colonymc.colonyvikingcore.items.ItemChecker;
import net.colonymc.colonyvikingcore.items.SpecialItem;

public class RepairListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null) {
			if(e.getInventory().getHolder() instanceof RepairInventory) {
				if(e.getWhoClicked() instanceof Player) {
					Player p = (Player) e.getWhoClicked();
					if(e.getClick() == ClickType.NUMBER_KEY) {
						e.setCancelled(true);
					}
					else {
						if(RepairInventory.repair.containsKey(p)) {
							RepairInventory inv = RepairInventory.repair.get(p);
							if(ItemChecker.isSpecialItem(e.getCurrentItem())) {
								e.setCancelled(true);
								SpecialItem item = SpecialItem.getByType(e.getCurrentItem(), p);
								if(item.hasDurability()) {
									if(inv.hasSelectedItem && e.getSlot() == inv.selectedItemSlot) {
										inv.deselectItem();
									}
									else if(!inv.hasSelectedItem) {
										if(item.getMaxDurability() > item.getDurability()) {
											inv.selectItem(e.getCurrentItem(), e.getSlot());
										}
										else {
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item isn't damaged!"));
											p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
										}
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item is not damageable!"));
									p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
								}
							}
							else if(e.getSlot() == inv.repairItemSlot && inv.hasSelectedItem) {
								if(inv.canRepair) {
									inv.repair();
								}
								else {
									p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot afford this repair order!"));
									e.setCancelled(true);
								}
							}
							else if(e.getSlot() == inv.durabilityGlasses[0] || e.getSlot() == inv.durabilityGlasses[1] || e.getSlot() == inv.durabilityGlasses[2] || 
									e.getSlot() == inv.durabilityGlasses[3] || e.getSlot() == inv.durabilityGlasses[4] && inv.hasSelectedItem) {
								e.setCancelled(true);
								switch(e.getSlot()) {
								case 41:
									if(inv.durability + 1 <= inv.selectedItem.getMaxDurability() - inv.selectedItem.getDurability()) {
										inv.durability = inv.durability + 1;
										p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cMax durability reached!"));
										p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
										inv.durability = (int) (inv.selectedItem.getMaxDurability() - inv.selectedItem.getDurability());
									}
									break;
								case 32:
									if(inv.durability + 10 <= inv.selectedItem.getMaxDurability() - inv.selectedItem.getDurability()) {
										inv.durability = inv.durability + 10;
										p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
									}
									else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cMax durability reached!"));
										p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
										inv.durability = (int) (inv.selectedItem.getMaxDurability() - inv.selectedItem.getDurability());
									}
									break;
								case 23:
									p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
									if(inv.getMaxAffordableDurability() == 0) {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot afford any repair!"));
										p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
									}
									else {
										p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
										inv.durability = inv.getMaxAffordableDurability();
									}
									break;
								case 30:
									p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
									if(inv.durability - 1 > 0) {
										inv.durability = inv.durability - 1;
									}
									else {
										inv.durability = 0;
									}
									break;
								case 21:
									p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
									if(inv.durability - 10 > 0) {
										inv.durability = inv.durability - 10;
									}
									else {
										inv.durability = 0;
									}
									break;
								}
								inv.checkDurabilityItem();
							}
							else {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory() != null) {
			if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&l&k:&5&lBrokkr&k:"))) {
				if(e.getPlayer() instanceof Player) {
					Player p = (Player) e.getPlayer();
					if(RepairInventory.repair.containsKey(p)) {
						RepairInventory inv = RepairInventory.repair.get(p);
						if(!inv.hasRepaired) {
							inv.closeInventory();
						}
					}
				}
			}
		}
	}

}

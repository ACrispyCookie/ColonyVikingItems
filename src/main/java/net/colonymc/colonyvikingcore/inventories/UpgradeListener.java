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

public class UpgradeListener implements Listener {
	

	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null) {
			if(e.getInventory().getHolder() instanceof UpgradeInventory) {
				if(e.getWhoClicked() instanceof Player) {
					Player p = (Player) e.getWhoClicked();
					if(e.getClick() == ClickType.NUMBER_KEY) {
						e.setCancelled(true);
					}
					else {
						if(UpgradeInventory.upgrade.containsKey(p)) {
							UpgradeInventory inv = UpgradeInventory.upgrade.get(p);
							if(ItemChecker.isSpecialItem(e.getCurrentItem())) {
								e.setCancelled(true);
								SpecialItem item = SpecialItem.getByType(e.getCurrentItem(), p);
								if(item.isUpgradeable()) {
									if(inv.hasSelectedItem && e.getSlot() == inv.selectedItemSlot) {
										inv.deselectItem();
									}
									else if(!inv.hasSelectedItem) {
										if(item.getLevel() < 10) {
											inv.selectItem(e.getCurrentItem(), e.getSlot());
										}
										else {
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item is already maxed out."));
											p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
										}
									}
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item is not upgradable!"));
									p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
								}
							}
							else if(e.getSlot() == inv.upgradeItemSlot) {
								if(inv.canUpgrade) {
									inv.upgradeSuccessful();
								}
								else {
									p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot afford this upgrade order!"));
									e.setCancelled(true);
								}
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
			if(e.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&5&l&k:&5&lDurnir&k:"))) {
				if(e.getPlayer() instanceof Player) {
					Player p = (Player) e.getPlayer();
					if(UpgradeInventory.upgrade.containsKey(p)) {
						UpgradeInventory inv = UpgradeInventory.upgrade.get(p);
						if(!inv.hasUpgraded) {
							inv.closeInventory();
						}
					}
				}
			}
		}
	}

}

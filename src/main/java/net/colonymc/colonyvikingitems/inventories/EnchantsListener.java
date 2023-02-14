package net.colonymc.colonyvikingitems.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnchantsListener implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null) {
			if(e.getInventory().getHolder() instanceof EnchantsInventory) {
				if(e.getWhoClicked() instanceof Player) {
					if(e.getClick() == ClickType.NUMBER_KEY) {
						e.setCancelled(true);
					}
					else {
						if(e.getSlot() == 19) {
							
						}
						else if(e.getSlot() == 21) {
							
						}
						else if(e.getSlot() == 23) {
							
						}
						else if(e.getSlot() == 25) {
							
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

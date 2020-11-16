package net.colonymc.colonyvikingcore.items.odinsshield;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyvikingcore.items.ItemChecker;
import net.colonymc.colonyvikingcore.items.ItemType;
import net.colonymc.colonyvikingcore.items.gungnir.GungnirHitEvent;

public class OdinsShieldListeners implements Listener {

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(ItemChecker.whatType(p.getItemInHand()) == ItemType.ODINS_SHIELD) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onHit(GungnirHitEvent e) {
		Player p = e.getPlayerHit();
		boolean contains = false;
		for(ItemStack i : p.getInventory().getContents()) {
			if(ItemChecker.whatType(i) == ItemType.ODINS_SHIELD) {
				contains = true;
				break;
			}
		}
		if(contains) {
			Random rand = new Random();
			if(rand.nextDouble() <= 0.3) {
				e.setCancelled(true);
				int slot = 0;
				for(int i = 0; i < p.getInventory().getSize(); i++) {
					ItemStack item = p.getInventory().getItem(i);
					if(ItemChecker.whatType(item) == ItemType.ODINS_SHIELD) {
						slot = i;
						break;
					}
				}
				ItemStack item = p.getInventory().getItem(slot);
				item.setAmount(item.getAmount() - 1);
				p.getInventory().setItem(slot, item);
				p.updateInventory();
				p.playSound(p.getLocation(), Sound.ENDERMAN_SCREAM, 2, 1);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYour shield repelled a spear and broke!"));
			}
		}
	}
	
}

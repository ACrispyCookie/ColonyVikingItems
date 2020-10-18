package net.colonymc.vikingcore.utils;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.colonymc.api.player.Particle;
import net.colonymc.vikingcore.items.ItemChecker;
import net.colonymc.vikingcore.items.SpecialItem;

public class UtilListeners implements Listener {
	
	HashMap<Player, Particle> animations = new HashMap<Player, Particle>();
	
	@EventHandler
	public void onPrepare(InventoryClickEvent e) {
		Player p = (Player) e.getView().getPlayer();
		if(e.getInventory().getType() == InventoryType.ANVIL) {
			if(e.getClick() == ClickType.NUMBER_KEY) {
				if(ItemChecker.isSpecialItem(p.getInventory().getItem(e.getHotbarButton()))) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou must go to the dwarves to upgrade or rename your special item!"));
				}
			}
			else {
				if(ItemChecker.isSpecialItem(e.getCurrentItem()) || ItemChecker.isSpecialItem(e.getCursor())) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou must go to the dwarves to upgrade or rename your special item!"));
				}
			}
		}
	}
	
	@EventHandler
	public void onHoldItem(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.isSpecialItem(p.getItemInHand())) {
			SpecialItem i = SpecialItem.getByType(p.getItemInHand(), p);
			if(i.isUpgradeable() && i.getLevel() == 10) {
				if(animations.containsKey(p)) {
					Particle particle = animations.get(p);
					Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
					particle.stop();
					particle.play(pl);
				}
				else {
					Collection<? extends Player> pl = Bukkit.getOnlinePlayers();
					Location finalLocation = getRightSide(p.getEyeLocation(), 0.45).subtract(0, .6, 0);
					Particle particle = new Particle(Effect.MAGIC_CRIT, 0, finalLocation);
					particle.play(pl);
					animations.put(p, particle);
				}
			}
		}
		else if(animations.containsKey(p)) {
			animations.get(p).stop();
			animations.remove(p);
		}
	}
	
	public Location getRightSide(Location location, double distance) {
	    float angle = location.getYaw() / 60;
	    return location.clone().subtract(new Vector(Math.cos(angle), 0, Math.sin(angle)).normalize().multiply(distance));
	}
	
}

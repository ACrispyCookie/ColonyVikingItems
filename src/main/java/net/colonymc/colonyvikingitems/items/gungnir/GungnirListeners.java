package net.colonymc.colonyvikingitems.items.gungnir;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.colonymc.colonyskyblockcore.guilds.Guild;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.colonymc.colonyspigotapi.api.player.PlayerInventory;
import net.colonymc.colonyvikingitems.items.ItemChecker;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;
import net.colonymc.colonyvikingitems.items.runnables.OdinsSpearRunnable;

public class GungnirListeners implements Listener {
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = p.getItemInHand();
			if(ItemChecker.whatType(item) == ItemType.GUNGNIR) {
				e.setCancelled(true);
				boolean isOutOfSpawn = true;
				for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
						.get(p.getWorld())
						.getApplicableRegions(p.getLocation())) {
					if(r.getId().equalsIgnoreCase("spawn")) {
						isOutOfSpawn = false;
					}
				}
				if(isOutOfSpawn) {
					if(!OdinsSpearRunnable.thrownSpears.containsKey(p)) {
						Entity closestEntity = null;
						if(!p.getNearbyEntities(40, 40, 40).isEmpty()) {
							for(Entity en : p.getNearbyEntities(40, 40, 40)) {
								if(!en.equals(p) && en instanceof LivingEntity && !(en instanceof ArmorStand)) {
									if(closestEntity == null || closestEntity.getLocation().distance(p.getLocation()) > en.getLocation().distance(p.getLocation())) {
										boolean isEntityOutOfSpawn = true;
										for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
												.get(p.getWorld())
												.getApplicableRegions(p.getLocation())) {
											if(r.getId().equalsIgnoreCase("spawn")) {
												isEntityOutOfSpawn = false;
											}
										}
										if(isEntityOutOfSpawn) {
											if(en instanceof Player) {
												if(!Guild.getByPlayer(p).getMemberUuids().containsValue(Guild.getByPlayer(p).getGuildPlayer((Player) en))) {
													closestEntity = en;
												}
											}
											else {
												closestEntity = en;
											}
										}
									}
								}
							}
							if(closestEntity != null) {
								Gungnir sitem = (Gungnir) SpecialItem.getByType(item.clone(), p);
								OdinsSpearRunnable runnable = new OdinsSpearRunnable(p, sitem, closestEntity);
								OdinsSpearRunnable.thrownSpears.put(p, runnable);
								item.setAmount(item.getAmount() - 1);
								p.setItemInHand(item);
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cNo enemies found nearby!"));
							}
						}
						else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cNo enemies found nearby!"));
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cWait for your other spear to come back!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot use this inside the spawn!"));
				}
			}
		}
	}
	
	@EventHandler
	public void onArmorStandChange(PlayerArmorStandManipulateEvent e) {
		Player p = e.getPlayer();
		if(OdinsSpearRunnable.thrownSpears.containsKey(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.whatType(p.getItemInHand()) == ItemType.GUNGNIR) {
				e.setCancelled(true);
		}
	}
	

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		Player p = e.getEntity().getKiller();
		if(OdinsSpearRunnable.thrownSpears.containsKey(p)) {
			PlayerInventory.addItems(e.getDrops(), p);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d+" + e.getDroppedExp() + " EXP"));
			p.giveExp(e.getDroppedExp());
			e.setDroppedExp(0);
			e.getDrops().clear();
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(OdinsSpearRunnable.thrownSpears.containsKey(p)) {
			OdinsSpearRunnable runnable = OdinsSpearRunnable.thrownSpears.get(p);
			runnable.as.remove();
			runnable.cancel();
			OdinsSpearRunnable.thrownSpears.remove(p);
			p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou lost your spear because you died!"));
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(OdinsSpearRunnable.thrownSpears.containsKey(p)) {
			OdinsSpearRunnable runnable = OdinsSpearRunnable.thrownSpears.get(p);
			runnable.as.remove();
			PlayerInventory.addItem(runnable.spear.getItemStack(), p, 1);
			runnable.cancel();
			OdinsSpearRunnable.thrownSpears.remove(p);
		}
	}

}

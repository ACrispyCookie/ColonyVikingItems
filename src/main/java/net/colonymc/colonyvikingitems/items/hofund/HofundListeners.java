package net.colonymc.colonyvikingitems.items.hofund;

import java.util.ArrayList;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.colonymc.colonyvikingitems.items.ItemChecker;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;
import net.colonymc.colonyvikingitems.items.runnables.HeimdallsSwordRunnable;

public class HofundListeners implements Listener {
	
	final ArrayList<Entity> closeEntities = new ArrayList<>();
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = p.getItemInHand();
			if(ItemChecker.whatType(item) == ItemType.HOFUND) {
				if(!HeimdallsSwordRunnable.supercharged.containsKey(p)) {
					boolean isOutOfSpawn = true;
					for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
							.get(p.getWorld())
							.getApplicableRegions(p.getLocation())) {
						if(r.getId().equalsIgnoreCase("spawn")) {
							isOutOfSpawn = false;
						}
					}
					if(isOutOfSpawn) {
						if(!HeimdallsSwordRunnable.supercharged.containsKey(p)) {
							if(!HeimdallsSwordRunnable.cooldowns.containsKey(p) || (HeimdallsSwordRunnable.cooldowns.containsKey(p) && HeimdallsSwordRunnable.cooldowns.get(p) < System.currentTimeMillis())) {
								if(!p.getNearbyEntities(15, 15, 15).isEmpty()) {
									for(Entity en : p.getNearbyEntities(15, 15, 15)) {
										if(en instanceof LivingEntity) {
											if(!en.equals(p)) {
												if(closeEntities.size() < 7) {
													if(!closeEntities.contains(en)) {
														closeEntities.add(en);
													}
												}
												else {
													break;
												}
											}
										}
									}
									new HeimdallsSwordRunnable(p, closeEntities, p.getItemInHand());
								}
								else {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cNo enemies found nearby!"));
								}
							}
							else {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease wait another " + 
										((HeimdallsSwordRunnable.cooldowns.get(p) - System.currentTimeMillis())/1000 + 1) + " seconds before supercharging your hofund!"));
							}
						}
					}
					else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot use this inside the spawn!"));
					}
				}
				else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cUse your other hofund or wait for it to get discharged!"));
				}
			}
		}
	}
	
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			ItemStack item = p.getItemInHand();
			if(ItemChecker.whatType(item) == ItemType.HOFUND) {
				boolean isOutOfSpawn = true;
				for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
						.get(p.getWorld())
						.getApplicableRegions(p.getLocation())) {
					if(r.getId().equalsIgnoreCase("spawn")) {
						isOutOfSpawn = false;
					}
				}
				if(isOutOfSpawn) {
					if(HeimdallsSwordRunnable.supercharged.containsKey(p)) {
						HeimdallsSwordRunnable runnable = HeimdallsSwordRunnable.supercharged.get(p);
						SpecialItem sitem = SpecialItem.getByType(item, p);
						sitem.removeDurability(1);
						sitem.setDamage(runnable.finalDamage);
						p.setItemInHand(sitem.getItemStack());
						e.setDamage(sitem.getFinalDamage());
						e.getEntity().getWorld().strikeLightning(e.getEntity().getLocation());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou used the supercharged and you dealt &d" + 
						(int) runnable.finalDamage + " &fdamage!"));
						runnable.cancel();
						HeimdallsSwordRunnable.supercharged.remove(p);
						HeimdallsSwordRunnable.cooldowns.put(p, System.currentTimeMillis() + 60000);
						ItemStack i = p.getInventory().getItem(runnable.slot);
						i.setType(Material.GOLD_SWORD);
						p.getInventory().setItem(runnable.slot, i);
					}
					else {
						SpecialItem sitem = SpecialItem.getByType(item, p);
						sitem.removeDurability(1);
						e.setDamage(sitem.getFinalDamage());
						p.setItemInHand(sitem.getItemStack());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClickItem(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getClickedInventory().getHolder() != null && e.getClickedInventory().getHolder() instanceof Player) {
				Player p = (Player) e.getClickedInventory().getHolder();
				if(e.getClick() == ClickType.NUMBER_KEY) {
					if(HeimdallsSwordRunnable.supercharged.containsKey(p) && (ItemChecker.whatType(p.getInventory().getItem(e.getHotbarButton())) == ItemType.HOFUND
							|| ItemChecker.whatType(e.getCurrentItem()) == ItemType.HOFUND)) {
						e.setCancelled(true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cUse your sword before moving it! (Or wait for it to discharge)"));
					}
				}
				else {
					if(HeimdallsSwordRunnable.supercharged.containsKey(p) && ItemChecker.whatType(e.getCurrentItem()) == ItemType.HOFUND) {
						e.setCancelled(true);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cUse your sword before moving it! (Or wait for it to discharge)"));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(HeimdallsSwordRunnable.supercharged.containsKey(p)) {
			HeimdallsSwordRunnable runnable = HeimdallsSwordRunnable.supercharged.get(p);
			runnable.toggleItemInHand(p);
			runnable.cancel();
			HeimdallsSwordRunnable.supercharged.remove(p);
			HeimdallsSwordRunnable.cooldowns.put(p, System.currentTimeMillis() + 60000);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(HeimdallsSwordRunnable.supercharged.containsKey(p)) {
			HeimdallsSwordRunnable runnable = HeimdallsSwordRunnable.supercharged.get(p);
			runnable.toggleItemInHand(p);
			runnable.cancel();
			HeimdallsSwordRunnable.supercharged.remove(p);
			HeimdallsSwordRunnable.cooldowns.put(p, System.currentTimeMillis() + 60000);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(HeimdallsSwordRunnable.supercharged.containsKey(e.getPlayer())) {
			if(ItemChecker.whatType(e.getItemDrop().getItemStack()) == ItemType.HOFUND) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot drop your Hofund while it's supercharged!"));
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(e.getCause() == DamageCause.LIGHTNING) {
				if(ItemChecker.whatType(p.getItemInHand()) == ItemType.HOFUND) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.whatType(p.getItemInHand()) == ItemType.HOFUND) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e) {
		if(ItemChecker.whatType(e.getPlayer().getItemInHand()) == ItemType.HOFUND) {
			e.getPlayer().getInventory().setItemInHand(e.getPlayer().getInventory().getItemInHand());
			e.getPlayer().updateInventory();
			e.setCancelled(true);
		}
	}
	
}

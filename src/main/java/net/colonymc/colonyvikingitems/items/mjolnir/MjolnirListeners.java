package net.colonymc.colonyvikingitems.items.mjolnir;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.colonymc.colonyvikingitems.items.ItemChecker;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;

public class MjolnirListeners implements Listener {
	
	@EventHandler
	public void onDurabilityLoss(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof LivingEntity) {
			Player p = (Player) e.getDamager();
			LivingEntity en = (LivingEntity) e.getEntity();
			ItemStack item = p.getItemInHand();
			if(ItemChecker.whatType(item) == ItemType.MJOLNIR) {
				boolean isOutOfSpawn = true;
				for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
						.get(p.getWorld())
						.getApplicableRegions(p.getLocation())) {
					if(r.getId().equalsIgnoreCase("spawn")) {
						isOutOfSpawn = false;
					}
				}
				if(isOutOfSpawn) {
					SpecialItem sitem = SpecialItem.getByType(item, p);
					sitem.removeDurability(1);
					p.setItemInHand(sitem.getItemStack());
					e.setDamage(sitem.getFinalDamage());
					en.setFireTicks(100);
					p.getWorld().strikeLightningEffect(e.getEntity().getLocation());
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(e.getCause() == DamageCause.LIGHTNING) {
				if(ItemChecker.whatType(p.getItemInHand()) == ItemType.MJOLNIR) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.whatType(p.getItemInHand()) == ItemType.MJOLNIR) {
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e) {
		if(ItemChecker.whatType(e.getPlayer().getItemInHand()) == ItemType.MJOLNIR) {
			e.getPlayer().getInventory().setItemInHand(e.getPlayer().getInventory().getItemInHand());
			e.getPlayer().updateInventory();
			e.setCancelled(true);
		}
	}

}

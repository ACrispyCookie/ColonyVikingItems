package net.colonymc.colonyvikingcore.items.bragispickaxe;

import java.util.ArrayList;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.colonymc.colonyspigotapi.api.player.PlayerInventory;
import net.colonymc.colonyvikingcore.items.ItemChecker;
import net.colonymc.colonyvikingcore.items.ItemEnchant;
import net.colonymc.colonyvikingcore.items.ItemType;
import net.colonymc.colonyvikingcore.items.SpecialItem;
import net.colonymc.colonyvikingcore.items.hoenirshopper.HoenirsHopperListeners;

public class BragisPickaxeListeners implements Listener {

	@EventHandler
	public void onClick(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.isSpecialItem(p.getItemInHand()) && ItemChecker.whatType(p.getItemInHand()) == ItemType.BRAGIS_PICKAXE) {
			SpecialItem pick = SpecialItem.getByType(p.getItemInHand(), p);
			p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2, 1);
			ArrayList<Block> blocks = new ArrayList<>();
			int blocksBroken = 0;
			for(int i = -1; i < 2; i++) {
				blocks.add(e.getBlock().getLocation().add(1, i, -1).getBlock());
				blocks.add(e.getBlock().getLocation().add(1, i, 0).getBlock());
				blocks.add(e.getBlock().getLocation().add(1, i, 1).getBlock());
				blocks.add(e.getBlock().getLocation().add(0, i, -1).getBlock());
				blocks.add(e.getBlock().getLocation().add(0, i, 0).getBlock());
				blocks.add(e.getBlock().getLocation().add(0, i, 1).getBlock());
				blocks.add(e.getBlock().getLocation().add(-1, i, -1).getBlock());
				blocks.add(e.getBlock().getLocation().add(-1, i, 0).getBlock());
				blocks.add(e.getBlock().getLocation().add(-1, i, 1).getBlock());
			}
			for(Block block : blocks) {
				if(block.getType() != Material.BEDROCK) {
					boolean canBreak = true;
					for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer().get(p.getWorld()).getApplicableRegions(block.getLocation())) {
						if(r.getFlag(DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY) {
							canBreak = false;
							break;
						}
					}
					if(canBreak) {
						blocksBroken++;
						if(!pick.getEnchants().containsKey(ItemEnchant.TELEKINESIS)) {
							if(HoenirsHopperListeners.hoppers.containsKey(block)) {
								HoenirsHopperListeners.hoppers.get(block).endJob(p);
							}
							else {
								block.breakNaturally();
							}
						}
						else {
							if(HoenirsHopperListeners.hoppers.containsKey(block)) {
								HoenirsHopperListeners.hoppers.get(block).endJob(p);
							}
							else {
								PlayerInventory.addItems(block.getDrops(), p);
								block.setType(Material.AIR);
							}
						}
					}
				}
			}
			if(blocksBroken > 0) {
				pick.removeDurability(1);
				p.setItemInHand(pick.getItemStack());
			}
		}
	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.isSpecialItem(p.getItemInHand()) && ItemChecker.whatType(p.getItemInHand()) == ItemType.BRAGIS_PICKAXE) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}

}

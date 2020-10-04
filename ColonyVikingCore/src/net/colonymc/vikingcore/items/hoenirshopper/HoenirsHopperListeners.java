package net.colonymc.vikingcore.items.hoenirshopper;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import net.colonymc.vikingcore.Main;
import net.colonymc.vikingcore.items.ItemChecker;
import net.colonymc.vikingcore.items.ItemType;

public class HoenirsHopperListeners implements Listener {
	
	public static HashMap<Block, HoenirsHopperBlock> hoppers = new HashMap<Block, HoenirsHopperBlock>();
	
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.isSpecialItem(e.getItemInHand()) && ItemChecker.whatType(e.getItemInHand()) == ItemType.HOENIRS_HOPPER) {
			FileConfiguration conf = Main.getInstance().getDataFileConf();
			conf.set("hoenirsHoppers." + e.getBlock().getWorld().getName() + "," + e.getBlock().getX() + "," + e.getBlock().getY() + "," + e.getBlock().getZ(), 0);
			Main.getInstance().saveDataFile(conf);
			HoenirsHopperBlock block  = new HoenirsHopperBlock(e.getBlock(), 0);
			hoppers.put(e.getBlock(),  block);
			block.startJob();
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have placed a &bHoenir's Hopper&f!"));
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.HOPPER) {
			if(hoppers.containsKey(e.getBlock())) {
				e.setCancelled(true);
				hoppers.get(e.getBlock()).endJob(e.getPlayer());
			}
		}
	}

}

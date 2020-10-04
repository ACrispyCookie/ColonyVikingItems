package net.colonymc.vikingcore.items.hoenirshopper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.colonymc.vikingcore.Main;
import net.colonymc.vikingcore.items.ItemType;
import net.colonymc.vikingcore.items.SpecialItem;

public class HoenirsHopperBlock {
	
	Hopper hopper;
	Block block;
	int collected;
	boolean firstTime = true;
	ArmorStand line1;
	ArmorStand line2;
	ArmorStand line3;
	
	public HoenirsHopperBlock(Block block, int collected) {
		this.collected = collected;
		this.block = block;
		if(block.getState() instanceof Hopper) {
			hopper = (Hopper) block.getState();
		}
	}
	
	public void startJob(){
		if(firstTime) {
			runnable.runTaskTimer(Main.getInstance(), 0, 60);
		}
	}
	
	public void endJob(Player p) {
		if(p != null) {
			FileConfiguration conf = Main.getInstance().getDataFileConf();
			conf.set("hoenirsHoppers." + hopper.getBlock().getWorld().getName() + "," + hopper.getBlock().getX() + "," + hopper.getBlock().getY() + "," + hopper.getBlock().getZ(), null);
			Main.getInstance().saveDataFile(conf);
			hopper.getBlock().setType(Material.AIR);
			hopper.getBlock().getWorld().dropItem(hopper.getBlock().getLocation(), SpecialItem.getByType(ItemType.HOENIRS_HOPPER, 0, 0, 0, p).getItemStack());
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have broken a &bHoenir's Hopper&f!"));
			HoenirsHopperListeners.hoppers.remove(block);
		}
		runnable.cancel();
		line1.remove();
		line2.remove();
		line3.remove();
	}
	
	BukkitRunnable runnable = new BukkitRunnable() {
		@Override
		public void run() {
			if(firstTime) {
				line1 = (ArmorStand) block.getWorld().spawnEntity(new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 0.5, block.getZ() + 0.5), EntityType.ARMOR_STAND);
				line2 = (ArmorStand) block.getWorld().spawnEntity(new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 0.25, block.getZ() + 0.5), EntityType.ARMOR_STAND);
				line3 = (ArmorStand) block.getWorld().spawnEntity(new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 0.75, block.getZ() + 0.5), EntityType.ARMOR_STAND);
				line1.setSmall(true);
				line1.setVisible(false);
				line1.setGravity(false);
				line1.setCustomNameVisible(true);
				line2.setSmall(true);
				line2.setVisible(false);
				line2.setGravity(false);
				line2.setCustomNameVisible(true);
				line3.setSmall(true);
				line3.setVisible(false);
				line3.setGravity(false);
				line1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&dStatus: &aRunning"));
				line2.setCustomName(ChatColor.translateAlternateColorCodes('&', "&dTotal Collected: &f" + collected));
				line3.setCustomName(ChatColor.translateAlternateColorCodes('&', "&cThe hopper is full!"));
				firstTime = false;
			}
			else {
				if(!isFull()) {
					for(Entity e : block.getChunk().getEntities()) {
						if(e.getType() == EntityType.DROPPED_ITEM) {
							if(!isFull()) {
								hopper.getInventory().addItem(((Item) e).getItemStack());
								hopper.update();
								collected = collected + ((Item) e).getItemStack().getAmount();
								e.remove();
							}
						}
					}
					line2.setCustomName(ChatColor.translateAlternateColorCodes('&', "&dTotal Collected: &f" + collected));
				}
			}
		}
	};
	
	private boolean isFull() {
		for(ItemStack i : hopper.getInventory().getContents()) {
			if(i == null) {
				line1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&dStatus: &aRunning"));
				line3.setCustomNameVisible(false);
				return false;
			}
		}
		line1.setCustomName(ChatColor.translateAlternateColorCodes('&', "&dStatus: &cPaused"));
		line3.setCustomNameVisible(true);
		return true;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public Location getLocation() {
		return block.getLocation();
	}
	
	public int getTotal() {
		return collected;
	}
	
}

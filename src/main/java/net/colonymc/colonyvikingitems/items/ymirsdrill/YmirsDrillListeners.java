package net.colonymc.colonyvikingitems.items.ymirsdrill;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.colonymc.colonyvikingitems.Main;
import net.colonymc.colonyvikingitems.items.ItemChecker;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class YmirsDrillListeners implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(ItemChecker.isSpecialItem(e.getItemInHand()) && ItemChecker.whatType(e.getItemInHand()) == ItemType.YMIRS_DRILL) {
			boolean canBreak = true;
			int bx = e.getBlock().getChunk().getX() << 4;
			int bz = e.getBlock().getChunk().getZ() << 4;
			BlockVector bv = new BlockVector(bx, 1, bz);
			BlockVector bv1 = new BlockVector(bx + 15, 1, bz + 15);
			ProtectedCuboidRegion region = new ProtectedCuboidRegion("id", bv, bv1);
			RegionManager manager = WGBukkit.getPlugin().getRegionContainer().get(e.getPlayer().getWorld());
			for(ProtectedRegion r : manager.getApplicableRegions(region)) {
				if(r.getFlag(DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY) {
					canBreak = false;
					break;
				}
			}
			if(canBreak) {
				Player p = e.getPlayer();
				e.setCancelled(true);
				ItemStack item = p.getItemInHand();
				item.setAmount(item.getAmount() - 1);
				p.setItemInHand(item);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &b&l&k:&b&lDRILL USED&k: &fYou have used your &bYmir's Drill&f!"));
				p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 2, 1);
				startRunnable(p, e.getBlock().getChunk());
			}
			else {
				e.setCancelled(true);
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENDERMAN_TELEPORT, 2, 1);
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYou cannot place this here!"));
			}
		}
	}
	
	
	public void startRunnable(Player p, Chunk c) {
		BukkitRunnable animatedText = getAnimatedBar(p, "&5&l» &fYour &b&l&k:&b&lYMIR'S DRILL&k:&r &fhas started!", "&5&l» &fYour &f&l&k:&f&lYMIR'S DRILL&k:&r &fhas started!");
		BukkitRunnable breakBlocks = new BukkitRunnable() {
			int y = 0;
			@Override
			public void run() {
				int x = c.getX() << 4;
				int z = c.getZ() << 4;
				for(int xx = x; xx < x + 16; xx++) {
					for(int zz = z; zz < z + 16; zz++) {
						Block block = c.getBlock(xx, y, zz);
						if(block.getType() != Material.BEDROCK) {
							block.breakNaturally(new ItemStack(Material.AIR));
						}
					}
				}
				if(y == 256) {
					animatedText.cancel();
					this.cancel();
					sendActionBar(p, "&5&l» &fYour &b&l&k:&b&lYMIR'S DRILL&k:&r &fhas finished its work!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 1);
				}
				y++;
			}
		};
		BukkitRunnable countDown = new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if(i == 10) {
					this.cancel();
					animatedText.runTaskTimer(Main.getInstance(), 0, 10);
					breakBlocks.runTaskTimer(Main.getInstance(), 0, 1L);
					p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 2, 1);
				}
				else {
					sendActionBar(p, "&5&l» &fYour &bYmir's Drill &fstarts at &d" + (10-i) + " &fseconds!");
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2, 2);
					i++;
				}
			}
		};
		countDown.runTaskTimer(Main.getInstance(), 0, 20L);
	}
	
	private void sendActionBar(Player p, String text) {
		CraftPlayer cp = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', text) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cp.getHandle().playerConnection.sendPacket(ppoc);
	}
	
	private BukkitRunnable getAnimatedBar(Player p, String text, String newText) {
		return new BukkitRunnable() {
			boolean value = true;
			@Override
			public void run() {
				if(value) {
					sendActionBar(p, text);
				}
				else {
					sendActionBar(p, newText);
				}
				value = !value;
			}
		};
	}

}

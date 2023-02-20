package net.colonymc.colonyvikingitems.items.joroshoe;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotlib.lib.player.PlayerInventory;
import net.colonymc.colonyvikingitems.items.ItemChecker;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class JorosHoeListeners implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(e.getBlock().getType() == Material.CROPS || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.POTATO) {
			if(e.getBlock().getData() == 7 || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
				if(ItemChecker.isSpecialItem(p.getItemInHand()) && ItemChecker.whatType(p.getItemInHand()) == ItemType.JOROS_HOE) {
					e.setCancelled(true);
					JorosHoe hoe = new JorosHoe(p.getItemInHand(), p);
					plantCrop(p, e.getBlock(), hoe, e);
					hoe.removeDurability(1);
					p.setItemInHand(hoe.getItemStack());
				}
			}
			else if(ItemChecker.isSpecialItem(p.getItemInHand()) && ItemChecker.whatType(p.getItemInHand()) == ItemType.JOROS_HOE) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(ItemChecker.isSpecialItem(e.getItem()) && ItemChecker.whatType(e.getItem()) == ItemType.JOROS_HOE && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock() == null || (e.getClickedBlock().getType() != Material.GRASS && e.getClickedBlock().getType() != Material.DIRT)) {
				JorosHoe hoe = new JorosHoe(e.getItem(), e.getPlayer()) {};
				hoe.setMode(-hoe.getMode());
				e.getPlayer().setItemInHand(hoe.getItemStack());
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHICKEN_EGG_POP, 2, 1);
			}
		}
	}
	
	@EventHandler
	public void onItemDamage(PlayerItemDamageEvent e) {
		Player p = e.getPlayer();
		if(ItemChecker.isSpecialItem(p.getItemInHand()) && ItemChecker.whatType(p.getItemInHand()) == ItemType.JOROS_HOE) {
			e.setCancelled(true);
			p.updateInventory();
		}
	}
	
	public void plantCrop(Player p, Block b, JorosHoe hoe, BlockBreakEvent e) {
		Random rand = new Random();
		p.playSound(p.getLocation(), Sound.ORB_PICKUP, 2, 2);
		Material mat = b.getType();
		if(mat == Material.CROPS) {
			int amountOfWheat = rand.nextInt(3) + 1;
			int amountOfSeeds = rand.nextInt(3);
			if(hoe.getMode() == -1) {
				ArrayList<ItemStack> items = new ArrayList<>();
				items.add(new ItemStack(Material.WHEAT, amountOfWheat));
				items.add(new ItemStack(Material.SEEDS, amountOfSeeds));
				PlayerInventory.addItems(items, p);
				String message = "&5&l» &bJoro's Hoe &f- &a+" + amountOfWheat + " ";
				if(amountOfWheat == 1) {
					message = message + "wheat, +";
				}
				else {
					message = message + "wheats, +";
				}
				if(amountOfSeeds == 1) {
					message = message + amountOfSeeds + " seed";
				}
				else {
					message = message + amountOfSeeds + " seeds";
				}
				sendActionBar(p, message);
			}
			else if(hoe.getMode() == 1) {
				sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+100g silver");
			}
		}
		else if(mat == Material.CARROT) {
			int amount = rand.nextInt(3) + 1;
			if(hoe.getMode() == -1) {
				PlayerInventory.addItem(new ItemStack(Material.CARROT_ITEM), p, amount);
				if(amount == 1) {
					sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+" + amount + " carrot");
				}
				else {
					sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+" + amount + " carrots");
				}
			}
			else if(hoe.getMode() == 1) {
				sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+100g silver");
			}
		}
		else if(mat == Material.POTATO) {
			int amount = rand.nextInt(3) + 1;
			if(hoe.getMode() == -1) {
				PlayerInventory.addItem(new ItemStack(Material.CARROT_ITEM), p, amount);
				if(amount == 1) {
					sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+" + amount + " potato");
				}
				else {
					sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+" + amount + " potatos");
				}
			}
			else if(hoe.getMode() == 1) {
				sendActionBar(p, "&5&l» &bJoro's Hoe &f- &a+100g silver");
			}
		}
		b.setType(mat);
	}
	
	private void sendActionBar(Player p, String text) {
		CraftPlayer cp = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', text) + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cp.getHandle().playerConnection.sendPacket(ppoc);
	}

}

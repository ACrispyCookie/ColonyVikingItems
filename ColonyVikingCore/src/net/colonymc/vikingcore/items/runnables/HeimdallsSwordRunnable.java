package net.colonymc.vikingcore.items.runnables;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.colonymc.vikingcore.Main;

public class HeimdallsSwordRunnable extends BukkitRunnable {
	
	public static HashMap<Player, HeimdallsSwordRunnable> supercharged = new HashMap<Player, HeimdallsSwordRunnable>();
	public static HashMap<Player, Long> cooldowns = new HashMap<Player, Long>();
	public double finalDamage = 0;
	public int slot = 0;
	public ItemStack item = null;
	ArrayList<Entity> entities = null;
	Player p = null;
	int i = 0;
	
	public HeimdallsSwordRunnable(Player p, ArrayList<Entity> entities, ItemStack item) {
		this.item = item;
		this.entities = entities;
		this.p = p;
		this.slot = p.getInventory().getHeldItemSlot();
		this.runTaskTimer(Main.getInstance(), 0L, 1L);
	}
	
	public void run() {
		if(i == 0) {
			i++;
			toggleItemInHand(p);
			supercharged.put(p, this);
			for(Entity e : entities) {
				LivingEntity en = (LivingEntity) e;
				finalDamage = finalDamage + (en.getHealth() * 0.2);
				en.damage(en.getHealth() * 0.2);
				en.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou lost &d20% &fof your health from &d" + p.getName() + "'s &fHofund!"));
			}
			finalDamage = 9 + finalDamage;
		}
		else if(i == 200) {
			i++;
			toggleItemInHand(p);
			supercharged.remove(p);
			cooldowns.put(p, System.currentTimeMillis() + 60000);
			cancel();
		}
		else {
			i++;
		}
	}
	
	public void toggleItemInHand(Player p) {
		if(!supercharged.containsKey(p)) {
			ItemStack i = p.getItemInHand();
			i.setType(Material.DIAMOND_SWORD);
			p.setItemInHand(i);
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.FIZZ, 2, 1);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have supercharged your &dHofund&f!"));
		}
		else {
			if(slot == -1) {
				ItemStack i = p.getItemOnCursor();
				i.setType(Material.GOLD_SWORD);
				p.setItemOnCursor(i);
			}
			else {
				ItemStack i = p.getInventory().getItem(slot);
				i.setType(Material.GOLD_SWORD);
				p.getInventory().setItem(slot, i);
			}
			p.updateInventory();
			p.playSound(p.getLocation(), Sound.CAT_MEOW, 2, 0.5f);
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cYour Hofund has been discharged!"));
		}
	}
}

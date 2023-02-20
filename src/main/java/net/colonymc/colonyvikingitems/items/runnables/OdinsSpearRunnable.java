package net.colonymc.colonyvikingitems.items.runnables;

import java.util.HashMap;

import com.sk89q.worldguard.bukkit.WGBukkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.colonymc.colonyspigotlib.lib.particles.Particle;
import net.colonymc.colonyspigotlib.lib.player.PlayerInventory;
import net.colonymc.colonyvikingitems.Main;
import net.colonymc.colonyvikingitems.items.gungnir.Gungnir;
import net.colonymc.colonyvikingitems.items.gungnir.GungnirHitEvent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class OdinsSpearRunnable extends BukkitRunnable {
	
	public static final HashMap<Player, OdinsSpearRunnable> thrownSpears = new HashMap<>();
	
	public final Player p;
	public final Gungnir spear;
	public CraftArmorStand as = null;
	Particle particle;
	Entity closestEntity = null;
	public int i = 0;
	int ticksOnAir = 0;
	
	public OdinsSpearRunnable(Player p, Gungnir item, Entity closest) {
		spear = item;
		closestEntity = closest;
		this.p = p;
		this.runTaskTimer(Main.getInstance(), 0L, 1L);
	}
	
	@Override
	public void run() {
		if(as == null) {
			spawnArmorStand();
		}
		else if(as.getLocation().distance(closestEntity.getLocation()) < 1.5 || as.isDead()) {
			if(i == 0) {
				if(closestEntity instanceof Player) {
					hitPlayer(p, (Player) closestEntity);
				}
				else {
					hitEntity(p, closestEntity);
				}
			}
			if(spear.getDurability() > 0) {
				doCountdownForReturn();
			}
			else {
				thrownSpears.remove(p);
				cancel();
			}
		}
		else {
			boolean isOutOfSpawn = true;
			for(ProtectedRegion r : WGBukkit.getPlugin().getRegionContainer()
					.get(closestEntity.getWorld())
					.getApplicableRegions(closestEntity.getLocation())) {
				if(r.getId().equalsIgnoreCase("spawn")) {
					isOutOfSpawn = false;
				}
			}
			if(ticksOnAir == 1200) {
				returnSpear(p, " &5&l» &cYour spear lost its energy and came back!");
			}
			if(closestEntity.isDead()) {
				returnSpear(p, " &5&l» &fYour target died and your spear came back!");
			}
			else if(closestEntity.getLocation().distance(as.getLocation()) > 70 || !isOutOfSpawn) {
				returnSpear(p, " &5&l» &cYou target got too far away!");
			}
			else {
				setVelocity();
			}
		}
	}
	
	public void spawnArmorStand() {
		p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 2, 1);
		Entity arrentity = p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
		if(arrentity instanceof ArmorStand) {
			as = (CraftArmorStand) arrentity;
			as.setVisible(false);
			as.setArms(true);
			as.setSmall(true);
			as.getHandle().noclip = true;
			as.setItemInHand(new ItemStack(Material.BLAZE_ROD));
			as.teleport(p.getEyeLocation().subtract(0, 0.6, 0));
		}
	}
	
	public void setVelocity() {
		Location loc = as.getLocation();
		particle = new Particle(Effect.MOBSPAWNER_FLAMES, 0, new Location(loc.getWorld(), loc.getX() + 0.2f, loc.getY() + 0.6f, loc.getZ() + 0.2f));
		particle.play(Bukkit.getOnlinePlayers(), 1);
		Vector to = closestEntity.getLocation().add(new Location(closestEntity.getWorld(), 0, 0.5f, 0)).subtract(as.getLocation()).toVector().normalize();
		as.setVelocity(to.multiply(spear.getSpeed()/100));
		ticksOnAir++;
	}
	
	public void doCountdownForReturn() {
		i++;
		int ticksToReturn = (int) ((double) spear.getReturnTime() * 20);
		CraftPlayer cp = (CraftPlayer) p;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"§5§l» §fYour spear comes back in §d" + (((ticksToReturn-i)/20) + 1) + " §fseconds!\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
        cp.getHandle().playerConnection.sendPacket(ppoc);
		if(i == ticksToReturn) {
	        cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"§5§l» §fYour spear has returned!\"}");
	        ppoc = new PacketPlayOutChat(cbc, (byte) 2);
	        cp.getHandle().playerConnection.sendPacket(ppoc);
			thrownSpears.remove(p);
			PlayerInventory.addItem(spear.getItemStack(), p, 1);
			cancel();
		}
	}
	
	public void returnSpear(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		thrownSpears.remove(p);
		as.remove();
		spear.removeDurability(1);
		PlayerInventory.addItem(spear.getItemStack(), p, 1);
		cancel();
	}
	
	public void hitEntity(Player p, Entity e) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou hit a &d" + closestEntity.getName() + " &fwith your spear!"));
		((Damageable) closestEntity).damage(spear.getFinalDamage(), p);
		as.getWorld().createExplosion(as.getLocation(), 2);
		as.remove();
		p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 2, 1);
		particle.stop();
	}
	
	public void hitPlayer(Player p, Player target) {
		particle.stop();
		GungnirHitEvent event = new GungnirHitEvent(p, spear, target);
		Bukkit.getPluginManager().callEvent(event);
		if(!event.isCancelled()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou hit &d" + closestEntity.getName() + " &fwith your spear!"));
			((Damageable) closestEntity).damage(spear.getFinalDamage(), p);
			as.getWorld().createExplosion(as.getLocation(), 2);
			as.remove();
			p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 2, 1);
		}
		else {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThe shield of " + closestEntity.getName() + " worked and they repelled your spear"));
			as.remove();
			p.playSound(p.getLocation(), Sound.NOTE_BASS, 2, 1);
		}
	}

}

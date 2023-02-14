package net.colonymc.colonyvikingitems.voth;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.colonymc.colonyvikingitems.Main;

public class VothEvent {
	
	boolean isStarted = false;
	boolean onlyOneCapper = false;
	Location currentLocationStart = null;
	Location currentLocationEnd = null;
	String timeLeft = "";
	int timeLeftInTicks = 12000;
	Player currentCapper = null;
	
	public void startKoTH() {
		isStarted = true;
		currentLocationStart = new Location(Bukkit.getWorld("world"), -13, 0, 32);
		currentLocationEnd = new Location(Bukkit.getWorld("world"), -5, 10, 24);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThe &dVoTH &fhas started!"));
		countdown.runTaskTimer(Main.getInstance(), 0, 1L);
		checkForPlayers();
	}
	
	public void endKoTH(Player capper) {
		if(capper != null) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + currentCapper.getName() + " &fhas won VoTH!"));
			currentCapper.playSound(currentCapper.getLocation(), Sound.LEVEL_UP, 2, 1);
			currentCapper.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d&l&k:&d&lCONGRATULATIONS!&k: &fYou have suceessfully captured the &dVoTH&f!"));
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "vikingitem Gungnir " + currentCapper.getName() + " 1 100");
		}
		else {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fThe &dVoTH &fhas ended with no winner!"));
		}
		countdown.cancel();
		VothEvent newEvent = new VothEvent();
		VothCommand.currentEvent = newEvent;
		VothListeners.currentEvent = newEvent;
	}
	
	public boolean isKothStarted() {
		return isStarted;
	}
	
	public boolean isInKoth(Player p) {
		if(isStarted) {
			return (p.getLocation().getX() <= Math.max(currentLocationStart.getX(), currentLocationEnd.getX())
					&& p.getLocation().getX() >= Math.min(currentLocationStart.getX(), currentLocationEnd.getX()))
					&& (p.getLocation().getY() <= Math.max(currentLocationStart.getY(), currentLocationEnd.getY())
					&& p.getLocation().getY() >= Math.min(currentLocationStart.getY(), currentLocationEnd.getY()))
					&& (p.getLocation().getZ() <= Math.max(currentLocationStart.getZ(), currentLocationEnd.getZ())
					&& p.getLocation().getZ() >= Math.min(currentLocationStart.getZ(), currentLocationEnd.getZ()));
		}
		return false;
	}
	
	public boolean isClose(Player p) {
		return p.getLocation().distance(currentLocationStart) < 20 || p.getLocation().distance(currentLocationEnd) < 20;
	}
	
	public void checkForPlayers() {
		Player capper = null;
		int count = 0;
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(isInKoth(p)) {
				capper = p;
				count++;
			}
		}
		if(count == 1) {
			onlyOneCapper = true;
			currentCapper = capper;
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + currentCapper.getName() + " &fhas started capping the VoTH!"));
		}
	}
	
	final BukkitRunnable countdown = new BukkitRunnable() {
		int i = 0;
		int ii = 0;
		final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		@Override
		public void run() {
			i++;
			if(currentCapper != null) {
				ii++;
				timeLeft = sdf.format(new Date(301000 - (ii * 50)));
				if(ii == 3600) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d2 minutes &fremaining on the &dVoTH &funtil &d" + currentCapper.getName() + " &fcaptures it!"));
				}
				if(ii == 6000) {
					endKoTH(currentCapper);
					this.cancel();
				}
			}
			else {
				ii = 0;
				timeLeft = "05:00";
				if(i == 12000) {
					endKoTH(null);
					this.cancel();
				}
			}
		}
	};
	
	public String getCurrentCapper() {
		if(currentCapper == null) {
			return "None";
		}
		return currentCapper.getName();
	}
	
	public String getTimeLeft() {
		return timeLeft;
	}
}

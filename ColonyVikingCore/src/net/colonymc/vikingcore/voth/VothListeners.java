package net.colonymc.vikingcore.voth;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VothListeners implements Listener {
	
	static VothEvent currentEvent = VothCommand.currentEvent;

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(currentEvent.currentCapper == e.getPlayer() && !currentEvent.isInKoth(currentEvent.currentCapper)) {
			currentEvent.currentCapper = null;
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + e.getPlayer().getName() + " &fhas stopped capping the VoTH!"));
		}
		else if(currentEvent.isInKoth(e.getPlayer()) && currentEvent.currentCapper == null) {
			currentEvent.currentCapper = e.getPlayer();
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &d" + e.getPlayer().getName() + " &fhas started capping the VoTH!"));
		}
	}

}

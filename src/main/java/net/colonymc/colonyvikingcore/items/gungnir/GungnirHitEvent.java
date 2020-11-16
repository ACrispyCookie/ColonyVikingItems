package net.colonymc.colonyvikingcore.items.gungnir;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GungnirHitEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private final Player p;
	private final Player playerHit;
	private final Gungnir g;
	private boolean cancelled = false;
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
	
	public GungnirHitEvent(Player p, Gungnir item, Player hit) {
		this.p = p;
		this.g = item;
		this.playerHit = hit;
	}
	
	public Player getThrower() {
		return p;
	}
	
	public Player getPlayerHit() {
		return playerHit;
	}
	
	public Gungnir getGungnir() {
		return g;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		cancelled = arg0;
		
	}
}

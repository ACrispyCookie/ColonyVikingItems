package net.colonymc.colonyvikingcore.items.bragispickaxe;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyvikingcore.items.ItemType;
import net.colonymc.colonyvikingcore.items.SpecialItem;
import net.minecraft.server.v1_8_R3.NBTBase;

public class BragisPickaxe extends SpecialItem {
	
	public BragisPickaxe(ItemType type, int durability, int maxDurability, int level, Player p) {
		super(type, durability, maxDurability, level, p);
	}
	
	public BragisPickaxe(ItemStack item, Player p) {
		super(item, p);
	}

	@Override
	protected String getOtherValues() {
		return null;
	}

	@Override
	protected void setOtherValues() {
		
	}

	@Override
	protected ArrayList<NBTBase> getOtherTags() {
		return null;
	}

	@Override
	protected boolean hasOtherValues() {
		return false;
	}

	@Override
	protected void applyChanges() {
		
	}

	@Override
	public String getUpgradeString(int level) {
		return null;
	}

}

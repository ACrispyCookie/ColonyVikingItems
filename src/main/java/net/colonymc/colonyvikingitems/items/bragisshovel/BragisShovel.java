package net.colonymc.colonyvikingitems.items.bragisshovel;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;
import net.minecraft.server.v1_8_R3.NBTBase;

public class BragisShovel extends SpecialItem {

	public BragisShovel(ItemStack i, Player p) {
		super(i, p);
	}
	
	public BragisShovel(ItemType type, int durability, int maxDurability, int level, Player p) {
		super(type, durability, maxDurability, level, p);
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

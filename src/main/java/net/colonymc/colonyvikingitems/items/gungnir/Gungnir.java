package net.colonymc.colonyvikingitems.items.gungnir;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotapi.api.itemstack.ItemStackNBT;
import net.colonymc.colonyvikingitems.items.ItemType;
import net.colonymc.colonyvikingitems.items.SpecialItem;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class Gungnir extends SpecialItem {

	double speed;
	double returnTime;
	
	public Gungnir(ItemStack i, Player p) {
		super(i, p);
		speed = ItemStackNBT.getDouble((NBTTagList) ItemStackNBT.getTag(i, "vikingAttributes"), attributeIndex);
		returnTime = ItemStackNBT.getDouble((NBTTagList) ItemStackNBT.getTag(i, "vikingAttributes"), attributeIndex + 1);
	}
	
	public Gungnir(ItemType type, int durability, int maxDurability, int level, Player p) {
		super(type, durability, maxDurability, level, p);
	}

	@Override
	protected String getOtherValues() {
		return "";
	}

	@Override
	protected void setOtherValues() {
		speed = 100;
		returnTime = 7;
	}

	@Override
	protected ArrayList<NBTBase> getOtherTags() {
		ArrayList<NBTBase> list = new ArrayList<>();
		list.add(new NBTTagDouble(speed));
		list.add(new NBTTagDouble(returnTime));
		return list;
	}

	@Override
	protected boolean hasOtherValues() {
		return true;
	}

	public double getSpeed() {
		return speed;
	}
	
	public double getReturnTime() {
		return returnTime;
	}

	@Override
	protected void applyChanges() {
		switch(level) {
		case 2:
			speed = 120;
			returnTime = 6.7;
			break;
		case 3:
			speed = 140;
			returnTime = 5.7;
			break;
		case 4:
			speed = 160;
			returnTime = 5.2;
			break;
		case 5:
			speed = 180;
			returnTime = 4.7;
			break;
		case 6:
			speed = 200;
			returnTime = 4.2;
			break;
		case 7:
			speed = 220;
			break;
		case 8:
			speed = 240;
			returnTime = 3.7;
			break;
		case 9:
			speed = 260;
			returnTime = 3.4;
			break;
		case 10:
			speed = 300;
			returnTime = 3;
			break;
		}
	}

	@Override
	public String getUpgradeString(int level) {
		String s = "";
		switch(level) {
		case 2:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-1 seconds return time");
			break;
		case 3:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.5 seconds return time");
			break;
		case 4:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.5 seconds return time");
			break;
		case 5:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.5 seconds return time");
			break;
		case 6:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.5 seconds return time");
			break;
		case 7:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed");
			break;
		case 8:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.3 seconds return time");
			break;
		case 9:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+20% speed\n&5» &d-0.3 seconds return time");
			break;
		case 10:
			s = ChatColor.translateAlternateColorCodes('&', "\n&5» &d+40% speed\n&5» &d-0.4 seconds return time");
			break;
		}
		return s;
	}
}

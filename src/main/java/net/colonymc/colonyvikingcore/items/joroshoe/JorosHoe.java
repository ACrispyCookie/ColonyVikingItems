package net.colonymc.colonyvikingcore.items.joroshoe;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotapi.api.itemstack.ItemStackNBT;
import net.colonymc.colonyvikingcore.items.ItemEnchant;
import net.colonymc.colonyvikingcore.items.ItemType;
import net.colonymc.colonyvikingcore.items.SpecialItem;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagList;

public class JorosHoe extends SpecialItem {

	int mode;
	
	public JorosHoe(ItemStack i, Player p) {
		super(i, p);
		mode = (int) ItemStackNBT.getDouble((NBTTagList) ItemStackNBT.getTag(i, "vikingAttributes"), attributeIndex);
	}
	
	public JorosHoe(ItemType type, int durability, int maxDurability, int level, Player p) {
		super(type, durability, maxDurability, level, p);
		addEnchant(ItemEnchant.TELEKINESIS, 1);
	}

	@Override
	public void setOtherValues() {
		mode = -1;
	}
	
	@Override
	protected String getOtherValues() {
		return "&5» &fMode: &d" + (mode == -1 ? "Teleport to inventory" : "Sell to shop") + "\n&5» &fRight-click to toggle it!\n\n";
	}

	@Override
	protected ArrayList<NBTBase> getOtherTags() {
		ArrayList<NBTBase> list = new ArrayList<>();
		list.add(new NBTTagDouble(mode));	
		return list;
	}

	@Override
	protected boolean hasOtherValues() {
		return true;
	}

	@Override
	protected void applyChanges() {
		
	}

	@Override
	public String getUpgradeString(int level) {
		return null;
	}
	
	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		if(mode == -1 || mode == 1) {
			this.mode = mode;
		}
		updateItem();
	}
	
	public String getStringMode() {
		return "&d" + (mode == -1 ? "Teleport to inventory" : "Sell to shop");
	}

}

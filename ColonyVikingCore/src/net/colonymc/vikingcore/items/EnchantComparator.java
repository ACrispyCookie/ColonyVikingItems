package net.colonymc.vikingcore.items;

import java.util.Comparator;

public class EnchantComparator implements Comparator<Enchantment> {

	@Override
	public int compare(Enchantment e1, Enchantment e2) {
		if(e1.type.rarity.ordinal() > e2.type.rarity.ordinal()) {
			return 1;
		}
		else if(e1.type.rarity.ordinal() < e2.type.rarity.ordinal()) {
			return -1;
		}
		else {
			if(e1.getLevel() > e2.getLevel()) {
				return 1;
			}
			else if(e1.getLevel() < e2.getLevel()) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}

}

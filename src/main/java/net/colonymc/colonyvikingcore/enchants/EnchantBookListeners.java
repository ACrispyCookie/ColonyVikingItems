package net.colonymc.colonyvikingcore.enchants;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.colonymc.colonyspigotapi.api.primitive.RomanNumber;
import net.colonymc.colonyvikingcore.items.EnchantmentBook;
import net.colonymc.colonyvikingcore.items.ItemChecker;
import net.colonymc.colonyvikingcore.items.SpecialItem;

public class EnchantBookListeners implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBookCombine(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(ItemChecker.isEnchantmentBook(e.getCursor())) {
			if(ItemChecker.isSpecialItem(e.getCurrentItem())) {
				e.setCancelled(true);
				SpecialItem item = SpecialItem.getByType(e.getCurrentItem(), p);
				if(item.isEnchantable()) {
					EnchantmentBook book = new EnchantmentBook(e.getCursor(), p);
					SpecialItem sitem = SpecialItem.getByType(e.getCurrentItem(), p);
					if(Arrays.asList(sitem.getValidEnchants()).contains(book.getEnchant())) {
						if(sitem.containsEnchant(book.getEnchant(), book.getLevel()) == -1) {
							sitem.addEnchant(book.getEnchant(), book.getLevel());
							ItemStack newCursor = e.getCursor().clone();
							newCursor.setAmount(newCursor.getAmount() - 1);
							e.setCursor(newCursor);
							e.setCurrentItem(sitem.getItemStack());
							p.playSound(p.getLocation(), Sound.LEVEL_UP, 2, 2);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have added &d" + book.getEnchant().name + " " + RomanNumber.toRoman(book.getLevel()) + " &fto your " + sitem.getName() + "&f."));
						}
						else {
							p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item already has this enchantment!"));
						}
					}
					else {
						p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis enchant cannot be applied to this item!"));
					}
				}
				else {
					p.playSound(p.getLocation(), Sound.BAT_DEATH, 2, 1);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis item cannot be enchanted!"));
				}
			}
		}
	}

}

package net.colonymc.vikingcore.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import net.colonymc.api.player.PlayerInventory;
import net.colonymc.api.primitive.Numbers;
import net.colonymc.vikingcore.MainMessages;

public class SpecialBookCommand implements CommandExecutor, TabExecutor {

	String youGave = ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have gave &d");
	String received = ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have received &d");
	String notOnline = ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!");
	String invalidAmount = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid amount bigger than 0!");
	String invalidDurability = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid durability bigger than 0!");
	String invalidLevel = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid level bigger than 0!");
	String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/vikingbook <type> [player] [amount] [level]");
	String invalidType = ChatColor.translateAlternateColorCodes('&', " &5&l» &fInvalid book type! Valid book types: &d");
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length == 1) {
			List<String> matches = new ArrayList<String>();
			String search = args[0].toLowerCase();
			for (ItemEnchant t : ItemEnchant.values()) {
	            if(t.encodedName.toLowerCase().startsWith(search)) {
	        		matches.add(t.encodedName);
	            }
			}
			return matches;
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender.hasPermission("*")) {
			switch(args.length) {
			case 1:
				if(ItemChecker.enchantFromEncodedName(args[0]) != null) {
					if(sender instanceof Player) {
						Player target = (Player) sender;
						ItemEnchant type = ItemChecker.enchantFromEncodedName(args[0]);
						EnchantmentBook item = new EnchantmentBook(type, 1, target);
						PlayerInventory.addItem(item.getItemStack(), target, 1);
						target.sendMessage(received + "1x " + type.name + " book");
					}
					else {
						sender.sendMessage(MainMessages.onlyPlayers);
					}
				}
				else {
					for(ItemEnchant t : ItemEnchant.values()) {
						if(t.ordinal() > 0) {
							invalidType = invalidType + ", " + t.encodedName;
						}
						else {
							invalidType = invalidType + t.encodedName;
						}
					}
					sender.sendMessage(invalidType);
				}
				break;
			case 2:
				if(ItemChecker.enchantFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						Player target = Bukkit.getPlayer(args[1]);
						ItemEnchant type = ItemChecker.enchantFromEncodedName(args[0]);
						EnchantmentBook item = new EnchantmentBook(type, 1, target);
						PlayerInventory.addItem(item.getItemStack(), target, 1);
						target.sendMessage(received + "1x " + type.name + " book");
						sender.sendMessage(youGave + "1x " + type.name + " book" + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
					}
					else {
						sender.sendMessage(notOnline);
					}
				}
				else {
					for(ItemEnchant t : ItemEnchant.values()) {
						if(t.ordinal() > 0) {
							invalidType = invalidType + ", " + t.encodedName;
						}
						else {
							invalidType = invalidType + t.encodedName;
						}
					}
					sender.sendMessage(invalidType);
				}
				break;
			case 3:
				if(ItemChecker.enchantFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
							Player target = Bukkit.getPlayer(args[1]);
							ItemEnchant type = ItemChecker.enchantFromEncodedName(args[0]);
							EnchantmentBook item = new EnchantmentBook(type, 1, target);
							PlayerInventory.addItem(item.getItemStack(), target, Integer.parseInt(args[2]));
							target.sendMessage(received + args[2] + "x " + type.name + " book");
							sender.sendMessage(youGave + args[2] + "x " + type.name + " book" + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
						}
						else{
							sender.sendMessage(invalidAmount);
						}
					}
					else {
						sender.sendMessage(notOnline);
					}
				}
				else {
					for(ItemEnchant t : ItemEnchant.values()) {
						if(t.ordinal() > 0) {
							invalidType = invalidType + ", " + t.encodedName;
						}
						else {
							invalidType = invalidType + t.encodedName;
						}
					}
					sender.sendMessage(invalidType);
				}
				break;
			case 4:
				if(ItemChecker.enchantFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0 && Numbers.isInt(args[3]) && Integer.parseInt(args[3]) > 0) {
							Player target = Bukkit.getPlayer(args[1]);
							ItemEnchant type = ItemChecker.enchantFromEncodedName(args[0]);
							EnchantmentBook item = new EnchantmentBook(type, Integer.parseInt(args[3]), target);
							PlayerInventory.addItem(item.getItemStack(), target, Integer.parseInt(args[2]));
							target.sendMessage(received + args[2] + "x " + type.name + " book");
							sender.sendMessage(youGave + args[2] + "x " + type.name + " book" + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
						}
						else{
							sender.sendMessage(invalidAmount);
						}
					}
					else {
						sender.sendMessage(notOnline);
					}
				}
				else {
					for(ItemEnchant t : ItemEnchant.values()) {
						if(t.ordinal() > 0) {
							invalidType = invalidType + ", " + t.encodedName;
						}
						else {
							invalidType = invalidType + t.encodedName;
						}
					}
					sender.sendMessage(invalidType);
				}
				break;
			default:
				sender.sendMessage(usage);
			}
		}
		else {
			sender.sendMessage(MainMessages.noPerm);
		}
		return false;
	}

}

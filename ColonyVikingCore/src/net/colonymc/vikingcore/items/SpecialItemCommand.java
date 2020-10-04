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

public class SpecialItemCommand implements CommandExecutor, TabExecutor {

	String youGave = ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have gave &d");
	String received = ChatColor.translateAlternateColorCodes('&', " &5&l» &fYou have received &d");
	String notOnline = ChatColor.translateAlternateColorCodes('&', " &5&l» &cThis player is not online!");
	String invalidAmount = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid amount bigger than 0!");
	String invalidDurability = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid durability bigger than 0!");
	String invalidLevel = ChatColor.translateAlternateColorCodes('&', " &5&l» &cPlease enter a valid level bigger than 0!");
	String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/vikingitem <type> [player] [amount] [durability] [level]");
	String invalidType = ChatColor.translateAlternateColorCodes('&', " &5&l» &fInvalid item type! Valid item types: &d");
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length == 1) {
			List<String> matches = new ArrayList<String>();
			String search = args[0].toLowerCase();
			for (ItemType t : ItemType.values()) {
	            if(t.encodedName.startsWith(search)) {
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
				if(ItemChecker.typeFromEncodedName(args[0]) != null) {
					if(sender instanceof Player) {
						Player target = (Player) sender;
						ItemType type = ItemChecker.typeFromEncodedName(args[0]);
						SpecialItem item = SpecialItem.getByType(type, 50, 50, 1, target);
						PlayerInventory.addItem(item.getItemStack(), target, 1);
						target.sendMessage(received + "1x " + item.getName());
					}
					else {
						sender.sendMessage(MainMessages.onlyPlayers);
					}
				}
				else {
					for(ItemType t : ItemType.values()) {
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
				if(ItemChecker.typeFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						Player target = Bukkit.getPlayer(args[1]);
						ItemType type = ItemChecker.typeFromEncodedName(args[0]);
						SpecialItem item = SpecialItem.getByType(type, 50, 50, 1, target);
						PlayerInventory.addItem(item.getItemStack(), target, 1);
						target.sendMessage(received + "1x " + item.getName());
						sender.sendMessage(youGave + "1x " + item.getName() + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
					}
					else {
						sender.sendMessage(notOnline);
					}
				}
				else {
					for(ItemType t : ItemType.values()) {
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
				if(ItemChecker.typeFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
							Player target = Bukkit.getPlayer(args[1]);
							ItemType type = ItemChecker.typeFromEncodedName(args[0]);
							SpecialItem item = SpecialItem.getByType(type, 50, 50, 1, target);
							PlayerInventory.addItem(item.getItemStack(), target, Integer.parseInt(args[2]));
							target.sendMessage(received + args[2] + "x " + item.getName());
							sender.sendMessage(youGave + args[2] + "x " + item.getName() + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
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
					for(ItemType t : ItemType.values()) {
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
				if(ItemChecker.typeFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
							if(Numbers.isInt(args[3]) && Integer.parseInt(args[3]) > 0) {
								Player target = Bukkit.getPlayer(args[1]);
								ItemType type = ItemChecker.typeFromEncodedName(args[0]);
								SpecialItem item = SpecialItem.getByType(type, Integer.parseInt(args[3]), Integer.parseInt(args[3]), 1, target);
								PlayerInventory.addItem(item.getItemStack(), target, Integer.parseInt(args[2]));
								target.sendMessage(received + args[2] + "x " + item.getName());
								sender.sendMessage(youGave + args[2] + "x " + item.getName() + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
							}
							else{
								sender.sendMessage(invalidDurability);
							}
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
					for(ItemType t : ItemType.values()) {
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
			case 5:
				if(ItemChecker.typeFromEncodedName(args[0]) != null) {
					if(Bukkit.getPlayer(args[1]) != null) {
						if(Numbers.isInt(args[2]) && Integer.parseInt(args[2]) > 0) {
							if(Numbers.isInt(args[3]) && Integer.parseInt(args[3]) > 0) {
								if(Numbers.isInt(args[4]) && Integer.parseInt(args[4]) > 0) {
									Player target = Bukkit.getPlayer(args[1]);
									ItemType type = ItemChecker.typeFromEncodedName(args[0]);
									SpecialItem item = SpecialItem.getByType(type, Integer.parseInt(args[3]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), target);
									PlayerInventory.addItem(item.getItemStack(), target, Integer.parseInt(args[2]));
									target.sendMessage(received + args[2] + "x " + item.getName());
									sender.sendMessage(youGave + args[2] + "x " + item.getName() + ChatColor.translateAlternateColorCodes('&', " &fto &d" + target.getName()));
								}
								else{
									sender.sendMessage(invalidLevel);
								}
							}
							else{
								sender.sendMessage(invalidDurability);
							}
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
					for(ItemType t : ItemType.values()) {
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

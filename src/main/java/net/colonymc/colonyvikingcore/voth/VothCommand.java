package net.colonymc.colonyvikingcore.voth;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.colonymc.colonyvikingcore.MainMessages;

public class VothCommand implements CommandExecutor {

	final String usage = ChatColor.translateAlternateColorCodes('&', " &5&l» &fUsage: &d/voth <start/stop>");
	public static VothEvent currentEvent = new VothEvent();
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender.hasPermission("staff.store")) {
			if(args.length == 1) {
				if(args[0].equals("start")) {
					if(!currentEvent.isKothStarted()) {
						currentEvent.startKoTH();
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cA VoTH is already running!"));
					}
				}
				else if(args[0].equals("stop")) {
					if(currentEvent.isKothStarted()) {
						currentEvent.endKoTH(currentEvent.currentCapper);
					}
					else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &5&l» &cThere is no VoTH running!"));
					}
				}
				else {
					sender.sendMessage(usage);
				}
			}
			else {
				sender.sendMessage(usage);
			}
		}
		else {
			sender.sendMessage(MainMessages.noPerm);
		}
		return false;
	}

}

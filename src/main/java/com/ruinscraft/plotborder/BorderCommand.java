package com.ruinscraft.plotborder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BorderCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (PlotBorder.getActivePlayers().contains(player.getUniqueId())) {
			PlotBorder.getActivePlayers().remove(player.getUniqueId());
			return false;
		}
		
		PlotBorder.getActivePlayers().add(player.getUniqueId());
		
		return true;
		
	}
	
}

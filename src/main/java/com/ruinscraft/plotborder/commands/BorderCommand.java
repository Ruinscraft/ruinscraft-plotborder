package com.ruinscraft.plotborder.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.model.BorderPlayer;

public class BorderCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		final LocationHandler locationHandler = PlotBorder.getInstance().getLocationHandler();
		Player player = (Player) sender;
		
		BorderPlayer borderPlayer = locationHandler.getBorderPlayer(player);
		
		if (!(borderPlayer == null)) {
			locationHandler.removeBorderPlayer(borderPlayer);
		}
		
		// create borderPlayer if not already here
		if (borderPlayer == null) {
			player.sendMessage("CREATE");
			borderPlayer = new BorderPlayer(player.getUniqueId());
			locationHandler.addBorderPlayer(borderPlayer);
		}
		
		return true;
		
	}
	
}

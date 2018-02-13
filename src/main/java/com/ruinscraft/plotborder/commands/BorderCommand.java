package com.ruinscraft.plotborder.commands;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.ruinscraft.plotborder.ParticleRunnable;
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
		BorderPlayer borderPlayer = null;
		
		Location location = player.getLocation();
		com.intellectualcrafters.plot.object.Location plocation = locationHandler.getLocation(location);
		
		Plot plot = Plot.getPlot(plocation);
		
		// create borderPlayer if not already here
		if (locationHandler.getCurrentPlayer(player) == null) {
			borderPlayer = new BorderPlayer(player, plot, location);
			locationHandler.addCurrentPlayer(borderPlayer);
		} else { borderPlayer = locationHandler.getCurrentPlayer(player); }
		
		// new list of points for the plot border
		Map<Double, Location> locations = locationHandler.getBorderPoints(borderPlayer);
		borderPlayer.setParticles(locations);
		
		// start dat
		ParticleRunnable run = new ParticleRunnable(borderPlayer);
		Thread thread = new Thread(run);
		thread.start();
		
		return true;
		
	}
	
}

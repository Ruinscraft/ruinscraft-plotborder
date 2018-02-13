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
import com.ruinscraft.plotborder.objects.BorderPlayer;

public class BorderCommand implements CommandExecutor {
	
	PlotBorder instance = PlotBorder.getInstance();
	LocationHandler handler = PlotBorder.getLocHandler();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		BorderPlayer bplayer;
		
		Location location = player.getLocation();
		com.intellectualcrafters.plot.object.Location plocation = handler.getLocation(location);
		
		Plot plot = Plot.getPlot(plocation);
		
		// create bplayer if not already here
		if (handler.getCurrentPlayer(player) == null) {
			bplayer = new BorderPlayer(player, plot, location);
			handler.addCurrentPlayer(bplayer);
		} else { bplayer = handler.getCurrentPlayer(player); }
		
		// new list of points for the plot border
		Map<Double, Location> locations = handler.getBorderPoints(bplayer);
		bplayer.setParticles(locations);
		
		// start dat
		ParticleRunnable run = new ParticleRunnable(bplayer);
		Thread thread = new Thread(run);
		thread.start();
		
		return false;
		
	}
	
}

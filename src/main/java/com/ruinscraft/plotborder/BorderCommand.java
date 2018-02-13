package com.ruinscraft.plotborder;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

public class BorderCommand implements CommandExecutor {
	
	PlotBorder instance = PlotBorder.getInstance();
	LocationHandler handler = PlotBorder.getLocHandler();

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		CurrentPlayer cplayer;
		
		Location location = player.getLocation();
		com.intellectualcrafters.plot.object.Location plocation = handler.getLocation(location);
		
		Plot plot = Plot.getPlot(plocation);
		
		if (handler.getCurrentPlayer(player) == null) {
			cplayer = new CurrentPlayer(player, plot, location);
			handler.addCurrentPlayer(cplayer);
		} else { cplayer = handler.getCurrentPlayer(player); }
		
		player.sendMessage(String.valueOf(plot.getAllCorners().size()));
		
		Map<Double, Location> locations = handler.enableWalls(cplayer);
		cplayer.setParticles(locations);
		
		for (Location loc : locations.values()) {
			instance.getLogger().info("Yes");
		}
		
		ParticleRunnable run = new ParticleRunnable(cplayer);
		Thread thread = new Thread(run);
		thread.start();
		
		return false;
		
	}
	
}

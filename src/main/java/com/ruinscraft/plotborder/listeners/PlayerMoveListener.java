package com.ruinscraft.plotborder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.intellectualcrafters.plot.object.Plot;
import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.model.BorderPlayer;

public class PlayerMoveListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		
		final LocationHandler locationHandler = PlotBorder.getInstance().getLocationHandler();
		
		Plot plot = Plot.getPlot(locationHandler.getLocation(event.getTo()));
		Player player = event.getPlayer();
		BorderPlayer borderPlayer = null;
		
		borderPlayer = locationHandler.getCurrentPlayer(player);
		
		if (borderPlayer == null || plot == null) {
			return;
		}
		
		borderPlayer.setLocation(event.getTo());
		
		if (!(plot.getId() == borderPlayer.getPlot().getId())) {
			borderPlayer.clearPlotWalls();
			// clear stuff
			// do all the things
		}
		
	}
	
}

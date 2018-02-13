package com.ruinscraft.plotborder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.intellectualcrafters.plot.object.Plot;
import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.objects.BorderPlayer;

public class PlayerMoveListener implements Listener {

	final private LocationHandler handler = PlotBorder.getLocHandler();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		
		Plot plot = Plot.getPlot(handler.getLocation(event.getTo()));
		Player player = event.getPlayer();
		BorderPlayer bplayer;
		
		bplayer = handler.getCurrentPlayer(player);
		
		if (bplayer == null || plot == null) {
			return;
		}
		
		bplayer.setLocation(event.getTo());
		
		if (!(plot.getId() == bplayer.getPlot().getId())) {
			bplayer.clearPlotWalls();
			// clear stuff
			// do all the things
		}
		
	}
	
}

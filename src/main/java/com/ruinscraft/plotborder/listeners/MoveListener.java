package com.ruinscraft.plotborder.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.intellectualcrafters.plot.object.Plot;
import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.objects.CurrentPlayer;

public class MoveListener implements Listener {

	final private LocationHandler handler = PlotBorder.getLocHandler();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		
		Plot plot = Plot.getPlot(handler.getLocation(event.getTo()));
		Player player = event.getPlayer();
		CurrentPlayer cplayer;
		
		cplayer = handler.getCurrentPlayer(player);
		
		if (cplayer == null || plot == null) {
			return;
		}
		
		cplayer.setLocation(event.getTo());
		
		if (!(plot.getId() == cplayer.getPlot().getId())) {
			cplayer.clearPlotWalls();
			// clear stuff
			// do all the things
		}
		
	}
	
}

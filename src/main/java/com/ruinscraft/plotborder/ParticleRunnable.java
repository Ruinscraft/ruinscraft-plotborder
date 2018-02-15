package com.ruinscraft.plotborder;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.handlers.PlotBorderHandler;
import com.ruinscraft.plotborder.model.BorderPlayer;

import java.util.Random;

public class ParticleRunnable implements Runnable {

	public void run() {
		
		LocationHandler locationHandler = PlotBorder.getInstance().getLocationHandler();
		PlotBorderHandler plotBorderHandler = PlotBorder.getInstance().getPlotBorderHandler();

		final List<BorderPlayer> borderPlayers = locationHandler.getBorderPlayers();

		for (BorderPlayer borderPlayer : borderPlayers) {

			PlotBorder.getInstance().getLogger().info("=)");
			Player player = Bukkit.getPlayer(borderPlayer.getPlayerUUID());
			if (player == null) {
				PlotBorder.getInstance().getLogger().info("NOPE Haha !");
				locationHandler.removeBorderPlayer(borderPlayer);
				continue;
			}
			List<Location> locations = borderPlayer.getParticles();
			Location playerLocation = player.getLocation();
			PlotBorder.getInstance().getLogger().info("Going");
			
			if (!(locationHandler.getLocation(playerLocation).getPlot() == borderPlayer.getPlot())) {
				borderPlayer.clearParticles();
				borderPlayer.setPlot(locationHandler.getLocation(playerLocation).getPlot());
				List<Location> particleLocations = plotBorderHandler.getPlotBorderPoints(borderPlayer);
				borderPlayer.sendMessage("WHAT!!");
				borderPlayer.setParticles(particleLocations);
			}

			for (Location location : locations) {
				
				if (!((playerLocation.getX() - location.getX()) < 25 && (playerLocation.getX() - location.getX()) > -25)
						|| !((playerLocation.getZ() - location.getZ()) < 25 && (playerLocation.getZ() - location.getZ()) > -25)
							|| !((playerLocation.getY() - location.getY()) < 25 && (playerLocation.getY() - location.getY()) > -25)) {
					continue;
				}
				
				if (location.distance(playerLocation) >= 25) {
					continue;
				}
				
				double distance = location.distance(playerLocation);
				Random random = new Random();
				double chance = (.1 * (distance * distance * distance)) + 1;

				// get tiny amount of points to display per a random amount of millis
				if (random.nextInt((int) chance) == 1) {
					
					Location nloc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
					
					if (location.getBlock().getType() == Material.AIR) {
						
						// diff amounts of randomness so they look like walls from diff directions
						PlotBorder.getInstance().getLogger().info("AAAAAAAAAAAAAAAAA " + String.valueOf(nloc.getX()) + String.valueOf(nloc.getY()) + String.valueOf(nloc.getZ()));
						player.spawnParticle(PlotBorder.PARTICLE, nloc.add((Math.random() - .5) / 3, (Math.random() - .5), (Math.random() - .5) / 3), 1);
						
					}

				}

			}

		}

	}

}


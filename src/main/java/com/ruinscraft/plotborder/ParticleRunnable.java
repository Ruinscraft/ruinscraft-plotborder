package com.ruinscraft.plotborder;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.model.BorderPlayer;

public class ParticleRunnable implements Runnable {
	
	private final BorderPlayer borderPlayer;
	
	public ParticleRunnable(BorderPlayer borderPlayer) {
		this.borderPlayer = borderPlayer;
	}

	public void run() {

		final LocationHandler locationHandler = PlotBorder.getInstance().getLocationHandler();
		
		Map<Double, Location> locations = borderPlayer.getParticles();

		while (locationHandler.getLocation((Location) locations.values().toArray()[0]).getPlot().getId() == borderPlayer.getPlot().getId()) {

			double rand = Math.random();

			for (double key : locations.keySet()) {

				// todo: check to see if within 20 blocks first
				// make the tiny value below bigger based off of player proximity to the particle
				// so then the particles will get stronger the closer u are to the border

				// get tiny amount of points to display per a random amount of millis
				if ((key - rand) < .002 && (key - rand) > 0) {

					Location location = locations.get(key);
					BorderPlayer bplayer = this.borderPlayer;
					if (location.distance(bplayer.getLocation()) >= 20) {
						continue;
					}
					Location nloc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
					Player player = bplayer.getPlayer();
					Direction direc = bplayer.getWall(location).getDirection();

					if (location.getBlock().getType() == Material.AIR) {

						// diff amounts of randomness so they look like walls from diff directions
						double random = (Math.random() - .5) / 2; // direc
						double randomTwo = (Math.random() - .5) / 4; // non-direc
						double randomThree = (Math.random() - .5) / 2; // y
						// add a lil bit to x/z so it looks like its in the correct spot
						if (direc == Direction.NORTH) {
							player.spawnParticle(PlotBorder.PARTICLE, nloc.add(randomTwo + 1, randomThree, random), 1);
						} else if (direc == Direction.SOUTH) {
							player.spawnParticle(PlotBorder.PARTICLE, nloc.add(randomTwo - .5, randomThree, random), 1);
						} else if (direc == Direction.EAST) {
							player.spawnParticle(PlotBorder.PARTICLE, nloc.add(randomTwo, randomThree, random + 1), 1);
						} else {
							player.spawnParticle(PlotBorder.PARTICLE, nloc.add(random, randomThree, randomTwo - .5), 1);
						}

					}

				}

			}

			try {
				Thread.sleep((long) (Math.random() * 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
				return; 
			}

		}

	}

}

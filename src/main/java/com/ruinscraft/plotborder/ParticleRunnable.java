package com.ruinscraft.plotborder;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.objects.BorderPlayer;

public class ParticleRunnable implements Runnable {
	
	private LocationHandler handler = PlotBorder.getLocHandler();
	
	private final BorderPlayer bplayer;
	private final Particle particle = Particle.HEART;
	
	
	public ParticleRunnable(BorderPlayer bplayer) {
		this.bplayer = bplayer;
	}
	
	public void run() {
		
		Map<Double, Location> locations = bplayer.getParticles();
		
		while (handler.getLocation((Location) locations.values().toArray()[0]).getPlot().getId() == bplayer.getPlot().getId()) {
			
			double rand = Math.random();
			
			for (Double key : locations.keySet()) {
				
				// todo: check to see if within 20 blocks first
				// make the tiny value below bigger based off of player proximity to the particle
				// so then the particles will get stronger the closer u are to the border
				
				// get tiny amount of points to display per a random amount of millis
				if ((key - rand) < .002 && (key - rand) > 0) {
					
					Location location = locations.get(key);
					BorderPlayer bplayer = this.bplayer;
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
							player.spawnParticle(particle, nloc.add(randomTwo + 1, randomThree, random), 1);
						} else if (direc == Direction.SOUTH) {
							player.spawnParticle(particle, nloc.add(randomTwo - .5, randomThree, random), 1);
						} else if (direc == Direction.EAST) {
							player.spawnParticle(particle, nloc.add(randomTwo, randomThree, random + 1), 1);
						} else {
							player.spawnParticle(particle, nloc.add(random, randomThree, randomTwo - .5), 1);
						}
						
					}
					
				}
				
			}
			
			try {
				Thread.sleep((long) (Math.random() * 100));
			} catch (InterruptedException e) { return; }
			
		}
		
	}
	
}

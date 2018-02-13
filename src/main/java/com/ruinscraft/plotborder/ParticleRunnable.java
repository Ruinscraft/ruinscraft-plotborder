package com.ruinscraft.plotborder;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.objects.CurrentPlayer;

public class ParticleRunnable implements Runnable {
	
	private LocationHandler handler = PlotBorder.getLocHandler();
	
	private final CurrentPlayer cplayer;
	private final Particle particle = Particle.HEART;
	
	
	public ParticleRunnable(CurrentPlayer cplayer) {
		this.cplayer = cplayer;
	}
	
	public void run() {
		
		Map<Double, Location> locations = cplayer.getParticles();
		
		while (handler.getLocation((Location) locations.values().toArray()[0]).getPlot().getId() == cplayer.getPlot().getId()) {
			
			double rand = Math.random();
			
			for (Double key : locations.keySet()) {
				
				if ((key - rand) < .002 && (key - rand) > 0) {
					
					Location location = locations.get(key);
					CurrentPlayer cplayer = this.cplayer;
					if (location.distance(cplayer.getLocation()) >= 20) {
						continue;
					}
					Location nloc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
					Player player = cplayer.getPlayer();
					Direction direc = cplayer.getWall(location).getDirection();
					
					if (location.getBlock().getType() == Material.AIR) {
						double random = (Math.random() - .5) / 2; // direc
						double randomTwo = (Math.random() - .5) / 4; // non-direc
						double randomThree = (Math.random() - .5) / 2; // y
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

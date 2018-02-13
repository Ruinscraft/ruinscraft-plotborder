package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.MathMan;

public class LocationHandler {
	
	final private PlotBorder instance = PlotBorder.getInstance();
	final private World world = instance.getServer().getWorlds().get(0);
	final Particle particle = Particle.HEART;
	
	private List<CurrentPlayer> players;
	
	public LocationHandler() {
		players = new ArrayList<CurrentPlayer>();
	}
	
	// Gets PlotSquared location
	public com.intellectualcrafters.plot.object.Location getLocation(Location location) {
		return new com.intellectualcrafters.plot.object.Location(world.getName(), 
				MathMan.roundInt(location.getX()), MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()));
	}
	
	public Location getLocation(com.intellectualcrafters.plot.object.Location location) {
		return new Location(world, location.getX(), location.getY(), location.getZ());
	}
	
	public Map<Double, Location> enableWalls(CurrentPlayer cplayer) {
		
		Plot plot = cplayer.getPlot();
		
		int i = -1;
		Map<Double, Location> locs = new HashMap<Double, Location>();
		for (final com.intellectualcrafters.plot.object.Location corner : plot.getAllCorners()) {
			
			final com.intellectualcrafters.plot.object.Location nextCorner;
			i++;
			
			if (i == (plot.getAllCorners().size() - 1)) {
				instance.getLogger().info("Yep");
				nextCorner = plot.getAllCorners().get(0);
			} else {
				instance.getLogger().info("Ugh");
				nextCorner = plot.getAllCorners().get(i + 1);
			}
			
			cplayer.sendMessage(String.valueOf(corner.getX()) + " " + String.valueOf(corner.getZ()));
			cplayer.sendMessage(String.valueOf(nextCorner.getX()) + " " + String.valueOf(nextCorner.getZ()));
			
			Direction direc = getDirec(corner, nextCorner);
			
			WallDirec wall = new WallDirec(getLocation(corner), getLocation(nextCorner), direc);
			cplayer.addPlotWall(wall);
			
			int y;
			for (y = 0; y <= 254; y++) {
				
				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());
				instance.getLogger().info("NEW Change !!!1");
				
				int d = 0;
				while (d <= plot.getLargestRegion().maxX || d <= plot.getLargestRegion().maxZ) {
					d++;
					change = addCoord(direc, change, nextCorner);
					if (change == null) {
						break;
					}
					wall.addTask(getLocation(change));
					locs.put(Math.random(), getLocation(change));
				}
				// do stuff
			}
			// everything
			
		}
		
		return locs;
		
	}
	
	public Direction getDirec(com.intellectualcrafters.plot.object.Location cornerOne, 
							  	com.intellectualcrafters.plot.object.Location cornerTwo) {
		
		Direction direc = null;
		
		if (cornerOne.getX() == cornerTwo.getX()) {
			
			int x = cornerOne.getX();
			com.intellectualcrafters.plot.object.Location half = new com.intellectualcrafters.plot.object.Location(
					world.getName(), x, 1, ((cornerOne.getZ() + cornerTwo.getZ()) / 2));
			if ((half.add(1, 0, 0)).getPlot() == null) {
				direc = Direction.NORTH;
			} else {
				direc = Direction.SOUTH;
			}
			
		} else if (cornerOne.getZ() == cornerTwo.getZ()) {
			int z = cornerOne.getZ();
			com.intellectualcrafters.plot.object.Location half = new com.intellectualcrafters.plot.object.Location(
					world.getName(), ((cornerOne.getX() + cornerTwo.getX()) / 2), 1, z);
			if ((half.add(0, 0, 1)).getPlot() == null) {
				direc = Direction.EAST;
			} else {
				direc = Direction.WEST;
			}
			
		} else {
			return null;
		}
		
		return direc;
		
	}
	
	public com.intellectualcrafters.plot.object.Location addCoord(Direction direc, com.intellectualcrafters.plot.object.Location change, 
		  										com.intellectualcrafters.plot.object.Location cornerTwo) {
		
		switch (direc) {
			case NORTH:
				if (change.getZ() == cornerTwo.getZ()) {
					return null;
				}
				change.setX(cornerTwo.getX() + 1);
				if (!(change.add(0, 0, -1).getPlot() == null) || (change.add(-1, 0, 0).getPlot() == null)) {
					return null;
				}
				return change;
			case SOUTH:
				if (change.getZ() == cornerTwo.getZ()) {
					return null;
				}
				change.setX(cornerTwo.getX() - 1);
				if (!(change.add(0, 0, 1).getPlot() == null) || (change.add(1, 0, 0).getPlot() == null)) {
					return null;
				}
				return change;
			case EAST:
				if (change.getX() == cornerTwo.getX()) {
					return null;
				}
				change.setZ(cornerTwo.getZ() + 1);
				
				if (!(change.add(1, 0, 0).getPlot() == null) || (change.add(0, 0, -1).getPlot() == null)) {
					return null;
				}
				return change;
			case WEST:
				if (change.getX() == cornerTwo.getX()) {
					return null;
				}
				change.setZ(cornerTwo.getZ() - 1);
				if (!(change.add(-1, 0, 0).getPlot() == null) || (change.add(0, 0, 1).getPlot() == null)) {
					return null;
				}
				return change;
			default:
				return null;
		}
		
	}
	
	public CurrentPlayer getCurrentPlayer(Player player) {
		for (CurrentPlayer cplayer : players) {
			if (cplayer.getPlayer() == player) {
				return cplayer;
			}
		}
		return null;
	}
	
	public void addCurrentPlayer(CurrentPlayer player) {
		players.add(player);
	}
	
}

package com.ruinscraft.plotborder.handlers;

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
import com.ruinscraft.plotborder.Direction;
import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.objects.BorderPlayer;
import com.ruinscraft.plotborder.objects.WallDirec;

public class LocationHandler {
	
	final private PlotBorder instance = PlotBorder.getInstance();
	final private World world = instance.getServer().getWorlds().get(0);
	final Particle particle = Particle.HEART;
	
	private List<BorderPlayer> players;
	
	public LocationHandler() {
		players = new ArrayList<BorderPlayer>();
	}
	
	// gets PlotSquared location from Bukkit location
	public com.intellectualcrafters.plot.object.Location getLocation(Location location) {
		return new com.intellectualcrafters.plot.object.Location(world.getName(), 
				MathMan.roundInt(location.getX()), MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()));
	}
	
	// vice versa
	public Location getLocation(com.intellectualcrafters.plot.object.Location location) {
		return new Location(world, location.getX(), location.getY(), location.getZ());
	}
	
	// collect all points on da wall for plot, save a random num with it
	// prob should reverse key/value but it works
	public Map<Double, Location> getBorderPoints(BorderPlayer bplayer) {
		
		Plot plot = bplayer.getPlot();
		
		int i = -1;
		Map<Double, Location> locs = new HashMap<Double, Location>();
		for (final com.intellectualcrafters.plot.object.Location corner : plot.getAllCorners()) {
			
			final com.intellectualcrafters.plot.object.Location nextCorner;
			i++;
			
			if (i == (plot.getAllCorners().size() - 1)) {
				nextCorner = plot.getAllCorners().get(0);
			} else {
				nextCorner = plot.getAllCorners().get(i + 1);
			}
			
			Direction direc = getDirec(corner, nextCorner);
			
			WallDirec wall = new WallDirec(getLocation(corner), getLocation(nextCorner), direc);
			bplayer.addPlotWall(wall);
			
			int y;
			for (y = 0; y <= 254; y++) {
				
				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());
				
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
	
	// gets direction between two consecutive corners of plot
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
	
	// move over a coord to add another point to da wall, etc
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
	
	public BorderPlayer getCurrentPlayer(Player player) {
		for (BorderPlayer bplayer : players) {
			if (bplayer.getPlayer() == player) {
				return bplayer;
			}
		}
		return null;
	}
	
	public void addCurrentPlayer(BorderPlayer player) {
		players.add(player);
	}
	
}

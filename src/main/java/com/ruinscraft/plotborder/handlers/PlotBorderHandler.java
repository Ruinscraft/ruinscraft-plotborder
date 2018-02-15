package com.ruinscraft.plotborder.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.intellectualcrafters.plot.object.Plot;
import com.ruinscraft.plotborder.Direction;
import com.ruinscraft.plotborder.PlotBorder;
import com.ruinscraft.plotborder.model.BorderPlayer;

public class PlotBorderHandler {
	
	private final PlotBorder instance = PlotBorder.getInstance();
	private final LocationHandler locationHandler = instance.getLocationHandler();
	private final World world = instance.getServer().getWorlds().get(0);
	
	public List<Location> getPlotBorderPoints(BorderPlayer bplayer) {
		
		bplayer.sendMessage("COOL!!");
		Plot plot = bplayer.getPlot();
		List<Location> locs = new ArrayList<Location>();
		
		if (plot == null) {
			bplayer.sendMessage("Not cool.");
			return locs;
		}
		
		int i = -1;
		for (final com.intellectualcrafters.plot.object.Location corner : plot.getAllCorners()) {
			
			final com.intellectualcrafters.plot.object.Location nextCorner;
			
			i++;
			if (i == (plot.getAllCorners().size() - 1)) {
				nextCorner = plot.getAllCorners().get(0);
			} else {
				nextCorner = plot.getAllCorners().get(i + 1);
			}
			
			Direction direc = getPlotBorderDirec(corner, nextCorner);
			if (direc == null) {
				instance.getLogger().info("Hahahaha !");
			}
			
			int y;
			for (y = 0; y <= 254; y++) {
				
				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());
				
				int ii;
				for (ii = plot.getConnectedPlots().size() * 50; ii > 0; ii--) {
					if (change == null) {
						break;
					}
					bplayer.sendMessage(String.valueOf(change.getX()) + " " + String.valueOf(change.getY()) + " " + String.valueOf(change.getZ()));
					locs.add(locationHandler.getLocation(change));
					change = addPlotCoord(direc, change, nextCorner);
				}
				
			}
			
		}
		
		return locs;
		
	}
	
	// gets direction between two consecutive corners of plot
	public Direction getPlotBorderDirec(com.intellectualcrafters.plot.object.Location cornerOne, 
							  	com.intellectualcrafters.plot.object.Location cornerTwo) {
		
		Direction direc;
		
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
	
	// something here
	public com.intellectualcrafters.plot.object.Location addPlotCoord(Direction direc, com.intellectualcrafters.plot.object.Location change, 
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
					instance.getLogger().info("A !");
					return null;
				}
				change.setZ(cornerTwo.getZ() - 1);
				if (!(change.add(-1, 0, 0).getPlot() == null) || (change.add(0, 0, 1).getPlot() == null)) {
					instance.getLogger().info("a !");
					return null;
				}
				instance.getLogger().info("aAa !");
				return change;
			default:
				instance.getLogger().info("aaa !");
				return null;
		}
		
	}
	
}

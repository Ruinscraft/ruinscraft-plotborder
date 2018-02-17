package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.MathMan;

public class PlotBorderUtil {

	// Get com.intellectualcrafters.plot.object.Location from org.bukkit.Location
	public static com.intellectualcrafters.plot.object.Location getLocation(Location location) {
		return new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), 
				MathMan.roundInt(location.getX()), MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()));
	}
	
	// Get org.bukkit.Location from com.intellectualcrafters.plot.object.Location
	public static Location getLocation(com.intellectualcrafters.plot.object.Location location) {
		return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
	}

	// spawns particles randomly for the player
	public static void spawnPoints(List<UUID> players) {

		for (UUID uuid : players) {

			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				PlotBorder.getActivePlayers().remove(uuid);
				continue;
			}

			List<Location> locations = PlotBorderUtil.getPlotBorderPoints(player);
			Location playerLocation = Bukkit.getPlayer(uuid).getLocation();

			for (Location location : locations) {

				if (!((playerLocation.getX() - location.getX()) < 25 && (playerLocation.getX() - location.getX()) > -25)
						|| !((playerLocation.getZ() - location.getZ()) < 25 && (playerLocation.getZ() - location.getZ()) > -25)) {
					continue;
				}

				double distance = location.distance(playerLocation);

				if (new Random().nextInt((int) (.1 * (distance * distance * distance)) + 1) == 1) {

					if (location.getBlock().getType() == Material.AIR) {
						player.spawnParticle(PlotBorder.PARTICLE, new Location(
								location.getWorld(), location.getX(), location.getY(), location.getZ()), 1);
					}

				}

			}

		}

	}

	// gets all locations/points where particles will have a chance to be spawned
	public static List<Location> getPlotBorderPoints(Player player) {

		Location location = player.getLocation();
		Plot plot = getLocation(location).getPlot();
		List<Location> locations = new ArrayList<Location>();

		if (plot == null) {
			return locations;
		}

		int i = -1;
		for (final com.intellectualcrafters.plot.object.Location corner : plot.getAllCorners()) {
			
			i++;
			
			if (!((corner.getX() - location.getX()) < 25 && (corner.getX() - location.getX()) > -25)
					&& !((corner.getZ() - location.getZ()) < 25 && (corner.getZ() - location.getZ()) > -25)) {
				continue;
			}

			final com.intellectualcrafters.plot.object.Location nextCorner;

			if (i == (plot.getAllCorners().size() - 1)) {
				nextCorner = plot.getAllCorners().get(0);
			} else {
				nextCorner = plot.getAllCorners().get(i + 1);
			}

			Direction direction = getPlotWallDirection(corner, nextCorner);

			for (int y = location.getBlockY() - 19; y <= location.getBlockY() + 19; y++) {

				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());

				for (int j = plot.getConnectedPlots().size() * 50; j > 0; j--) {
					change = addPlotCoord(direction, change, nextCorner);
					if (change == null) {
						break;
					}
					locations.add(adjustLocation(new com.intellectualcrafters.plot.object.Location(
							change.getWorld(), change.getX(), change.getY(), change.getZ()), direction));
				}

			}

		}

		return locations;

	}

	// gets the direction between two consecutive corners of plot ("wall")
	public static Direction getPlotWallDirection(com.intellectualcrafters.plot.object.Location cornerOne, 
			com.intellectualcrafters.plot.object.Location cornerTwo) {

		Direction direction;

		if (cornerOne.getX() == cornerTwo.getX()) {

			int x = cornerOne.getX();
			com.intellectualcrafters.plot.object.Location half = new com.intellectualcrafters.plot.object.Location(
					cornerOne.getWorld(), x, 1, ((cornerOne.getZ() + cornerTwo.getZ()) / 2));
			if ((half.add(1, 0, 0)).getPlot() == null) {
				direction = Direction.NORTH;
			} else {
				direction = Direction.SOUTH;
			}

		} else if (cornerOne.getZ() == cornerTwo.getZ()) {
			int z = cornerOne.getZ();
			com.intellectualcrafters.plot.object.Location half = new com.intellectualcrafters.plot.object.Location(
					cornerOne.getWorld(), ((cornerOne.getX() + cornerTwo.getX()) / 2), 1, z);
			if ((half.add(0, 0, 1)).getPlot() == null) {
				direction = Direction.EAST;
			} else {
				direction = Direction.WEST;
			}

		} else {
			return null;
		}

		return direction;

	}

	// adds a coord while collecting points in a plot wall based on the direction of the wall
	public static com.intellectualcrafters.plot.object.Location addPlotCoord(Direction direction, com.intellectualcrafters.plot.object.Location change, 
			com.intellectualcrafters.plot.object.Location cornerTwo) {

		switch (direction) {
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
	
	// does some minor adjustments to the location to make it look nice
	public static Location adjustLocation(com.intellectualcrafters.plot.object.Location location, Direction direction) {
		
		switch (direction) {
		case NORTH:
			return getLocation(location).add(1, (Math.random() - .5) / 2, (Math.random() - .5) / 2);
		case EAST:
			return getLocation(location).add((Math.random() - .5) / 2, (Math.random() - .5) / 2, 1);
		case SOUTH:
			return getLocation(location).add(0, (Math.random() - .5) / 2, (Math.random() - .5) / 2);
		case WEST:
			return getLocation(location).add((Math.random() - .5) / 2, (Math.random() - .5) / 2, 0);
		default:
			return getLocation(location);
		}
		
	}

}

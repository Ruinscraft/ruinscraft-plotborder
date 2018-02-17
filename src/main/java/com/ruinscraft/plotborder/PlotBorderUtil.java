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
				Random random = new Random();
				double chance = (.1 * (distance * distance * distance)) + 1;

				if (random.nextInt((int) chance) == 1) {

					Location nloc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());

					if (location.getBlock().getType() == Material.AIR) {

						// PlotBorder.getInstance().getLogger().info("AAAAA " + String.valueOf(nloc.getX()) + String.valueOf(nloc.getY()) + String.valueOf(nloc.getZ()));
						player.spawnParticle(PlotBorder.PARTICLE, nloc.add((Math.random() - .5) / 3, (Math.random() - .5), (Math.random() - .5) / 3), 1);

					}

				}

			}

		}

	}

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
			if (direction == null) {
			}

			for (int y = location.getBlockY() - 19; y <= location.getBlockY() + 19; y++) {

				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());

				int j;
				for (j = plot.getConnectedPlots().size() * 50; j > 0; j--) {
					if (change == null) {
						break;
					}
					locations.add(getLocation(change));
					change = addPlotCoord(direction, change, nextCorner);
				}

			}

		}

		return locations;

	}

	// gets direction between two consecutive corners of plot
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

	// something here
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

}

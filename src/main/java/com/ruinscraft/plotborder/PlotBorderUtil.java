package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

public class PlotBorderUtil {

	private static final PlotBorder instance = PlotBorder.getInstance();
	private static final World world = instance.getServer().getWorlds().get(0);

	public static void spawnPoints(List<UUID> players) {

		for (UUID uuid : players) {

			Player player = Bukkit.getPlayer(uuid);
			if (player == null) {
				PlotBorder.getInstance().getLogger().info("NOPE Haha !");
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
		Plot plot = PlotBorder.getLocation(location).getPlot();
		List<Location> locs = new ArrayList<Location>();

		if (plot == null) {
			return locs;
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

			Direction direc = getPlotBorderDirec(corner, nextCorner);
			if (direc == null) {
				instance.getLogger().info("Hahahaha !");
			}

			for (int y = location.getBlockY() - 19; y <= location.getBlockY() + 19; y++) {

				com.intellectualcrafters.plot.object.Location change = new com.intellectualcrafters.plot.object.Location(
						corner.getWorld(), corner.getX(), y, corner.getZ());

				int ii;
				for (ii = plot.getConnectedPlots().size() * 50; ii > 0; ii--) {
					if (change == null) {
						break;
					}
					locs.add(PlotBorder.getLocation(change));
					change = addPlotCoord(direc, change, nextCorner);
				}

			}

		}

		return locs;

	}

	// gets direction between two consecutive corners of plot
	public static Direction getPlotBorderDirec(com.intellectualcrafters.plot.object.Location cornerOne, 
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
	public static com.intellectualcrafters.plot.object.Location addPlotCoord(Direction direc, com.intellectualcrafters.plot.object.Location change, 
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

}

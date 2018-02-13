package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;

public class CurrentPlayer {

	LocationHandler handler = PlotBorder.getLocHandler();

	private Player player;
	private Plot plot;
	private Location location;
	private List<WallDirec> walls;
	private Map<Double, Location> particles;

	public CurrentPlayer(Player player, Plot plot, Location location) {
		this.player = player;
		this.plot = plot;
		this.location = location;
		walls = new ArrayList<WallDirec>();
		particles = new HashMap<Double, Location>();
	}

	public Player getPlayer() {
		return player;
	}

	public Plot getPlot() {
		return plot;
	}

	public Location getLocation() {
		return location;
	}

	public List<WallDirec> getWalls() {
		return walls;
	}
	
	public Map<Double, Location> getParticles() {
		return particles;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean addPlotWall(WallDirec wall) {
		if (walls.contains(wall)) {
			return false;
		}
		walls.add(wall);
		return true;
	}

	public boolean removeWall(WallDirec wall) {
		if (walls.contains(wall)) {
			walls.remove(wall);
			return true;
		}
		return false;
	}

	public WallDirec getWall(Location location) {
		for (WallDirec wall : getWalls()) {
			for (Location taskLocation : wall.getTasks()) {
				if (taskLocation.getBlockX() == location.getBlockX() && taskLocation.getBlockZ() == location.getBlockZ()) {
					return wall;
				}
			}
		}
		return null;
	}

	public void enablePlotWalls() {
		handler.enableWalls(this);
	}

	public void clearPlotWalls() {
		for (WallDirec wall : getWalls()) {
			wall.clearTasks();
		}
		walls.clear();
	}
	
	public void setParticles(Map<Double, Location> particles) {
		this.particles.clear();
		this.particles = particles;
	}

	public void sendMessage(String message) {
		player.getPlayer().sendMessage(message);
	}

}

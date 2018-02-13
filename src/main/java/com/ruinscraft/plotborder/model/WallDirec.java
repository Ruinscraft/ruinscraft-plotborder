package com.ruinscraft.plotborder.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.ruinscraft.plotborder.Direction;

@Deprecated
public class WallDirec {
	
	private Location corner;
	private Location corner2;
	private Direction direction;
	private List<Location> tasks;
	
	public WallDirec(Location corner, Location corner2, Direction direction) {
		this.corner = corner;
		this.corner2 = corner2;
		this.direction = direction;
		tasks = new ArrayList<Location>();
	}
	
	public Location getBeginning() {
		return corner;
	}
	
	public Location getEnd() {
		return corner2;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public List<Location> getTasks() {
		return tasks;
	}
	
	public void addTask(Location location) {
		tasks.add(location);
	}
	
	public void clearTasks() {
		tasks.clear();
	}

}

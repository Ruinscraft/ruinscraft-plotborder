package com.ruinscraft.plotborder.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.util.MathMan;
import com.ruinscraft.plotborder.model.BorderPlayer;

public class LocationHandler {
	
	private List<BorderPlayer> borderPlayers;
	
	public LocationHandler() {
		borderPlayers = new ArrayList<BorderPlayer>();
	}
	
	// Get com.intellectualcrafters.plot.object.Location from org.bukkit.Location
	public com.intellectualcrafters.plot.object.Location getLocation(Location location) {
		return new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), 
				MathMan.roundInt(location.getX()), MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()));
	}
	
	// Get org.bukkit.Location from com.intellectualcrafters.plot.object.Location
	public Location getLocation(com.intellectualcrafters.plot.object.Location location) {
		return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
	}
	
	public List<BorderPlayer> getBorderPlayers() {
		return borderPlayers;
	}
	
	public BorderPlayer getBorderPlayer(Player player) {
		for (BorderPlayer bplayer : borderPlayers) {
			if (Bukkit.getPlayer(bplayer.getPlayerUUID()) == player) {
				return bplayer;
			}
		}
		return null;
	}
	
	public void addBorderPlayer(BorderPlayer player) {
		borderPlayers.add(player);
	}
	
	public void removeBorderPlayer(BorderPlayer player) {
		borderPlayers.remove(player);
	}
	
}

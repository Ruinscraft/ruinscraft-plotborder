package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import com.intellectualcrafters.plot.util.MathMan;

public class PlotBorder extends JavaPlugin {
	
	public static final Particle PARTICLE = Particle.HEART;
	
	private static PlotBorder instance;
	
	private static List<UUID> activePlayers;
	
	public void onEnable() {
		
		instance = this;
		
		activePlayers = new ArrayList<UUID>();
		
		this.getCommand("pborder").setExecutor(new BorderCommand());
		
		Bukkit.getScheduler().runTaskTimer(this, new ParticleRunnable(), 0L, 4L);
		
	}
	
	public void onDisable() {
		instance = null;
	}
	
	public static PlotBorder getInstance() {
		return instance;
	}
	
	public static List<UUID> getActivePlayers() {
		return activePlayers;
	}
	
	// Get com.intellectualcrafters.plot.object.Location from org.bukkit.Location
	public static com.intellectualcrafters.plot.object.Location getLocation(Location location) {
		return new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), 
				MathMan.roundInt(location.getX()), MathMan.roundInt(location.getY()), MathMan.roundInt(location.getZ()));
	}
	
	// Get org.bukkit.Location from com.intellectualcrafters.plot.object.Location
	public static Location getLocation(com.intellectualcrafters.plot.object.Location location) {
		return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
	}
	
	
	
}

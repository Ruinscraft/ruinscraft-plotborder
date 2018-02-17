package com.ruinscraft.plotborder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

public class PlotBorder extends JavaPlugin {
	
	public static final Particle PARTICLE = Particle.HEART;
	
	private static PlotBorder plotBorder;
	
	private static List<UUID> activePlayers;
	
	public void onEnable() {
		
		plotBorder = this;
		
		activePlayers = new ArrayList<UUID>();
		
		this.getCommand("pborder").setExecutor(new BorderCommand());
		
		Bukkit.getScheduler().runTaskTimer(this, new ParticleRunnable(), 0L, 4L);
		
	}
	
	public void onDisable() {
		plotBorder = null;
	}
	
	public static PlotBorder getInstance() {
		return plotBorder;
	}
	
	public static List<UUID> getActivePlayers() {
		return activePlayers;
	}
	
}

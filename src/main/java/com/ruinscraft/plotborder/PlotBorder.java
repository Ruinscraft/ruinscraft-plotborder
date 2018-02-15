package com.ruinscraft.plotborder;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.plotborder.commands.BorderCommand;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.handlers.PlotBorderHandler;

public class PlotBorder extends JavaPlugin {
	
	public static final Particle PARTICLE = Particle.HEART;
	
	private static PlotBorder instance;

	private LocationHandler locationHandler;
	private PlotBorderHandler plotBorderHandler;
	
	public void onEnable() {
		
		instance = this;
		
		locationHandler = new LocationHandler();
		plotBorderHandler = new PlotBorderHandler();
		
		this.getCommand("pborder").setExecutor(new BorderCommand());
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new ParticleRunnable(), 0L, 4L);
		
	}
	
	public static PlotBorder getInstance() {
		return instance;
	}
	
	public LocationHandler getLocationHandler() {
		return locationHandler;
	}
	
	public PlotBorderHandler getPlotBorderHandler() {
		return plotBorderHandler;
	}

	public void onDisable() {
		instance = null;
	}
	
}

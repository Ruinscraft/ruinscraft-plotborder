package com.ruinscraft.plotborder;

import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.plotborder.commands.BorderCommand;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.listeners.PlayerMoveListener;

public class PlotBorder extends JavaPlugin {
	
	public static final Particle PARTICLE = Particle.HEART;
	
	private static PlotBorder instance;

	private LocationHandler locationHandler;
	
	public void onEnable() {
		
		instance = this;
		
		locationHandler = new LocationHandler();
		
		this.getCommand("pborder").setExecutor(new BorderCommand());
		
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		
	}
	
	public static PlotBorder getInstance() {
		return instance;
	}
	
	public LocationHandler getLocationHandler() {
		return locationHandler;
	}

	public void onDisable() {
		instance = null;
	}
	
}

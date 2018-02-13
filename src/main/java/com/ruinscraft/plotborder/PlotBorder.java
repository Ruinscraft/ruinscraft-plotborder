package com.ruinscraft.plotborder;

import org.bukkit.plugin.java.JavaPlugin;

import com.ruinscraft.plotborder.commands.BorderCommand;
import com.ruinscraft.plotborder.handlers.LocationHandler;
import com.ruinscraft.plotborder.listeners.PlayerMoveListener;

public class PlotBorder extends JavaPlugin {
	
	private static PlotBorder instance;
	private static LocationHandler lhandler;
	
	public static PlotBorder getInstance() {
		return instance;
	}
	
	public static LocationHandler getLocHandler() {
		return lhandler;
	}
	
	public void onEnable() {
		
		instance = this;
		lhandler = new LocationHandler();
		
		this.getCommand("pborder").setExecutor(new BorderCommand());
		
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
		
	}
	
	public void onDisable() {
		instance = null;
	}
	
}

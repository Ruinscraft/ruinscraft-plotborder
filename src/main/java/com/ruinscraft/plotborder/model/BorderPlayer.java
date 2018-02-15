package com.ruinscraft.plotborder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.intellectualcrafters.plot.object.Plot;

public class BorderPlayer {

	private UUID playerUUID;
	private Plot plot;
	private List<Location> particles;

	public BorderPlayer(UUID playerUUID) {
		this.playerUUID = playerUUID;
		particles = new ArrayList<Location>();
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Plot getPlot() {
		return plot;
	}

	public List<Location> getParticles() {
		return particles;
	}

	public void setPlot(Plot plot) {
		this.plot = plot;
	}
	
	public void setParticles(List<Location> particles) {
		this.particles = particles;
	}
	
	public void removeParticle(Location particle) {
		particles.remove(particle);
	}
	
	public void clearParticles() {
		particles.clear();
	}

	public void sendMessage(String message) {
		Bukkit.getPlayer(this.playerUUID).sendMessage(message);
	}

}

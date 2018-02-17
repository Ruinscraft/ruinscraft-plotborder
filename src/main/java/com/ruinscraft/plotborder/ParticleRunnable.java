package com.ruinscraft.plotborder;

import java.util.List;
import java.util.UUID;

public class ParticleRunnable implements Runnable {

	public void run() {
		
		final List<UUID> players = PlotBorder.getActivePlayers();
		PlotBorderUtil.spawnPoints(players);

	}

}


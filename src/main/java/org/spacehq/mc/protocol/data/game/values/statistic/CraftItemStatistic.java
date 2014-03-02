package org.spacehq.mc.protocol.data.game.values.statistic;

public class CraftItemStatistic implements Statistic {

	private int id;

	public CraftItemStatistic(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}

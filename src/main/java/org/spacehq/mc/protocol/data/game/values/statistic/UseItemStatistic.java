package org.spacehq.mc.protocol.data.game.values.statistic;

public class UseItemStatistic implements Statistic {

	private int id;

	public UseItemStatistic(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

}

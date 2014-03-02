package org.spacehq.mc.protocol.data.game.values.world.effect;

public class BreakPotionEffectData implements WorldEffectData {

	private int potionId;

	public BreakPotionEffectData(int potionId) {
		this.potionId = potionId;
	}

	public int getPotionId() {
		return this.potionId;
	}

}
